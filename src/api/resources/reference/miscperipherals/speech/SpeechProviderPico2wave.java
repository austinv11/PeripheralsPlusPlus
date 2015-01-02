package miscperipherals.speech;

import java.io.File;

public class SpeechProviderPico2wave implements ISpeechProvider {
	private String executable;
	
	@Override
	public String getName() {
		return "pico2wave";
	}

	@Override
	public int getPriority() {
		return 4; // I've heard good things about it, but also about festival
	}

	@Override
	public boolean canUse(String text, double x, double y, double z, double speed) {
		return true;
	}

	@Override
	public File speak(String text, double speed) {
		try {
			File f = SpeechManager.makeTempFile(".wav");
			
			return SpeechManager.runProcess(executable, "-w", f.getPath(), text) ? f : null;
		} catch (Throwable e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String getPico2waveExecutable() {		
		try {
			Runtime.getRuntime().exec(new String[] {"pico2wave", "--help"});
			return "pico2wave";
		} catch (Throwable e) {}
		
		try {
			Runtime.getRuntime().exec(new String[] {"lt-pico2wave", "--help"});
			return "lt-pico2wave";
		} catch (Throwable e) {}
		
		return null;
	}
}
