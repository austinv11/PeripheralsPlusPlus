package com.austinv11.peripheralsplusplus.network;

import com.austinv11.peripheralsplusplus.PeripheralsPlusPlus;
import com.austinv11.peripheralsplusplus.reference.Reference;
import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
import dan200.computercraft.api.turtle.TurtleSide;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SynthPacket implements IMessage {

    private BlockPos pos;
    public String text;
	public String voice;
	public Float pitch;
	public Float pitchRange;
	public Float pitchShift;
	public Float rate;
	public Float volume;
	public TurtleSide side;
	
	public SynthPacket() {
		
	}
	
	public SynthPacket(String text, String voice, Float pitch, Float pitchRange, Float pitchShift, Float rate, Float volume,
                       BlockPos pos, int world, TurtleSide side) {
		this.text = text;
		this.voice = voice;
		this.pitch = pitch;
		this.pitchRange = pitchRange;
		this.pitchShift = pitchShift;
		this.rate = rate;
		this.volume = volume;
		this.pos = pos;
		this.side = side;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		NBTTagCompound tag = ByteBufUtils.readTag(buf);
		text = tag.getString("text");
		voice = tag.getString("voice");
		pitch = tag.getString("pitch").equals("null") ? null : Float.parseFloat(tag.getString("pitch"));
		pitchRange = tag.getString("pitchRange").equals("null") ? null : Float.parseFloat(tag.getString("pitchRange"));
		pitchShift = tag.getString("pitchShift").equals("null") ? null : Float.parseFloat(tag.getString("pitchShift"));
		rate = tag.getString("pitch").equals("null") ? null : Float.parseFloat(tag.getString("pitch"));
		volume = tag.getString("volume").equals("null") ? null : Float.parseFloat(tag.getString("volume"));
        int[] posArray = tag.getIntArray("pos");
        pos = new BlockPos(posArray[0], posArray[1], posArray[2]);
		side = tag.getString("side").equals("null") ? null : TurtleSide.valueOf(tag.getString("side"));
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setString("text", text);
		tag.setString("voice", voice);
		tag.setString("pitch", pitch == null ? "null" : pitch.toString());
		tag.setString("pitchRange", pitchRange == null ? "null" : pitchRange.toString());
		tag.setString("pitchShift", pitchShift == null ? "null" : pitchShift.toString());
		tag.setString("rate", rate == null ? "null" : rate.toString());
		tag.setString("volume", volume == null ? "null" : volume.toString());
        tag.setIntArray("pos", new int[]{pos.getX(), pos.getY(), pos.getZ()});
		tag.setString("side", side == null ? "null" : side.name());
		ByteBufUtils.writeTag(buf, tag);
	}
	
	public static class SynthPacketHandler implements IMessageHandler<SynthPacket, IMessage> { //TODO: Use SimpleRunnable
		
		@Override
		public IMessage onMessage(SynthPacket message, MessageContext ctx) {
			new Thread(new SynthThread(message), Reference.MOD_NAME+" Synth Thread").start();
			return null;
		}
		
		private class SynthThread implements Runnable {
			
			private SynthPacket message;
			
			public SynthThread(SynthPacket message) {
				this.message = message;
			}
			
			@Override
			public void run() {
				System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
				try {
					Voice voice =  VoiceManager.getInstance().getVoice(message.voice);
					voice.allocate();
					if (message.pitch != null)
						voice.setPitch(message.pitch);
					if (message.pitchRange != null)
						voice.setPitchRange(message.pitchRange);
					if (message.pitchShift != null)
						voice.setPitchShift(message.pitchShift);
					if (message.rate != null)
						voice.setRate(message.rate);
					if (message.volume != null)
						voice.setPitch(message.volume);
					voice.speak(message.text);
					synchronized (this) {
						PeripheralsPlusPlus.NETWORK.sendToServer(new SynthResponsePacket(message.text, message.pos,
                                Minecraft.getMinecraft().world, message.side));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
