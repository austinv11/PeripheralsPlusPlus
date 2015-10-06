package com.austinv11.peripheralsplusplus.blocks;

import com.austinv11.peripheralsplusplus.PeripheralsPlusPlus;
import com.austinv11.peripheralsplusplus.creativetab.CreativeTabPPP;
import com.austinv11.peripheralsplusplus.reference.Reference;
import com.austinv11.peripheralsplusplus.tiles.TileEntityAnalyzer;
import com.austinv11.peripheralsplusplus.tiles.TileEntityResupplyStation;
import com.google.common.collect.Lists;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.ArrayList;

public class BlockResupplyStation extends BlockContainerPPP {
	
	public BlockResupplyStation() {
		super(Material.rock);
		this.setBlockName("resupplyStation");
		this.setCreativeTab(CreativeTabPPP.PPP_TAB);
		this.setHardness(4f);
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int meta, float hitX, float hitY, float hitZ){
		TileEntity te = world.getTileEntity(x, y, z);
		if (!world.isRemote) {
			if (te != null)
				player.openGui(PeripheralsPlusPlus.instance, Reference.GUIs.RESUPPLY_STATION.ordinal(), world, x, y, z);
		}
		return true;
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityResupplyStation();
	}
}
