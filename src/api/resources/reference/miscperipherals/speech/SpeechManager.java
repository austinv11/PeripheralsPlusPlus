package miscperipherals.speech;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import miscperipherals.core.MiscPeripherals;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumOS;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SpeechManager {
	static final List<ISpeechProvider> providers = new ArrayList<ISpeechProvider>();
	private static final Comparator<ISpeechProvider> comparator = new PriorityComparator();
	static ISpeechProvider preferred;
	
	private static Throwable jacobFail = null;
	private static Throwable addpathFail = null;
	
	public static void init() {
		if (isWindows()) {
			try {
				for (String file : new String[] {"jacob-1.17-M2-x86.dll", "jacob-1.17-M2-x64.dll"}) {
					//File f = new File(Minecraft.getMinecraftDir(), file);
					File f = new File(".", file);
					
					InputStream is = SpeechManager.class.getResourceAsStream("/com/jacob/" + file);
					FileOutputStream os = new FileOutputStream(f);
					
					byte[] buffer = new byte[8192];
					int read;
					while ((read = is.read(buffer)) != -1) {
						os.write(buffer, 0, read);
						os.flush();
					}
					
					is.close();
					os.close();
				}
			} catch (Throwable e) {
				MiscPeripherals.log.warning("Failed to extract JACOB!");
				e.printStackTrace();
				
				jacobFail = e;
			}
		}
		
		if (isWindows()) {
			try {
				addLibraryPath(".");
			} catch (Throwable e) {
				addpathFail = e;
			}
			
			try {
				ISpeechProvider sp = new SpeechProviderWindows();
				providers.add(sp);
			} catch (UnsatisfiedLinkError e) {
				MiscPeripherals.log.warning("Unsatisfied link when loading JACOB! Debug information follows");
				
				MiscPeripherals.log.warning("");
				
				File f = new File(".", "jacob-1.17-M2-x86.dll");
				MiscPeripherals.log.warning("JACOB x86 exists: " + f.exists() + " - size: " + f.length());
				f = new File(".", "jacob-1.17-M2-x64.dll");
				MiscPeripherals.log.warning("JACOB x64 exists: " + f.exists() + " - size: " + f.length());
				MiscPeripherals.log.warning("And in MC dir: x86:" + new File(Minecraft.getMinecraftDir(), "jacob-1.17-M2-x86.dll").exists() + " x64:" + new File(Minecraft.getMinecraftDir(), "jacob-1.17-M2-x64.dll").exists());
				
				MiscPeripherals.log.warning("");
				
				MiscPeripherals.log.warning(". is " + new File(".").getAbsolutePath());
				MiscPeripherals.log.warning("minecraftDir is " + Minecraft.getMinecraftDir().getAbsolutePath());
				
				MiscPeripherals.log.warning("");
				
				MiscPeripherals.log.warning("Extraction failed: " + (jacobFail != null));
				if (jacobFail != null) jacobFail.printStackTrace();
				
				MiscPeripherals.log.warning("");
				
				MiscPeripherals.log.warning("Adding library path failed: " + (addpathFail != null));
				if (addpathFail != null) addpathFail.printStackTrace();
				
				MiscPeripherals.log.warning("");
				
				MiscPeripherals.log.warning("Unsatisfied link:");
				e.printStackTrace();
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		if (isMac()) providers.add(new SpeechProviderOSX());
		if (SpeechProviderEspeak.getEspeakExecutable() != null) providers.add(new SpeechProviderEspeak());
		if (SpeechProviderFestival.getFestivalExecutable() != null) providers.add(new SpeechProviderFestival());
		if (SpeechProviderPico2wave.getPico2waveExecutable() != null) providers.add(new SpeechProviderPico2wave());
		if (SpeechProviderGoogle.getEncoder() != null) providers.add(new SpeechProviderGoogle());
		Collections.sort(providers, comparator);
		
		if (!MiscPeripherals.instance.speechProvider.isEmpty()) {
			for (ISpeechProvider provider : providers) {
				if (provider.getName().equalsIgnoreCase(MiscPeripherals.instance.speechProvider)) {
					preferred = provider;
					return;
				}
			}
		}
	}
	
	public static void registerProvider(ISpeechProvider provider) {
		providers.add(provider);
		Collections.sort(providers, comparator);
	}
	
	public static void speak(String text, double speed, double x, double y, double z) {
		new ThreadSpeechProvider(text, speed, x, y, z).start();
	}
	
	public static File makeTempFile(String suffix) throws IOException {
		return File.createTempFile("miscperipherals_speech_temp", suffix, new File(Minecraft.getMinecraftDir(), "miscperipherals"));
	}
	
	public static boolean runProcess(String... cmdline) throws IOException, InterruptedException {
		return runProcessStdin(null, cmdline);
	}
	
	public static boolean runProcessStdin(String stdin, String... cmdline) throws IOException, InterruptedException {
		File f = File.createTempFile("miscperipherals_speech_log", ".txt", new File(Minecraft.getMinecraftDir(), "miscperipherals"));
		OutputStream os = new FileOutputStream(f);
		
		Process process = new ProcessBuilder(cmdline).start();
		new StreamGobbler(process.getInputStream(), os);
		new StreamGobbler(process.getErrorStream(), os);
		
		if (stdin != null) {
			PrintWriter pw = new PrintWriter(process.getOutputStream(), true);
			pw.println(stdin);
			pw.close();
		}
		
		int exitCode = process.waitFor();
		os.flush();
		os.close();
		if (exitCode != 0) {
			MiscPeripherals.log.warning("Speech process '" + cmdline[0] + "' returned exit code " + exitCode + " - log saved to " + f.getName());
			MiscPeripherals.log.warning("Full command line: " + Arrays.toString(cmdline));
			return false;
		} else {
			if (!f.delete()) f.deleteOnExit();
			return true;
		}
	}
	
	/**
	 * JACOB hax...
	 * 
	 * http://fahdshariff.blogspot.com/2011/08/changing-java-library-path-at-runtime.html
	 */
	public static void addLibraryPath(String pathToAdd) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Field usrPathsField = ClassLoader.class.getDeclaredField("usr_paths");
		usrPathsField.setAccessible(true);
		
		String[] paths = (String[])usrPathsField.get(null);
		
		for(String path : paths) {
			if (path.equals(pathToAdd)) return;
		}
		
		String[] newPaths = Arrays.copyOf(paths, paths.length + 1);
		newPaths[newPaths.length-1] = pathToAdd;
		usrPathsField.set(null, newPaths);
	}
	
	public static boolean isWindows() {
		return Minecraft.getOs() == EnumOS.WINDOWS;
	}
	
	public static boolean isMac() {
		return Minecraft.getOs() == EnumOS.MACOS;
	}
}
