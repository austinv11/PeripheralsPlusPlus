package com.austinv11.peripheralsplusplus.event;

import com.austinv11.collectiveframework.minecraft.CollectiveFramework;
import com.austinv11.peripheralsplusplus.PeripheralsPlusPlus;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.client.Minecraft;

public class VersionCheckHandler {
	
	@SubscribeEvent
	public void onTick(TickEvent.ClientTickEvent event) {
		if (event.phase == TickEvent.Phase.END && Minecraft.getMinecraft().thePlayer != null && !CollectiveFramework.IS_DEV_ENVIRONMENT) {
			if (PeripheralsPlusPlus.tries > 0 && PeripheralsPlusPlus.versionMessage != null) {
				Minecraft.getMinecraft().thePlayer.addChatComponentMessage(PeripheralsPlusPlus.versionMessage);
				PeripheralsPlusPlus.tries--;
			}
		}
	}
}
