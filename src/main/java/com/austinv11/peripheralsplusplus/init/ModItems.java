package com.austinv11.peripheralsplusplus.init;

import com.austinv11.peripheralsplusplus.PeripheralsPlusPlus;
import com.austinv11.peripheralsplusplus.items.ItemFeederUpgrade;
import com.austinv11.peripheralsplusplus.items.ItemRocket;
import com.austinv11.peripheralsplusplus.items.ItemSatellite;
import com.austinv11.peripheralsplusplus.items.ItemPPP;
import com.austinv11.peripheralsplusplus.reference.Reference;
import cpw.mods.fml.common.registry.GameRegistry;

@GameRegistry.ObjectHolder(Reference.MOD_ID)
public class ModItems {

	public static final ItemPPP feederUpgrade = new ItemFeederUpgrade();
	public static final ItemPPP satellite = new ItemSatellite();
	public static final ItemPPP rocket = new ItemRocket();

	public static void preInit(){
		GameRegistry.registerItem(feederUpgrade, "feederUpgrade");
		GameRegistry.registerItem(satellite, "satellite");
		GameRegistry.registerItem(rocket, "rocket");
	}

	public static void init() {
		for (int i = 0; i < PeripheralsPlusPlus.SATELLITE_UPGRADE_REGISTRY.size(); i++)
			GameRegistry.registerItem(PeripheralsPlusPlus.SATELLITE_UPGRADE_REGISTRY.get(i), PeripheralsPlusPlus.SATELLITE_UPGRADE_REGISTRY.get(i).getUpgrade().getUnlocalisedName());
	}
}
