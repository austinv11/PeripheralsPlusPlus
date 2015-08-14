package com.austinv11.peripheralsplusplus.utils;

import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Implement this on any TileEntity in Peripherals++ instead of {@code IPeripheral} to have a way of detecting if a peripheral is from this mod.
 */
public interface IPlusPlusPeripheral extends IPeripheral {

	/**
	 * This is the common provider for all Peripherals++ TileEntities
	 */
	public static class Provider implements IPeripheralProvider {

		@Override
		public IPeripheral getPeripheral(World world, int x, int y, int z, int side) {
			TileEntity tile = world.getTileEntity(x, y, z);
			return tile instanceof IPlusPlusPeripheral ? (IPlusPlusPeripheral) tile : null;
		}
	}
}
