package com.austinv11.peripheralsplusplus.network;

import com.austinv11.peripheralsplusplus.tiles.TileEntityAntenna;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import dan200.computercraft.api.peripheral.IComputerAccess;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;

import java.util.UUID;

public class TextFieldInputEventPacket implements IMessage {

	public UUID uuid;
	public String event, player, text, key;

	public TextFieldInputEventPacket() {}

	public TextFieldInputEventPacket(UUID uuid, String key, String text, String event, String player) {
		this.uuid = uuid;
		this.key = key;
		this.text = text;
		this.event = event;
		this.player = player;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		NBTTagCompound tag = ByteBufUtils.readTag(buf);
		uuid = UUID.fromString(tag.getString("uuid"));
		key = tag.getString("key");
		event = tag.getString("event");
		player = tag.getString("player");
		text = tag.getString("text");
	}

	@Override
	public void toBytes(ByteBuf buf) {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setString("uuid", uuid.toString());
		tag.setString("key", key);
		tag.setString("event", event);
		tag.setString("player", player);
		tag.setString("text", text);
		ByteBufUtils.writeTag(buf, tag);
	}

	public static class TextFieldInputEventPacketHandler implements IMessageHandler<TextFieldInputEventPacket, IMessage> {

		@Override
		public IMessage onMessage(TextFieldInputEventPacket message, MessageContext ctx) {
			TileEntityAntenna antenna = TileEntityAntenna.antenna_registry.get(message.uuid);
			if (antenna != null) {
				for (IComputerAccess computer : antenna.computers.keySet())
					computer.queueEvent(message.event, new Object[]{message.player, message.key, message.text});
			}
			return null;
		}
	}
}
