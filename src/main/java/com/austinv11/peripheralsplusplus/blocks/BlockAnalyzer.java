package com.austinv11.peripheralsplusplus.blocks;

import com.austinv11.peripheralsplusplus.PeripheralsPlusPlus;
import com.austinv11.peripheralsplusplus.creativetab.CreativeTabPPP;
import com.austinv11.peripheralsplusplus.reference.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BlockAnalyzer extends BlockContainerPPP {

	public BlockAnalyzer() {
		super(Material.ROCK);
		this.setCreativeTab(CreativeTabPPP.PPP_TAB);
		this.setHardness(4f);
	}

	public abstract Block getBlock();

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player,
									EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		TileEntity te = world.getTileEntity(new BlockPos(pos.getX(), pos.getY(), pos.getZ()));
		if (!world.isRemote) {
			if (te != null)
				player.openGui(PeripheralsPlusPlus.instance, Reference.GUIs.ANALYZER.ordinal(), world,
						pos.getX(), pos.getY(), pos.getZ());
		}
		return true;
	}

	@Override
	public abstract TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_);

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}
}
