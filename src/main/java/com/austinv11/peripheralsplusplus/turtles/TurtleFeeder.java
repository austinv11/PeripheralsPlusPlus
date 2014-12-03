package com.austinv11.peripheralsplusplus.turtles;

import com.austinv11.peripheralsplusplus.init.ModItems;
import com.austinv11.peripheralsplusplus.reference.Reference;
import com.austinv11.peripheralsplusplus.turtles.peripherals.PeripheralFeeder;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.*;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class TurtleFeeder implements ITurtleUpgrade {

	@Override
	public int getUpgradeID() {
		return Reference.FEEDER_UPGRADE;
	}

	@Override
	public String getUnlocalisedAdjective() {
		return "peripheralsplusplus.turtleUpgrade.feeder";
	}

	@Override
	public TurtleUpgradeType getType() {
		return TurtleUpgradeType.Peripheral;
	}

	@Override
	public ItemStack getCraftingItem() {
		return new ItemStack(ModItems.feederUpgrade);
	}

	@Override
	public IPeripheral createPeripheral(ITurtleAccess turtle, TurtleSide side) {
		return new PeripheralFeeder(turtle);
	}

	@Override
	public TurtleCommandResult useTool(ITurtleAccess turtle, TurtleSide side, TurtleVerb verb, int direction) {
		return null;
	}

	@Override
	public IIcon getIcon(ITurtleAccess turtle, TurtleSide side) {
		return ModItems.feederUpgrade.getIconIndex(new ItemStack(ModItems.feederUpgrade));
	}

	@Override
	public void update(ITurtleAccess turtle, TurtleSide side) {}
}
