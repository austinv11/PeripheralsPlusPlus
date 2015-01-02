package miscperipherals.speech;

import java.io.File;

import miscperipherals.core.MiscPeripherals;
import miscperipherals.core.TickHandlerClient;
import net.minecraft.client.audio.SoundManager;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ThreadSpeechProvider extends Thread {
	public final String text;
	public final double speed;
	public final double x;
	public final double y;
	public final double z;
	
	private boolean locked = false;
	public File file;
	public String source;
	
	public ThreadSpeechProvider(String text, double speed, double x, double y, double z) {
		super("MiscPeripherals Speech Provider");
		
		this.text = text;
		this.speed = speed;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	@Override
	public void run() {
		if (SpeechManager.preferred != null && SpeechManager.preferred.canUse(text, x, y, z, speed)) {
			file = SpeechManager.preferred.speak(text, speed);
		}
		if (file == null) {
			for (ISpeechProvider provider : SpeechManager.providers) {
				if (provider.canUse(text, speed, x, y, z)) {
					file = provider.speak(text, speed);
					if (file != null) {
						if (file.exists()) break;
						else MiscPeripherals.log.warning("Speech provider '" + provider.getName() + "' provided a non-existant file!");
					}
				}
			}
		} else if (!file.exists() && SpeechManager.preferred != null) MiscPeripherals.log.warning("Preferred speech provider '" + SpeechManager.preferred.getName() + "' provided a non-existant file!");
		if (file == null) return;
		
		source = file.getName();
		
		TickHandlerClient.addSpeech(this);
		try {
			locked = true;
			while (locked) {
				Thread.sleep(100L);
			}
		} catch (Throwable e) {
			if (!file.delete()) MiscPeripherals.log.warning("Could not delete temporary speech file " + file.getName());
			return;
		}
		
		try {
			while (SoundManager.sndSystem.playing(source)) {
				Thread.sleep(250L);
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
		
		if (!file.delete()) MiscPeripherals.log.warning("Could not delete temporary speech file " + file.getName());
	}
	
	public void unlock() {
		locked = false;
	}
}
