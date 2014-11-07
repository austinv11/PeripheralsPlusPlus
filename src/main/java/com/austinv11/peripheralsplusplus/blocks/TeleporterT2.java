package com.austinv11.peripheralsplusplus.blocks;

import com.austinv11.peripheralsplusplus.reference.Reference;
import com.austinv11.peripheralsplusplus.tiles.TileEntityTeleporterT2;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TeleporterT2 extends Teleporter{

	public TeleporterT2() {
		super();
		this.setBlockName("teleporterT2");
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileEntityTeleporterT2();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister){//Registers the block icon(s)
		blockIcon = iconRegister.registerIcon(String.format("%s", getUnwrappedUnlocalizedName(this.getUnlocalizedName())));
		frontIcon = iconRegister.registerIcon(Reference.MOD_ID.toLowerCase()+":teleporterT2Front");
	}
}
