package com.austinv11.peripheralsplusplus.network;

import com.austinv11.peripheralsplusplus.tiles.TileEntitySpeaker;
import com.austinv11.peripheralsplusplus.utils.ReflectionHelper;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.TurtleSide;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

public class AudioResponsePacket implements IMessage {

	public String text, lang;
	public int x,y,z;
	public World world;
	public TurtleSide side;

	public AudioResponsePacket(){}

	public AudioResponsePacket(String text, String lang, int x, int y, int z, World world, TurtleSide side) {
		this.text = text;
		this.lang = lang;
		this.x = x;
		this.y = y;
		this.z = z;
		this.world = world;
		this.side = side;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		NBTTagCompound tag = ByteBufUtils.readTag(buf);
		lang = tag.getString("lang");
		text = tag.getString("text");
		x = tag.getInteger("x");
		y = tag.getInteger("y");
		z = tag.getInteger("z");
		world = MinecraftServer.getServer().worldServerForDimension(tag.getInteger("dim"));
		side = tag.getString("side").equals("null") ? null : TurtleSide.valueOf(tag.getString("side"));
	}

	@Override
	public void toBytes(ByteBuf buf) {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setString("lang", lang);
		tag.setString("text", text);
		tag.setInteger("x", x);
		tag.setInteger("y", y);
		tag.setInteger("z", z);
		tag.setInteger("dim", world.provider.dimensionId);
		tag.setString("side", side == null ? "null" : side.name());
		ByteBufUtils.writeTag(buf, tag);
	}

	public static class AudioResponsePacketHandler implements IMessageHandler<AudioResponsePacket, IMessage> {

		@Override
		public IMessage onMessage(AudioResponsePacket message, MessageContext ctx) {
			if (message.side == null)
				((TileEntitySpeaker) message.world.getTileEntity(message.x, message.y, message.z)).onSpeechCompletion(message.text, message.lang);
			else
				try {
					ITurtleAccess turtle = ReflectionHelper.getTurtle(message.world.getTileEntity(message.x, message.y, message.z));
					((TileEntitySpeaker)turtle.getPeripheral(message.side)).onSpeechCompletion(message.text, message.lang);
				} catch (Exception e) {
					e.printStackTrace();
				}
			return null;
		}
	}
}
