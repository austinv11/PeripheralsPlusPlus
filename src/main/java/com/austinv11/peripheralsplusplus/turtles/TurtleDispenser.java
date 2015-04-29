package com.austinv11.peripheralsplusplus.turtles;

import com.austinv11.peripheralsplusplus.reference.Reference;
import com.austinv11.peripheralsplusplus.turtles.peripherals.PeripheralDispenser;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.*;
import net.minecraft.block.BlockDispenser;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class TurtleDispenser implements ITurtleUpgrade {
	
	@Override
	public int getUpgradeID() {
		return Reference.DISPENSER_UPGRADE;
	}
	
	@Override
	public String getUnlocalisedAdjective() {
		return "peripheralsplusplus.turtleUpgrade.dispenser";
	}
	
	@Override
	public TurtleUpgradeType getType() {
		return TurtleUpgradeType.Peripheral;
	}
	
	@Override
	public ItemStack getCraftingItem() {
		return new ItemStack(Blocks.dispenser);
	}
	
	@Override
	public IPeripheral createPeripheral(ITurtleAccess turtle, TurtleSide side) {
		return new PeripheralDispenser(turtle);
	}
	
	@Override
	public TurtleCommandResult useTool(ITurtleAccess turtle, TurtleSide side, TurtleVerb verb, int direction) {
		return null;
	}
	
	@Override
	public IIcon getIcon(ITurtleAccess turtle, TurtleSide side) {
		return ((BlockDispenser)Blocks.dispenser).field_149945_N;
	}
	
	@Override
	public void update(ITurtleAccess turtle, TurtleSide side) {
		
	}
}
