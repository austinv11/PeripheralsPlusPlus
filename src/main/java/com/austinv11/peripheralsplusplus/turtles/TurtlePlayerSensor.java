package com.austinv11.peripheralsplusplus.turtles;

import com.austinv11.peripheralsplusplus.init.ModBlocks;
import com.austinv11.peripheralsplusplus.reference.Reference;
import com.austinv11.peripheralsplusplus.tiles.TileEntityPlayerSensor;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.*;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class TurtlePlayerSensor implements ITurtleUpgrade {

	@Override
	public int getUpgradeID() {
		return Reference.PLAYER_SENSOR_UPGRADE;
	}

	@Override
	public String getUnlocalisedAdjective() {
		return Reference.MOD_ID.toLowerCase()+".turtleUpgrade.playerSensor";
	}

	@Override
	public TurtleUpgradeType getType() {
		return TurtleUpgradeType.Peripheral;
	}

	@Override
	public ItemStack getCraftingItem() {
		return new ItemStack(ModBlocks.playerSensor);
	}

	@Override
	public IPeripheral createPeripheral(ITurtleAccess turtle, TurtleSide side) {
		return new TileEntityPlayerSensor(turtle);
	}

	@Override
	public TurtleCommandResult useTool(ITurtleAccess turtle, TurtleSide side, TurtleVerb verb, int direction) {
		return null;
	}

	@Override
	public IIcon getIcon(ITurtleAccess turtle, TurtleSide side) {
		return ModBlocks.playerSensor.getIcon(1, 0);
	}

	@Override
	public void update(ITurtleAccess turtle, TurtleSide side) {//Nothing

	}
}
