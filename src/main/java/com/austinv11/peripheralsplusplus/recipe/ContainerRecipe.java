package com.austinv11.peripheralsplusplus.recipe;

import com.austinv11.collectiveframework.minecraft.utils.Colors;
import com.austinv11.collectiveframework.minecraft.utils.NBTHelper;
import com.austinv11.collectiveframework.utils.ArrayUtils;
import com.austinv11.peripheralsplusplus.blocks.BlockPeripheralContainer;
import com.austinv11.peripheralsplusplus.init.ModBlocks;
import com.austinv11.peripheralsplusplus.reference.Config;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralProvider;
import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class ContainerRecipe implements IRecipe {
	
	private static Block[] blacklist = new Block[]{ModBlocks.antenna};
	
	@Override
	public boolean matches(InventoryCrafting craftingInventory, World world) {
		boolean hasContainer = false;
		boolean hasPeripheral = false;
		int numOfPeripherals = 0;
		int numOfContained = 0;
		ItemStack container = new ItemStack(ModBlocks.peripheralContainer);
		for (int i = 0; i < craftingInventory.getSizeInventory(); i++)
			if (craftingInventory.getStackInSlot(i) != null) {
				if (!(craftingInventory.getStackInSlot(i).getItem() instanceof ItemBlock))
					return false;
				else {
					Block block = Block.getBlockFromItem(craftingInventory.getStackInSlot(i).getItem());
					if (block instanceof BlockPeripheralContainer && hasContainer)
						return false;
					else if (block instanceof BlockPeripheralContainer) {
						hasContainer = true;
						container = craftingInventory.getStackInSlot(i);
					} else if (ArrayUtils.indexOf(blacklist, block) == -1) {
						return false;
					} else if (block instanceof IPeripheralProvider) {
						hasPeripheral = true;
						numOfPeripherals++;
					}
				}
			}
		numOfContained = container.stackTagCompound == null || container.stackTagCompound.hasNoTags() ? 0 : NBTHelper.getIntArray(container, "ids").length;
		return hasContainer && hasPeripheral && (numOfContained+numOfPeripherals <= Config.maxNumberOfPeripherals);
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting craftingInventory) {
		HashMap<Integer,IPeripheral> map = new HashMap<Integer,IPeripheral>();
		ItemStack base = new ItemStack(ModBlocks.peripheralContainer);
		for (int i = 0; i < craftingInventory.getSizeInventory(); i++)
			if (craftingInventory.getStackInSlot(i) != null)
				if (Block.getBlockFromItem(craftingInventory.getStackInSlot(i).getItem()) instanceof IPeripheralProvider && !(Block.getBlockFromItem(craftingInventory.getStackInSlot(i).getItem()) instanceof BlockPeripheralContainer)) {
					TileEntity ent = Block.getBlockFromItem(craftingInventory.getStackInSlot(i).getItem()).createTileEntity(null, 0);
					if (ent != null && ent instanceof IPeripheral)
						map.put(Block.getIdFromBlock(Block.getBlockFromItem(craftingInventory.getStackInSlot(i).getItem())), (IPeripheral)ent);
				}else if (Block.getBlockFromItem(craftingInventory.getStackInSlot(i).getItem()) instanceof BlockPeripheralContainer)
					base.stackTagCompound = craftingInventory.getStackInSlot(i).stackTagCompound == null ? null : (NBTTagCompound) craftingInventory.getStackInSlot(i).stackTagCompound.copy();
		List<String> text = new ArrayList<String>();
		if (base.stackTagCompound == null || base.stackTagCompound.hasNoTags() || !base.stackTagCompound.hasKey("ids")) {
			NBTHelper.setIntArray(base, "ids", setToArray(map.keySet()));
			text.add(Colors.RESET.toString()+Colors.UNDERLINE+"Contained Peripherals:");
			for (IPeripheral p : map.values())
				text.add(Colors.RESET+p.getType());
			NBTHelper.addInfo(base, text);
		} else {
			text.add(Colors.RESET.toString()+Colors.UNDERLINE+"Contained Peripherals:");
			int[] ids = NBTHelper.getIntArray(base, "ids");
			int[] newIds = new int[ids.length+map.size()];
			for (int j = 0; j < ids.length+map.size(); j++)
				newIds[j] = j >= ids.length ? (Integer)map.keySet().toArray()[j-ids.length] : ids[j];
			NBTHelper.setIntArray(base, "ids", newIds);
			for (int id : newIds) {
				Block peripheral = Block.getBlockById(id);
				IPeripheral iPeripheral = (IPeripheral)peripheral.createTileEntity(null, 0);
				text.add(Colors.RESET+iPeripheral.getType());
			}
			NBTHelper.setInfo(base, text);
		}
		return base;
	}

	private int[] setToArray(Set<Integer> c) {
		int[] array = new int[c.size()];
		int i = 0;
		for (int j : c) {
			array[i] = j;
			i++;
		}
		return array;
	}

	@Override
	public int getRecipeSize() {
		return 9;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return new ItemStack(ModBlocks.peripheralContainer);
	}
}
