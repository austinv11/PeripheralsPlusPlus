package com.austinv11.peripheralsplusplus.tile;

import com.austinv11.peripheralsplusplus.reference.Config;
import com.austinv11.peripheralsplusplus.util.CCMethod;
import com.austinv11.peripheralsplusplus.util.Util;
import dan200.computercraft.api.lua.LuaException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TilePlayerSensor extends TilePeripheral {
	public static final String name = "tilePlayerSensor";

	@CCMethod
	public HashMap<String, Integer> getNearbyPlayers(Object[] arguments) throws LuaException {
		if (arguments.length < 1)
			throw new LuaException("Wrong number of arguments. Expected 1.");
		if (!(arguments[0] instanceof Double))
			throw new LuaException("Bad argument #1. Expected number.");

		int range = (int) (double) (Double) arguments[0];
		if (range > Config.playerSensorMaxRange) range = Config.playerSensorMaxRange;

		AxisAlignedBB bb = AxisAlignedBB.fromBounds(pos.getX() - range, pos.getY() - range, pos.getZ() - range, pos.getX() + range, pos.getY() + range, pos.getZ() + range);
		List<? extends EntityPlayer> players = worldObj.getEntitiesWithinAABB(EntityPlayer.class, bb);

		HashMap<String, Integer> playerDists = new HashMap<String, Integer>();
		for (EntityPlayer player : players) {
			playerDists.put(player.getName(), (int) player.getDistance((double) pos.getX(), (double) pos.getY(), (double) pos.getZ()));
		}
		return playerDists;
	}

	@CCMethod
	public Map<Integer, Object> getAllPlayers(Object[] arguments) throws LuaException {
		if (arguments.length < 1)
			throw new LuaException("Wrong number of arguments. Expected 1.");
		if (!(arguments[0] instanceof Boolean))
			throw new LuaException("Bad argument #1. Expected boolean.");
		boolean limit = (Boolean) arguments[0];

		List<? extends EntityPlayer> players = limit ? worldObj.playerEntities : MinecraftServer.getServer().getConfigurationManager().playerEntityList;
		ArrayList<String> playerNames = new ArrayList<String>();
		for (EntityPlayer player : players) {
			playerNames.add(player.getName());
		}
		return Util.listToIndexedMap(playerNames);
	}

	@Override
	public String getType() {
		return "playerSensor";
	}
}
