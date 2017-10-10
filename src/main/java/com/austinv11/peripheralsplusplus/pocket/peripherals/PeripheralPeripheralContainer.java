package com.austinv11.peripheralsplusplus.pocket.peripherals;

import com.austinv11.peripheralsplusplus.lua.LuaObjectPeripheralWrap;
import com.austinv11.peripheralsplusplus.reference.Config;
import com.austinv11.peripheralsplusplus.turtles.peripherals.MountedPeripheral;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.pocket.IPocketUpgrade;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PeripheralPeripheralContainer extends MountedPeripheral {

	private final Map<IPocketUpgrade, IPeripheral> pocketUpgrades;
	private Map<ResourceLocation, ItemStack> unequippedItems;

	public PeripheralPeripheralContainer(Map<IPocketUpgrade, IPeripheral> pocketUpgrades) {
		super();
		this.pocketUpgrades = pocketUpgrades;
		unequippedItems = new HashMap<>();
	}
	
	@Override
	public String getType() {
		return "peripheralContainer";
	}
	
	@Override
	public String[] getMethodNames() {
		return new String[]{"getContainedPeripherals", "wrapPeripheral", "unequipPeripheral"};
	}
	
	@Override
	public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws LuaException, InterruptedException {
		if (!Config.enablePeripheralContainer)
			throw new LuaException("Peripheral Containers have been disabled");
		if (method == 0) {
			HashMap<Integer, String> returnVals = new HashMap<Integer,String>();
			for (int i = 0; i < getPeripherals().size(); i++)
				returnVals.put(i+1, getPeripherals().get(i).getType());
			return new Object[]{returnVals};
		} else if (method == 1) {
			if (arguments.length < 1)
				throw new LuaException("Too few arguments");
			if (!(arguments[0] instanceof String))
				throw new LuaException("Bad argument #1 (expected string)");
			return new Object[]{new LuaObjectPeripheralWrap(getPeripheralByName((String)arguments[0]), computer)};
		} else if (method == 2) {
			if (arguments.length < 1)
				throw new LuaException("Usage: unequip <String peripheral type>");
			if (!(arguments[0] instanceof String))
				throw new LuaException("First argument expected to be a string");
			IPeripheral peripheral = getPeripheralByName((String) arguments[0]);
			if (peripheral == null)
				return new Object[]{false};
			Map<ResourceLocation, ItemStack> unequipped = new HashMap<>();
			for (Map.Entry<IPocketUpgrade, IPeripheral> upgrade : getUpgrades().entrySet())
				if (upgrade.getValue().equals(peripheral)) {
					unequipped.put(upgrade.getKey().getUpgradeID(), upgrade.getKey().getCraftingItem());
					getUpgrades().remove(upgrade.getKey(), upgrade.getValue());
					break;
				}
			if (unequipped.isEmpty())
				return new Object[]{false};
			unequippedItems.putAll(unequipped);
			return new Object[]{true};
		}
		return new Object[0];
	}
	
	private IPeripheral getPeripheralByName(String argument) {
		for (IPeripheral peripheral : getPeripherals())
			if (peripheral.getType().equals(argument))
				return peripheral;
		return null;
	}
	
	private List<IPeripheral> getPeripherals() {
		List<IPeripheral> peripherals = new ArrayList<>();
		peripherals.addAll(this.pocketUpgrades.values());
		return peripherals;
	}
	
	@Override
	public boolean equals(IPeripheral other) {
		return other == this;
	}
	
	@Override
	public void attach(IComputerAccess computer) {
		super.attach(computer);
		for (IPeripheral peripheral : getPeripherals())
			peripheral.attach(computer);
	}
	
	@Override
	public void detach(IComputerAccess computer) {
		super.detach(computer);
		for (IPeripheral peripheral : getPeripherals())
			peripheral.detach(computer);
	}

	public Map<IPocketUpgrade, IPeripheral> getUpgrades() {
		return pocketUpgrades;
	}

	public Map<ResourceLocation, ItemStack> getUnequippedItems() {
		return unequippedItems;
	}
}
