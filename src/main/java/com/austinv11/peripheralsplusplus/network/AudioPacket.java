package com.austinv11.peripheralsplusplus.network;

import com.austinv11.peripheralsplusplus.PeripheralsPlusPlus;
import com.austinv11.peripheralsplusplus.reference.Reference;
import com.austinv11.peripheralsplusplus.utils.TranslateUtils;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

public class AudioPacket implements IMessage {

	public String lang;
	public String text;
	public int x,y,z;
	public World world;

	public AudioPacket() {}

	public AudioPacket(String lang, String text, int x, int y, int z, int world) {
		this.lang = lang;
		this.text = text;
		this.x = x;
		this.y = y;
		this.z = z;
		this.world = MinecraftServer.getServer().worldServerForDimension(world);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		NBTTagCompound tag = ByteBufUtils.readTag(buf);
		lang = tag.getString("lang");
		text = tag.getString("text");
		x = tag.getInteger("x");
		y = tag.getInteger("y");
		z = tag.getInteger("z");
		world  = Minecraft.getMinecraft().theWorld;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setString("lang", lang);
		tag.setString("text", text);
		tag.setInteger("x", x);
		tag.setInteger("y", y);
		tag.setInteger("z", z);
		ByteBufUtils.writeTag(buf, tag);
	}

	public static class AudioPacketHandler implements IMessageHandler<AudioPacket, IMessage> {

		@Override
		public IMessage onMessage(AudioPacket message, MessageContext ctx) {
			Thread audio = new AudioThread(message);
			audio.start();
			return null;
		}

		private class AudioThread extends Thread {

			private AudioPacket message;
			private boolean work = true;

			public AudioThread(AudioPacket message) {
				super(Reference.MOD_NAME+" Audio Thread");
				this.message = message;
			}

			@Override
			public void run(){
				if (work)
					try {
						TranslateUtils.playAudio(message.text, message.lang);
						work = false;
						PeripheralsPlusPlus.NETWORK.sendToServer(new AudioResponsePacket(message.text, message.lang, message.x, message.y, message.z, message.world));
					} catch (Exception e) {
						e.printStackTrace();
					}
			}
		}
	}
}
