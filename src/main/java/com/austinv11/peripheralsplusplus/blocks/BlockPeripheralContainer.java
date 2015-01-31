package com.austinv11.peripheralsplusplus.blocks;

import com.austinv11.peripheralsplusplus.tiles.TileEntityPeripheralContainer;
import com.austinv11.peripheralsplusplus.utils.NBTHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralProvider;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class BlockPeripheralContainer extends BlockPPP implements ITileEntityProvider, IPeripheralProvider{

//	BlockSnapshot blockSnapshot;

	public BlockPeripheralContainer() {
		super();
		this.setBlockName("peripheralContainer");
	}

	@Override
	public IPeripheral getPeripheral(World world, int x, int y, int z, int side) {
		return (IPeripheral)world.getTileEntity(x,y,z);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityPeripheralContainer();
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack itemStack) {
		int[] ids = NBTHelper.getIntArray(itemStack, "ids");
		Block[] blocks = new Block[ids.length];
		for (int i = 0; i < ids.length; i++)
			blocks[i] = Block.getBlockById(ids[i]);
		TileEntityPeripheralContainer ent = (TileEntityPeripheralContainer)world.getTileEntity(x,y,z);
		for (Block block : blocks)
			ent.addPeripheral(block);
	}

//	@Override
//	public void onBlockHarvested(World world, int x, int y, int z, int meta, EntityPlayer player) {
//		blockSnapshot = BlockSnapshot.getBlockSnapshot(world, x, y, z);
//	}

	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
		ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
//		TileEntity te = blockSnapshot.getTileEntity();
//		NBTTagCompound tag = new NBTTagCompound();
//		te.writeToNBT(tag);
//		ItemStack drop = new ItemStack(ModBlocks.peripheralContainer);
//		drop.stackTagCompound = tag;
//		List<String> text = new ArrayList<String>();
//		text.add(Reference.Colors.RESET+Reference.Colors.UNDERLINE+"Contained Peripherals:");
//		for (int id : NBTHelper.getIntArray(drop, "ids")) {
//			Block peripheral = Block.getBlockById(id);
//			IPeripheral iPeripheral = (IPeripheral)peripheral.createTileEntity(null, 0);
//			text.add(Reference.Colors.RESET+iPeripheral.getType());
//		}
//		NBTHelper.setInfo(drop, text);
		return drops;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
		TileEntityPeripheralContainer te = (TileEntityPeripheralContainer)world.getTileEntity(x,y,z);
		List<Block> contained = te.getBlocksContained();
		if (contained.size() >= side+1)
			return contained.get(side).getIcon(side, 0);
		else
			return blockIcon;
	}

//	@Override
//	@SideOnly(Side.CLIENT)
//	public IIcon getIcon(int side, int meta) {
//		ForgeDirection dir = ForgeDirection.getOrientation(side);
//		return dir == ForgeDirection.DOWN || dir == ForgeDirection.UP ? this.blockIcon : this.frontIcon;
//	}
}
