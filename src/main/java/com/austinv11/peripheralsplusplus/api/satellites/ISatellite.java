package com.austinv11.peripheralsplusplus.api.satellites;

import com.austinv11.peripheralsplusplus.api.satellites.upgrades.ISatelliteUpgrade;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

import java.util.List;

/**
 * Do NOT implement this, this is just used as a wrapper for my satellite class
 */
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
	 * Gets the world the satellite is orbiting
	 * @return The world
	 */
	public World getWorld();

	/**
	 * Causes satellite to crash back down to Earth with a default explosion radius
	 * @param doExplode Whether the satellite causes an explosion
	 * @param drops The items dropped by the satellite
	 * @return The coordinates of where the satellite landed
	 */
	public ChunkCoordinates recall(boolean doExplode, List<ItemStack> drops);

	/**
	 * Causes satellite to crash back down to Earth with a set explosion radius
	 * @param doExplode Whether the satellite causes an explosion
	 * @param drops The items dropped by the satellite
	 * @param explosionRadius The radius of the explosion (if doExplode is true)
	 * @return The coordinates of where the satellite landed
	 */
	public ChunkCoordinates recall(boolean doExplode, List<ItemStack> drops, float explosionRadius);

	/**
	 * Gets the unique id of the satellite
	 * @return The id of the satellite
	 */
	public int getID();

	/**
	 * Sets the main upgrade of the satellite
	 * @param upgrade The upgrade
	 * @throws Exception Thrown when the upgrade is not a MAIN upgrade
	 */
	public void setMainUpgrade(ISatelliteUpgrade upgrade) throws Exception;

	/**
	 * Sets the addon upgrades list of the satellite
	 * @param addons The list of addons
	 * @throws Exception Thrown when the list contains an upgrade that is not a MODIFIER upgrade
	 */
	public void setAddons(List<ISatelliteUpgrade> addons) throws Exception;
}
