package com.austinv11.peripheralsplusplus.event.handler;

import com.austinv11.collectiveframework.minecraft.event.FindMatchingRecipeEvent;
import com.austinv11.collectiveframework.minecraft.utils.NBTHelper;
import com.austinv11.peripheralsplusplus.hooks.ComputerCraftRegistry;
import com.austinv11.peripheralsplusplus.reference.Config;
import com.austinv11.peripheralsplusplus.reference.Reference;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import dan200.computercraft.shared.pocket.items.ItemPocketComputer;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;

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
		if (NBTHelper.hasTag(computerStack, "upgrade")) {
			if (NBTHelper.getShort(computerStack, "upgrade") == Reference.PERIPHERAL_CONTAINER) {
				if (!NBTHelper.hasTag(computerStack, "upgrades"))
					NBTHelper.setList(computerStack, "upgrades", new NBTTagList());
				NBTTagList list = NBTHelper.getList(computerStack, "upgrades", Constants.NBT.TAG_FLOAT);
				if (list.tagCount() > Config.maxNumberOfPeripherals)
					return;
				list.appendTag(new NBTTagFloat(upgrade));
				event.result = computerStack;
			}
			return;
		}
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
