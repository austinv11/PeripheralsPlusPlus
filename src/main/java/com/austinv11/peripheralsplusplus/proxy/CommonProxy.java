package com.austinv11.peripheralsplusplus.proxy;

import com.austinv11.peripheralsplusplus.tiles.TileEntityChatBox;
import com.austinv11.peripheralsplusplus.tiles.TileEntityPlayerSensor;
import com.austinv11.peripheralsplusplus.tiles.TileEntityRFCharger;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;

public class CommonProxy {

	public void registerTileEntities() {
		GameRegistry.registerTileEntity(TileEntityChatBox.class, TileEntityChatBox.publicName);
		GameRegistry.registerTileEntity(TileEntityPlayerSensor.class, TileEntityPlayerSensor.publicName);
		if (Loader.isModLoaded("ThermalExpansion"))
			GameRegistry.registerTileEntity(TileEntityRFCharger.class, TileEntityRFCharger.publicName);
	}
}
