package com.austinv11.peripheralsplusplus.tile;

import com.austinv11.peripheralsplusplus.util.Logger;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;

public class TileTest extends TilePeripheral {
	public static final String name = "tileTest";

	public TileTest() {
		Logger.info("Tile Test");
	}

	@Override
	public String[] getMethodNames() {
		return new String[] {"test"};
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws LuaException, InterruptedException {
		switch (method) {
			case 0:
				return new Object[] {"Test"};
		}
		return new Object[0];
	}

	@Override
	public String getType() {
		return "test";
	}
}
