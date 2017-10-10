package com.austinv11.peripheralsplusplus.turtles.peripherals;

import com.austinv11.peripheralsplusplus.reference.Config;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.ITurtleAccess;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class PeripheralFeeder extends MountedPeripheral {

	private ITurtleAccess turtle;

	public PeripheralFeeder(ITurtleAccess turtle) {
		this.turtle = turtle;
	}

	@Override
	public String getType() {
		return "feeder";
	}

	@Override
	public String[] getMethodNames() {
		return new String[]{"feed"};
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments)
            throws LuaException, InterruptedException {
		if (!Config.enableFeederTurtle)
			throw new LuaException("Feeder Turtles have been disabled");
		if (method == 0) {
			ItemStack curItem = turtle.getInventory().getStackInSlot(turtle.getSelectedSlot());
			if (curItem.isEmpty() || curItem.getCount() <= 0)
				return new Object[] {false};
			BlockPos pos = turtle.getPosition();
			for (EntityAnimal animal : turtle.getWorld().getEntitiesWithinAABB(EntityAnimal.class,
                    new AxisAlignedBB(pos.getX() - 1.5D, pos.getY() - 1.5D, pos.getZ() - 1.5D,
                            pos.getX() + 1.5D, pos.getY() + 1.5D, pos.getZ() + 1.5D))) {
				if (animal.getGrowingAge() == 0 && !animal.isInLove() && animal.isBreedingItem(curItem)) {
					animal.setAttackTarget(null);
					animal.setInLove(null);//setting inLove to 600
					curItem.setCount(curItem.getCount() - 1);
					if (curItem.getCount() <= 0)
						turtle.getInventory().setInventorySlotContents(turtle.getSelectedSlot(), ItemStack.EMPTY);
					else
						turtle.getInventory().setInventorySlotContents(turtle.getSelectedSlot(), curItem);
					return new Object[]{true};
				}
			}
			return new Object[]{false};
		}
		return new Object[0];
	}

	@Override
	public boolean equals(IPeripheral other) {
		return (other == this);
	}
}
