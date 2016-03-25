package com.austinv11.peripheralsplusplus.init;

import com.austinv11.peripheralsplusplus.blocks.*;
import com.austinv11.peripheralsplusplus.reference.Config;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModBlocks {
	public static final BlockPPP blockTest = new BlockTest();
	public static final BlockPPP blockOreDict = new BlockOreDict();
	public static final BlockPPP blockEnvScanner = new BlockEnvScanner();
	public static final BlockPPP blockPlayerSensor = new BlockPlayerSensor();
	public static final BlockPPP blockChatBox = new BlockChatBox();
	public static final BlockPPP blockSorter = new BlockSorter();
	public static final BlockPPP blockPlayerInterface = new BlockPlayerInterface();

	public static void init() {
		GameRegistry.registerBlock(blockTest, blockTest.getName());
		if (Config.enableOreDict) GameRegistry.registerBlock(blockOreDict, blockOreDict.getName());
		if (Config.enableEnvScanner) GameRegistry.registerBlock(blockEnvScanner, blockEnvScanner.getName());
		if (Config.enablePlayerSensor) GameRegistry.registerBlock(blockPlayerSensor, blockPlayerSensor.getName());
		if (Config.enableChatBox) GameRegistry.registerBlock(blockChatBox, blockChatBox.getName());
		if (Config.enableSorter) GameRegistry.registerBlock(blockSorter, blockSorter.getName());
		if (Config.enablePlayerInterface)
			GameRegistry.registerBlock(blockPlayerInterface, blockPlayerInterface.getName());
	}
}
