package com.austinv11.peripheralsplusplus.event;

import com.austinv11.peripheralsplusplus.api.satellites.ISatellite;
import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.util.ChunkCoordinates;

public class SateliiteCrashEvent extends Event {

	public ISatellite satellite;
	public ChunkCoordinates coords;

	public SateliiteCrashEvent(ISatellite satellite, ChunkCoordinates coords) {
		super();
		this.satellite = satellite;
		this.coords = coords;
	}
}
