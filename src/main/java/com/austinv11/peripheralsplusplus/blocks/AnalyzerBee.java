package com.austinv11.peripheralsplusplus.blocks;

import com.austinv11.peripheralsplusplus.tiles.TileEntityAnalyzerBee;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class AnalyzerBee extends Analyzer {

	public AnalyzerBee() {
		super();
		this.setBlockName("beeAnalyzer");
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileEntityAnalyzerBee();
	}
}
