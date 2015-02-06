package com.austinv11.peripheralsplusplus.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

public class JoinPacket implements IMessage {

	public JoinPacket() {}

	public JoinPacket(EntityPlayer player, int height, int width) {

	}

	@Override
	public void fromBytes(ByteBuf buf) {

	}

	@Override
	public void toBytes(ByteBuf buf) {

	}

	public static class JoinPacketHandler implements IMessageHandler<JoinPacket, IMessage> {

		@Override
		public IMessage onMessage(JoinPacket message, MessageContext ctx) {
			return null;
		}
	}
}
