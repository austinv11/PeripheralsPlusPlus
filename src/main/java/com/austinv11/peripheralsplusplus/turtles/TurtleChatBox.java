package com.austinv11.peripheralsplusplus.turtles;

import com.austinv11.peripheralsplusplus.init.ModBlocks;
import com.austinv11.peripheralsplusplus.reference.Reference;
import com.austinv11.peripheralsplusplus.tiles.TileEntityChatBox;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.*;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class TurtleChatBox implements ITurtleUpgrade {

	TileEntityChatBox chatBox;

	@Override
	public int getUpgradeID() {
		return Reference.CHAT_BOX_UPGRADE;
	}

	@Override
	public String getUnlocalisedAdjective() {
		return "Chatty";
	}

	@Override
	public TurtleUpgradeType getType() {
		return TurtleUpgradeType.Peripheral;
	}

	@Override
	public ItemStack getCraftingItem() {
		return new ItemStack(ModBlocks.chatBox);
	}

	@Override
	public IPeripheral createPeripheral(ITurtleAccess turtle, TurtleSide side) {
		chatBox = new TileEntityChatBox(turtle);
		return chatBox;
	}

	@Override
	public TurtleCommandResult useTool(ITurtleAccess turtle, TurtleSide side, TurtleVerb verb, int direction) {
		return null;
	}

	@Override
	public IIcon getIcon(ITurtleAccess turtle, TurtleSide side) {
		return ModBlocks.chatBox.getIcon(1, 0);
	}

	@Override
	public void update(ITurtleAccess turtle, TurtleSide side) {
		if (chatBox != null)
			chatBox.updateEntity(true);
	}
}
