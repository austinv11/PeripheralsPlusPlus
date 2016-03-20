package com.austinv11.peripheralsplusplus.blocks;

import com.austinv11.peripheralsplusplus.tile.TileChatBox;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockChatBox extends BlockPPP implements ITileEntityProvider {
	public BlockChatBox() {
		super(Material.iron);
	}

	@Override
	public String getName() {
		return "blockChatBox";
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileChatBox();
	}
}
