package com.austinv11.peripheralsplusplus.mount;

import com.austinv11.peripheralsplusplus.tiles.TileEntityAnalyzer;
import com.austinv11.peripheralsplusplus.utils.*;
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
		for (String d : dirs) {
			JSONFileList files = gson.fromJson(Util.listToString(WebUtil.readGithub("lua/"+d+"/index.json")), JSONFileList.class);
			if (Util.checkFileVersion(MOUNT_DIRECTORY+"/"+d, files)) {
				String[] files1 = files.files;
				for (String f : files1) {
					File file = new File((MOUNT_DIRECTORY+"/"+d+"/"+f).replace(".lua", ""));
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
		return new File(MOUNT_DIRECTORY+"/"+path).exists();
	}

	@Override
	public boolean isDirectory(String path) throws IOException {
		return new File(MOUNT_DIRECTORY+"/"+path).isDirectory();
	}

	@Override
	public void list(String path, List<String> contents) throws IOException {
		File file = new File(MOUNT_DIRECTORY+"/"+path);
		for (File f : file.listFiles()) {
			String type = getSafeType();
			if (f.getName().equals(type) || file.getAbsolutePath().contains(type))
				if (!path.contains("json"))
					contents.add(f.getName());
		}
	}

	@Override
	public long getSize(String path) throws IOException {
		return new File(MOUNT_DIRECTORY+"/"+path).getTotalSpace();
	}

	@Override
	public InputStream openForRead(String path) throws IOException {
		return new FileInputStream(new File(MOUNT_DIRECTORY+"/"+path));
	}

	private String getSafeType() {
		return peripheral instanceof TileEntityAnalyzer ? "analyzers" : peripheral.getType(); //FIXME: Hardcoding is no bueno
	}
}
