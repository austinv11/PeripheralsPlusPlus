package com.austinv11.peripheralsplusplus.init;

import com.austinv11.peripheralsplusplus.blocks.*;
import com.austinv11.peripheralsplusplus.reference.Reference;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.BlockContainer;

@GameRegistry.ObjectHolder(Reference.MOD_ID)
public class ModBlocks {
	public static final PPPBlock chatBox = new ChatBox();
	public static final PPPBlock playerSensor = new PlayerSensor();
	public static PPPBlock rfCharger;
	public static final PPPBlock oreDictionary = new OreDictionaryBlock();
	public static final BlockContainer beeAnalyzer = new AnalyzerBee();

	public static void init(){
		GameRegistry.registerBlock(chatBox, "chatBox");
		GameRegistry.registerBlock(playerSensor, "playerSensor");
		if (Loader.isModLoaded("ThermalExpansion")) {
			rfCharger = new RFCharger();
			GameRegistry.registerBlock(rfCharger, "rfCharger");
		}
		GameRegistry.registerBlock(oreDictionary, "oreDictionary");
		GameRegistry.registerBlock(beeAnalyzer, "beeAnalyzer");
	}
}
