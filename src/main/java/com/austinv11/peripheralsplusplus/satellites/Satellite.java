package com.austinv11.peripheralsplusplus.satellites;

import com.austinv11.peripheralsplusplus.PeripheralsPlusPlus;
import com.austinv11.peripheralsplusplus.api.satellites.ISatellite;
import com.austinv11.peripheralsplusplus.api.satellites.upgrades.ISatelliteUpgrade;
import com.austinv11.peripheralsplusplus.api.satellites.upgrades.SatelliteUpgradeType;
import com.austinv11.peripheralsplusplus.utils.Logger;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Satellite implements ISatellite{

	/**
	 * K = Satellite object, V = dimensionId
	 */
	public final static HashMap<Satellite, Integer> SATELLITE_REGISTRY = new HashMap<Satellite, Integer>();

	private ChunkCoordinates coords;
	private int id;
	private World world;
	private ISatelliteUpgrade mainUpgrade;
	private List<ISatelliteUpgrade> addons = new ArrayList<ISatelliteUpgrade>();

	public Satellite(int x, int y, int z, World w) {
		coords = new ChunkCoordinates(x,y,z);
		world = w;
		Satellite.SATELLITE_REGISTRY.put(this, world.provider.dimensionId);
	}

	public void setID(int id) {
		this.id = id;
	}

	public NBTTagCompound toNBT() {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setInteger("x", coords.posX);
		nbt.setInteger("y", coords.posY);
		nbt.setInteger("z", coords.posZ);
		nbt.setInteger("id", id);
		nbt.setInteger("dim", world.provider.dimensionId);
		nbt.setInteger("mainId", mainUpgrade.getUpgradeID());
		int[] ids = new int[addons.size()];
		for (int i = 0; i < addons.size(); i++)
			ids[i] = addons.get(i).getUpgradeID();
		nbt.setIntArray("addonIds", ids);
		return nbt;
	}

	public static Satellite fromNBT(NBTTagCompound nbt) {
		Satellite sat = new Satellite(nbt.getInteger("x"), nbt.getInteger("y"), nbt.getInteger("z"),
				MinecraftServer.getServer().worldServerForDimension(nbt.getInteger("dim")));
		sat.setID(nbt.getInteger("id"));
		try {
			sat.setMainUpgrade(PeripheralsPlusPlus.instance.UPGRADE_REGISTY.get(nbt.getInteger("mainId")));
		} catch (Exception e) {
			Logger.error("There was a problem loading the upgrade for satellite "+sat.getID());
		}
		try {
			List<ISatelliteUpgrade> upgrades = new ArrayList<ISatelliteUpgrade>();
			for (int i : nbt.getIntArray("addonIds"))
				upgrades.add(PeripheralsPlusPlus.instance.UPGRADE_REGISTY.get(i));
			sat.setAddons(upgrades);
		} catch (Exception e) {
			Logger.error("There was a problem loading the addons for satellite "+sat.getID());
		}
		return sat;
	}

	@Override
	public ChunkCoordinates getPosition() {
		return coords;
	}

	@Override
	public ISatelliteUpgrade getMainUpgrade() {
		return mainUpgrade;
	}

	@Override
	public List<ISatelliteUpgrade> getAddons() {
		return addons;
	}

	@Override
	public World getWorld() {
		return world;
	}

	@Override
	public void recall() {
		//TODO
	}

	@Override
	public int getID() {
		return id;
	}

	@Override
	public void setMainUpgrade(ISatelliteUpgrade upgrade) throws Exception {
		if (upgrade.getType() == SatelliteUpgradeType.MAIN) {
			mainUpgrade = upgrade;
			return;
		}
		throw new Exception("Incompatible Satellite Upgrade Type");
	}

	@Override
	public void setAddons(List<ISatelliteUpgrade> addons) throws Exception {
		for (ISatelliteUpgrade up : addons) {
			if (up.getType() != SatelliteUpgradeType.MODIFIER)
				throw new Exception("Incompatible Satellite Addon Type");
		}
		this.addons = addons;
	}
}
