package com.austinv11.peripheralsplusplus.blocks;

import com.austinv11.peripheralsplusplus.reference.Config;
import com.austinv11.peripheralsplusplus.tiles.TileEntityOreDictionary;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralProvider;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockOreDictionary extends BlockPPP implements ITileEntityProvider, IPeripheralProvider {

	public BlockOreDictionary() {
		super();
		this.setBlockName("oreDictionary");
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileEntityOreDictionary();
	}

	@Override
	public boolean hasTileEntity(int metadata) {
		return true;
	}

	@Override
	public IPeripheral getPeripheral(World world, int x, int y, int z, int side) {
		return (IPeripheral) world.getTileEntity(x,y,z);
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int meta, float hitX, float hitY, float hitZ) {
		if (Config.enableOreDictionary) {
			if (!world.isRemote)
				((TileEntityOreDictionary) world.getTileEntity(x, y, z)).blockActivated(player);
//			NBTTagCompound tag =player.getHeldItem().stackTagCompound;
//			try {
//				Logger.info(tag.toString());FIXME DEBUG
//			} catch (Exception e) {
//				//e.printStackTrace();
//			}
		}
		return true;
	}
}
