package com.austinv11.peripheralsplusplus.items;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockPeripheralContainer extends ItemBlock {
	
	public ItemBlockPeripheralContainer(Block block) {
		super(block);
	}

	@Override
	public boolean doesContainerItemLeaveCraftingGrid(ItemStack p_77630_1_) {
		return false;
	}

	@Override
	public Item getContainerItem() {
		return this;
	}

	public boolean hasContainerItem(ItemStack stack) {
		return true;
	}
}
