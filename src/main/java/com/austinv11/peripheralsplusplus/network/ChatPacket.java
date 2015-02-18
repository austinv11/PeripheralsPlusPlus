package com.austinv11.peripheralsplusplus.network;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

public class ChatPacket implements IMessage {

	public String text;

	public ChatPacket() {}

	public ChatPacket(String text) {
		this.text = text;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		text = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, text);
	}

	public static class ChatPacketHandler implements IMessageHandler<ChatPacket, IMessage> {

		@Override
		public IMessage onMessage(ChatPacket message, MessageContext ctx) {
			Minecraft.getMinecraft().thePlayer.addChatComponentMessage(new ChatComponentText(message.text));
			return null;
		}
	}
}
