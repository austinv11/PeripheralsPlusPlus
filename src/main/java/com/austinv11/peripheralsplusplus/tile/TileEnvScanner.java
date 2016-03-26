package com.austinv11.peripheralsplusplus.tile;

import com.austinv11.peripheralsplusplus.util.CCMethod;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;

public class TileEnvScanner extends TilePeripheral {
	public static final String name = "tileEnvScanner";

	@CCMethod
	public boolean isRaining() {
		return worldObj.isRaining();
	}

	@CCMethod
	public String getBiome() {
		return worldObj.getBiomeGenForCoords(getPos()).biomeName;
	}

	@CCMethod
	public String getTemperature() {
		return worldObj.getBiomeGenForCoords(getPos()).getTempCategory().name();
	}

	@CCMethod
	public boolean getSnow() {
		return worldObj.getBiomeGenForCoords(getPos()).getEnableSnow();
	}

	@Override
	public String getType() {
		return "envScanner";
	}
}
