package com.austinv11.peripheralsplusplus.init;

import com.austinv11.peripheralsplusplus.items.FeederUpgrade;
import com.austinv11.peripheralsplusplus.items.PPPItem;
import com.austinv11.peripheralsplusplus.items.SatelliteUpgradeBase;
import com.austinv11.peripheralsplusplus.reference.Reference;
import cpw.mods.fml.common.registry.GameRegistry;

import java.util.ArrayList;
import java.util.List;

@GameRegistry.ObjectHolder(Reference.MOD_ID)
public class ModItems {

	public static final PPPItem feederUpgrade = new FeederUpgrade();
	public static final List<SatelliteUpgradeBase> SATELLITE_UPGRADE_REGISTRY = new ArrayList<SatelliteUpgradeBase>();

	public static void preInit(){
		GameRegistry.registerItem(feederUpgrade, "feederUpgrade");
	}

	public static void init() {
		for (int i = 0; i < SATELLITE_UPGRADE_REGISTRY.size(); i++)
			GameRegistry.registerItem(SATELLITE_UPGRADE_REGISTRY.get(i), SATELLITE_UPGRADE_REGISTRY.get(i).getUpgrade().getName());
	}
}
