package com.austinv11.peripheralsplusplus.tile;

import com.austinv11.peripheralsplusplus.util.CCMethod;
import com.austinv11.peripheralsplusplus.util.Logger;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;

public class TileTest extends TilePeripheral {
	public static final String name = "tileTest";

	public TileTest() {
		Logger.info("Tile Test");
	}

	@CCMethod
	public String test(Object[] arguments) {
		return "Test";
	}

	@Override
	public String getType() {
		return "test";
	}
}
