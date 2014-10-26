package com.austinv11.peripheralsplusplus.utils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;
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

	public HashMap<String, Double> getPlayers(TileEntity te, double range) {
		HashMap<String, Double> map = new HashMap<String,Double>();
		for (EntityPlayer player : (Iterable<EntityPlayer>) te.getWorldObj().playerEntities) {
			if (player.getPosition(1f).distanceTo(Vec3.createVectorHelper(te.xCoord, te.yCoord, te.zCoord)) <= range) {
				map.put(player.getDisplayName(), player.getPosition(1f).distanceTo(Vec3.createVectorHelper(te.xCoord, te.yCoord, te.zCoord)));
			}
		}
		return map;
	}

	public List<String> getPlayers(boolean inWorld) {
		List<String> list = new ArrayList<String>();
		if (inWorld) {
			for (EntityPlayer player : (Iterable<EntityPlayer>) world.playerEntities)
				list.add(player.getDisplayName());
		}else {
			for (String player : MinecraftServer.getServer().getAllUsernames())
				list.add(player);
		}
		return list;
	}

	@Deprecated
	public List<EntityPlayer> getPlayersInWorld() {
		try {
			if (this.getWorld().playerEntities != null)
				return this.getWorld().playerEntities;
		}catch (NullPointerException e) {} finally {
			return null;
		}
	}
}
