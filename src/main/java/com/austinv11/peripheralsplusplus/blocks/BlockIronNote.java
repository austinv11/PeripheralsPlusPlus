package com.austinv11.peripheralsplusplus.blocks;

import com.austinv11.peripheralsplusplus.tile.TileIronNote;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockIronNote extends BlockPPP implements ITileEntityProvider {
	public BlockIronNote() {
		super(Material.iron);
	}

	@Override
	public String getName() {
		return "blockIronNote";
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileIronNote();
	}
}
