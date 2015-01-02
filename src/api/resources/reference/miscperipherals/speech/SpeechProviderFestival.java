package miscperipherals.speech;

import java.io.File;

public class SpeechProviderFestival implements ISpeechProvider {
	private final String executable;
	
	public SpeechProviderFestival() {
		executable = getFestivalExecutable();
	}
	
	@Override
	public String getName() {
		return "festival";
	}
	
	@Override
	public int getPriority() {
		return 5;
	}

	@Override
	public boolean canUse(String text, double x, double y, double z, double speed) {
		return true;
	}

	@Override
	public File speak(String text, double speed) {
		try {
			File f = SpeechManager.makeTempFile(".wav");
			
			return SpeechManager.runProcess(executable, "-o", f.getPath()) ? f : null;
		} catch (Throwable e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String getFestivalExecutable() {
		try {
			Runtime.getRuntime().exec(new String[] {"text2wave", "-h"});
			return "text2wave";
		} catch (Throwable e) {}
		
		return null;
	}
}
