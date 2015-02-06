package com.austinv11.peripheralsplusplus.tiles;

import com.austinv11.peripheralsplusplus.api.satellites.ISatellite;
import com.austinv11.peripheralsplusplus.api.satellites.upgrades.ISatelliteUpgrade;
import com.austinv11.peripheralsplusplus.event.SateliiteCrashEvent;
import com.austinv11.peripheralsplusplus.event.SatelliteLaunchEvent;
import com.austinv11.peripheralsplusplus.items.ItemSmartHelmet;
import com.austinv11.peripheralsplusplus.lua.LuaObjectHUD;
import com.austinv11.peripheralsplusplus.lua.LuaObjectSatellite;
import com.austinv11.peripheralsplusplus.reference.Config;
import com.austinv11.peripheralsplusplus.satellites.SatelliteData;
import com.austinv11.peripheralsplusplus.utils.NBTHelper;
import com.austinv11.peripheralsplusplus.utils.Util;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.StatCollector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class TileEntityAntenna extends MountedTileEntity {

	public static String publicName = "antenna";
	private  String name = "tileEntityAntenna";
	private int world = 0;
	private static HashMap<Integer, HashMap<Integer, List<IComputerAccess>>> connectedComputers = new HashMap<Integer,HashMap<Integer,List<IComputerAccess>>>();
	private HashMap<IComputerAccess, Boolean> computers = new HashMap<IComputerAccess,Boolean>();
	public UUID identifier;

	public TileEntityAntenna() {
		super();
		identifier = UUID.randomUUID();
	}

	public String getName() {
		return name;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		if (nbttagcompound.hasKey("identifier"))
			identifier = UUID.fromString(nbttagcompound.getString("identifier"));
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
		nbttagcompound.setString("identifier", identifier.toString());
	}

	@Override
	public String getType() {
		return publicName;
	}

	@Override
	public String[] getMethodNames() {
		return new String[]{"listSatellites",/*Lists info about all satellites in the current world*/
				"connectToSatelliteById",/*Gets an handle representing a satellite*/
				//===Satellite APIs end===
				"getPlayers",/*Lists players wearing smart helmets linked to this antenna*/
				"getHUD"/*Returns a hud handle for the given player*/};
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws LuaException, InterruptedException {
		if (!Config.enableSatellites && method < 2)
			throw new LuaException("Satellites and associated systems have been disabled");
		else if (!Config.enableSmartHelmet)
			throw new LuaException("Smart Helmets have been disabled");
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
			case 2://getPlayers
				synchronized (this) {
					List<String> players = new ArrayList<String>();
					for (Object player : MinecraftServer.getServer().getConfigurationManager().playerEntityList)
						if (player instanceof EntityPlayer)
							if (((EntityPlayer) player).getCurrentArmor(0).getItem() instanceof ItemSmartHelmet && NBTHelper.hasTag(((EntityPlayer) player).getCurrentArmor(0), "identifier"))
								if (identifier.equals(UUID.fromString(NBTHelper.getString(((EntityPlayer) player).getCurrentArmor(0), "identifier"))))
									players.add(((EntityPlayer) player).getCommandSenderName());
					return new Object[]{Util.listToString(players)};
				}
			case 3:
				if (arguments.length < 1)
					throw new LuaException("Not enough arguments");
				if (!(arguments[0] instanceof String))
					throw new LuaException("Bad argument #1 (expected string)");
				if (Util.getPlayer((String)arguments[0]) == null)
					return new Object[]{null};
				return new Object[]{new LuaObjectHUD((String)arguments[0])};
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

	@SubscribeEvent
	public void onSatelliteCrash(SateliiteCrashEvent event) {
		for (IComputerAccess comp : computers.keySet())
			comp.queueEvent("satelliteCrash", new Object[]{event.satellite.getID(), event.coords.posX, event.coords.posY, event.coords.posZ, event.satellite.getWorld().provider.dimensionId});
	}

	@SubscribeEvent
	public void onSatelliteLaunch(SatelliteLaunchEvent event) {
		for (IComputerAccess comp : computers.keySet())
			comp.queueEvent("satelliteLaunch", new Object[]{event.coords.posX, event.y, event.coords.posZ, event.world.provider.dimensionId});
	}

	@Override
	public void attach(IComputerAccess computer) {
		computers.put(computer, true);
		super.attach(computer);
	}

	@Override
	public void detach(IComputerAccess computer) {
		computers.remove(computer);
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
