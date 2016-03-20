package com.austinv11.peripheralsplusplus.init;

import com.austinv11.peripheralsplusplus.blocks.*;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModBlocks {
	public static final BlockPPP blockTest = new BlockTest();
	public static final BlockPPP blockOreDict = new BlockOreDict();
	public static final BlockPPP blockEnvScanner = new BlockEnvScanner();
	public static final BlockPPP blockPlayerSensor = new BlockPlayerSensor();

	public static void init() {
		GameRegistry.registerBlock(blockTest, blockTest.getName());
		GameRegistry.registerBlock(blockOreDict, blockOreDict.getName());
		GameRegistry.registerBlock(blockEnvScanner, blockEnvScanner.getName());
		GameRegistry.registerBlock(blockPlayerSensor, blockPlayerSensor.getName());
	}
}
