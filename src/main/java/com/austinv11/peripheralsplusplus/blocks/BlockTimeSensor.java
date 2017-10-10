package com.austinv11.peripheralsplusplus.blocks;

import com.austinv11.peripheralsplusplus.reference.Reference;
import com.austinv11.peripheralsplusplus.tiles.TileEntityTimeSensor;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockTimeSensor extends BlockPppBase implements ITileEntityProvider {

	public BlockTimeSensor() {
		super();
		this.setRegistryName(Reference.MOD_ID, "time_sensor");
		this.setUnlocalizedName("time_sensor");
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileEntityTimeSensor();
	}
}
