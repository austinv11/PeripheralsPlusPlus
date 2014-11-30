package com.austinv11.peripheralsplusplus.init;

import com.austinv11.peripheralsplusplus.blocks.*;
import com.austinv11.peripheralsplusplus.reference.Reference;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.BlockContainer;

@GameRegistry.ObjectHolder(Reference.MOD_ID)
public class ModBlocks {
	public static final BlockPPP chatBox = new BlockChatBox();
	public static final BlockPPP playerSensor = new BlockPlayerSensor();
	public static BlockPPP rfCharger;
	public static final BlockPPP oreDictionary = new BlockOreDictionary();
	public static BlockContainer beeAnalyzer;
	public static BlockContainer treeAnalyzer;
	public static BlockContainer butterflyAnalyzer;
	public static final BlockPPP teleporter = new BlockTeleporter();
	public static final BlockPPP teleporterT2 = new BlockTeleporterT2();
	public static final BlockPPP environmentScanner = new BlockEnvironmentScanner();

	public static void init(){
		GameRegistry.registerBlock(chatBox, "chatBox");
		GameRegistry.registerBlock(playerSensor, "playerSensor");
		if (Loader.isModLoaded("ThermalExpansion")) {
			rfCharger = new BlockRFCharger();
			GameRegistry.registerBlock(rfCharger, "rfCharger");
		}
		GameRegistry.registerBlock(oreDictionary, "oreDictionary");
		if (Loader.isModLoaded("Forestry")) {
			beeAnalyzer = new BlockAnalyzerBee();
			GameRegistry.registerBlock(beeAnalyzer, "beeAnalyzer");
			treeAnalyzer = new BlockAnalyzerTree();
			GameRegistry.registerBlock(treeAnalyzer, "treeAnalyzer");
			butterflyAnalyzer = new BlockAnalyzerButterfly();
			GameRegistry.registerBlock(butterflyAnalyzer, "butterflyAnalyzer");
		}
		GameRegistry.registerBlock(teleporter, "teleporter");
		GameRegistry.registerBlock(teleporterT2, "teleporterT2");
		GameRegistry.registerBlock(environmentScanner, "environmentScanner");
	}
}
