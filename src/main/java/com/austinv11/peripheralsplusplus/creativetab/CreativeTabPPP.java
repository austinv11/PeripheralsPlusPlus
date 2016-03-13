package com.austinv11.peripheralsplusplus.creativetab;

import com.austinv11.peripheralsplusplus.init.ModBlocks;
import com.austinv11.peripheralsplusplus.reference.Reference;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class CreativeTabPPP {
	public static final CreativeTabs TAB_PPP = new CreativeTabs(Reference.MOD_ID.toLowerCase()) {
		@Override
		public Item getTabIconItem() {
			return Item.getItemFromBlock(ModBlocks.blockTest);
		}
	};
}
