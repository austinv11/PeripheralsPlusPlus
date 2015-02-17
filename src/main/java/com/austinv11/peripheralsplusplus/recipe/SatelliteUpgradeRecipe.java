package com.austinv11.peripheralsplusplus.recipe;

import com.austinv11.peripheralsplusplus.init.ModItems;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class SatelliteUpgradeRecipe implements IRecipe {

	private ItemStack result, required;
	private boolean retainNBT;

	public SatelliteUpgradeRecipe(ItemStack result, ItemStack required, boolean retainNBT) {
		this.result = result;
		this.required = required;
		this.retainNBT = retainNBT;
	}

	@Override
	public boolean matches(InventoryCrafting inventory, World world) {
		int slotsTaken = 0;
		boolean hasUpgrade = false;
		boolean hasSocket = false;
		for (int i = 0; i < inventory.getSizeInventory(); i++)
			if (inventory.getStackInSlot(i) != null) {
				if (inventory.getStackInSlot(i).getItem() == ModItems.socket) {
					hasSocket = true;
					slotsTaken++;
				} else if (inventory.getStackInSlot(i).equals(required)){
					hasUpgrade = true;
					slotsTaken++;
				} else {
					return false;
				}
			}
		return hasSocket && hasUpgrade && slotsTaken == 2;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inventory) {
		ItemStack toCraft = required.copy();
		ItemStack toReturn = result.copy();
		for (int i = 0; i < inventory.getSizeInventory(); i++)
			if (inventory.getStackInSlot(i) != null) {
				 if (inventory.getStackInSlot(i).equals(required)) {
					 toCraft = inventory.getStackInSlot(i);
					 break;
				}
			}
		if (retainNBT)
			toReturn.setTagCompound(toCraft.hasTagCompound() ? toCraft.getTagCompound() : new NBTTagCompound());
		return toReturn;
	}

	@Override
	public int getRecipeSize() {
		return 2;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return result;
	}
}
