package com.austinv11.peripheralsplusplus.blocks;

import com.austinv11.collectiveframework.minecraft.blocks.BlockBase;
import com.austinv11.peripheralsplusplus.creativetab.CreativeTabPPP;
import com.austinv11.peripheralsplusplus.reference.Reference;
import net.minecraft.creativetab.CreativeTabs;

public class BlockPppBase extends BlockBase implements BlockPpp {

	@Override
	public CreativeTabs getTab() {
		return CreativeTabPPP.PPP_TAB;
	}
	
	@Override
	public String getModId() {
		return Reference.MOD_ID;
	}
}
