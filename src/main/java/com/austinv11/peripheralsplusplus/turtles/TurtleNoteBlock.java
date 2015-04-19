package com.austinv11.peripheralsplusplus.turtles;

import com.austinv11.peripheralsplusplus.init.ModBlocks;
import com.austinv11.peripheralsplusplus.reference.Reference;
import com.austinv11.peripheralsplusplus.tiles.TileEntityNoteBlock;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.ITurtleUpgrade;
import dan200.computercraft.api.turtle.TurtleCommandResult;
import dan200.computercraft.api.turtle.TurtleSide;
import dan200.computercraft.api.turtle.TurtleUpgradeType;
import dan200.computercraft.api.turtle.TurtleVerb;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class TurtleNoteBlock implements ITurtleUpgrade {

	@Override
	public int getUpgradeID() {
		return Reference.NOTE_BLOCK_UPGRADE;
	}

	@Override
	public String getUnlocalisedAdjective() {
		return Reference.MOD_ID.toLowerCase() + ".turtleUpgrade.note";
	}

	@Override
	public TurtleUpgradeType getType() {
		return TurtleUpgradeType.Peripheral;
	}

	@Override
	public ItemStack getCraftingItem() {
		return new ItemStack(ModBlocks.noteBlock);
	}

	@Override
	public IPeripheral createPeripheral(ITurtleAccess turtle, TurtleSide side) {
		return new TileEntityNoteBlock(turtle);
	}

	@Override
	public TurtleCommandResult useTool(ITurtleAccess turtle, TurtleSide side, TurtleVerb verb, int direction) {
		return null;
	}

	@Override
	public IIcon getIcon(ITurtleAccess turtle, TurtleSide side) {
		return ModBlocks.noteBlock.getIcon(1, 0);
	}

	@Override
	public void update(ITurtleAccess turtle, TurtleSide side) {
		IPeripheral peripheral = turtle.getPeripheral(side);
		if (peripheral instanceof TileEntityNoteBlock) {
            ((TileEntityNoteBlock) peripheral).updateEntity(true);
        }
	}

}
