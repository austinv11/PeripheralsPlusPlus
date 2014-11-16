package com.austinv11.peripheralsplusplus.api.satellites;

import com.austinv11.peripheralsplusplus.api.satellites.upgrades.ISatelliteUpgrade;
import net.minecraft.util.ChunkCoordinates;

import java.util.List;

public interface ISatellite {

	/**
	 * Gets the position of the sattelite over Earth (note: ignore the y-value)
	 * @return Position of the satellite
	 */
	public ChunkCoordinates getPosition();

	/**
	 * Gets the current upgrade used on the satellite
	 * @return Main upgrade of the satellite
	 */
	public ISatelliteUpgrade getMainUpgrade();

	/**
	 * Gets all the modifications on the satellite
	 * @return List of addons
	 */
	public List<ISatelliteUpgrade> getAddons();

	/**
	 * Causes satellite to crash back down to Earth
	 */
	public void recall();
}
