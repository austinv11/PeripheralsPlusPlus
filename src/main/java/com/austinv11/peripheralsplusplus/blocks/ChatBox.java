package com.austinv11.peripheralsplusplus.blocks;

import com.austinv11.peripheralsplusplus.tiles.TileEntityChatBox;
import com.austinv11.peripheralsplusplus.utils.Logger;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralProvider;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class ChatBox extends PPPBlock implements ITileEntityProvider, IPeripheralProvider {

	public ChatBox () {
		super();
		this.setBlockName("chatBox");
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		//Logger.info("test");
		return new TileEntityChatBox(p_149915_1_);
	}

	@Override
	public boolean hasTileEntity(int metadata) {
		return true;
	}

	@Override
	public IPeripheral getPeripheral(World world, int x, int y, int z, int side ) {
		return new TileEntityChatBox(world);
	}
}
