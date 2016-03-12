package com.austinv11.peripheralsplusplus.client;

import com.austinv11.peripheralsplusplus.items.ItemPPP;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;

public class ItemRenderRegister {
	public static void init() {

	}

	private void register(ItemPPP item) {
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0, new ModelResourceLocation(item.getUnlocalizedName(), "inventory"));
	}
}
