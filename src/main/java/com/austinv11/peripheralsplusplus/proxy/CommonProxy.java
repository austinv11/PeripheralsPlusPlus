package com.austinv11.peripheralsplusplus.proxy;

import com.austinv11.peripheralsplusplus.tile.TileTest;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CommonProxy {
	public void registerTileEntities() {
		GameRegistry.registerTileEntity(TileTest.class, TileTest.name);
	}

	public void setupItemRenderer() {}
}
