package com.austinv11.peripheralsplusplus.utils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Location {

	private double x,y,z;
	private World world;
	private ChunkCoordinates position;

	public Location(double x, double y, double z, World world) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.world = world;
		this.position = new ChunkCoordinates((int)x, (int)y, (int)z);
	}
	
	public Location(Entity ent) {
		this(ent.posX, ent.posY, ent.posZ, ent.worldObj);
	}
	
	public Location(TileEntity block) {
		this(block.xCoord, block.yCoord, block.zCoord, block.getWorldObj());
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

	@Deprecated
	public ChunkCoordinates getPosition() {
		return this.position;
	}

	public HashMap<String, Double> getPlayers(double range) {
		HashMap<String, Double> map = new HashMap<String,Double>();
        if(world == null)
            return map;

        for (EntityPlayer player : (Iterable<EntityPlayer>) world.playerEntities) {
            if (new Location(player).getDistance(this) <= range) {
                map.put(player.getDisplayName(), new Location(player).getDistance(this));
            }
        }
		return map;
	}

	public List<String> getPlayers(boolean inWorld) {
		List<String> list = new ArrayList<String>();
		if (inWorld) {
            if(world == null)
                return list;

            for (EntityPlayer player : (Iterable<EntityPlayer>) world.playerEntities)
				list.add(player.getDisplayName());
		}else {
			for (String player : MinecraftServer.getServer().getAllUsernames())
				list.add(player);
		}
		return list;
	}
	
	//sqrt((x2 - x1)^2 + (y2 - y1)^2 + (z2 - z1)^2) --Geometry ftw!
	public double getDistance(Location other) {
		double xDist = (this.getX() - other.getX());
        xDist *= xDist;
        double yDist = (this.getY() - other.getY());
        yDist *= yDist;
		double zDist = (this.getZ() - other.getZ());
        zDist *= zDist;
        return Math.sqrt(xDist + yDist + zDist);
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
