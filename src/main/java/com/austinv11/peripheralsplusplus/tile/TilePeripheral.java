package com.austinv11.peripheralsplusplus.tile;

import com.austinv11.peripheralsplusplus.util.IPlusPlusPeripheral;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.tileentity.TileEntity;

import java.util.ArrayList;

public abstract class TilePeripheral extends TileEntity implements IPlusPlusPeripheral {
	protected ArrayList<IComputerAccess> computers = new ArrayList<IComputerAccess>();

	public void queueEvent(String eventName, Object[] args) {
		for (IComputerAccess computer : computers) {
			computer.queueEvent(eventName, args);
		}
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
