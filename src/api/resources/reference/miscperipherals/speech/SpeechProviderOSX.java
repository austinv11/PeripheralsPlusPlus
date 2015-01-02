package miscperipherals.speech;

import java.io.File;

/**
 * RG for public source release: This is broken. Don't ask why, I don't have a Mac
 */
public class SpeechProviderOSX implements ISpeechProvider {
	@Override
	public String getName() {
		return "osx";
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
	public File speak(String text, double speed) {
		try {
			File f = SpeechManager.makeTempFile(".wav");
			
			return SpeechManager.runProcess("say", "-o", f.getPath(), "--file-format=WAVE", "--data-format=LEF32@44100", text) ? f : null;
		} catch (Throwable e) {
			e.printStackTrace();
			return null;
		}
	}
}
