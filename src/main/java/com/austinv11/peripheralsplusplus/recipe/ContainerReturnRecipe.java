package com.austinv11.peripheralsplusplus.recipe;

import com.austinv11.peripheralsplusplus.init.ModBlocks;
import com.austinv11.peripheralsplusplus.items.ItemBlockPeripheralContainer;
import com.austinv11.peripheralsplusplus.reference.Reference;
import com.austinv11.peripheralsplusplus.utils.NBTHelper;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class ContainerReturnRecipe implements IRecipe {
	
	@Override
	public boolean matches(InventoryCrafting p_77569_1_, World p_77569_2_) {
		boolean hasContainer = false;
		for (int i = 0; i < p_77569_1_.getSizeInventory(); i++)
			if (p_77569_1_.getStackInSlot(i) != null)
				if (p_77569_1_.getStackInSlot(i).getItem() instanceof ItemBlockPeripheralContainer) {
					if (hasContainer)
						return false;
					hasContainer = true;
				} else {
					return false;
				}
		return hasContainer;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting p_77572_1_) {
		ItemStack start = getRecipeOutput();
		for (int i = 0; i < p_77572_1_.getSizeInventory(); i++)
			if (p_77572_1_.getStackInSlot(i) != null)
				start = p_77572_1_.getStackInSlot(i);
		if (start.stackTagCompound == null)
			return null;
		int[] ids = ((NBTTagIntArray)NBTHelper.getOther(start, "ids")).func_150302_c();
		ItemStack returnVal = new ItemStack(Block.getBlockById(ids[0]));
		if (ids.length > 1) {
			int[] newIds = new int[ids.length-1];
			for (int j = 0; j < ids.length-1; j++)
				newIds[j] = ids[j+1];
			NBTHelper.setOther(start, "ids", new NBTTagIntArray(newIds));
			List<String> text = new ArrayList<String>();
			text.add(Reference.Colors.RESET+Reference.Colors.UNDERLINE+"Contained Peripherals:");
			for (int id : newIds) {
				Block peripheral = Block.getBlockById(id);
				IPeripheral iPeripheral = (IPeripheral)peripheral.createTileEntity(null, 0);
				text.add(Reference.Colors.RESET+iPeripheral.getType());
			}
			NBTHelper.setInfo(start, text);
		}
		return returnVal;
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
