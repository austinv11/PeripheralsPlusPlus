package com.austinv11.peripheralsplusplus.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class WebUtil {

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
}
