package com.austinv11.peripheralsplusplus.blocks;

import com.austinv11.peripheralsplusplus.tile.TileTest;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockTest extends BlockPPP implements ITileEntityProvider {
	public BlockTest() {
		super(Material.iron);
	}

	@Override
	public String getName() {
		return "blockTest";
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileTest();
	}
}
