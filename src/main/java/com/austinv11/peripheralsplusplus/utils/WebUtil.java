package com.austinv11.peripheralsplusplus.utils;

import com.austinv11.peripheralsplusplus.reference.Reference;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class WebUtil {

	private static ByteBuffer downloadBuffer = ByteBuffer.allocateDirect(1 << 23);

	public static List<String> readURL(String url) throws IOException{
		if (!url.contains("http://") && !url.contains("https://"))
			url = "http://"+url;
		URL input = new URL(url);
		BufferedReader in = new BufferedReader(new InputStreamReader(input.openStream()));
		String temp;
		List<String> read = new ArrayList<String>();
		while ((temp = in.readLine()) != null)
			read.add(temp);
		in.close();
		return read;
	}

	public static List<String> readGithub(String path) throws IOException{
		String branch = "master";
		List<String> in = readURL("https://raw.github.com/austinv11/PeripheralsPlusPlus/"+branch+"/"+path);
		return in;
	}

	public static boolean downloadFile(String site, String path, String name) {
		File file = new File((path.endsWith("/") ? path : path+"/")+name);
		URL url;
		URLConnection connection;
		InputStream is = null;
		try {
			url = new URL(site);
			connection = url.openConnection();
			connection.setConnectTimeout(5000);
			connection.setReadTimeout(5000);
			connection.setRequestProperty("User-Agent", Reference.MOD_NAME);
			double size = connection.getContentLength();
			if (size > downloadBuffer.capacity())
				throw new RuntimeException("The file '"+name+"' is too large! The maximum file size is:"+downloadBuffer.capacity());
			downloadBuffer.clear();
			int bytesRead, fullLength = 0;
			Logger.info("Downloading file '"+name+"'");
			is = connection.getInputStream();
			byte[] smallBuffer = new byte[1024];
			while ((bytesRead = is.read(smallBuffer)) >= 0) {
				downloadBuffer.put(smallBuffer, 0, bytesRead);
				fullLength += bytesRead;
				Logger.info(String.format("\rDownloading file %s %d%%", name, (int)(fullLength*100/size)));
			}
			Logger.info(String.format("\rDownloaded file %s         \n", name));
			is.close();
			downloadBuffer.limit(fullLength);
			if(!file.exists())
				file.createNewFile();
			downloadBuffer.position(0);
			FileOutputStream fos = new FileOutputStream(file);
			fos.getChannel().write(downloadBuffer);
			fos.close();
			return true;
		}catch (Exception e) {
			Logger.error("An error has occurred attempting to download: "+name+"; please report the following to the mod author:");
			e.printStackTrace();
			file.delete();
			return false;
		}
	}
}
