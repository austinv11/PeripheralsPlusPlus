package com.austinv11.peripheralsplusplus.init;

import com.austinv11.peripheralsplusplus.blocks.ChatBox;
import com.austinv11.peripheralsplusplus.blocks.PPPBlock;
import com.austinv11.peripheralsplusplus.blocks.PlayerSensor;
import com.austinv11.peripheralsplusplus.blocks.RFCharger;
import com.austinv11.peripheralsplusplus.reference.Reference;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;

@GameRegistry.ObjectHolder(Reference.MOD_ID)
public class ModBlocks {
	public static final PPPBlock chatBox = new ChatBox();
	public static final PPPBlock playerSensor = new PlayerSensor();
	public static PPPBlock rfCharger;

	public static void init(){
		GameRegistry.registerBlock(chatBox, "chatBox");
		GameRegistry.registerBlock(playerSensor, "playerSensor");
		if (Loader.isModLoaded("ThermalExpansion")) {
			rfCharger = new RFCharger();
			GameRegistry.registerBlock(rfCharger, "rfCharger");
		}
	}
}
