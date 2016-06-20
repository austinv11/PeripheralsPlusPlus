package com.austinv11.peripheralsplusplus.event.handler;

import com.austinv11.collectiveframework.minecraft.utils.NBTHelper;
import com.austinv11.peripheralsplusplus.hooks.ComputerCraftRegistry;
import com.austinv11.peripheralsplusplus.reference.Config;
import com.austinv11.peripheralsplusplus.reference.Reference;
import cpw.mods.fml.common.registry.GameRegistry;
import dan200.computercraft.shared.pocket.items.ItemPocketComputer;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

public class PocketComputerCraftingHandler implements IRecipe {
	
	private int findUpgradeItem(ItemStack stack) {
		for (ItemStack stack1 : ComputerCraftRegistry.craftingRecipes.keySet())
			if (stack.isItemEqual(stack1))
				return ComputerCraftRegistry.craftingRecipes.get(stack1);
		return -1;
	}
	
	@Override
	public boolean matches(InventoryCrafting inventory, World world) {
		int computerSlot = -1;
		int upgradeSlot = -1;
		for (int i = 0; i < inventory.getSizeInventory(); i++) {
			if (inventory.getStackInSlot(i) != null) {
				if (inventory.getStackInSlot(i).getItem() instanceof ItemPocketComputer) {
					if (computerSlot != -1)
						return false;
					computerSlot = i;
				} else if (findUpgradeItem(inventory.getStackInSlot(i)) != -1) {
					if (upgradeSlot != -1)
						return false;
					upgradeSlot = i;
				} else
					return false;
			}
		}
		if (computerSlot == -1 || upgradeSlot == -1)
			return false;
		ItemStack computerStack = inventory.getStackInSlot(computerSlot).copy();
		if (NBTHelper.hasTag(computerStack, "upgrade")) {
			if (NBTHelper.getShort(computerStack, "upgrade") == Reference.PERIPHERAL_CONTAINER) {
				if (!NBTHelper.hasTag(computerStack, "upgrades"))
					return true;
				NBTTagList list = NBTHelper.getList(computerStack, "upgrades", Constants.NBT.TAG_FLOAT);
				if (list.tagCount() > Config.maxNumberOfPeripherals)
					return false;
				return true;
			}
			return false;
		}
		return true;
	}
	
	@Override
	public ItemStack getCraftingResult(InventoryCrafting inventory) {
		int computerSlot = -1;
		int upgradeSlot = -1;
		int upgrade = 1;
		for (int i = 0; i < inventory.getSizeInventory(); i++) {
			if (inventory.getStackInSlot(i) != null) {
				if (inventory.getStackInSlot(i).getItem() instanceof ItemPocketComputer) {
					if (computerSlot != -1)
						return null;
					computerSlot = i;
				} else if ((upgrade = findUpgradeItem(inventory.getStackInSlot(i))) != -1) {
					if (upgradeSlot != -1)
						return null;
					upgradeSlot = i;
				} else
					return null;
			}
		}
		if (computerSlot == -1 || upgradeSlot == -1)
			return null;
		ItemStack computerStack = inventory.getStackInSlot(computerSlot).copy();
		if (NBTHelper.hasTag(computerStack, "upgrade")) {
			if (NBTHelper.getShort(computerStack, "upgrade") == Reference.PERIPHERAL_CONTAINER) {
				if (!NBTHelper.hasTag(computerStack, "upgrades"))
					NBTHelper.setList(computerStack, "upgrades", new NBTTagList());
				NBTTagList list = NBTHelper.getList(computerStack, "upgrades", Constants.NBT.TAG_FLOAT);
				if (list.tagCount() > Config.maxNumberOfPeripherals)
					return null;
				list.appendTag(new NBTTagFloat(upgrade));
			} else
				return null;
		} else
			NBTHelper.setShort(computerStack, "upgrade", (short)upgrade);
		NBTHelper.removeTag(computerStack, "instanceID");
		NBTHelper.removeTag(computerStack, "sessionID");
		return computerStack;
	}
	
	@Override
	public int getRecipeSize() {
		return 2;
	}
	
	@Override
	public ItemStack getRecipeOutput() {
		return new ItemStack(GameRegistry.findItem("ComputerCraft", "pocketComputer"));
	}
}
