package com.austinv11.peripheralsplusplus.proxy;

import com.austinv11.peripheralsplusplus.tiles.TileEntityChatBox;
import cpw.mods.fml.common.registry.GameRegistry;

public class CommonProxy {

	public void registerTileEntities() {
		GameRegistry.registerTileEntity(TileEntityChatBox.class, TileEntityChatBox.publicName);
	}
}
