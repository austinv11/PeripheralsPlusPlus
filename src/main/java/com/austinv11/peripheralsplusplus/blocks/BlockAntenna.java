package com.austinv11.peripheralsplusplus.blocks;

import com.austinv11.collectiveframework.minecraft.utils.NBTHelper;
import com.austinv11.peripheralsplusplus.items.ItemSmartHelmet;
import com.austinv11.peripheralsplusplus.reference.Reference;
import com.austinv11.peripheralsplusplus.tiles.TileEntityAntenna;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralProvider;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BlockAntenna extends BlockPPP implements ITileEntityProvider, IPeripheralProvider {

	public BlockAntenna() {
		super();
		this.setBlockName("antenna");
		this.setLightOpacity(0);
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileEntityAntenna();
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public int getRenderType() {
		return -1;
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack itemStack) {
		int direction = BlockPistonBase.determineOrientation(world, x, y, z, entity);
		world.setBlockMetadataWithNotify(x, y, z, direction, 2);
	}

	@Override
	public IPeripheral getPeripheral(World world, int x, int y, int z, int side) {
		return (IPeripheral) world.getTileEntity(x,y,z);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister){//Registers the block icon(s)
		blockIcon = iconRegister.registerIcon(Reference.MOD_ID.toLowerCase()+":peripheralContainer");
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int meta, float hitX, float hitY, float hitZ) {
		if (player.getCurrentEquippedItem() == null || !(player.getCurrentEquippedItem().getItem() instanceof ItemSmartHelmet))
			return false;
		if (!world.isRemote) {
			TileEntityAntenna antenna = (TileEntityAntenna) world.getTileEntity(x,y,z);
			UUID id = antenna.identifier;
			NBTHelper.setString(player.getCurrentEquippedItem(), "identifier", id.toString());
			List<String> info = new ArrayList<String>();

			if (antenna.getLabel() == null) {
				info.add(Reference.Colors.RESET + Reference.Colors.GRAY + id.toString());
			}
			else {
				String label = antenna.getLabel();
				info.add(Reference.Colors.RESET + Reference.Colors.GRAY + label);
				NBTHelper.setString(player.getCurrentEquippedItem(), "label", label);
			}

			NBTHelper.setInfo(player.getCurrentEquippedItem(), info);
		}
		return true;
	}
}
