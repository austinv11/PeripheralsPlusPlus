package com.austinv11.peripheralsplusplus.blocks;

import com.austinv11.peripheralsplusplus.tiles.TileEntityPeripheralContainer;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralProvider;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockPeripheralContainer extends BlockPPP implements ITileEntityProvider, IPeripheralProvider{

	public BlockPeripheralContainer() {
		super();
		this.setBlockName("peripheralContainer");
	}

	@Override
	public IPeripheral getPeripheral(World world, int x, int y, int z, int side) {
		return (IPeripheral)world.getTileEntity(x,y,z);
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileEntityPeripheralContainer();
	}

	@Override
	public void onBlockPlacedBy(World p_149689_1_, int p_149689_2_, int p_149689_3_, int p_149689_4_, EntityLivingBase p_149689_5_, ItemStack p_149689_6_) {

	}
}
