package com.austinv11.peripheralsplusplus.tiles;

import com.austinv11.collectiveframework.utils.TimeProfiler;
import com.austinv11.peripheralsplusplus.reference.Config;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class TileEntityTimeSensor extends MountedTileEntity {
	
	public static String publicName = "timeSensor";
	private String name = "tileEntityTimeSensor";
	
	private TimeProfiler profiler;

	public TileEntityTimeSensor() {
		super();
	}

	public String getName() {
		return name;
	}

	@Override
	public String getType() {
		return publicName;
	}

	@Override
	public String[] getMethodNames() {
		return new String[] {"getDate", "getTime", "startTimer", "stopTimer"};
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws LuaException, InterruptedException {
		if (!Config.enableTimeSensor)
			throw new LuaException("Time Sensors have been disabled!");
		if (method == 0) {
			String timeStamp = new SimpleDateFormat("yyyy@MM@dd@HH@mm@ss").format(new Date());
			HashMap<String, Integer> map = new HashMap<String, Integer>();
			String[] split = timeStamp.split("@");
			map.put("year", Integer.valueOf(split[0]));
			map.put("month", Integer.valueOf(split[1]));
			map.put("day", Integer.valueOf(split[2]));
			map.put("hour", Integer.valueOf(split[3]));
			map.put("minute", Integer.valueOf(split[4]));
			map.put("second", Integer.valueOf(split[5]));
			return new Object[]{map};
		} else if (method == 1) {
			return new Object[]{System.currentTimeMillis()};
		} else if (method == 2) {
			profiler = new TimeProfiler();
		} else if (method == 3) {
			long time = profiler == null ? 0 : profiler.getTime();
			profiler = null;
			return new Object[]{time};
		}
		return new Object[0];
	}

	@Override
	public boolean equals(IPeripheral other) {
		return (other == this);
	}
}
