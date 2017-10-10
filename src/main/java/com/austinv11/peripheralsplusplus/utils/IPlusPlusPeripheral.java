package com.austinv11.peripheralsplusplus.utils;

import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Implement this on any TileEntity in Peripherals++ instead of {@code IPeripheral} to have a way of detecting if a peripheral is from this mod.
 */
public interface IPlusPlusPeripheral extends IPeripheral {

	/**
	 * This is the common provider for all Peripherals++ TileEntities
	 */
	public static class Provider implements IPeripheralProvider {

		@Nullable
		@Override
		public IPeripheral getPeripheral(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull EnumFacing side) {
			TileEntity tile = world.getTileEntity(pos);
			return tile instanceof IPlusPlusPeripheral ? (IPlusPlusPeripheral) tile : null;
		}
	}
}
