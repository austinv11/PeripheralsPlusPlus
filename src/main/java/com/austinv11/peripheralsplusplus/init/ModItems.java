package com.austinv11.peripheralsplusplus.init;

import com.austinv11.peripheralsplusplus.items.PPPItem;
import com.austinv11.peripheralsplusplus.items.UpgradeBase;
import com.austinv11.peripheralsplusplus.items.UpgradeCompass;
import com.austinv11.peripheralsplusplus.reference.Reference;
import cpw.mods.fml.common.registry.GameRegistry;

@GameRegistry.ObjectHolder(Reference.MOD_ID)
public class ModItems {
	public static final PPPItem upgradeBase = new UpgradeBase();
	public static final PPPItem upgradeCompass = new UpgradeCompass();

	public static void init(){
		GameRegistry.registerItem(upgradeBase, "upgradeBase");
		GameRegistry.registerItem(upgradeCompass, "upgradeCompass");
	}
}
