package com.austinv11.peripheralsplusplus.satellites;

import com.austinv11.peripheralsplusplus.api.satellites.ISatellite;
import com.austinv11.peripheralsplusplus.entities.EntityRocket;
import com.austinv11.peripheralsplusplus.event.SateliiteCrashEvent;
import com.austinv11.peripheralsplusplus.reference.Config;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;

import java.util.List;
import java.util.Random;

public class SatelliteTickHandler {

	private static Random rng = new Random();

	@SubscribeEvent
	public void onServerTick(TickEvent.ServerTickEvent event) {
		for (Integer i : Config.dimWhitelist) {
			List<ISatellite> satellites = SatelliteData.forWorld(MinecraftServer.getServer().worldServerForDimension(i)).getSatellites();
			for (ISatellite sat : satellites) {
				if (doDrop(sat.getPosition().posY)) {
					MinecraftForge.EVENT_BUS.post(new SateliiteCrashEvent(sat, sat.getPosition()));
					sat.recall(true, null);
					continue;
				}
				sat.getMainUpgrade().update(sat);
			}
		}
	}

	private boolean doDrop(int height) {
		return height == EntityRocket.MAX_HEIGHT || (height/EntityRocket.MAX_HEIGHT) < rng.nextGaussian();
	}
}
