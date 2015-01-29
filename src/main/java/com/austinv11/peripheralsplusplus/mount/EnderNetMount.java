package com.austinv11.peripheralsplusplus.mount;

import com.austinv11.peripheralsplusplus.PeripheralsPlusPlus;
import dan200.computercraft.api.filesystem.IMount;
import dan200.computercraft.api.filesystem.IWritableMount;

import java.io.*;
import java.util.List;

public class EnderNetMount implements IWritableMount {

	public static String BASE_SERVER_DIRECTORY = PeripheralsPlusPlus.BASE_PPP_DIR+"endernet_storage";

	private String domain;
	private boolean isSubdomain;
	private File directory;

	public EnderNetMount(String domain, boolean isSubdomain) {
		this.domain = domain;
		this.isSubdomain = isSubdomain;
		directory = getFileForDomain(domain, isSubdomain);
	}

	public static File getFileForDomain(String domain, boolean isSubdomain) {
		if (isSubdomain)
			return new File(getFileForDomain(domain.replaceFirst(domain.split(".")[0], ""), false).getPath()+"/"+domain.split(".")[0]);
		else
			return new File(BASE_SERVER_DIRECTORY+"/"+domain.replace(".","/"));
	}


	@Override
	public void makeDirectory(String path) throws IOException {
		new File(directory.getPath()+"/"+path).mkdir();
	}

	@Override
	public void delete(String path) throws IOException {
		new File(directory.getPath()+"/"+path).delete();
	}

	@Override
	public OutputStream openForWrite(String path) throws IOException {
		return new FileOutputStream(new File(directory.getPath()+"/"+path));
	}

	@Override
	public OutputStream openForAppend(String path) throws IOException {
		return new FileOutputStream(new File(directory.getPath()+"/"+path));
	}

	@Override
	public long getRemainingSpace() throws IOException {
		return directory.getFreeSpace();
	}

	@Override
	public boolean exists(String path) throws IOException {
		return new File(directory.getPath()+"/"+path).exists();
	}

	@Override
	public boolean isDirectory(String path) throws IOException {
		return new File(directory.getPath()+"/"+path).isDirectory();
	}

	@Override
	public void list(String path, List<String> contents) throws IOException {
		File file = new File(directory.getPath()+"/"+path);
		for (File f : file.listFiles()) {
			contents.add(f.getName());
		}
	}

	@Override
	public long getSize(String path) throws IOException {
		return new File(directory.getPath()+"/"+path).getTotalSpace();
	}

	@Override
	public InputStream openForRead(String path) throws IOException {
		return new FileInputStream(new File(directory.getPath()+"/"+path));
	}

	public static class GhostEnderNetMount implements IMount {

		private String domain;
		private boolean isSubdomain;
		private File directory;

		public GhostEnderNetMount(String domain, boolean isSubdomain) {
			this.domain = domain;
			this.isSubdomain = isSubdomain;
			directory = getFileForDomain(domain, isSubdomain);
		}

		@Override
		public boolean exists(String path) throws IOException {
			return new File(directory.getPath()+"/"+path).exists();
		}

		@Override
		public boolean isDirectory(String path) throws IOException {
			return new File(directory.getPath()+"/"+path).isDirectory();
		}

		@Override
		public void list(String path, List<String> contents) throws IOException {
			File file = new File(directory.getPath()+"/"+path);
			for (File f : file.listFiles()) {
				contents.add(f.getName());
			}
		}

		@Override
		public long getSize(String path) throws IOException {
			return new File(directory.getPath()+"/"+path).getTotalSpace();
		}

		@Override
		public InputStream openForRead(String path) throws IOException {
			return new FileInputStream(new File(directory.getPath()+"/"+path));
		}
	}
}
