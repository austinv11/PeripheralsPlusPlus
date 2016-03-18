package com.austinv11.peripheralsplusplus.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.common.util.Constants;

// TODO This class is a mess. Half copied from CF half just... bleh. REWRITE IT
public abstract class TileEntityInventory extends TileEntity implements IInventory {
	public ItemStack[] items;
	public String invName = "null";

	public TileEntityInventory() {
		items = new ItemStack[getSize()];
	}

	public abstract int getSize();

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		NBTTagList nbttaglist = nbttagcompound.getTagList("Items", Constants.NBT.TAG_COMPOUND);
		items = new ItemStack[getSize()];
		for (int i = 0; i < nbttaglist.tagCount(); i++) {
			NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
			int j = nbttagcompound1.getByte("Slot") & 0xff;
			if (j >= 0 && j < items.length) {
				items[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
			}
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
		NBTTagList nbttaglist = new NBTTagList();
		for (int i = 0; i < items.length; i++) {
			if (items[i] != null) {
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte) i);
				items[i].writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}
		nbttagcompound.setTag("Items", nbttaglist);
	}

	@Override
	public int getSizeInventory() {
		return getSize();
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		if (items.length > slot)
			return items[slot];
		return null;
	}

	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		if (items.length > slot) {
			if (items[slot] != null) {
				if (items[slot].stackSize <= amount) {
					ItemStack item = items[slot];
					items[slot] = null;
					markDirty();
					return item;
				}
				ItemStack item = items[slot].splitStack(amount);
				if (items[slot].stackSize == 0) {
					items[slot] = null;
				}
				markDirty();
				return item;
			}
		}
		return null;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		if (items.length > slot) {
			items[slot] = stack;
			if (stack != null && stack.stackSize > getInventoryStackLimit()) {
				stack.stackSize = getInventoryStackLimit();
			}
			markDirty();
		}
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		if (worldObj == null) {
			return true;
		}
		if (worldObj.getTileEntity(pos) != this) {
			return false;
		}
		return player.getDistanceSq((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D) <= 64D;
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return true;
	}

	@Override
	public void markDirty() {
		for (int i = 0; i < items.length; i++)
			if (items[i] != null && items[i].stackSize < 1)
				items[i] = null;
		super.markDirty();
	}

	@Override
	public int getFieldCount() {
		return 0;
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		return items[index] = null;
	}

	@Override
	public IChatComponent getDisplayName() {
		return null;
	}

	@Override
	public String getName() {
		return invName;
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public void clear() {

	}

	@Override
	public void setField(int id, int value) {

	}

	@Override
	public void openInventory(EntityPlayer player) {

	}

	@Override
	public void closeInventory(EntityPlayer player) {

	}

	@Override
	public int getField(int id) {
		return 0;
	}
}
