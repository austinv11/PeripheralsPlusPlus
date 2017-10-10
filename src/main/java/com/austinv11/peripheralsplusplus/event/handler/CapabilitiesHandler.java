package com.austinv11.peripheralsplusplus.event.handler;

import com.austinv11.peripheralsplusplus.capabilities.nano.CapabilityNanoBot;
import com.austinv11.peripheralsplusplus.capabilities.nano.NanoBotHolder;
import com.austinv11.peripheralsplusplus.reference.Config;
import com.austinv11.peripheralsplusplus.tiles.TileEntityAntenna;
import net.minecraft.entity.Entity;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CapabilitiesHandler {
	@SubscribeEvent
	public void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
		if (Config.enableNanoBots) {
			if (event.getObject().getCapability(CapabilityNanoBot.INSTANCE, null) == null) {
			    event.addCapability(CapabilityNanoBot.ID, new CapabilityNanoBot());
			    event.getCapabilities().get(CapabilityNanoBot.ID).getCapability(CapabilityNanoBot.INSTANCE, null)
						.setEntity(event.getObject());
			}
		}
	}

	@SubscribeEvent
	public void onEntityJoinWorld(EntityJoinWorldEvent event) {
		NanoBotHolder nanoBotHolder = event.getEntity().getCapability(CapabilityNanoBot.INSTANCE, null);
		if (nanoBotHolder != null) {
			if (nanoBotHolder.getAntenna() != null &&
					TileEntityAntenna.ANTENNA_REGISTRY.containsKey(nanoBotHolder.getAntenna())) {
				TileEntityAntenna tileEntityAntenna = TileEntityAntenna.ANTENNA_REGISTRY.get(
						nanoBotHolder.getAntenna());
				tileEntityAntenna.registerEntity(event.getEntity());
			}
		}
	}
}
