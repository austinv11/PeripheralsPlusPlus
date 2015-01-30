package com.austinv11.peripheralsplusplus.lua;

import com.austinv11.peripheralsplusplus.api.satellites.ISatellite;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.ILuaObject;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;

public class LuaObjectSatellite implements ILuaObject {

	private static String[] additionalMethods = new String[]{};
	private ISatellite sat;
	private IComputerAccess computer;

	public LuaObjectSatellite(ISatellite sat, IComputerAccess computer) {
		this.sat = sat;
		this.computer = computer;
	}

	@Override
	public String[] getMethodNames() {
		return modifyMethods(sat.getMainUpgrade().getMethodNames());
	}

	@Override
	public Object[] callMethod(ILuaContext context, int method, Object[] arguments) throws LuaException, InterruptedException {
		return callModifiedMethod(context, method, arguments);
	}

	private String[] modifyMethods(String[] oldMethods) {
		String[] newMethods = new String[additionalMethods.length+oldMethods.length];
		for (int i = 0; i < additionalMethods.length; i++)
			newMethods[i] = additionalMethods[i];
		for (int j = 0; j < oldMethods.length; j++)
			newMethods[j+additionalMethods.length] = oldMethods[j];
		return newMethods;
	}

	private Object[] callModifiedMethod(ILuaContext context, int method, Object[] arguments) throws LuaException, InterruptedException {
		if (method < additionalMethods.length) {//Call my methods
			return new Object[0];
		} else
			return sat.getMainUpgrade().callMethod(sat, computer, context, method-additionalMethods.length, arguments);
	}
}
