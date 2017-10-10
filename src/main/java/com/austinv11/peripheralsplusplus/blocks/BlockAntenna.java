package com.austinv11.peripheralsplusplus.blocks;

import com.austinv11.collectiveframework.minecraft.utils.Colors;
import com.austinv11.collectiveframework.minecraft.utils.NBTHelper;
import com.austinv11.peripheralsplusplus.items.ItemNanoSwarm;
import com.austinv11.peripheralsplusplus.items.ItemSmartHelmet;
import com.austinv11.peripheralsplusplus.reference.Reference;
import com.austinv11.peripheralsplusplus.tiles.TileEntityAntenna;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BlockAntenna extends BlockPppDirectional implements ITileEntityProvider {

	public BlockAntenna() {
		super();
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
		this.setRegistryName(Reference.MOD_ID, "antenna");
		this.setUnlocalizedName("antenna");
		this.setLightOpacity(0);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING);
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileEntityAntenna();
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.INVISIBLE;
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
								ItemStack stack) {
		worldIn.setBlockState(pos, state.withProperty(FACING,
				EnumFacing.getDirectionFromEntityLiving(pos, placer)), 2);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(FACING).getIndex();
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(FACING, EnumFacing.getFront(meta));
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player,
									EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (player.getHeldItemMainhand().isEmpty() ||
				!((player.getHeldItemMainhand().getItem() instanceof ItemSmartHelmet) ||
						(player.getHeldItemMainhand().getItem() instanceof ItemNanoSwarm)))
			return false;
		if (!world.isRemote) {
			TileEntityAntenna antenna = (TileEntityAntenna) world.getTileEntity(pos);
			if (antenna == null)
				return false;
			UUID id = antenna.identifier;
			NBTHelper.setString(player.getHeldItemMainhand(), "identifier", id.toString());
			List<String> info = new ArrayList<String>();

			if (antenna.getLabel() == null) {
				info.add(Colors.RESET.toString() + Colors.GRAY + id.toString());
			}
			else {
				String label = antenna.getLabel();
				info.add(Colors.RESET.toString() + Colors.GRAY + label);
				NBTHelper.setString(player.getHeldItemMainhand(), "label", label);
			}

			NBTHelper.setInfo(player.getHeldItemMainhand(), info);
		}
		return true;
	}
}
