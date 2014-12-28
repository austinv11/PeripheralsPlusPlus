package com.austinv11.peripheralsplusplus.mount;

import dan200.computercraft.api.filesystem.IMount;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class DynamicMount implements IMount {

	public static final String MOUNT_DIRECTORY = "./mods/ppp_mount";
	public static final String DIRECTORY = "/ppp";

	public DynamicMount() {

	}

	public static void prepareMount() {
		File dir = new File(MOUNT_DIRECTORY);
		boolean skipVersionCheck = !dir.exists();
		
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
		File file = new File(MOUNT_DIRECTORY);
		for (File f : file.listFiles())
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
}
