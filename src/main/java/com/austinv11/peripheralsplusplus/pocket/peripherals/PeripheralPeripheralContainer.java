package com.austinv11.peripheralsplusplus.pocket.peripherals;

import com.austinv11.collectiveframework.minecraft.utils.NBTHelper;
import com.austinv11.peripheralsplusplus.hooks.ComputerCraftHooks;
import com.austinv11.peripheralsplusplus.lua.LuaObjectPeripheralWrap;
import com.austinv11.peripheralsplusplus.reference.Config;
import com.austinv11.peripheralsplusplus.turtles.peripherals.MountedPeripheral;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PeripheralPeripheralContainer extends MountedPeripheral {
	
	private ItemStack item;
	
	public PeripheralPeripheralContainer(ItemStack item) {
		this.item = item;
	}
	
	@Override
	public String getType() {
		return "peripheralContainer";
	}
	
	@Override
	public String[] getMethodNames() {
		return new String[]{"getContainedPeripherals", "wrapPeripheral"};
	}
	
	@Override
	public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws LuaException, InterruptedException {
		if (!Config.enablePeripheralContainer)
			throw new LuaException("Peripheral Containers have been disabled");
		if (method == 0) {
			HashMap<Integer, String> returnVals = new HashMap<Integer,String>();
			for (int i = 0; i < getPeripherals(computer).size(); i++)
				returnVals.put(i+1, getPeripherals(computer).get(i).getType());
			return new Object[]{returnVals};
		} else if (method == 1) {
			if (arguments.length < 1)
				throw new LuaException("Too few arguments");
			if (!(arguments[0] instanceof String))
				throw new LuaException("Bad argument #1 (expected string)");
			return new Object[]{new LuaObjectPeripheralWrap(getPeripheralByName(computer, (String)arguments[0]), computer)};
		}
		return new Object[0];
	}
	
	private IPeripheral getPeripheralByName(IComputerAccess computer, String argument) {
		for (IPeripheral peripheral : getPeripherals(computer))
			if (peripheral.getType().equals(argument))
				return peripheral;
		return null;
	}
	
	public List<IPeripheral> getPeripherals(IComputerAccess computer) {
		if (NBTHelper.hasTag(item, "upgrades")) {
			HashMap<Integer, IPeripheral> peripherals = ComputerCraftHooks.cachedExtraPeripherals.get(computer.getID());
			return new ArrayList<IPeripheral>(peripherals.values());
		} else
			return new ArrayList<IPeripheral>();
	}
	
	@Override
	public boolean equals(IPeripheral other) {
		return other == this;
	}
	
	@Override
	public void attach(IComputerAccess computer) {
		super.attach(computer);
		for (IPeripheral peripheral : getPeripherals(computer))
			peripheral.attach(computer);
	}
	
	@Override
	public void detach(IComputerAccess computer) {
		super.detach(computer);
		for (IPeripheral peripheral : getPeripherals(computer))
			peripheral.detach(computer);
	}
}
