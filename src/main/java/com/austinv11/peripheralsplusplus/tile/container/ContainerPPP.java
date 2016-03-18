package com.austinv11.peripheralsplusplus.tile.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public abstract class ContainerPPP extends Container {

	IInventory inventory;
	EntityPlayer player;

	public ContainerPPP (IInventory inventory, EntityPlayer player) {
		this.inventory = inventory;
		this.player = player;
		inventory.openInventory(player);
	}

	public abstract void layout();

	public void addPlayerInvSlots() {
		for (int playerInvRow = 0; playerInvRow < 3; playerInvRow++) {
			for (int playerInvCol = 0; playerInvCol < 9; playerInvCol++) {
				addSlotToContainer(new Slot(player.inventory, playerInvCol + playerInvRow * 9 + 9, 8 + playerInvCol * 18, 166 - (4 - playerInvRow) * 18 - 10));
			}
		}
		for (int hotbarSlot = 0; hotbarSlot < 9; hotbarSlot++) {
			addSlotToContainer(new Slot(player.inventory, hotbarSlot, 8 + hotbarSlot * 18, 142));
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return inventory.isUseableByPlayer(playerIn);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		// Create a new itemstack. This is the stack that will be manipulated and returned.
		ItemStack itemstack = null;
		// Get the slot that was just shift clicked. This is the slot that the itemstack will be transferring from.
		Slot slot = this.inventorySlots.get(index);

		// Check that the slot exists and has an itemstack in it
		if (slot != null && slot.getHasStack())
		{
			// Get the stack in the slot that was shift-clicked. This stack will act as a base for our return itemstack.
			ItemStack itemstack1 = slot.getStack();

			// Copy that stack to our return itemstack.
			itemstack = itemstack1.copy();

			if (index < inventory.getSizeInventory()) // If the slot being transferred from is in the force belt inventory.
			{
				if (!this.mergeItemStack(itemstack1, inventory.getSizeInventory(), 36 + inventory.getSizeInventory(), true)) // Tries to merge itemstack with any in the player's main inv. (slots 8-44) 36 is the player's main inv size (excludes armor slots)
				{
					return null;
				}
			}
			else if (!this.mergeItemStack(itemstack1, 0, inventory.getSizeInventory(), false)) // If the itemstack can't merge with any stacks in the container, return.
			{                                                       // Implies that the stack being transferred is from a slot in the player's main inv
				return null;
			}

			// After the merging has completed, if the itemstack has a size of 0, replace it with an empty slot.
			if (itemstack1.stackSize == 0)
			{
				slot.putStack(null);
			}
			else
			{
				// Inform the game that the slot was changed.
				slot.onSlotChanged();
			}
		}

		// Return the new itemstack. This is the itemstack that will be placed in the slot that is being transferred to.
		return itemstack;
	}
}
