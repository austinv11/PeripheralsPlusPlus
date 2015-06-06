package com.austinv11.peripheralsplusplus.blocks;

import com.austinv11.peripheralsplusplus.init.ModBlocks;
import com.austinv11.peripheralsplusplus.tiles.TileEntityAnalyzerBee;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockAnalyzerBee extends BlockAnalyzer {
	
	public BlockAnalyzerBee() {
		super();
		this.setBlockName("beeAnalyzer");
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileEntityAnalyzerBee();
	}

	public Block getBlock(){
		return ModBlocks.beeAnalyzer;
	}
}
