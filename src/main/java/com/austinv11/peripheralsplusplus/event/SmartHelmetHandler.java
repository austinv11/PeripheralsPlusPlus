package com.austinv11.peripheralsplusplus.event;

import com.austinv11.peripheralsplusplus.PeripheralsPlusPlus;
import com.austinv11.peripheralsplusplus.network.JoinPacket;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

public class SmartHelmetHandler {

	@SubscribeEvent
	public void onJoin(EntityJoinWorldEvent event) {
		if (event.entity instanceof EntityPlayer)
			if (event.world.isRemote)
				PeripheralsPlusPlus.NETWORK.sendToServer(new JoinPacket((EntityPlayer) event.entity, Minecraft.getMinecraft().displayHeight,  Minecraft.getMinecraft().displayWidth));
	}
}
