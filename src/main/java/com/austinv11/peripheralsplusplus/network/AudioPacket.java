package com.austinv11.peripheralsplusplus.network;

import com.austinv11.collectiveframework.multithreading.SimpleRunnable;
import com.austinv11.peripheralsplusplus.PeripheralsPlusPlus;
import com.austinv11.peripheralsplusplus.reference.Reference;
import com.austinv11.peripheralsplusplus.utils.TranslateUtils;
import dan200.computercraft.api.turtle.TurtleSide;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.ArrayList;

public class AudioPacket implements IMessage {

    private BlockPos pos;
    public String lang;
	public String text;
	public TurtleSide side;

	public AudioPacket() {}

	public AudioPacket(String lang, String text, BlockPos pos, int world, TurtleSide side) {
		this.lang = lang;
		this.text = text;
		this.pos = pos;
		this.side = side;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		NBTTagCompound tag = ByteBufUtils.readTag(buf);
		lang = tag.getString("lang");
		text = tag.getString("text");
        int[] posArray = tag.getIntArray("pos");
		pos = new BlockPos(posArray[0], posArray[1], posArray[2]);
		side = tag.getString("side").equals("null") ? null : TurtleSide.valueOf(tag.getString("side"));
	}

	@Override
	public void toBytes(ByteBuf buf) {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setString("lang", lang);
		tag.setString("text", text);
		tag.setIntArray("pos", new int[]{pos.getX(), pos.getY(), pos.getZ()});
		tag.setString("side", side == null ? "null" : side.name());
		ByteBufUtils.writeTag(buf, tag);
	}

	public static class AudioPacketHandler implements IMessageHandler<AudioPacket, IMessage> {

		@Override
		public IMessage onMessage(AudioPacket message, MessageContext ctx) {
			new AudioThread(message).start();
			return null;
		}

		private class AudioThread extends SimpleRunnable {

			private AudioPacket message;

			public AudioThread(AudioPacket message) {
				this.message = message;
			}

			@Override
			public void run(){
					try {
						if (message.text.replace(" ", "%20").length() < 100)
							TranslateUtils.playAudio(message.text, message.lang);
						else
							playSplitAudio();
						PeripheralsPlusPlus.NETWORK.sendToServer(
								new AudioResponsePacket(message.text, message.lang, message.pos,
										Minecraft.getMinecraft().world, message.side));
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						this.disable(true);
					}
			}
			
			@Override
			public String getName() {
				return Reference.MOD_NAME+" Audio Thread";
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
