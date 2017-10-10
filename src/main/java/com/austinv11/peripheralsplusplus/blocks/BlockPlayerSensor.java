package com.austinv11.peripheralsplusplus.blocks;

import com.austinv11.peripheralsplusplus.reference.Config;
import com.austinv11.peripheralsplusplus.reference.Reference;
import com.austinv11.peripheralsplusplus.tiles.TileEntityPlayerSensor;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockPlayerSensor extends BlockPppBase implements ITileEntityProvider {

	public BlockPlayerSensor() {
		super();
        this.setRegistryName(Reference.MOD_ID, "player_sensor");
        this.setUnlocalizedName("player_sensor");
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileEntityPlayerSensor();
	}

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
                                    EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (Config.enablePlayerSensor)
            ((TileEntityPlayerSensor)world.getTileEntity(pos)).blockActivated(player.getDisplayNameString());
        return true;
    }
}
