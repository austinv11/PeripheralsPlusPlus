package com.austinv11.peripheralsplusplus.blocks;

import com.austinv11.peripheralsplusplus.reference.Reference;
import com.austinv11.peripheralsplusplus.tiles.TileEntityAIChatBox;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockAIChatBox extends BlockPppBase implements ITileEntityProvider  {

	public BlockAIChatBox() {
		super();
		this.setRegistryName(Reference.MOD_ID, "ai_chat_box");
		this.setUnlocalizedName("ai_chat_box");
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileEntityAIChatBox();
	}
}
