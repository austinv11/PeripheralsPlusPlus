package com.austinv11.peripheralsplusplus.lua;

import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.ILuaObject;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;

public class LuaObjectPeripheralWrap implements ILuaObject {

	private IPeripheral peripheral;
	private IComputerAccess computer;

	public LuaObjectPeripheralWrap(IPeripheral peripheral, IComputerAccess computer) {
		this.peripheral = peripheral;
		this.computer = computer;
	}

	@Override
	public String[] getMethodNames() {
		return peripheral.getMethodNames();
	}

	@Override
	public Object[] callMethod(ILuaContext context, int method, Object[] arguments) throws LuaException, InterruptedException {
		return peripheral.callMethod(computer, context, method, arguments);
	}
}
