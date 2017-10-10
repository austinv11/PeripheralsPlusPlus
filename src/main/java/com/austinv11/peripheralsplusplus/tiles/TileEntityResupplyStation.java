package com.austinv11.peripheralsplusplus.tiles;

import com.austinv11.collectiveframework.minecraft.tiles.TileEntityInventory;
import com.austinv11.peripheralsplusplus.init.ModBlocks;
import dan200.computercraft.api.turtle.ITurtleAccess;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

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
    public ITextComponent getDisplayName() {
        return new TextComponentString(
        		I18n.translateToLocal(ModBlocks.RESUPPLY_STATION.getUnlocalizedName()+".name"));
    }

    public synchronized boolean resupply(ITurtleAccess turtle, int toSlot, ResourceLocation id, int meta) {
		ItemStack currentStack = turtle.getInventory().getStackInSlot(toSlot);
		if (!hasCorrectIdAndMeta(currentStack, id, meta))
			return false;
		Item item = ForgeRegistries.ITEMS.getValue(id);
		if (item == null)
			return false;
		int amountToFill;
		if (currentStack.isEmpty())
			amountToFill = new ItemStack(item, 1, meta).getMaxStackSize();
		else
			amountToFill = currentStack.getMaxStackSize()-currentStack.getCount();
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
				stackToMerge = getStackInSlot(currentSlot).splitStack(MathHelper.clamp(amountToFill, 0,
						getStackInSlot(currentSlot).getCount()));
				amountToFill -= stackToMerge.getCount();
			} else {
				int toTake = MathHelper.clamp(amountToFill, 0, getStackInSlot(currentSlot).getCount());
				getStackInSlot(currentSlot).setCount(getStackInSlot(currentSlot).getCount() - toTake);
				stackToMerge.setCount(stackToMerge.getCount() - toTake);
				amountToFill -= toTake;
			}
			currentSlot++;
		}
		if (stackToMerge == null)
			return false;
		turtle.getInventory().setInventorySlotContents(toSlot, currentStack.isEmpty() ? stackToMerge :
				new ItemStack(currentStack.getItem(), currentStack.getCount() + stackToMerge.getCount(), meta));
		markDirty();
		turtle.getInventory().markDirty();
		return true;
	}
	
	private boolean hasCorrectIdAndMeta(ItemStack stack, ResourceLocation id, int meta) {
		if (stack == null)
			return true;
		ResourceLocation otherId;
		if (stack.getItem() instanceof ItemBlock) {
			Block block = Block.getBlockFromItem(stack.getItem());
			otherId = ForgeRegistries.BLOCKS.getKey(block);
		} else {
			Item item = stack.getItem();
			otherId = ForgeRegistries.ITEMS.getKey(item);
		}
		return otherId != null && otherId.equals(id) && stack.getItemDamage() == meta;
	}
}
