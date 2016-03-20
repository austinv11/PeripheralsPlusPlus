package com.austinv11.peripheralsplusplus.client;

import com.austinv11.peripheralsplusplus.init.ModBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;

public class ItemRenderRegister {
	public static void init() {
		register(Item.getItemFromBlock(ModBlocks.blockTest));
		register(Item.getItemFromBlock(ModBlocks.blockOreDict));
		register(Item.getItemFromBlock(ModBlocks.blockEnvScanner));
		register(Item.getItemFromBlock(ModBlocks.blockPlayerSensor));
	}

	private static void register(Item item) {
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0, new ModelResourceLocation(item.getUnlocalizedName().substring(5), "inventory"));
	}
}
