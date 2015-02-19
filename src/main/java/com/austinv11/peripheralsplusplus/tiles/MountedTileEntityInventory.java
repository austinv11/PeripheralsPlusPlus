package com.austinv11.peripheralsplusplus.tiles;

import com.austinv11.peripheralsplusplus.mount.DynamicMount;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;

public abstract class MountedTileEntityInventory extends TileEntityInventory implements IPeripheral {

	public MountedTileEntityInventory() {
		super();
	}

	@Override
	public void attach(IComputerAccess computer) {
		computer.mount(DynamicMount.DIRECTORY, new DynamicMount(this));
	}

	@Override
	public void detach(IComputerAccess computer) {
		computer.mount(DynamicMount.DIRECTORY, new DynamicMount(this));
		computer.unmount(DynamicMount.DIRECTORY);
	}
}
