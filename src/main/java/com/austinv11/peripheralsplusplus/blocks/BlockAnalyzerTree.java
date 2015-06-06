package com.austinv11.peripheralsplusplus.blocks;

import com.austinv11.peripheralsplusplus.init.ModBlocks;
import com.austinv11.peripheralsplusplus.tiles.TileEntityAnalyzerTree;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockAnalyzerTree extends BlockAnalyzer {
	
	public BlockAnalyzerTree() {
		super();
		this.setBlockName("treeAnalyzer");
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileEntityAnalyzerTree();
	}

	public Block getBlock(){
		return ModBlocks.treeAnalyzer;
	}
}
