package com.austinv11.peripheralsplusplus.proxy;

import com.austinv11.peripheralsplusplus.tiles.*;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;

public class CommonProxy {

	public void registerTileEntities() {
		GameRegistry.registerTileEntity(TileEntityChatBox.class, TileEntityChatBox.publicName);
		GameRegistry.registerTileEntity(TileEntityPlayerSensor.class, TileEntityPlayerSensor.publicName);
		if (Loader.isModLoaded("ThermalExpansion"))
			GameRegistry.registerTileEntity(TileEntityRFCharger.class, TileEntityRFCharger.publicName);
		GameRegistry.registerTileEntity(TileEntityOreDictionary.class, TileEntityOreDictionary.publicName);
		if (Loader.isModLoaded("Forestry")) {
			GameRegistry.registerTileEntity(TileEntityAnalyzerBee.class, TileEntityAnalyzerBee.publicName);
			GameRegistry.registerTileEntity(TileEntityAnalyzerButterfly.class, TileEntityAnalyzerButterfly.publicName);
			GameRegistry.registerTileEntity(TileEntityAnalyzerTree.class, TileEntityAnalyzerTree.publicName);
		}
		GameRegistry.registerTileEntity(TileEntityTeleporter.class, TileEntityTeleporter.publicName);
		GameRegistry.registerTileEntity(TileEntityTeleporterT2.class, TileEntityTeleporterT2.publicName+"T2");
		GameRegistry.registerTileEntity(TileEntityEnvironmentScanner.class, TileEntityEnvironmentScanner.publicName);
	}

	public void iconManagerInit() {}
}
