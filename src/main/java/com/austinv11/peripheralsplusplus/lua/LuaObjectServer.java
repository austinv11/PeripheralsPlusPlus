package com.austinv11.peripheralsplusplus.lua;

import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.ILuaObject;
import dan200.computercraft.api.lua.LuaException;

public class LuaObjectServer implements ILuaObject {
	
	@Override
	public String[] getMethodNames() {
		return new String[0];
	}

	@Override
	public Object[] callMethod(ILuaContext context, int method, Object[] arguments) throws LuaException, InterruptedException {
		return new Object[0];
	}
}
