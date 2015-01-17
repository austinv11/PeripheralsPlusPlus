package com.austinv11.peripheralsplusplus.proxy;

import com.austinv11.peripheralsplusplus.PeripheralsPlusPlus;
import com.austinv11.peripheralsplusplus.tiles.*;
import com.austinv11.peripheralsplusplus.villagers.TradeHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.VillagerRegistry;

public class CommonProxy {

	public void setupVillagers() {
		while (VillagerRegistry.getRegisteredVillagers().contains(PeripheralsPlusPlus.VILLAGER_ID)) //Dynamic villager ids ftw?
			PeripheralsPlusPlus.VILLAGER_ID++;
		VillagerRegistry.instance().registerVillagerId(PeripheralsPlusPlus.VILLAGER_ID);
		VillagerRegistry.instance().registerVillageTradeHandler(PeripheralsPlusPlus.VILLAGER_ID, new TradeHandler());
	}

	public void registerTileEntities() {
		GameRegistry.registerTileEntity(TileEntityChatBox.class, TileEntityChatBox.publicName);
		GameRegistry.registerTileEntity(TileEntityPlayerSensor.class, TileEntityPlayerSensor.publicName);
		if (Loader.isModLoaded("ThermalExpansion") || Loader.isModLoaded("BuildCraft|Core"))
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
		GameRegistry.registerTileEntity(TileEntitySpeaker.class, TileEntitySpeaker.publicName);
	}

	public void iconManagerInit() {}

	public void registerRenderers() {}

	public void prepareGuis() {}
}
