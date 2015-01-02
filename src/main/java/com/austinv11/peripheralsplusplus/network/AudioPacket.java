package com.austinv11.peripheralsplusplus.network;

import com.austinv11.peripheralsplusplus.reference.Reference;
import com.austinv11.peripheralsplusplus.utils.TranslateUtils;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;

public class AudioPacket implements IMessage {

	public String lang;
	public String text;

	public AudioPacket() {}

	public AudioPacket(String lang, String text) {
		this.lang = lang;
		this.text = text;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		NBTTagCompound tag = ByteBufUtils.readTag(buf);
		lang = tag.getString("lang");
		text = tag.getString("text");
	}

	@Override
	public void toBytes(ByteBuf buf) {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setString("lang", lang);
		tag.setString("text", text);
		ByteBufUtils.writeTag(buf, tag);
	}

	public static class AudioPacketHandler implements IMessageHandler<AudioPacket, IMessage> {

		@Override
		public IMessage onMessage(final AudioPacket message, MessageContext ctx) {
			//Thread audio = new Thread(new AudioThread(message));
			//audio.start();
			new Thread(Reference.MOD_NAME+" Audio Thread"){

				@Override
				public void run(){
					try {
						TranslateUtils.playAudio(message.text, message.lang);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}.start();
			return null;
		}
	}
}
