package com.austinv11.peripheralsplusplus.turtles.peripherals;

import com.austinv11.peripheralsplusplus.mount.DynamicMount;
import com.austinv11.peripheralsplusplus.reference.Config;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.ITurtleAccess;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;

import java.util.List;

public class PeripheralFeeder implements IPeripheral {

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
	public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws LuaException, InterruptedException {
		if (!Config.enableFeederTurtle)
			throw new LuaException("Feeder Turtles have been disabled");
		if (method == 0) {
			ItemStack curItem = turtle.getInventory().getStackInSlot(turtle.getSelectedSlot());
			if (curItem == null || curItem.stackSize <= 0)
				return new Object[] {false};
			ChunkCoordinates pos = turtle.getPosition();
			for (EntityAnimal animal : (List<EntityAnimal>)turtle.getWorld().getEntitiesWithinAABB(EntityAnimal.class, AxisAlignedBB.getBoundingBox(pos.posX - 1.5D, pos.posY - 1.5D, pos.posZ - 1.5D, pos.posX + 1.5D, pos.posY + 1.5D, pos.posZ + 1.5D))) {
				if (animal.getGrowingAge() == 0 && !animal.isInLove() && animal.isBreedingItem(curItem)) {
					animal.setTarget(null);
					animal.func_146082_f(null);//setting inLove to 600
					curItem.stackSize--;
					if (curItem.stackSize <= 0)
						turtle.getInventory().setInventorySlotContents(turtle.getSelectedSlot(), null);
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
	public void attach(IComputerAccess computer) {
		computer.mount(DynamicMount.DIRECTORY, new DynamicMount(this));
	}

	@Override
	public void detach(IComputerAccess computer) {
		computer.unmount(DynamicMount.DIRECTORY+"/"+getType());
	}

	@Override
	public boolean equals(IPeripheral other) {
		return (other == this);
	}
}
