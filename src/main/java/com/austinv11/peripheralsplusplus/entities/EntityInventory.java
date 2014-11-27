package com.austinv11.peripheralsplusplus.entities;

import com.austinv11.peripheralsplusplus.init.ModItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

public abstract class EntityInventory extends Entity implements IInventory {

	ItemStack[] items;

	public EntityInventory(World p_i1582_1_) {
		super(p_i1582_1_);
		items = new ItemStack[getSizeInventory()];
	}

	@Override
	protected void entityInit() {}

	@Override
	public void readEntityFromNBT(NBTTagCompound p_70037_1_) {
		NBTTagList nbttaglist = p_70037_1_.getTagList("Items", Constants.NBT.TAG_COMPOUND);
		items = new ItemStack[getSizeInventory()];
		for (int i = 0; i < nbttaglist.tagCount(); i++) {
			NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
			int j = nbttagcompound1.getByte("Slot") & 0xff;
			if (j >= 0 && j < items.length) {
				items[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
			}
		}
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound p_70014_1_) {
		NBTTagList nbttaglist = new NBTTagList();
		for (int i = 0; i < items.length; i++) {
			if (items[i] != null) {
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte) i);
				items[i].writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}
		p_70014_1_.setTag("Items", nbttaglist);
	}

	@Override
	public abstract int getSizeInventory();

	@Override
	public ItemStack getStackInSlot(int p_70301_1_) {
		return items != null ? null : items[p_70301_1_];
	}

	@Override
	public ItemStack decrStackSize(int p_70298_1_, int p_70298_2_) {
		if (items.length > p_70298_1_) {
			if (items[p_70298_1_] != null) {
				if (items[p_70298_1_].stackSize <= p_70298_2_) {
					ItemStack item = items[p_70298_1_];
					items[p_70298_1_] = null;
					markDirty();
					return item;
				}
				ItemStack item = items[p_70298_1_].splitStack(p_70298_2_);
				if (items[p_70298_1_].stackSize == 0) {
					items[p_70298_1_] = null;
				}
				markDirty();
				return item;
			}
		}
		return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int p_70304_1_) {
		if (items.length > p_70304_1_) {
			if (this.items[p_70304_1_] != null) {
				ItemStack item = this.items[p_70304_1_];
				this.items[p_70304_1_] = null;
				return item;
			}
		}
		return null;
	}

	@Override
	public void setInventorySlotContents(int p_70299_1_, ItemStack p_70299_2_) {
		if (items.length > p_70299_1_) {
			items[p_70299_1_] = p_70299_2_;
			if (p_70299_2_ != null && p_70299_2_.stackSize > getInventoryStackLimit()) {
				p_70299_2_.stackSize = getInventoryStackLimit();
			}
			markDirty();
		}
	}

	@Override
	public abstract String getInventoryName();

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	@Override
	public abstract int getInventoryStackLimit();

	@Override
	public void markDirty() {}

	@Override
	public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
		return true;
	}

	@Override
	public void openInventory() {}

	@Override
	public void closeInventory() {}

	@Override
	public abstract boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_);

	@Override
	public abstract boolean interactFirst(EntityPlayer p_130002_1_);

	@Override
	public void onKillEntity(EntityLivingBase p_70074_1_) {
		for (ItemStack i : items)
			this.entityDropItem(i, 0.0F);
		this.entityDropItem(new ItemStack(ModItems.rocket), 0.0F);
	}
}
