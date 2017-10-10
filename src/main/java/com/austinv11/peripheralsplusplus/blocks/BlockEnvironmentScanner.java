package com.austinv11.peripheralsplusplus.blocks;

import com.austinv11.peripheralsplusplus.reference.Reference;
import com.austinv11.peripheralsplusplus.tiles.TileEntityEnvironmentScanner;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockEnvironmentScanner extends BlockPppBase implements ITileEntityProvider {

	public BlockEnvironmentScanner() {
		super();
		this.setRegistryName(Reference.MOD_ID, "environment_scanner");
		this.setUnlocalizedName("environment_scanner");
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileEntityEnvironmentScanner();
	}

}
