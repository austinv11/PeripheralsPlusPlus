package miscperipherals.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

import miscperipherals.item.ItemSmartHelmet;
import miscperipherals.speech.ThreadSpeechProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundManager;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TickHandlerClient implements ITickHandler {
	private final Minecraft mc = Minecraft.getMinecraft();
	private static List<ThreadSpeechProvider> speechQueue = Collections.synchronizedList(new ArrayList<ThreadSpeechProvider>());
	
	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
		
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
		boolean keepLines = false;
		if (mc.thePlayer != null) {
			ItemStack helmet = mc.thePlayer.getCurrentItemOrArmor(4);
			if (helmet != null && helmet.getItem() instanceof ItemSmartHelmet) {
				keepLines = true;
			}
		}
		
		if (!keepLines) ItemSmartHelmet.clientLines = new String[ItemSmartHelmet.LINES];
		
		while (speechQueue.size() > 0) {
			ThreadSpeechProvider thread = speechQueue.remove(0);
			
			try {
				SoundManager.sndSystem.newSource(false, thread.source, thread.file.toURI().toURL(), thread.source, false, (float)thread.x, (float)thread.y, (float)thread.z, 2, 16.0F);
				SoundManager.sndSystem.play(thread.source);
			} catch (Throwable e) {}
			
			thread.unlock();
		}
	}
	
	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.RENDER);
	}

	@Override
	public String getLabel() {
		return "MiscPeripherals";
	}
	
	public static void addSpeech(ThreadSpeechProvider thread) {
		speechQueue.add(thread);
	}
}
