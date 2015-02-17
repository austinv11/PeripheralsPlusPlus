package com.austinv11.peripheralsplusplus.items;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

public class ItemBlockPeripheralContainer extends ItemBlock {
	
	public ItemBlockPeripheralContainer(Block block) {
		super(block);
		this.maxStackSize = 1;
	}

//	@Override
//	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
//		if (!world.isRemote) {
//
//		}
//		return super.onItemUseFirst(stack, player, world, x, y, z, side, hitX, hitY, hitZ);
//	}
}
