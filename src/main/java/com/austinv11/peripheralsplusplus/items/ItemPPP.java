package com.austinv11.peripheralsplusplus.items;

import com.austinv11.collectiveframework.minecraft.items.ItemBase;
import com.austinv11.peripheralsplusplus.creativetab.CreativeTabPPP;
import com.austinv11.peripheralsplusplus.reference.Reference;
import net.minecraft.creativetab.CreativeTabs;

public class ItemPPP extends ItemBase {
	
	@Override
	public CreativeTabs getTab() {
		return CreativeTabPPP.PPP_TAB;
	}
	
	@Override
	public String getModId() {
		return Reference.MOD_ID;
	}
}
