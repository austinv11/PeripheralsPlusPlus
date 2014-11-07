package com.austinv11.peripheralsplusplus.blocks;

import com.austinv11.peripheralsplusplus.reference.Config;
import com.austinv11.peripheralsplusplus.reference.Reference;
import com.austinv11.peripheralsplusplus.tiles.TileEntityTeleporter;
import com.austinv11.peripheralsplusplus.utils.IconManager;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralProvider;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class Teleporter extends PPPBlock implements ITileEntityProvider, IPeripheralProvider {

	public IIcon frontIcon;

	public Teleporter() {
		super();
		this.setBlockName("teleporter");
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileEntityTeleporter();
	}

	@Override
	public boolean hasTileEntity(int metadata) {
		return true;
	}

	@Override
	public IPeripheral getPeripheral(World world, int x, int y, int z, int side ) {
		return (IPeripheral) world.getTileEntity(x,y,z);
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack itemStack) {
		int direction = BlockPistonBase.determineOrientation(world, x, y, z, entity);
		world.setBlockMetadataWithNotify(x, y, z, direction, 2);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
		return (side == world.getBlockMetadata(x, y, z)) ? this.frontIcon : this.blockIcon;
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int meta, float hitX, float hitY, float hitZ) {
		TileEntityTeleporter tp = (TileEntityTeleporter)world.getTileEntity(x,y,z);
		if (player.getCurrentEquippedItem() == null || !player.getCurrentEquippedItem().isItemEqual(new ItemStack(Items.repeater)))
			return false;
		if (!world.isRemote)
			tp.blockActivated(player);
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister){//Registers the block icon(s)
		blockIcon = iconRegister.registerIcon(String.format("%s", getUnwrappedUnlocalizedName(this.getUnlocalizedName())));
		frontIcon = iconRegister.registerIcon(Reference.MOD_ID.toLowerCase()+":teleporterFront");
	}
}
