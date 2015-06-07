package com.austinv11.peripheralsplusplus.tiles;

import com.austinv11.collectiveframework.minecraft.tiles.TileEntityInventory;
import com.austinv11.peripheralsplusplus.init.ModBlocks;
import dan200.computercraft.api.turtle.ITurtleAccess;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;

public class TileEntityResupplyStation extends TileEntityInventory {
	
	public static String publicName = "resupplyStation";
	private  String name = "tileEntityResupplyStation";
	
	public String getName() {
		return name;
	}
	
	@Override
	public int getSize() {
		return 56;
	}
	
	@Override
	public String getInventoryName() {
		return StatCollector.translateToLocal(ModBlocks.resupplyStation.getUnlocalizedName()+".name");
	}
	
	public synchronized boolean resupply(ITurtleAccess turtle, int toSlot, String id, int meta) {
		ItemStack currentStack = turtle.getInventory().getStackInSlot(toSlot);
		if (!hasCorrectIdAndMeta(currentStack, id, meta))
			return false;
		Item item = (Item) Item.itemRegistry.getObject(id);
		if (item == null)
			return false;
		int amountToFill;
		if (currentStack == null)
			amountToFill = new ItemStack(item, 1, meta).getMaxStackSize();
		else
			amountToFill = currentStack.getMaxStackSize()-currentStack.stackSize;
		int currentSlot = 0;
		ItemStack stackToMerge = null;
		while (currentSlot < getSizeInventory() && amountToFill > 0) {
			if (getStackInSlot(currentSlot) == null) {
				currentSlot++;
				continue;
			}
			if (!hasCorrectIdAndMeta(getStackInSlot(currentSlot), id, meta)) {
				currentSlot++;
				continue;
			}
			if (stackToMerge == null) {
				stackToMerge = getStackInSlot(currentSlot).splitStack(MathHelper.clamp_int(amountToFill, 0, 
						getStackInSlot(currentSlot).stackSize));
				amountToFill -= stackToMerge.stackSize;
			} else {
				int toTake = MathHelper.clamp_int(amountToFill, 0, getStackInSlot(currentSlot).stackSize);
				getStackInSlot(currentSlot).stackSize -= toTake;
				stackToMerge.stackSize += toTake;
				amountToFill -= toTake;
			}
			currentSlot++;
		}
		if (stackToMerge == null)
			return false;
		turtle.getInventory().setInventorySlotContents(toSlot, currentStack == null ? stackToMerge : 
				new ItemStack(currentStack.getItem(), currentStack.stackSize + stackToMerge.stackSize, meta));
		markDirty();
		turtle.getInventory().markDirty();
		return true;
	}
	
	private boolean hasCorrectIdAndMeta(ItemStack stack, String id, int meta) {
		if (stack == null)
			return true;
		String otherId;
		if (stack.getItem() instanceof ItemBlock) {
			Block block = Block.getBlockFromItem(stack.getItem());
			otherId = Block.blockRegistry.getNameForObject(block);
		} else {
			Item item = stack.getItem();
			otherId = Item.itemRegistry.getNameForObject(item);
		}
		return otherId.equals(id) && stack.getItemDamage() == meta;
	}
}
