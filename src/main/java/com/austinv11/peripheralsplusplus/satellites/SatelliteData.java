package com.austinv11.peripheralsplusplus.satellites;

import com.austinv11.peripheralsplusplus.api.satellites.ISatellite;
import com.austinv11.peripheralsplusplus.reference.Config;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.storage.MapStorage;
import net.minecraftforge.common.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class SatelliteData extends WorldSavedData {

	private static final String key = "com.austinv11.ppp.satellites";
	private List<ISatellite> satellites = new ArrayList<ISatellite>();

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

	public int assignNextId() {
		int id = 0;
		for (ISatellite s : satellites)
			id = s.getID() >= id ? s.getID() : id;
		id++;
		return id;
	}

	@Override
	public void readFromNBT(NBTTagCompound p_76184_1_) {
		satellites.clear();
		NBTTagList list = p_76184_1_.getTagList("satellites", Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < list.tagCount(); i++) {
			Satellite sat = Satellite.fromNBT(list.getCompoundTagAt(i));
			satellites.set(sat.getID(), sat);
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound p_76187_1_) {
		NBTTagList list = new NBTTagList();
		for (ISatellite s : satellites) {
			list.appendTag(((Satellite)s).toNBT());
		}
		p_76187_1_.setTag("satellites", list);
	}

	public ISatellite getSatelliteForCoords(int x, int z) {
		ISatellite returnVal = null;
		for (ISatellite sat : satellites)
			if (sat.getPosition().posX == x && sat.getPosition().posZ == z) {
				returnVal = sat;
				break;
			}
		return returnVal;
	}

	public ISatellite getSatelliteForID(int id) {
		return satellites.get(id);
	}

	public void addSatellite(Satellite sat) {
		if (sat.getID() == -1)
			sat.setID(assignNextId());
		setSatellite(sat, sat.getID());
	}

	public void removeSatellite(int id) {
		satellites.remove(id);
	}

	public void setSatellite(Satellite sat, int id) {
		satellites.set(id, sat);
	}

	public List<ISatellite> getSatellites() {
		return satellites;
	}
}
