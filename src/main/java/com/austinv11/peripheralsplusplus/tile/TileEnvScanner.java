package com.austinv11.peripheralsplusplus.tile;

import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;

public class TileEnvScanner extends TilePeripheral {
	public static final String name = "tileEnvScanner";

	@Override
	public String[] getMethodNames() {
		return new String[] {"isRaining", "getBiome", "getTemperature", "isSnow"};
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws LuaException, InterruptedException {
		switch (method) {
			case 0: // isRaining
				return new Object[] {worldObj.isRaining()};
			case 1: // getBiome
				return new Object[] {worldObj.getBiomeGenForCoords(getPos()).biomeName};
			case 2: // getTemperature
				return new Object[] {worldObj.getBiomeGenForCoords(getPos()).getTempCategory().name()};
			case 3: // isSnow
				return new Object[] {worldObj.getBiomeGenForCoords(getPos()).getEnableSnow()};
		}
		return new Object[0];
	}

	@Override
	public String getType() {
		return "envScanner";
	}
}
