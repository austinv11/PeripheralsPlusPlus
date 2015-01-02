package miscperipherals.speech;

import java.io.File;

import com.jacob.com.Dispatch;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SpeechProviderWindows implements ISpeechProvider {
	/**
	 * http://msdn.microsoft.com/en-us/library/ms720595%28v=vs.85%29.aspx
	 */
	// 39 (SAFT48kHz16BitStereo) dropped due to problems with some audio drivers, also too high (Overmind/Player)
	public static final int SAPI_FILE_TYPE = 35; // SAFT44kHz16BitStereo
	public static final boolean DEBUG = false;
	
	private Dispatch voice, format, stream;
	
	public SpeechProviderWindows() {
		voice = new Dispatch("SAPI.SpVoice");
		format = new Dispatch("SAPI.SpAudioFormat");
		Dispatch.put(format, "Type", SAPI_FILE_TYPE);
		stream = new Dispatch("SAPI.SpFileStream");
		Dispatch.putRef(stream, "Format", format);
		
		// RG for public source release: This has to be done or the JVM will crash on quit
		Runtime.getRuntime().addShutdownHook(new Thread("MiscPeripherals Speech Release") {
			@Override
			public void run() {
				voice.safeRelease();
				format.safeRelease();
				stream.safeRelease();
			}
		});
	}
	
	@Override
	public String getName() {
		return "windows";
	}
	
	@Override
	public int getPriority() {
		return 10;
	}

	@Override
	public boolean canUse(String text, double speed, double x, double y, double z) {
		return true;
	}

	@Override
	public synchronized File speak(String text, double speed) {
		try {
			File f = SpeechManager.makeTempFile(".wav");
			
			int rate = (int)((float)speed * 10.0F);
			if (rate < -10) rate = -10;
			else if (rate > 10) rate = 10;
			Dispatch.put(voice, "Rate", speed);
			
			// RG for public source release: used to test different formats and their quality
			if (DEBUG) {
				for (int fmt : new int[] {7,11,15,19,23,37,31,35,39,33,37}) {
					format = new Dispatch("SAPI.SpAudioFormat");
					Dispatch.put(format, "Type", fmt);
					stream = new Dispatch("SAPI.SpFileStream");
					Dispatch.putRef(stream, "Format", format);
					
					// ported from AutoIt3: http://michaeltbeeitprof.blogspot.com/2010/01/sapi-5-text-to-wav-sample.html
					Dispatch.call(stream, "Open", "ttsdebug_" + fmt + ".wav", 3, false);
					Dispatch.putRef(voice, "AudioOutputStream", stream);
					Dispatch.call(voice, "Speak", text);
					Dispatch.call(stream, "Close");
				}
			}
			
			Dispatch.call(stream, "Open", f.getPath(), 3, false);
			Dispatch.putRef(voice, "AudioOutputStream", stream);
			Dispatch.call(voice, "Speak", text);
			Dispatch.call(stream, "Close");
			
			return f;
		} catch (Throwable e) {
			e.printStackTrace();
			return null;
		}
	}
}
