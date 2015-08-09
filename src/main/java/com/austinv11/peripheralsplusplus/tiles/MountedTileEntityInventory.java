package com.austinv11.peripheralsplusplus.tiles;

import com.austinv11.collectiveframework.minecraft.tiles.TileEntityInventory;
import com.austinv11.peripheralsplusplus.mount.DynamicMount;
import com.austinv11.peripheralsplusplus.utils.IPlusPlusPeripheral;
import dan200.computercraft.api.peripheral.IComputerAccess;

public abstract class MountedTileEntityInventory extends TileEntityInventory implements IPlusPlusPeripheral {

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
