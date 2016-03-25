package com.austinv11.peripheralsplusplus.init;

import com.austinv11.peripheralsplusplus.items.ItemPPP;
import com.austinv11.peripheralsplusplus.items.ItemPermCard;
import com.austinv11.peripheralsplusplus.reference.Config;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModItems {
	public static final ItemPPP itemPermCard = new ItemPermCard();

	public static void init() {
		if (Config.enablePlayerInterface) GameRegistry.registerItem(itemPermCard, itemPermCard.getName());
	}
}
