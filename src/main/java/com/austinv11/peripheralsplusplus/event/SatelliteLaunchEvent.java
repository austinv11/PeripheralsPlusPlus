package com.austinv11.peripheralsplusplus.event;

import com.austinv11.peripheralsplusplus.entities.EntityRocket;
import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

public class SatelliteLaunchEvent extends Event {

	public int y;
	public World world;
	public ChunkCoordinates coords;
	public EntityRocket rocket;

	public SatelliteLaunchEvent(int y, World world, ChunkCoordinates coords, EntityRocket rocket) {
		super();
		this.y = y;
		this.world = world;
		this.coords = coords;
		this.rocket = rocket;
	}
}
