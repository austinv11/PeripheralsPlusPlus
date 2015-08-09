package com.austinv11.peripheralsplusplus.blocks;

import com.austinv11.peripheralsplusplus.tiles.TileEntityChatBox;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralProvider;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockChatBox extends BlockPPP implements ITileEntityProvider {

	public BlockChatBox() {
		super();
		this.setBlockName("chatBox");
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		//Logger.info("test");
		return new TileEntityChatBox();
	}

	@Override
	public boolean hasTileEntity(int metadata) {
		return true;
	}

}
