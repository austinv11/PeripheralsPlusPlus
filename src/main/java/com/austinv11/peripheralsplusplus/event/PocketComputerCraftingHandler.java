package com.austinv11.peripheralsplusplus.event;

import com.austinv11.collectiveframework.minecraft.event.FindMatchingRecipeEvent;
import com.austinv11.collectiveframework.minecraft.utils.NBTHelper;
import com.austinv11.peripheralsplusplus.hooks.ComputerCraftRegistry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import dan200.computercraft.shared.pocket.items.ItemPocketComputer;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;

public class PocketComputerCraftingHandler {
	
	@SubscribeEvent
	public void onLookForRecipe(FindMatchingRecipeEvent event) {
		if (event.result != null)
			return;
		InventoryCrafting inventory = event.craftingInventory;
		int computerSlot = -1;
		int upgradeSlot = -1;
		int upgrade = 1;
		for (int i = 0; i < inventory.getSizeInventory(); i++) {
			if (inventory.getStackInSlot(i) != null) {
				if (inventory.getStackInSlot(i).getItem() instanceof ItemPocketComputer) {
					if (computerSlot != -1)
						return;
					computerSlot = i;
				} else if ((upgrade = findUpgradeItem(inventory.getStackInSlot(i))) != -1) {
					if (upgradeSlot != -1)
						return;
					upgradeSlot = i;
				} else
					return;
			}
		}
		if (computerSlot == -1 || upgradeSlot == -1)
			return;
		ItemStack computerStack = inventory.getStackInSlot(computerSlot).copy();
		if (NBTHelper.hasTag(computerStack, "upgrade"))
			return;
		NBTHelper.setShort(computerStack, "upgrade", (short)upgrade);
		event.result = computerStack;
	}
	
	private int findUpgradeItem(ItemStack stack) {
		for (ItemStack stack1 : ComputerCraftRegistry.craftingRecipes.keySet())
			if (stack.isItemEqual(stack1))
				return ComputerCraftRegistry.craftingRecipes.get(stack1);
		return -1;
	}
}
