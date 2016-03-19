package com.austinv11.peripheralsplusplus.proxy;

import com.austinv11.peripheralsplusplus.tile.TileEnvScanner;
import com.austinv11.peripheralsplusplus.tile.TileOreDict;
import com.austinv11.peripheralsplusplus.tile.TileTest;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CommonProxy {
	public void registerTileEntities() {
		GameRegistry.registerTileEntity(TileTest.class, TileTest.name);
		GameRegistry.registerTileEntity(TileOreDict.class, TileOreDict.name);
		GameRegistry.registerTileEntity(TileEnvScanner.class, TileEnvScanner.name);
	}

	public void setupItemRenderer() {
	}
}
