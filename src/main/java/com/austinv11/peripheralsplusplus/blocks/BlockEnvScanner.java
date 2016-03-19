package com.austinv11.peripheralsplusplus.blocks;

import com.austinv11.peripheralsplusplus.tile.TileEnvScanner;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockEnvScanner extends BlockPPP implements ITileEntityProvider {

	public BlockEnvScanner() {
		super(Material.iron);
	}

	@Override
	public String getName() {
		return "blockEnvScanner";
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEnvScanner();
	}
}
