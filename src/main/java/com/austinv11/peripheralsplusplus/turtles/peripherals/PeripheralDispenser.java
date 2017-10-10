package com.austinv11.peripheralsplusplus.turtles.peripherals;

import com.austinv11.peripheralsplusplus.reference.Config;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.ITurtleAccess;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.state.IBlockState;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PeripheralDispenser extends MountedPeripheral {
	
	private ITurtleAccess turtle;
	
	public PeripheralDispenser(ITurtleAccess turtle) {
		this.turtle = turtle;
	}
	
	@Override
	public String getType() {
		return "flinging";
	}
	
	@Override
	public String[] getMethodNames() {
		return new String[]{"dispense", "dispenseUp", "dispenseDown"};
	}
	
	@Override
	public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments)
            throws LuaException, InterruptedException {
		if (!Config.enableFlingingTurtle)
			throw new LuaException("Flinging turtles have been disabled");
		if (arguments.length > 0 && !(arguments[0] instanceof Double))
			throw new LuaException("Bad argument #1 (expected number)");
		int slot = 1;
		EnumFacing direction;
		switch (method) {
			case 0:
				direction = turtle.getDirection();
				break;
			case 1:
				direction = EnumFacing.UP;
				break;
			case 2:
				direction = EnumFacing.DOWN;
				break;
			default:
				throw new LuaException("Unhandled method");
		}
		if (arguments.length > 0)
			slot += (int)(double)(Double)arguments[0];
		else
			slot = turtle.getSelectedSlot();
		
		synchronized (this) {
			ItemStack stack = turtle.getInventory().getStackInSlot(slot);
			if (!stack.isEmpty()) {
				IBehaviorDispenseItem behavior = BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.getObject(stack.getItem());
				if (behavior != IBehaviorDispenseItem.DEFAULT_BEHAVIOR) {
					BlockSourceTurtle blockSource = new BlockSourceTurtle(direction);
					ItemStack newStack = behavior.dispense(blockSource, stack);
					turtle.getInventory().setInventorySlotContents(slot, newStack);
					turtle.getInventory().markDirty();
				}
			}
		}
		return new Object[0];
	}
	
	@Override
	public boolean equals(IPeripheral other) {
		return this == other;
	}
	
	public class BlockSourceTurtle implements IBlockSource {
		
		public EnumFacing direction;
		
		public BlockSourceTurtle(EnumFacing direction) {
			this.direction = direction;
		}
		
		@Override
		public double getX() {
			return turtle.getPosition().getX();
		}
		
		@Override
		public double getY() {
			return turtle.getPosition().getY();
		}
		
		@Override
		public double getZ() {
			return turtle.getPosition().getZ();
		}

		@Override
		public BlockPos getBlockPos() {
			return turtle.getPosition();
		}

		@Override
		public IBlockState getBlockState() {
			return Blocks.DISPENSER.getDefaultState().withProperty(BlockDirectional.FACING, direction);
		}
		
		@Override
		public TileEntity getBlockTileEntity() {
			return getWorld().getTileEntity(turtle.getPosition());
		}
		
		@Override
		public World getWorld() {
			return turtle.getWorld();
		}
	}
}
