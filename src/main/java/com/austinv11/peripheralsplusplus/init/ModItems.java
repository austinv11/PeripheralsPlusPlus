package com.austinv11.peripheralsplusplus.init;

import com.austinv11.peripheralsplusplus.PeripheralsPlusPlus;
import com.austinv11.peripheralsplusplus.items.*;
import com.austinv11.peripheralsplusplus.reference.Reference;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;

@GameRegistry.ObjectHolder(Reference.MOD_ID)
public class ModItems {

	public static final ItemPPP feederUpgrade = new ItemFeederUpgrade();
	public static final ItemPPP satellite = new ItemSatellite();
	public static final ItemPPP rocket = new ItemRocket();
	public static final ItemPPP tank = new ItemTank();
	public static final Item smartHelmet = new ItemSmartHelmet();
	public static final ItemPPP socket = new ItemSocket();
	public static final ItemPPP positionalUnit = new ItemPositionalUnit();
	public static final ItemPPP nanoSwarm = new ItemNanoSwarm();
	public static final ItemPPP chunkLoaderUpgrade = new ItemChunkLoaderUpgrade();
    public static final ItemPPP permCard = new ItemPermissionsCard();
	public static final ItemPPP resupplyUpgrade = new ItemResupplyUpgrade();
	public static final ItemPPP motionDetector = new ItemMotionDetector();

	public static void preInit(){
		GameRegistry.registerItem(feederUpgrade, "feederUpgrade");
		GameRegistry.registerItem(satellite, "satellite");
		GameRegistry.registerItem(rocket, "rocket");
		GameRegistry.registerItem(tank, "tank");
		GameRegistry.registerItem(smartHelmet, "smartHelmet");
		GameRegistry.registerItem(socket, "socket");
		GameRegistry.registerItem(positionalUnit, "positionalUnit");
		GameRegistry.registerItem(nanoSwarm, "nanoSwarm");
		GameRegistry.registerItem(chunkLoaderUpgrade, "chunkLoaderUpgrade");
        GameRegistry.registerItem(permCard, "permCard");
		GameRegistry.registerItem(resupplyUpgrade, "resupplyUpgrade");
		GameRegistry.registerItem(motionDetector, "motionDetector");
    }

	public static void init() {
		for (int i = 0; i < PeripheralsPlusPlus.SATELLITE_UPGRADE_REGISTRY.size(); i++)
			GameRegistry.registerItem(PeripheralsPlusPlus.SATELLITE_UPGRADE_REGISTRY.get(i), PeripheralsPlusPlus.SATELLITE_UPGRADE_REGISTRY.get(i).getUpgrade().getUnlocalisedName());
	}
}
