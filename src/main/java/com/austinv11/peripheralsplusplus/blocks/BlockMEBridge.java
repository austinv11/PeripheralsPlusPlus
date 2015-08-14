package com.austinv11.peripheralsplusplus.blocks;

import com.austinv11.peripheralsplusplus.tiles.TileEntityMEBridge;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockMEBridge extends BlockPPP implements ITileEntityProvider {

	public BlockMEBridge() {
		super();
		this.setBlockName("meBridge");
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileEntityMEBridge();
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack itemStack) {
		TileEntityMEBridge meBridge = (TileEntityMEBridge) world.getTileEntity(x,y,z);
		meBridge.placed = (EntityPlayer) entity;
	}
}
