package miscperipherals.tile;

import miscperipherals.util.Util;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileInventory extends Tile implements ISidedInventory {
	ItemStack[] inventory;
	
	public TileInventory(int slots) {
		inventory = new ItemStack[slots];
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		
		Util.readInventoryFromNBT(this, tag.getTagList("Items"));
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		
		tag.setTag("Items", Util.writeInventoryToNBT(this));
	}
	
	@Override
	public int getSizeInventory() {
		return inventory.length;
	}

	@Override
	public ItemStack getStackInSlot(int var1) {
		return inventory[var1];
	}

	@Override
	public ItemStack decrStackSize(int var1, int var2) {
		if (inventory[var1] != null) {
			ItemStack stack;
			
			if (inventory[var1].stackSize <= var2) {
				stack = inventory[var1];
				inventory[var1] = null;
			} else {
				stack = inventory[var1].splitStack(var2);
				
				if (inventory[var1].stackSize == 0) inventory[var1] = null;
			}
			
			onInventoryChanged();
			return stack;
		} else return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int var1) {
		if (inventory[var1] != null) {
			ItemStack stack = inventory[var1];
			this.inventory[var1] = null;
			return stack;
		} else return null;
	}

	@Override
	public void setInventorySlotContents(int var1, ItemStack var2) {
		inventory[var1] = var2;
		
		if (var2 != null && var2.stackSize > getInventoryStackLimit()) {
			var2.stackSize = getInventoryStackLimit();
		}
		
		onInventoryChanged();
	}

	@Override
	public String getInvName() {
		return displayName != null ? displayName : getInventoryName();
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer var1) {
		return worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) == this && var1.getDistance(xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D) <= 64D;
	}

	@Override
	public void openChest() {
		
	}

	@Override
	public void closeChest() {
		
	}
	
	@Override
	public int[] getAccessibleSlotsFromSide(int var1) {
		return Util.makeSlotArray(this);
	}

	@Override
	public boolean canInsertItem(int i, ItemStack itemstack, int j) {
		return isStackValidForSlot(i, itemstack);
	}

	@Override
	public boolean canExtractItem(int i, ItemStack itemstack, int j) {
		return true;
	}

	@Override
	public boolean isInvNameLocalized() {
		return false;
	}

	@Override
	public boolean isStackValidForSlot(int i, ItemStack itemstack) {
		return true;
	}
	
	@Override
	public int getComparator(int side) {
		return Container.calcRedstoneFromInventory(this);
	}
	
	public void addItemToInventory(ItemStack item, boolean adjacent) {
		if (adjacent) {
			// TODO
		}
		
		if (item.isItemDamaged()) {
			int slot = getFirstEmptyStack();
			if (slot >= 0) {
				ItemStack stack = ItemStack.copyItemStack(item);
				setInventorySlotContents(slot, stack);
				stack.animationsToGo = 5;
				item.stackSize = 0;
			}
		} else {
			int size;
			
			do {
				size = item.stackSize;
				item.stackSize = storePartialItemStack(item);
			} while (item.stackSize > 0 && item.stackSize < size);
		}
	}
	
	private int getFirstEmptyStack() {
		for (int i = 0; i < getSizeInventory(); i++) {
			if (getStackInSlot(i) == null) return i;
		}
		
		return -1;
	}
	
	private int storePartialItemStack(ItemStack item) {
		int id = item.itemID;
		int size = item.stackSize;
		
		if (item.getMaxStackSize() == 1) {
			int slot = getFirstEmptyStack();
			
			if (slot < 0) return size;
			else {
				if (getStackInSlot(slot) == null) setInventorySlotContents(slot, ItemStack.copyItemStack(item));
				
				return 0;
			}
		} else {
			int slot = storeItemStack(item);
			
			if (slot < 0) slot = getFirstEmptyStack();
			if (slot < 0) return size;
			else {
				ItemStack localstack = getStackInSlot(slot);
				
				if (localstack == null) {
					ItemStack stack = new ItemStack(id, 0, item.getItemDamage());
					if (item.hasTagCompound()) stack.setTagCompound((NBTTagCompound)stack.getTagCompound().copy());
					setInventorySlotContents(slot, stack);
				}
				
				int newSize = size;
				
				if (newSize > localstack.getMaxStackSize() - localstack.stackSize) newSize = localstack.getMaxStackSize() - localstack.stackSize;
				if (newSize > getInventoryStackLimit() - localstack.stackSize) newSize = getInventoryStackLimit() - localstack.stackSize;
				
				if (newSize == 0) return size;
				else {
					size -= newSize;
					localstack.stackSize += newSize;
					localstack.animationsToGo = 5;
					setInventorySlotContents(slot, localstack);
					return size;
				}
			}
		}
	}
	
	private int storeItemStack(ItemStack item) {
		for (int i = 0; i < getSizeInventory(); i++) {
			ItemStack stack = getStackInSlot(i);
			if (stack != null && stack.itemID == item.itemID && stack.isStackable() && stack.stackSize < stack.getMaxStackSize() && stack.stackSize < getInventoryStackLimit() && (!stack.getHasSubtypes() || stack.getItemDamage() == item.getItemDamage()) && ItemStack.areItemStackTagsEqual(stack, item)) {
				return i;
			}
		}
		
		return -1;
	}
}
