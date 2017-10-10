package com.austinv11.peripheralsplusplus.blocks;

import com.austinv11.peripheralsplusplus.reference.Reference;
import com.austinv11.peripheralsplusplus.tiles.TileEntityChatBox;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockChatBox extends BlockPppBase implements ITileEntityProvider {

	public BlockChatBox() {
		super();
		this.setRegistryName(Reference.MOD_ID, "chat_box");
		this.setUnlocalizedName("chat_box");
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityChatBox();
	}

	@Override
	public boolean hasTileEntity(IBlockState blockState) {
		return true;
	}

}
