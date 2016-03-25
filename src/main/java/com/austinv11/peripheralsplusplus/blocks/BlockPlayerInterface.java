package com.austinv11.peripheralsplusplus.blocks;

import com.austinv11.peripheralsplusplus.PeripheralsPlusPlus;
import com.austinv11.peripheralsplusplus.reference.Reference;
import com.austinv11.peripheralsplusplus.tile.TilePlayerInterface;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BlockPlayerInterface extends BlockPPP implements ITileEntityProvider {

	public BlockPlayerInterface() {
		super(Material.iron);
	}

	@Override
	public String getName() {
		return "blockPlayerInterface";
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TilePlayerInterface();
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
		TileEntity tile = worldIn.getTileEntity(pos);
		if (!worldIn.isRemote) {
			if (tile != null) {
				playerIn.openGui(PeripheralsPlusPlus.instance, Reference.GUIs.PLAYER_INTERFACE.ordinal(), worldIn, pos.getX(), pos.getY(), pos.getZ());
			}
		}
		return true;
	}
}
