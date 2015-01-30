package com.austinv11.peripheralsplusplus.tiles;

import com.austinv11.peripheralsplusplus.api.satellites.ISatellite;
import com.austinv11.peripheralsplusplus.api.satellites.upgrades.ISatelliteUpgrade;
import com.austinv11.peripheralsplusplus.lua.LuaObjectSatellite;
import com.austinv11.peripheralsplusplus.reference.Config;
import com.austinv11.peripheralsplusplus.satellites.SatelliteData;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TileEntityAntenna extends MountedTileEntity {

	public static String publicName = "antenna";
	private  String name = "tileEntityAntenna";
	private int world = 0;
	private static HashMap<Integer, HashMap<Integer, List<IComputerAccess>>> connectedComputers = new HashMap<Integer,HashMap<Integer,List<IComputerAccess>>>();

	public TileEntityAntenna() {
		super();
	}

	public String getName() {
		return name;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
	}

	@Override
	public String getType() {
		return publicName;
	}

	@Override
	public String[] getMethodNames() {
		return new String[]{"listSatellites",/*Lists info about all satellites in the current world*/
		"connectToSatelliteById"/*Gets an handle representing a satellite*/};
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws LuaException, InterruptedException {
		if (!Config.additionalMethods)
			throw new LuaException("Satellites and associated systems have been disabled");
		switch (method) {
			case 0://listSatellites
				if (!SatelliteData.isWorldWhitelisted(world))
					throw new LuaException("This world has not been allowed to contain satellites");
				SatelliteData data = SatelliteData.forWorld(world);
				List<ISatellite> satellites = data.getSatellites();
				HashMap<Integer, HashMap<String, Object>> map = new HashMap<Integer,HashMap<String,Object>>();
				for (int i = 0; i < satellites.size(); i++) {
					HashMap<String, Object> map1 = new HashMap<String,Object>();
					ISatellite satellite = satellites.get(i);
					map1.put("id", satellite.getID());
					map1.put("x", satellite.getPosition().posX);
					map1.put("y", satellite.getPosition().posY);
					map1.put("z", satellite.getPosition().posZ);
					map1.put("upgrade", StatCollector.translateToLocal(satellite.getMainUpgrade().getUnlocalisedName()));
					map1.put("addons", satListToMap(satellite.getAddons()));
					map.put(i+1, map1);
				}
				return new Object[]{map};
			case 1://connectToSatelliteById
				if (!SatelliteData.isWorldWhitelisted(world))
					throw new LuaException("This world has not been allowed to contain satellites");
				SatelliteData data_ = SatelliteData.forWorld(world);
				if (arguments.length < 1)
					throw new LuaException("Too few arguments");
				if (!(arguments[0] instanceof Double))
					throw new LuaException("Bad argument #1 (expected number)");
				if (data_.getSatelliteForID((int)(double)(Double)arguments[0]) != null)
					return new Object[]{null};
				HashMap<Integer, List<IComputerAccess>> compsForWorld;
				if (connectedComputers.containsKey(world))
					compsForWorld = connectedComputers.get(world);
				else
					compsForWorld = new HashMap<Integer,List<IComputerAccess>>();
				List<IComputerAccess> computers;
				if (compsForWorld.containsKey((int)(double)(Double)arguments[0]))
					computers = compsForWorld.get((int)(double)(Double)arguments[0]);
				else
					computers = new ArrayList<IComputerAccess>();
				ISatellite satellite = data_.getSatelliteForID((int)(double)(Double)arguments[0]);
				if (!computers.contains(computer)) {
					satellite.getMainUpgrade().onConnect(satellite, computer);
					computers.add(computer);
					compsForWorld.put((int)(double)(Double)arguments[0], computers);
					connectedComputers.put(world, compsForWorld);
				}
				return new Object[]{new LuaObjectSatellite(satellite, computer)};
		}
		return new Object[0];
	}

	private HashMap<Integer, String> satListToMap(List<ISatelliteUpgrade> list) {
		HashMap<Integer, String> map = new HashMap<Integer,String>();
		for (int i = 0; i < list.size(); i++)
			map.put(i+1, StatCollector.translateToLocal(list.get(i).getUnlocalisedName()));
		return map;
	}

	@Override
	public void updateEntity() {
		if (worldObj != null)
			world = worldObj.provider.dimensionId;
	}

	@Override
	public void detach(IComputerAccess computer) {
		SatelliteData data = SatelliteData.forWorld(worldObj);
		HashMap<Integer, List<IComputerAccess>> compsForWorld;
		if (connectedComputers.containsKey(world))
			compsForWorld = connectedComputers.get(world);
		else
			compsForWorld = new HashMap<Integer,List<IComputerAccess>>();
		for (int i = 0; i < compsForWorld.keySet().size(); i++) {
			List<IComputerAccess> computers;
			if (compsForWorld.containsKey(i))
				computers = compsForWorld.get(i);
			else
				computers = new ArrayList<IComputerAccess>();
			ISatellite satellite = data.getSatelliteForID(i);
			if (computers.contains(computer)) {
				satellite.getMainUpgrade().onDisconnect(satellite, computer);
				computers.remove(computer);
				compsForWorld.put(i, computers);
				connectedComputers.put(world, compsForWorld);
			}
		}
		super.detach(computer);
	}

	@Override
	public boolean equals(IPeripheral other) {
		return (this == other);
	}
}
