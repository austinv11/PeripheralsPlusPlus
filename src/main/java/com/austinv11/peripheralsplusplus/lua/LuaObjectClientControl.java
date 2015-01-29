package com.austinv11.peripheralsplusplus.lua;

import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.ILuaObject;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;

public class LuaObjectClientControl implements ILuaObject {

	private String domain;
	private IComputerAccess computer;

	public LuaObjectClientControl(String domain, IComputerAccess computer) {
		this.domain = domain;
		this.computer = computer;
	}

	@Override
	public String[] getMethodNames() {
		return new String[]{"readContent", /*Reads from the given domain*/
				"loadServer" /*Gets all the files on the server*/};
	}

	@Override
	public Object[] callMethod(ILuaContext context, int method, Object[] arguments) throws LuaException, InterruptedException {
		return new Object[0];
	}
}
