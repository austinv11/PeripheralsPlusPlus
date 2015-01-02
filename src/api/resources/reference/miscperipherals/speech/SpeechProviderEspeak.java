package miscperipherals.speech;

import java.io.File;

public class SpeechProviderEspeak implements ISpeechProvider {
	private final String executable;
	
	public SpeechProviderEspeak() {
		executable = getEspeakExecutable();
	}
	
	@Override
	public String getName() {
		return "espeak";
	}
	
	@Override
	public int getPriority() {
		return 3;
	}

	@Override
	public boolean canUse(String text, double x, double y, double z, double speed) {
		return true;
	}

	@Override
	public File speak(String text, double speed) {
		try {
			File f = SpeechManager.makeTempFile(".wav");
			
			int spd = 80 + (int)((speed + 1.0F) * 95.0F);
			return SpeechManager.runProcess(executable, "-w", f.getPath(), "-s", "" + spd, text) ? f : null;
		} catch (Throwable e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String getEspeakExecutable() {
		if (SpeechManager.isWindows()) {
			File f = new File(System.getenv("ProgramFiles"), "eSpeak");
			f = new File(f, "command_line");
			f = new File(f, "espeak.exe");
			if (f.exists()) return f.getPath();
			
			String pfx86 = System.getenv("ProgramFiles(x86)");
			if (pfx86 != null && !pfx86.isEmpty()) {
				f = new File(pfx86, "eSpeak");
				f = new File(f, "command_line");
				f = new File(f, "espeak.exe");
				if (f.exists()) return f.getPath();
			}
		}
		
		try {
			Runtime.getRuntime().exec(new String[] {"espeak", "--help"});
			return "espeak";
		} catch (Throwable e) {}
		
		return null;
	}
}
