package miscperipherals.speech;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class SpeechProviderGoogle implements ISpeechProvider {
	private static final Random RANDOM = new Random();	
	private String[] encoder;
	
	public SpeechProviderGoogle() {
		encoder = getEncoder();
	}
	
	@Override
	public String getName() {
		return "google";
	}

	@Override
	public int getPriority() {
		return -10; // last resort really
	}

	@Override
	public boolean canUse(String text, double x, double y, double z, double speed) {
		return true;
	}

	@Override
	public File speak(String text, double speed) {
		try {
			File f = SpeechManager.makeTempFile(".wav");
			File mp3 = SpeechManager.makeTempFile(".mp3");
			
			HttpURLConnection conn = (HttpURLConnection) new URL("http://translate.google.com/translate_tts?idx=0&ie=UTF-8&q=" + URLEncoder.encode(text.length() < 100 ? text : text.substring(0, 100), "UTF-8") + "&textlen=" + text.length() + "&tl=en&total=1").openConnection();
			conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			conn.setRequestProperty("Accept-Language", "en-us,en;q=0.8");
			conn.setRequestProperty("Referer", "http://ssl.gstatic.com/translate/sound_player2.swf");
			conn.setRequestProperty("User-Agent", makeRealisticUserAgent());
			conn.setDoInput(true);
			conn.connect();
			
			InputStream is = conn.getInputStream();
			OutputStream os = new FileOutputStream(mp3);
			
			byte[] buf = new byte[8192];
			int read;
			while ((read = is.read(buf)) != -1) {
				System.out.println("read " + read);
				os.write(buf, 0, read);
				os.flush();
			}
			
			is.close();
			os.close();
			
			String input = mp3.getPath();
			String output = f.getPath();
			String[] enc = encoder.clone();
			for (int i = 0; i < enc.length; i++) enc[i] = enc[i].replace("@INPUT@", input).replace("@OUTPUT@", output);
			
			boolean ret = SpeechManager.runProcess(enc);
			if (mp3.exists() && !mp3.delete()) mp3.deleteOnExit();
			return ret ? f : null;
		} catch (Throwable e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String[] getEncoder() {
		try {
			Runtime.getRuntime().exec(new String[] {"lame", "--help"});
			return new String[] {"lame", "--decode", "@INPUT@", "@OUTPUT@"};
		} catch (Throwable e) {}
		
		try {
			Runtime.getRuntime().exec(new String[] {"ffmpeg", "-h"});
			return new String[] {"ffmpeg", "-yyyyy", "-i", "@INPUT@", "@OUTPUT@"};
		} catch (Throwable e) {}
		
		return null;
	}
	
	public static String makeRealisticUserAgent() {
		String platform = randomOf(new String[] {"Win32", "Win64", "WOW64"});
		int ffMajor = 4 + RANDOM.nextInt(20);
		long geckoBuildBase = 1262304000 * 1000L; // 20100101
		String geckoBuild = new SimpleDateFormat("yyyyMMdd").format(new Date(geckoBuildBase + (long)(RANDOM.nextFloat() * (System.currentTimeMillis() - geckoBuildBase))));
		return "Mozilla/5.0 (Windows NT " + randomOf(new String[] {platform.equals("Win32") ? "6.1" : "5.2", platform.equals("Win32") ? "5.1" : "5.2", "5.2", "6.0", "6.1", "6.2"}) + "; " + platform + (platform.equals("Win64") ? "; x64" : "") + "; rv:" + ffMajor + ".0) Gecko/" + geckoBuild + " Firefox/" + ffMajor + ".0";
	}
	
	public static <T> T randomOf(T[] array) {
		return array[RANDOM.nextInt(array.length)];
	}
}
