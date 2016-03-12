package com.austinv11.peripheralsplusplus.util;

import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

/**
 * Implement this on any TileEntity in Peripherals++ instead of {@code IPeripheral} to have a way of detecting if a peripheral is from this mod.
 */
public interface IPlusPlusPeripheral extends IPeripheral {

	/**
	 * This is the common provider for all Peripherals++ TileEntities
	 */
	class Provider implements IPeripheralProvider {
		@Override
		public IPeripheral getPeripheral(World world, BlockPos pos, EnumFacing facing) {
			TileEntity tile = world.getTileEntity(pos);
			return tile instanceof IPlusPlusPeripheral ? (IPlusPlusPeripheral) tile : null;
		}
	}
}