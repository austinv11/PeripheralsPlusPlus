package com.austinv11.peripheralsplusplus.blocks;

import com.austinv11.peripheralsplusplus.reference.Reference;
import com.austinv11.peripheralsplusplus.tiles.TileEntityRFCharger;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockRFCharger extends BlockPppBase implements ITileEntityProvider {

	public BlockRFCharger() {
		super();
		this.setUnlocalizedName("rf_charger");
		this.setRegistryName(Reference.MOD_ID, "rf_charger");
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileEntityRFCharger();
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player,
									EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		// TODO gui
		if (world.isRemote)
			return true;
		TileEntity tileEntity = world.getTileEntity(pos);
		if (!(tileEntity instanceof TileEntityRFCharger))
			return false;
		((TileEntityRFCharger)tileEntity).showFuel(player);
		return true;
	}
}
