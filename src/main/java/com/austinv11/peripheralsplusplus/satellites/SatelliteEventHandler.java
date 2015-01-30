package com.austinv11.peripheralsplusplus.satellites;

import com.austinv11.peripheralsplusplus.api.PeripheralsPlusPlusAPI;
import com.austinv11.peripheralsplusplus.api.satellites.upgrades.ISatelliteUpgrade;
import com.austinv11.peripheralsplusplus.api.satellites.upgrades.SatelliteUpgradeType;
import com.austinv11.peripheralsplusplus.event.SatelliteLaunchEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.item.ItemStack;

import java.util.List;

public class SatelliteEventHandler {

	@SubscribeEvent
	public void onSatelliteLaunch(SatelliteLaunchEvent event) {
		if (SatelliteData.isWorldWhitelisted(event.world)) {
			SatelliteData data = SatelliteData.forWorld(event.world);
			if (data.getSatelliteForCoords(event.coords.posX, event.coords.posZ) == null) {
				Satellite sat = new Satellite(event.coords.posX, event.y, event.coords.posZ, event.world);
				ItemStack stack = event.rocket.getStackInSlot(0);
				List<ISatelliteUpgrade> upgrades = PeripheralsPlusPlusAPI.getUpgradesFromItemStack(stack);
				if (upgrades != null)
					for (ISatelliteUpgrade upgrade : upgrades)
						try {
							if (upgrade.getType() == SatelliteUpgradeType.MAIN)
								sat.setMainUpgrade(upgrade);
							else {
								List<ISatelliteUpgrade> addons = sat.getAddons();
								addons.add(upgrade);
								sat.setAddons(addons);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
				data.addSatellite(sat);
				data.markDirty();
			}
		}
	}
}
