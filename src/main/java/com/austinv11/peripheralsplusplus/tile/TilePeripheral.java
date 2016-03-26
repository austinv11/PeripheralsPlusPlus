package com.austinv11.peripheralsplusplus.tile;

import com.austinv11.peripheralsplusplus.util.CCMethod;
import com.austinv11.peripheralsplusplus.util.IPlusPlusPeripheral;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.tileentity.TileEntity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public abstract class TilePeripheral extends TileEntity implements IPlusPlusPeripheral {
	protected ArrayList<IComputerAccess> computers = new ArrayList<IComputerAccess>();

	public void queueEvent(String eventName, Object[] args) {
		for (IComputerAccess computer : computers) {
			computer.queueEvent(eventName, args);
		}
	}

	@Override
	public String[] getMethodNames() {
		ArrayList<String> methods = new ArrayList<String>();
		IPlusPlusPeripheral peripheral = (IPlusPlusPeripheral) worldObj.getTileEntity(getPos());
		for (Method method : peripheral.getClass().getMethods()) {
			if (method.isAnnotationPresent(CCMethod.class)) {
				methods.add(method.getName());
			}
		}
		return methods.toArray(new String[0]);
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, ILuaContext context, int methodIndex, Object[] arguments) throws LuaException, InterruptedException {
		IPlusPlusPeripheral peripheral = (IPlusPlusPeripheral) worldObj.getTileEntity(getPos());
		for (Method method : peripheral.getClass().getMethods()) {
			if (method.isAnnotationPresent(CCMethod.class)) {
				if (method.getName().equals(getMethodNames()[methodIndex])) {
					try {
						return new Object[] {method.invoke(peripheral, (Object)arguments)};
					} catch (Exception e) {
						if (e instanceof InvocationTargetException) {
							if (((InvocationTargetException) e).getTargetException() instanceof LuaException) {
								throw new LuaException(((InvocationTargetException) e).getTargetException().getMessage());
							}
						}
						e.printStackTrace();
					}
				}
			}
		}
		return new Object[0];
	}

	@Override
	public void attach(IComputerAccess computer) {
		computers.add(computer);
	}

	@Override
	public void detach(IComputerAccess computer) {
		computers.remove(computer);
	}

	@Override
	public boolean equals(IPeripheral other) {
		return other == this;
	}
}
