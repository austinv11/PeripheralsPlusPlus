package com.austinv11.peripheralsplusplus.blocks;

import com.austinv11.peripheralsplusplus.reference.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public abstract class BlockPPP extends Block {

	public BlockPPP(Material material) {
		super(material);
		this.setUnlocalizedName(Reference.MOD_ID.toLowerCase() + ":" + getName());
	}

	public abstract String getName();
}
