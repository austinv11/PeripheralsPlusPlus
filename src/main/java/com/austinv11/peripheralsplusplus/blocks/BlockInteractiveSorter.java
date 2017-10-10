package com.austinv11.peripheralsplusplus.blocks;

import com.austinv11.peripheralsplusplus.PeripheralsPlusPlus;
import com.austinv11.peripheralsplusplus.creativetab.CreativeTabPPP;
import com.austinv11.peripheralsplusplus.reference.Reference;
import com.austinv11.peripheralsplusplus.tiles.TileEntityInteractiveSorter;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockInteractiveSorter extends BlockContainerPPP {
	
	public BlockInteractiveSorter() {
		super(Material.ROCK);
		this.setRegistryName(Reference.MOD_ID, "interactive_sorter");
		this.setUnlocalizedName("interactive_sorter");
		this.setCreativeTab(CreativeTabPPP.PPP_TAB);
		this.setHardness(4f);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityInteractiveSorter();
	}

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player,
                                    EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            TileEntity te = world.getTileEntity(pos);
            if (te != null)
                player.openGui(PeripheralsPlusPlus.instance, Reference.GUIs.INTERACTIVE_SORTER.ordinal(), world,
                        pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}
}
