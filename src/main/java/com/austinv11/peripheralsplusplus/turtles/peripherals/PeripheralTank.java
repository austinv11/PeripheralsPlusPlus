package com.austinv11.peripheralsplusplus.turtles.peripherals;

import com.austinv11.peripheralsplusplus.reference.Config;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.TurtleSide;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;

import java.util.HashMap;

public class PeripheralTank extends MountedPeripheral {

	private ITurtleAccess turtle;
	private TurtleSide side;
	private FluidTank tank = new FluidTank(Config.maxNumberOfMillibuckets);
	private boolean changed = false;

	public PeripheralTank(ITurtleAccess turtle, TurtleSide side) {
		this.turtle = turtle;
		this.side = side;
		NBTTagCompound tag = turtle.getUpgradeNBTData(side);
		if (tag != null) {
			tank = tank.readFromNBT(tag);
			changed = true;
		}
	}

	@Override
	public String getType() {
		return "tank";
	}

	@Override
	public String[] getMethodNames() {
		return new String[]{"drop", "dropUp", "dropDown", "suck", "suckUp", "suckDown", "getLiquid", "pack", "unpack"};
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws LuaException, InterruptedException {
		if (!Config.enableTankTurtle)
			throw new LuaException("Thirsty Turtles have been disabled");
		ForgeDirection dir;
//		try {
		switch (method) {
			case 0:
			case 1:
			case 2:
				synchronized (this) {
					if (arguments.length > 0 && !(arguments[0] instanceof Double))
						throw new LuaException("Bad argument #1 (expected number)");
					if (method == 0)
						dir = ForgeDirection.getOrientation(turtle.getDirection());
					else if (method == 1)
						dir = ForgeDirection.UP;
					else
						dir = ForgeDirection.DOWN;
					int placed = 0;
					int maxPlaced = arguments.length > 0 ? (int) (double) (Double) arguments[0] : Integer.MAX_VALUE;
					if (tank.getFluidAmount() > 0) {
						if (turtle.getWorld().isAirBlock(turtle.getPosition().posX+dir.offsetX, turtle.getPosition().posY+dir.offsetY, turtle.getPosition().posZ+dir.offsetZ)) {
							if (tank.getFluidAmount() >= 1000 && 1000 <= maxPlaced)
								if (tank.getFluid().getFluid().canBePlacedInWorld()) {
									turtle.getWorld().setBlock(turtle.getPosition().posX+dir.offsetX, turtle.getPosition().posY+dir.offsetY, turtle.getPosition().posZ+dir.offsetZ, reverseGetFluidBlock(tank.getFluid().getFluid().getBlock()));
									placed = 1000;
								}
						} else {
							Block block = turtle.getWorld().getBlock(turtle.getPosition().posX+dir.offsetX, turtle.getPosition().posY+dir.offsetY, turtle.getPosition().posZ+dir.offsetZ);
							if (block instanceof ITileEntityProvider)
								if (turtle.getWorld().getTileEntity(turtle.getPosition().posX+dir.offsetX, turtle.getPosition().posY+dir.offsetY, turtle.getPosition().posZ+dir.offsetZ) instanceof IFluidHandler) {
									IFluidHandler handler = (IFluidHandler) turtle.getWorld().getTileEntity(turtle.getPosition().posX+dir.offsetX, turtle.getPosition().posY+dir.offsetY, turtle.getPosition().posZ+dir.offsetZ);
									if (handler.canFill(dir.getOpposite(), tank.getFluid().getFluid()))
										placed = handler.fill(dir.getOpposite(), tank.getFluid(), true);
								}
						}
						placed = tank.drain(placed, true).amount;
					}
					changed = true;
					return new Object[]{placed};
				}
			case 3:
			case 4:
			case 5:
				synchronized (this) {
					if (method == 3)
						dir = ForgeDirection.getOrientation(turtle.getDirection());
					else if (method == 4)
						dir = ForgeDirection.UP;
					else
						dir = ForgeDirection.DOWN;
					if (arguments.length > 0 && !(arguments[0] instanceof Double))
						throw new LuaException("Bad argument #1 (expected number)");
					int sucked = 0;
					Fluid fluidSucked = null;
					int maxSucked = arguments.length > 0 ? (int) (double) (Double) arguments[0] : Integer.MAX_VALUE;
					if (turtle.getWorld().isAnyLiquid(AxisAlignedBB.getBoundingBox(turtle.getPosition().posX+dir.offsetX, turtle.getPosition().posX+dir.offsetX, turtle.getPosition().posY+dir.offsetY, turtle.getPosition().posY+dir.offsetY, turtle.getPosition().posZ+dir.offsetZ, turtle.getPosition().posZ+dir.offsetZ))) {
						if ((tank.getCapacity()-tank.getFluidAmount()) >= 1000 && 1000 <= maxSucked) {
							Block block = turtle.getWorld().getBlock(turtle.getPosition().posX+dir.offsetX, turtle.getPosition().posY+dir.offsetY, turtle.getPosition().posZ+dir.offsetZ);
							if (block instanceof IFluidBlock || FluidRegistry.lookupFluidForBlock(getFluidBlock(block)) != null) {
								if (tank.getFluidAmount() < 1 || tank.getFluid().isFluidEqual(new FluidStack(FluidRegistry.lookupFluidForBlock(getFluidBlock(block)), 1000))) {
									fluidSucked = FluidRegistry.lookupFluidForBlock(getFluidBlock(block));
									sucked = 1000;
									turtle.getWorld().setBlockToAir(turtle.getPosition().posX+dir.offsetX, turtle.getPosition().posY+dir.offsetY, turtle.getPosition().posZ+dir.offsetZ);
								}
							}
						}
					} else {
						Block block = turtle.getWorld().getBlock(turtle.getPosition().posX+dir.offsetX, turtle.getPosition().posY+dir.offsetY, turtle.getPosition().posZ+dir.offsetZ);
						if (block instanceof ITileEntityProvider)
							if (turtle.getWorld().getTileEntity(turtle.getPosition().posX+dir.offsetX, turtle.getPosition().posY+dir.offsetY, turtle.getPosition().posZ+dir.offsetZ) instanceof IFluidHandler && (tank.getFluidAmount() < tank.getCapacity())) {
								IFluidHandler handler = (IFluidHandler) turtle.getWorld().getTileEntity(turtle.getPosition().posX+dir.offsetX, turtle.getPosition().posY+dir.offsetY, turtle.getPosition().posZ+dir.offsetZ);
								FluidTankInfo[] infos = handler.getTankInfo(dir.getOpposite());
								for (FluidTankInfo info : infos) {
									if ((tank.getFluidAmount() < 1 || tank.getFluid().isFluidEqual(info.fluid)) && handler.canFill(dir.getOpposite(), info.fluid.getFluid())) {
										FluidStack stack = handler.drain(dir.getOpposite(), new FluidStack(info.fluid, maxSucked), true);
										fluidSucked = stack.getFluid();
										sucked = stack.amount;
										break;
									}
								}
							}

					}
					if (fluidSucked == null)
						sucked = 0;
					else
						sucked = tank.fill(new FluidStack(fluidSucked, sucked), true);
					changed = true;
					return new Object[]{sucked};
				}
			case 6:
				if (tank.getFluidAmount() > 0) {
					HashMap<String,Object> map = new HashMap<String,Object>();
					map.put("amount", tank.getFluidAmount());
					map.put("name", tank.getFluid().getLocalizedName());
					map.put("id", tank.getFluid().fluidID);
					return new Object[]{map};
				}
				return new Object[0];
			case 7:
				if (arguments.length > 0 && !(arguments[0] instanceof Double))
					throw new LuaException("Bad argument #1 (expected number)");
				int slotToPack = arguments.length > 0 ? (int) (double) (Double) arguments[0]-1 : findContainerSlot();
				if (slotToPack == -1)
					throw new LuaException("No suitable item found");
				ItemStack toPack = turtle.getInventory().getStackInSlot(slotToPack);
				if (toPack.getItem() instanceof IFluidContainerItem)
					return new Object[]{((IFluidContainerItem) toPack.getItem()).fill(toPack, tank.getFluid(), true)};
				if (toPack.getItem() instanceof ItemBucket) {
					if (tank.getFluid() != null) {
						Block fluid = tank.getFluid().getFluid().getBlock();
						ItemBucket item = (ItemBucket) Items.bucket;
						item.isFull = reverseGetFluidBlock(fluid);
						ItemStack stack = new ItemStack(item);
						turtle.getInventory().setInventorySlotContents(slotToPack-1, stack);
						return new Object[]{1000};
					}
				}
				changed = true;
				return new Object[]{0};
			case 8:
				if (arguments.length > 0 && !(arguments[0] instanceof Double))
					throw new LuaException("Bad argument #1 (expected number)");
				int slotToUnpack = arguments.length > 0 ? (int) (double) (Double) arguments[0]-1 : findContainerSlot();
				if (slotToUnpack == -1)
					throw new LuaException("No suitable item found");
				ItemStack toUnpack = turtle.getInventory().getStackInSlot(slotToUnpack);
				if (toUnpack.getItem() instanceof IFluidContainerItem) {
					FluidStack unpacked = ((IFluidContainerItem) toUnpack.getItem()).drain(toUnpack, tank.getCapacity()-tank.getFluidAmount(), false);
					if (tank.getFluid() == null || unpacked.isFluidEqual(tank.getFluid()))
						return new Object[]{tank.fill(((IFluidContainerItem) toUnpack.getItem()).drain(toUnpack, tank.getCapacity()-tank.getFluidAmount(), true), true)};
				} else if (toUnpack.getItem() instanceof ItemBucket) {
					if (((ItemBucket)toUnpack.getItem()).isFull != Blocks.air) {
						turtle.getInventory().setInventorySlotContents(slotToUnpack, new ItemStack(Items.bucket));
						return new Object[]{tank.fill(new FluidStack(FluidRegistry.lookupFluidForBlock(getFluidBlock((((ItemBucket) toUnpack.getItem())).isFull)), 1000), true)};
					}
				}
				changed = true;
				return new Object[]{0};
		}
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
		return new Object[0];
	}

	private int findContainerSlot() {
		for (int i = 0; i < turtle.getInventory().getSizeInventory(); i++)
			if (turtle.getInventory().getStackInSlot(i) != null && (turtle.getInventory().getStackInSlot(i).getItem() instanceof IFluidContainerItem || turtle.getInventory().getStackInSlot(i).getItem() instanceof ItemBucket))
				return i;
		return -1;
	}

	private Block getFluidBlock(Block b) {
		if (b == Blocks.flowing_lava)
			return Blocks.lava;
		else if (b == Blocks.flowing_water)
			return Blocks.water;
		return b;
	}

	private Block reverseGetFluidBlock(Block b) {
		if (b == Blocks.lava)
			return Blocks.flowing_lava;
		else if (b == Blocks.water)
			return Blocks.flowing_water;
		return b;
	}

	@Override
	public boolean equals(IPeripheral other) {
		return (this == other);
	}

	public void update() {
		if (changed) {
			if (tank.getFluid() != null) {
				NBTTagCompound tag = turtle.getUpgradeNBTData(side);
				tank.writeToNBT(tag);
				turtle.updateUpgradeNBTData(side);
				changed = false;
			}
		}
	}
}
