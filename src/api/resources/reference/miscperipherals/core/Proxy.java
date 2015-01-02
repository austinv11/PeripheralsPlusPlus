package miscperipherals.core;

import java.io.File;

import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

public class Proxy {
	public World getClientWorld() {
		return MinecraftServer.getServer().worldServerForDimension(0);
	}
	
	public Entity getClientEntityById(int id) {
		return MinecraftServer.getServer().worldServerForDimension(0).getEntityByID(id);
	}
	
	public void spawnHeartParticles(Entity ent) {
		
	}
	
	public File getMinecraftFolder() {
		return new File(".");
	}
	
	public boolean isServer() {
		return true;
	}
	
	public void spawnSmoke(double x, double y, double z, double motionX, double motionY, double motionZ) {
		
	}
	
	public int getRenderID(String key) {
		return 0;
	}
	
	public Icon getIcon(Object o) {
		return null;
	}

	public void onPostInit() {
		
	}

	public void speak(String text, double speed, double x, double y, double z) {
		
	}
}
