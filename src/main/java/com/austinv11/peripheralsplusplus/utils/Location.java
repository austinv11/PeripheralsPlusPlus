package com.austinv11.peripheralsplusplus.utils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import java.util.List;

public class Location {

	private double x,y,z;
	private World world;
	private Vec3 position;

	public Location (double x, double y, double z, World world) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.world = world;
		this.position = Vec3.createVectorHelper(x, y, z);
	}

	public double getX() {
		return this.x;
	}

	public double getY() {
		return this.y;
	}

	public double getZ() {
		return this.z;
	}

	public World getWorld() {
		return this.world;
	}

	public Vec3 getPosition() {
		return this.position;
	}

	public List<EntityPlayer> getPlayersInWorld() {
		try {
			if (this.getWorld().playerEntities != null)
				return this.getWorld().playerEntities;
		}catch (NullPointerException e) {} finally {
			return null;
		}
	}
}
