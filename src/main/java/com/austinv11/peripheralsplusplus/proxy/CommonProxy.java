package com.austinv11.peripheralsplusplus.proxy;

import com.austinv11.peripheralsplusplus.tile.*;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CommonProxy {
	public void registerTileEntities() {
		GameRegistry.registerTileEntity(TileTest.class, TileTest.name);
		GameRegistry.registerTileEntity(TileOreDict.class, TileOreDict.name);
		GameRegistry.registerTileEntity(TileEnvScanner.class, TileEnvScanner.name);
		GameRegistry.registerTileEntity(TilePlayerSensor.class, TilePlayerSensor.name);
		GameRegistry.registerTileEntity(TileChatBox.class, TileChatBox.name);
		GameRegistry.registerTileEntity(TileSorter.class, TileSorter.name);
		GameRegistry.registerTileEntity(TilePlayerInterface.class, TilePlayerInterface.name);
	}

	public void setupItemRenderer() {
	}
}
