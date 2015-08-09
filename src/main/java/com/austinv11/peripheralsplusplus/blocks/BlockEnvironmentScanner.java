package com.austinv11.peripheralsplusplus.blocks;

import com.austinv11.peripheralsplusplus.tiles.TileEntityEnvironmentScanner;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralProvider;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockEnvironmentScanner extends BlockPPP implements ITileEntityProvider {

	public BlockEnvironmentScanner() {
		super();
		this.setBlockName("environmentScanner");
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileEntityEnvironmentScanner();
	}

	@Override
	public boolean hasTileEntity(int metadata) {
		return true;
	}

}
