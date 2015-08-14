package com.austinv11.peripheralsplusplus.blocks;

import com.austinv11.peripheralsplusplus.tiles.TileEntityNoteBlock;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockNote extends BlockPPP implements ITileEntityProvider {

	public BlockNote() {
		super();
		this.setBlockName("noteBlock");
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileEntityNoteBlock();
	}

}
