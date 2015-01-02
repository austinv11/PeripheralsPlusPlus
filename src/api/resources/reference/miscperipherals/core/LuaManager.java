package miscperipherals.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import dan200.computer.api.IComputerAccess;

public class LuaManager {
	private static File BASE = new File(MiscPeripherals.proxy.getMinecraftFolder(), "miscperipherals");
	private static List<String> files = new ArrayList<String>();
	
	public static void init() {
		String classPath = "/miscperipherals/core/LuaManager.class";
		String path = LuaManager.class.getResource(classPath).getPath();
		String jarPath = path.substring(0, path.length() - classPath.length());
		if (!jarPath.endsWith("!")) {
			MiscPeripherals.log.warning("Not installed correctly, no demo programs!");
			return;
		}
		
		try {
			int count = 0;
			
			JarFile jar = new JarFile(new File(new URL(jarPath.substring(0, jarPath.length() - 1)).toURI()));
			Enumeration<JarEntry> entries = jar.entries();
			while (entries.hasMoreElements()) {
				JarEntry entry = entries.nextElement();
				if (entry.getName().startsWith("lua/") && !entry.isDirectory()) {
					String entryName = entry.getName().substring(4);
					files.add(entryName);
					File file = new File(BASE, entryName);
					
					if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) throw new FileNotFoundException("Failed to create folder");
					
					FileOutputStream os = new FileOutputStream(file);
					InputStream is = LuaManager.class.getResourceAsStream("/"+entry.getName());
					int read = 0;
					byte[] buf = new byte[2048];
					while ((read = is.read(buf)) != -1) {
						os.write(buf, 0, read);
					}
					
					os.flush();
					os.close();
					is.close();
					
					count++;
				}
			}
			jar.close();
			
			MiscPeripherals.log.info("Loaded "+count+" demo files");
		} catch (Throwable e) {
			MiscPeripherals.log.warning("Demo programs not loaded");
			e.printStackTrace();
		}
	}
	
	public static void mount(IComputerAccess computer) {
		boolean first = true;
		for (String file : files) {
			String ret = computer.mountFixedDir(file, "miscperipherals/"+file, true, 0);
			if (first && !file.equals(ret)) {
				computer.unmount(ret);
				break;
			}
			first = false;
		}
	}
}
