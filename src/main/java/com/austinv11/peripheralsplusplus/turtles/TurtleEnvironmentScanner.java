package com.austinv11.peripheralsplusplus.turtles;

import com.austinv11.peripheralsplusplus.init.ModBlocks;
import com.austinv11.peripheralsplusplus.reference.Reference;
import com.austinv11.peripheralsplusplus.tiles.TileEntityEnvironmentScanner;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.*;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class TurtleEnvironmentScanner implements ITurtleUpgrade {

	@Override
	public int getUpgradeID() {
		return Reference.ENVIRONMENT_UPGRADE;
	}

	@Override
	public String getUnlocalisedAdjective() {
		return "peripheralsplusplus.turtleUpgrade.environmentScanner";
	}

	@Override
	public TurtleUpgradeType getType() {
		return TurtleUpgradeType.Peripheral;
	}

	@Override
	public ItemStack getCraftingItem() {
		return new ItemStack(ModBlocks.environmentScanner);
	}

	@Override
	public IPeripheral createPeripheral(ITurtleAccess turtle, TurtleSide side) {
		TileEntityEnvironmentScanner te = new TileEntityEnvironmentScanner(turtle);
		te.setWorldObj(turtle.getWorld());
		te.xCoord = turtle.getPosition().posX;
		te.zCoord = turtle.getPosition().posZ;
		return te;
	}

	@Override
	public TurtleCommandResult useTool(ITurtleAccess turtle, TurtleSide side, TurtleVerb verb, int direction) {
		return null;
	}

	@Override
	public IIcon getIcon(ITurtleAccess turtle, TurtleSide side) {
		return ModBlocks.environmentScanner.getIcon(0,0);
	}

	@Override
	public void update(ITurtleAccess turtle, TurtleSide side) {
		IPeripheral peripheral = turtle.getPeripheral(side);
		if (peripheral instanceof TileEntityEnvironmentScanner)
			((TileEntityEnvironmentScanner)peripheral).updateEntity();
	}
}
