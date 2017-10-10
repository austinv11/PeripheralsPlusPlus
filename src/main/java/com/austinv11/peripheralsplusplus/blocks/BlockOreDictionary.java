package com.austinv11.peripheralsplusplus.blocks;

import com.austinv11.peripheralsplusplus.reference.Config;
import com.austinv11.peripheralsplusplus.reference.Reference;
import com.austinv11.peripheralsplusplus.tiles.TileEntityOreDictionary;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockOreDictionary extends BlockPppBase implements ITileEntityProvider {

	public BlockOreDictionary() {
		super();
		this.setRegistryName(Reference.MOD_ID, "ore_dictionary");
		this.setUnlocalizedName("ore_dictionary");
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileEntityOreDictionary();
	}

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player,
                                    EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
	    if (world.isRemote)
            return true;
        if (Config.enableOreDictionary) {
            TileEntity tileEntity = world.getTileEntity(pos);
            return tileEntity != null && ((TileEntityOreDictionary) tileEntity).blockActivated(player);
        }
        return false;
    }
}
