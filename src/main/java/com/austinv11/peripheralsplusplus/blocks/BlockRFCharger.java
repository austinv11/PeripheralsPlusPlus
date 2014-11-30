package com.austinv11.peripheralsplusplus.blocks;

import com.austinv11.peripheralsplusplus.tiles.TileEntityRFCharger;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockRFCharger extends BlockPPP implements ITileEntityProvider {

	public BlockRFCharger() {
		super();
		this.setBlockName("rfCharger");
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileEntityRFCharger();
	}

	@Override
	public boolean hasTileEntity(int metadata) {
		return true;
	}
}
