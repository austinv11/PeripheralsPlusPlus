package com.austinv11.peripheralsplusplus.recipe;

import com.austinv11.peripheralsplusplus.blocks.BlockPeripheralContainer;
import com.austinv11.peripheralsplusplus.init.ModBlocks;
import com.austinv11.peripheralsplusplus.reference.Reference;
import com.austinv11.peripheralsplusplus.utils.NBTHelper;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralProvider;
import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class ContainerRecipe implements IRecipe {
	
	@Override
	public boolean matches(InventoryCrafting p_77569_1_, World p_77569_2_) {
		boolean hasContainer = false;
		boolean hasPeripheral = false;
		for (int i = 0; i < p_77569_1_.getSizeInventory(); i++)
			if (p_77569_1_.getStackInSlot(i) != null) {
				if (!(p_77569_1_.getStackInSlot(i).getItem() instanceof ItemBlock))
					return false;
				else {
					Block block = Block.getBlockFromItem(p_77569_1_.getStackInSlot(i).getItem());
					if (block instanceof BlockPeripheralContainer && hasContainer)
						return false;
					else if (block instanceof BlockPeripheralContainer)
						hasContainer = true;
					else if (block instanceof IPeripheralProvider)
						hasPeripheral = true;
				}
			}
		return hasContainer && hasPeripheral;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting p_77572_1_) {
		HashMap<Integer,IPeripheral> map = new HashMap<Integer,IPeripheral>();
		ItemStack base = new ItemStack(ModBlocks.container);
		for (int i = 0; i < p_77572_1_.getSizeInventory(); i++)
			if (p_77572_1_.getStackInSlot(i) != null)
				if (Block.getBlockFromItem(p_77572_1_.getStackInSlot(i).getItem()) instanceof IPeripheralProvider) {
					TileEntity ent = Block.getBlockFromItem(p_77572_1_.getStackInSlot(i).getItem()).createTileEntity(null, 0);
					if (ent != null && ent instanceof IPeripheral)
						map.put(Block.getIdFromBlock(Block.getBlockFromItem(p_77572_1_.getStackInSlot(i).getItem())), (IPeripheral)ent);
				}else if (Block.getBlockFromItem(p_77572_1_.getStackInSlot(i).getItem()) instanceof BlockPeripheralContainer)
					base = p_77572_1_.getStackInSlot(i);
		if (base.stackTagCompound == null || base.stackTagCompound.hasNoTags()) {
			NBTTagIntArray ids = new NBTTagIntArray(setToArray(map.keySet()));
			NBTHelper.setOther(base, "ids", ids);
			List<String> text = new ArrayList<String>();
			text.add(Reference.Colors.RESET+Reference.Colors.UNDERLINE+"Contained Peripherals:");
			for (IPeripheral p : map.values())
				text.add(Reference.Colors.RESET+p.getType());
			NBTHelper.addInfo(base, text);
		} else {
			int[] ids = ((NBTTagIntArray)NBTHelper.getOther(base, "ids")).func_150302_c();
			int[] newIds = new int[ids.length+map.size()];
			for (int j = 0; j < ids.length+map.size(); j++)
				newIds[j] = ids[j];
			NBTHelper.setOther(base, "ids", new NBTTagIntArray(newIds));
			List<String> text = new ArrayList<String>();
			for (int id : newIds) {
				Block peripheral = Block.getBlockById(id);
				IPeripheral iPeripheral = (IPeripheral)peripheral.createTileEntity(null, 0);
				text.add(Reference.Colors.RESET+iPeripheral.getType());
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
		return new ItemStack(ModBlocks.container);
	}
}
