package com.austinv11.peripheralsplusplus.tile;

import com.austinv11.peripheralsplusplus.reference.Config;
import com.austinv11.peripheralsplusplus.util.Util;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TilePlayerSensor extends TilePeripheral {
	public static final String name = "tilePlayerSensor";

	@Override
	public String[] getMethodNames() {
		return new String[] {"getNearbyPlayers", "getAllPlayers"};
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws LuaException, InterruptedException {
		List<? extends EntityPlayer> players;
		switch (method) {
			case 0: // getNearbyPlayers
				if (arguments.length < 1)
					throw new LuaException("Wrong number of arguments. Expected 1.");
				if (!(arguments[0] instanceof Double))
					throw new LuaException("Bad argument #1. Expected number.");

				int range = (int) (double) (Double) arguments[0];
				if (range > Config.playerSensorMaxRange) range = Config.playerSensorMaxRange;
				
				AxisAlignedBB bb = AxisAlignedBB.fromBounds(pos.getX() - range, pos.getY() - range, pos.getZ() - range, pos.getX() + range, pos.getY() + range, pos.getZ() + range);
				players = worldObj.getEntitiesWithinAABB(EntityPlayer.class, bb);

				HashMap<String, Integer> playerDists = new HashMap<String, Integer>();
				for (EntityPlayer player : players) {
					playerDists.put(player.getName(), (int) player.getDistance((double) pos.getX(), (double) pos.getY(), (double) pos.getZ()));
				}
				return new Object[] {playerDists};
			case 1: // getAllPlayers
				if (arguments.length < 1)
					throw new LuaException("Wrong number of arguments. Expected 1.");
				if (!(arguments[0] instanceof Boolean))
					throw new LuaException("Bad argument #1. Expected boolean.");
				boolean limit = (Boolean) arguments[0];

				players = limit ? worldObj.playerEntities : MinecraftServer.getServer().getConfigurationManager().playerEntityList;
				ArrayList<String> playerNames = new ArrayList<String>();
				for (EntityPlayer player : players) {
					playerNames.add(player.getName());
				}
				return new Object[] {Util.listToIndexedMap(playerNames)};
		}
		return new Object[0];
	}

	@Override
	public String getType() {
		return "playerSensor";
	}
}
