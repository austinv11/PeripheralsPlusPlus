package com.austinv11.peripheralsplusplus.satellites;

import com.austinv11.peripheralsplusplus.api.satellites.ISatellite;
import com.austinv11.peripheralsplusplus.api.satellites.upgrades.ISatelliteUpgrade;
import com.austinv11.peripheralsplusplus.reference.Config;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.server.MinecraftServer;

import java.util.List;

public class SatelliteTickHandler {

	@SubscribeEvent
	public void onServerTick(TickEvent.ServerTickEvent event) {
		for (Integer i : Config.dimWhitelist) {
			List<ISatellite> satellites = SatelliteData.forWorld(MinecraftServer.getServer().worldServerForDimension(i)).getSatellites();
			for (ISatellite sat : satellites) {
				sat.getMainUpgrade().update(sat);
				for (ISatelliteUpgrade up : sat.getAddons())
					up.update(sat);
			}
		}
	}
}
