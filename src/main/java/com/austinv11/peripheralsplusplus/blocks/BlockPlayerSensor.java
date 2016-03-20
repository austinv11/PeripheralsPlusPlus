package com.austinv11.peripheralsplusplus.blocks;

import com.austinv11.peripheralsplusplus.tile.TilePlayerSensor;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BlockPlayerSensor extends BlockPPP implements ITileEntityProvider {

	public BlockPlayerSensor() {
		super(Material.iron);
	}

	@Override
	public String getName() {
		return "blockPlayerSensor";
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TilePlayerSensor();
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (worldIn.getTileEntity(pos) != null) {
			((TilePlayerSensor)worldIn.getTileEntity(pos)).onBlockActivated(playerIn.getName());
			return true;
		}
		return false;
	}
}
