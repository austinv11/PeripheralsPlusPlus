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
	public List<ISatellite> satellites = new ArrayList<ISatellite>();

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
		satellites.clear();
		NBTTagList list = p_76184_1_.getTagList("satellites", Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < list.tagCount(); i++)
			satellites.add(Satellite.fromNBT(list.getCompoundTagAt(i)));
	}

	@Override
	public void writeToNBT(NBTTagCompound p_76187_1_) {
		NBTTagList list = new NBTTagList();
		for (ISatellite s : satellites) {
			list.appendTag(((Satellite)s).toNBT());
		}
		p_76187_1_.setTag("satellites", list);
	}
}
