package com.austinv11.peripheralsplusplus.satellites;

import com.austinv11.peripheralsplusplus.reference.Config;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.storage.MapStorage;

public class SatelliteData extends WorldSavedData {

	private static final String key = "com.austinv11.ppp.satellites";

	public SatelliteData(String mapName) {
		super(mapName);
	}

	public SatelliteData() {
		this("SatelliteMap");
	}

	public static SatelliteData forWorld(World world) {
		MapStorage storage = world.perWorldStorage;
		SatelliteData result = (SatelliteData)storage.loadData(SatelliteData.class, key);
		if (result == null) {
			result = new SatelliteData();
			storage.setData(key, result);
		}
		return result;
	}

	public static boolean isWorldWhitelisted(World world) {
		return (Config.dimWhitelist.contains(world.provider.dimensionId));
	}

	public void save() {
		this.markDirty();
	}

	@Override
	public void readFromNBT(NBTTagCompound p_76184_1_) {
		//TODO
	}

	@Override
	public void writeToNBT(NBTTagCompound p_76187_1_) {
		//TODO
	}
}
