package com.austinv11.peripheralsplusplus.mount;

import com.austinv11.peripheralsplusplus.utils.Logger;
import com.austinv11.peripheralsplusplus.utils.Util;
import com.austinv11.peripheralsplusplus.utils.WebUtil;
import com.google.gson.Gson;
import cpw.mods.fml.common.FMLCommonHandler;
import dan200.computercraft.api.filesystem.IMount;
import dan200.computercraft.api.peripheral.IPeripheral;

import java.io.*;
import java.util.List;

public class DynamicMount implements IMount {

	public static final String MOUNT_DIRECTORY = FMLCommonHandler.instance().getSavesDirectory().getParent()+"/mods/ppp_mount";
	public static final String DIRECTORY = "/ppp";
	public static final String JSON_VER = "1.0";
	private IPeripheral peripheral;

	public DynamicMount(IPeripheral peripheral) {
		this.peripheral = peripheral;
	}

	public static void prepareMount() throws Exception{
		Gson gson = new Gson();
		JSONIndex index = gson.fromJson(Util.listToString(WebUtil.readGithub("lua/index.json")), JSONIndex.class);
		if (!index.ver.equals(JSON_VER))
			throw new Exception("JSON version mismatch!");
		String[] dirs = index.dirs;
		Logger.info(dirs.length+" directories found! Attempting to update (if necessary)...");
		for (int i = 0; i < dirs.length; i++) {
			String d = dirs[i];
			JSONFileList files = gson.fromJson(Util.listToString(WebUtil.readGithub("lua/"+d+"/index.json")), JSONFileList.class);
			if (checkFileVersion(d, files)) {
				String[] files1 = files.files;
				for (int i1 = 0; i1 < files1.length; i1++) {
					String f = files1[i1];
					File file = new File(MOUNT_DIRECTORY+"/"+d+"/"+f);
					file.mkdirs();
					file.delete();//FIXME:Too inefficient
					file.createNewFile();
					FileWriter writer = new FileWriter(file);
					writer.write(Util.listToString(WebUtil.readGithub("lua/"+d+"/"+f)));
					writer.close();
				}
			}
		}
	}

	@Override
	public boolean exists(String path) throws IOException {
		return new File(MOUNT_DIRECTORY+path).exists();
	}

	@Override
	public boolean isDirectory(String path) throws IOException {
		return new File(MOUNT_DIRECTORY+path).isDirectory();
	}

	@Override
	public void list(String path, List<String> contents) throws IOException {
		File file = new File(MOUNT_DIRECTORY+"/"+path);
		for (File f : file.listFiles())
			if (f.getName().equals(peripheral.getType()) || f.getParent().equals(peripheral.getType()))
				contents.add(f.getName());
	}

	@Override
	public long getSize(String path) throws IOException {
		return new File(MOUNT_DIRECTORY+path).getTotalSpace();
	}

	@Override
	public InputStream openForRead(String path) throws IOException {
		return new FileInputStream(new File(MOUNT_DIRECTORY+path));
	}

	private static boolean checkFileVersion(String dir, JSONFileList json) throws IOException{
		File file = new File(MOUNT_DIRECTORY+"/"+dir+"/index.json");
		if (!file.exists())
			return true;
		Gson gson = new Gson();
		String localJson = Util.readFile(file);
		JSONFileList localFile = gson.fromJson(localJson, JSONFileList.class);
		return !localFile.ver.equals(json.ver);
	}
}
