package com.austinv11.peripheralsplusplus.turtles.peripherals;

import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.ITurtleAccess;

public class PeripheralClick implements IPeripheral {

	public PeripheralClick(ITurtleAccess turtle) {

	}

	@Override
	public String getType() {
		return null;
	}

	@Override
	public String[] getMethodNames() {
		return new String[0];
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws LuaException, InterruptedException {
		return new Object[0];
	}

	@Override
	public void attach(IComputerAccess computer) {

	}
	
	@Override
	public void detach(IComputerAccess computer) {

	}

	@Override
	public boolean equals(IPeripheral other) {
		return false;
	}
}
