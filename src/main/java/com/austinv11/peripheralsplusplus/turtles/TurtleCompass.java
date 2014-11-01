package com.austinv11.peripheralsplusplus.turtles;

import com.austinv11.peripheralsplusplus.init.ModItems;
import com.austinv11.peripheralsplusplus.reference.Reference;
import com.austinv11.peripheralsplusplus.turtles.peripherals.PeripheralCompass;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.*;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class TurtleCompass implements ITurtleUpgrade{

	@Override
	public int getUpgradeID() {
		return Reference.COMPASS_UPGRADE;
	}

	@Override
	public String getUnlocalisedAdjective() {
		return "peripheralsplusplus.turtleUpgrade.compass";
	}

	@Override
	public TurtleUpgradeType getType() {
		return TurtleUpgradeType.Peripheral;
	}

	@Override
	public ItemStack getCraftingItem() {
		return new ItemStack(ModItems.upgradeCompass);
	}

	@Override
	public IPeripheral createPeripheral(ITurtleAccess turtle, TurtleSide side) {
		return new PeripheralCompass(turtle);
	}

	@Override
	public TurtleCommandResult useTool(ITurtleAccess turtle, TurtleSide side, TurtleVerb verb, int direction) {
		return null;
	}

	@Override
	public IIcon getIcon(ITurtleAccess turtle, TurtleSide side) {
		return ModItems.upgradeCompass.getIconFromDamage(1);
	}

	@Override
	public void update(ITurtleAccess turtle, TurtleSide side) {}
}
