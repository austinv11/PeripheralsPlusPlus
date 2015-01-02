package com.austinv11.peripheralsplusplus.blocks;

import com.austinv11.peripheralsplusplus.init.ModBlocks;
import com.austinv11.peripheralsplusplus.reference.Reference;
import com.austinv11.peripheralsplusplus.tiles.TileEntitySpeaker;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralProvider;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockSpeaker extends BlockPPP implements ITileEntityProvider, IPeripheralProvider {

	public IIcon frontIcon;

	public BlockSpeaker() {
		super();
		this.setBlockName("speaker");
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileEntitySpeaker();
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
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
		ForgeDirection dir = ForgeDirection.getOrientation(side);
		return dir == ForgeDirection.DOWN || dir == ForgeDirection.UP ? this.blockIcon : this.frontIcon;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		ForgeDirection dir = ForgeDirection.getOrientation(side);
		return dir == ForgeDirection.DOWN || dir == ForgeDirection.UP ? this.blockIcon : this.frontIcon;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister){//Registers the block icon(s)
		blockIcon = iconRegister.registerIcon(String.format("%s", getUnwrappedUnlocalizedName(ModBlocks.chatBox.getUnlocalizedName())));
		frontIcon = iconRegister.registerIcon(Reference.MOD_ID.toLowerCase()+":speaker");
	}
}
