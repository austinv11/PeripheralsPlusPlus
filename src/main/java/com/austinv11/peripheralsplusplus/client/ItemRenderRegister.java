package com.austinv11.peripheralsplusplus.client;

import com.austinv11.peripheralsplusplus.init.ModBlocks;
import com.austinv11.peripheralsplusplus.reference.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;

public class ItemRenderRegister {
	public static void init() {
		register(Item.getItemFromBlock(ModBlocks.blockTest));
		if (Config.enableOreDict) register(Item.getItemFromBlock(ModBlocks.blockOreDict));
		if (Config.enableEnvScanner) register(Item.getItemFromBlock(ModBlocks.blockEnvScanner));
		if (Config.enablePlayerSensor) register(Item.getItemFromBlock(ModBlocks.blockPlayerSensor));
		if (Config.enableChatBox) register(Item.getItemFromBlock(ModBlocks.blockChatBox));
	}

	private static void register(Item item) {
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0, new ModelResourceLocation(item.getUnlocalizedName().substring(5), "inventory"));
	}
}
