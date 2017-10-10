package com.austinv11.peripheralsplusplus.turtles.peripherals;

import com.austinv11.peripheralsplusplus.reference.Config;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.TurtleSide;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

import java.util.HashMap;

public class PeripheralTank extends MountedPeripheral {

	private static final int TRANSFER_AMOUNT = 1000;
	private ITurtleAccess turtle;
	private TurtleSide side;
	private FluidTank fluidTank = new FluidTank(Config.maxNumberOfMillibuckets);

	public PeripheralTank(ITurtleAccess turtle, TurtleSide side) {
		this.turtle = turtle;
		this.side = side;

		if (!turtle.getWorld().isRemote) {
			NBTTagCompound turtleTag = turtle.getUpgradeNBTData(side);
			NBTTagCompound tankData = turtleTag.getCompoundTag("TankData");
            fluidTank = fluidTank.readFromNBT(tankData);
		}

	}

	@Override
	public String getType() {
		return "tank";
	}

	@Override
	public String[] getMethodNames() {
		return new String[] {"getFluid", "fill", "drain", "place", "placeUp", "placeDown", "suck", "suckUp", "suckDown"};
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments)
			throws LuaException, InterruptedException {
		switch (method) {
			// "getFluid": Return information about the fluid within the tank
			case 0:
				return getFluid();
			// "fill": Move fluid from internal tank to container in specified slot / currently selected slot
			// Returns amount of fluid moved
			case 1:
				return fill(getFirstArgumentAsSlot(arguments));
			// "drain": Moves fluid from container in specified slot to internal tank
			// Returns amount of fluid moved
			case 2:
				return drain(getFirstArgumentAsSlot(arguments));
			// "empty[dir]" Places a block of the fluid from the internal tank in the world.
			// Returns the amount of fluid emptied (always either 0 or 1000 for now)
			case 3:
			case 4:
			case 5:
				return emptyToWorld(getMethodIdAsDirection(method));
			// "suck[dir]" Takes a block of fluid from the world into the internal tank
			// Returns the amount of fluid moved (always either 0 or 1000 for now)
			case 6:
			case 7:
			case 8:
				return suckFromWorld(getMethodIdAsDirection(method));
			}
		throw new LuaException("Unhandled method");
	}

	private EnumFacing getMethodIdAsDirection(int method) throws LuaException {
		switch (method) {
			case 3: // forward
			case 6:
				return turtle.getDirection();
			case 4: // up
			case 7:
				return EnumFacing.UP;
			case 5: // down
			case 8:
				return EnumFacing.DOWN;
			default:
				throw new LuaException("Unhandled method direction mapping: " + method);
		}
	}

	private Object[] suckFromWorld(EnumFacing direction) throws LuaException {
		BlockPos pos = turtle.getPosition().offset(direction);
		// Ensure the block is a fluid that can be manipulated
		IFluidHandler fluidHandler = FluidUtil.getFluidHandler(turtle.getWorld(), pos,
				direction.getOpposite());
		if (fluidHandler == null)
			throw new LuaException("Block is not a fluid block");
		// Move to tank
		FluidStack fluidStack = FluidUtil.tryFluidTransfer(fluidTank, fluidHandler, TRANSFER_AMOUNT,
				true);
		if (fluidStack == null)
			return new Object[]{0};
		saveTankDataToTurtle();
		// Remove from world
		Block block = turtle.getWorld().getBlockState(pos).getBlock();
		if (block instanceof IFluidBlock || block instanceof BlockLiquid)
			turtle.getWorld().setBlockToAir(pos);
		return new Object[]{fluidStack.amount};
	}

	private Object[] emptyToWorld(EnumFacing direction) {
		// Try the empty
		boolean placed = FluidUtil.tryPlaceFluid(null,
				turtle.getWorld(),
				turtle.getPosition().offset(direction),
				fluidTank,
				fluidTank.getFluid());
		int amount = 0;
		// Placed in world
		if (placed) {
			amount = 1000; // Amount of a bucket
			saveTankDataToTurtle();
		}
		// Space was not clear. Try to put into a fluid handler
		else {
			BlockPos pos = turtle.getPosition().offset(direction);
			// Ensure the block is a fluid handler
			IFluidHandler fluidHandler = FluidUtil.getFluidHandler(turtle.getWorld(), pos,
					direction.getOpposite());
			if (fluidHandler != null) {
				FluidStack transferResult = FluidUtil.tryFluidTransfer(fluidHandler, fluidTank,
						TRANSFER_AMOUNT, true);
				if (transferResult != null) {
					amount = transferResult.amount;
					saveTankDataToTurtle();
				}
			}
		}
		return new Object[]{amount};
	}

	private int getFirstArgumentAsSlot(Object[] arguments) throws LuaException {
		int drainSlot; // The slot that contains the container
		// If a slot is specified, check that it is valid and set it. If none is specified, use the currently selected slot
		if (arguments.length > 1) {
			if (arguments[0] instanceof Double) {
				drainSlot = (int) (double) (Double) arguments[0];
				if (drainSlot < 1 || drainSlot > turtle.getInventory().getSizeInventory())
					throw new LuaException("Slot index out of bounds");
			} else {
				throw new LuaException("Bad argument #1 (expected number)");
			}
		} else {
			drainSlot = turtle.getSelectedSlot();
		}
		return drainSlot;
	}

	private Object[] drain(int drainSlot) throws LuaException {
		ItemStack drainStack = turtle.getInventory().getStackInSlot(drainSlot);
		FluidStack drainFluidStack = FluidUtil.getFluidContained(drainStack);
		IFluidHandlerItem drainFluidHandler = FluidUtil.getFluidHandler(drainStack);
		if (drainFluidStack == null || drainFluidHandler == null || drainFluidStack.amount <= 0)
			throw new LuaException("Item does not contain fluid");
		if (fluidTank.getFluid() != null && !fluidTank.getFluid().isFluidEqual(drainFluidStack))
			throw new LuaException("Fluid types do not match");
		FluidStack transfer = FluidUtil.tryFluidTransfer(fluidTank, drainFluidHandler, Config.maxNumberOfMillibuckets,
				true);
		if (transfer == null)
			return new Object[]{0};
		turtle.getInventory().setInventorySlotContents(drainSlot, drainFluidHandler.getContainer());
		saveTankDataToTurtle();
		return new Object[]{transfer.amount};
	}

	private Object[] fill(int fillSlot) throws LuaException {
		ItemStack fillStack = turtle.getInventory().getStackInSlot(fillSlot);
		FluidStack fillFluidStack = FluidUtil.getFluidContained(fillStack);
		IFluidHandlerItem fillFluidHandler = FluidUtil.getFluidHandler(fillStack);
		if (fillFluidHandler == null)
			throw new LuaException("Item cannot contain fluid");
		if (fluidTank.getFluid() == null)
			throw new LuaException("Internal tank does not contain fluid");
		if (fillFluidStack != null && !fluidTank.getFluid().equals(fillFluidStack))
			throw new LuaException("Fluid types do not match");
		FluidStack transfer = FluidUtil.tryFluidTransfer(fillFluidHandler, fluidTank, Config.maxNumberOfMillibuckets,
				true);
		if (transfer == null)
			return new Object[]{0};
		turtle.getInventory().setInventorySlotContents(fillSlot, fillFluidHandler.getContainer());
		saveTankDataToTurtle();
		return new Object[]{transfer.amount};
	}

	private Object[] getFluid() {
		if (fluidTank.getFluid() != null) {
			HashMap<String, Object> map = new HashMap<>();
			map.put("amount", fluidTank.getFluidAmount());
			map.put("name", fluidTank.getFluid().getLocalizedName());
			map.put("id", fluidTank.getFluid().getFluid().getName());
			map.put("registered", FluidRegistry.isFluidRegistered(fluidTank.getFluid().getFluid()));
			return new Object[]{map};
		}
		return new Object[0];
	}

	private void saveTankDataToTurtle() {
		NBTTagCompound newTag = new NBTTagCompound();
		NBTTagCompound turtleTag = turtle.getUpgradeNBTData(side);
		fluidTank.writeToNBT(newTag);
		turtleTag.setTag("TankData", newTag);
		turtle.updateUpgradeNBTData(side);
	}

	@Override
	public boolean equals(IPeripheral other) {
		return (this == other);
	}
}
