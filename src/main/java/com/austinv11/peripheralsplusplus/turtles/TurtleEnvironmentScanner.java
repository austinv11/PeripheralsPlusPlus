package com.austinv11.peripheralsplusplus.turtles;

import com.austinv11.peripheralsplusplus.init.ModBlocks;
import com.austinv11.peripheralsplusplus.reference.Reference;
import com.austinv11.peripheralsplusplus.tiles.TileEntityEnvironmentScanner;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.*;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class TurtleEnvironmentScanner implements ITurtleUpgrade {

	private TileEntityEnvironmentScanner te;

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
		te = new TileEntityEnvironmentScanner();
		te.setWorldObj(turtle.getWorld());
		te.xCoord = turtle.getPosition().posX;
		te.zCoord = turtle.getPosition().posY;
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
		if (te != null)
			te.updateEntity();
	}
}
