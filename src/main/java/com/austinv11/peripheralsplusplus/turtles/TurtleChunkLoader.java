package com.austinv11.peripheralsplusplus.turtles;

import com.austinv11.peripheralsplusplus.init.ModBlocks;
import com.austinv11.peripheralsplusplus.init.ModItems;
import com.austinv11.peripheralsplusplus.reference.Reference;
import com.austinv11.peripheralsplusplus.turtles.peripherals.PeripheralChunkLoader;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.*;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class TurtleChunkLoader implements ITurtleUpgrade {
	
	@Override
	public int getUpgradeID() {
		return Reference.CHUNK_LOADER_UPGRADE;
	}
	
	@Override
	public String getUnlocalisedAdjective() {
		return "peripheralsplusplus.turtleUpgrade.loader";
	}
	
	@Override
	public TurtleUpgradeType getType() {
		return TurtleUpgradeType.Peripheral;
	}
	
	@Override
	public ItemStack getCraftingItem() {
		return new ItemStack(ModItems.chunkLoaderUpgrade);
	}
	
	@Override
	public IPeripheral createPeripheral(ITurtleAccess turtle, TurtleSide side) {
		return new PeripheralChunkLoader(turtle);
	}
	
	@Override
	public TurtleCommandResult useTool(ITurtleAccess turtle, TurtleSide side, TurtleVerb verb, int direction) {
		return null;
	}
	
	@Override
	public IIcon getIcon(ITurtleAccess turtle, TurtleSide side) {
		return ModBlocks.dummyBlock.getIcon(0, 5);
	}
	
	@Override
	public void update(ITurtleAccess turtle, TurtleSide side) {
		IPeripheral peripheral = turtle.getPeripheral(side);
		if (peripheral instanceof PeripheralChunkLoader)
			((PeripheralChunkLoader) peripheral).update();
	}
}
