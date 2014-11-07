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
	public static BlockContainer beeAnalyzer;
	public static BlockContainer treeAnalyzer;
	public static BlockContainer butterflyAnalyzer;
	public static final PPPBlock teleporter = new Teleporter();
	public static final PPPBlock teleporterT2 = new TeleporterT2();

	public static void init(){
		GameRegistry.registerBlock(chatBox, "chatBox");
		GameRegistry.registerBlock(playerSensor, "playerSensor");
		if (Loader.isModLoaded("ThermalExpansion")) {
			rfCharger = new RFCharger();
			GameRegistry.registerBlock(rfCharger, "rfCharger");
		}
		GameRegistry.registerBlock(oreDictionary, "oreDictionary");
		if (Loader.isModLoaded("Forestry")) {
			beeAnalyzer = new AnalyzerBee();
			GameRegistry.registerBlock(beeAnalyzer, "beeAnalyzer");
			treeAnalyzer = new AnalyzerTree();
			GameRegistry.registerBlock(treeAnalyzer, "treeAnalyzer");
			butterflyAnalyzer = new AnalyzerButterfly();
			GameRegistry.registerBlock(butterflyAnalyzer, "butterflyAnalyzer");
		}
		GameRegistry.registerBlock(teleporter, "teleporter");
		GameRegistry.registerBlock(teleporterT2, "teleporterT2");
	}
}
