package com.austinv11.peripheralsplusplus.network;

import com.austinv11.peripheralsplusplus.PeripheralsPlusPlus;
import com.austinv11.peripheralsplusplus.reference.Reference;
import com.austinv11.peripheralsplusplus.utils.Logger;
import com.austinv11.peripheralsplusplus.utils.TranslateUtils;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import dan200.computercraft.api.turtle.TurtleSide;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;

public class AudioPacket implements IMessage {

	public String lang;
	public String text;
	public int x,y,z;
	//public World world;
	public TurtleSide side;

	public AudioPacket() {}

	public AudioPacket(String lang, String text, int x, int y, int z, int world, TurtleSide side) {
		this.lang = lang;
		this.text = text;
		this.x = x;
		this.y = y;
		this.z = z;
		//this.world = MinecraftServer.getServer().worldServerForDimension(world);
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
		//world  = Minecraft.getMinecraft().theWorld;
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
		tag.setString("side", side == null ? "null" : side.name());
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
						if (message.text.replace(" ", "%20").length() < 100)
							TranslateUtils.playAudio(message.text, message.lang);
						else
							playSplitAudio();
						work = false;
						PeripheralsPlusPlus.NETWORK.sendToServer(new AudioResponsePacket(message.text, message.lang, message.x, message.y, message.z, Minecraft.getMinecraft().theWorld, message.side));
					} catch (Exception e) {
						e.printStackTrace();
					}
			}

			private void playSplitAudio() {
				String[] splitString = message.text.split(" ");
				ArrayList<String> words = new ArrayList<String>();
				String combinedString = "";
				for (int i = 0; i < splitString.length; i++) {
					String tempString = combinedString+"%20"+splitString[i];
					if (tempString.length() >= 100) {
						words.add(combinedString);
						combinedString = splitString[i];
					}else if(i == splitString.length-1)
						words.add(tempString);
					else
						combinedString = tempString;
				}
				for (String query : words)
					try {
						TranslateUtils.playAudio(query, message.lang);
					} catch (Exception e) {
						e.printStackTrace();
					}
			}
		}
	}
}
