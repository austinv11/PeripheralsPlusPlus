package com.austinv11.peripheralsplusplus.proxy;

import com.austinv11.peripheralsplusplus.tiles.TileEntityChatBox;
import com.austinv11.peripheralsplusplus.tiles.TileEntityMEBridge;
import com.austinv11.peripheralsplusplus.tiles.TileEntityPlayerSensor;
import cpw.mods.fml.common.registry.GameRegistry;

public class CommonProxy {

	public void registerTileEntities() {
		GameRegistry.registerTileEntity(TileEntityChatBox.class, TileEntityChatBox.publicName);
		GameRegistry.registerTileEntity(TileEntityPlayerSensor.class, TileEntityPlayerSensor.publicName);
		GameRegistry.registerTileEntity(TileEntityMEBridge.class, TileEntityMEBridge.publicName);
	}
}
