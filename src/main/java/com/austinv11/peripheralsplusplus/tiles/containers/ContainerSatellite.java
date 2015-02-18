package com.austinv11.peripheralsplusplus.tiles.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerSatellite extends Container {
	
	private EntityPlayer player;
	private IInventory inv;
	private final int slotX = 80;
	private final int slotY = 18;

	public ContainerSatellite(EntityPlayer player, IInventory inv, int xSize, int ySize) {
		this.player = player;
		this.inv = inv;
		inv.openInventory();
		layout(xSize,ySize);
	}

	protected void layout(int xSize, int ySize) {
		//for (int invRow = 0; invRow < inv.amountOfRows(); invRow++) {
		//	for (int invCol = 0; invCol < inv.amountOfColumns(); invCol++) {
		//addSlotToContainer(inv, invCol+row*column, 12+invCol*18,8+invRow*18);
		//	}
		//}
		addSlotToContainer(new Slot(inv,0,slotX,slotY));
		addSlotToContainer(new Slot(inv, 1, slotX, (slotY+28)));
//		for (int i = 0; i < inventory.getSizeInventory(); i++) {
//			if (i == 0 || i == 1)
//				continue;
//			addSlotToContainer(new Slot(inv, i, slotX+3+(18*i), (int) (slotY+10+(18*Math.floor(i/9)))));
//		}
		int leftCol = (xSize - 162) / 2 + 1;
		for (int playerInvRow = 0; playerInvRow < 3; playerInvRow++) {
			for (int playerInvCol = 0; playerInvCol < 9; playerInvCol++) {
				addSlotToContainer(new Slot(player.inventory, playerInvCol + playerInvRow * 9 + 9, leftCol + playerInvCol * 18, ySize - (4 - playerInvRow) * 18 - 10));
			}
		}
		for (int hotbarSlot = 0; hotbarSlot < 9; hotbarSlot++) {
			addSlotToContainer(new Slot(player.inventory, hotbarSlot, leftCol + hotbarSlot * 18, ySize - 24));
		}
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotNum) {
		/*ItemStack itemstack = null;
		Slot slot = (Slot) inventorySlots.get(slotNum);
		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			if (slotNum < inventorySlots.size()) {
				if (!mergeItemStack(itemstack1, 0, inventorySlots.size(), false)) {
					return null;
				}
			} else if (!mergeItemStack(itemstack1, 0, 1, true)) {
				return null;
			}
			if (itemstack1.stackSize == 0) {
				slot.putStack(null);
			} else {
				slot.onSlotChanged();
			}
		}
		return itemstack;*/
		ItemStack var2 = null;
		Slot var3 = (Slot)this.inventorySlots.get(slotNum);

		if (var3 != null && var3.getHasStack()) {
			ItemStack var4 = var3.getStack();
			var2 = var4.copy();

			if (slotNum < 1) {
				if (!this.mergeItemStack(var4, 1, this.inventorySlots.size(), true)) {
					return null;
				}
			} else if (!this.mergeItemStack(var4, 0, 1, false)) return null;

			if (var4.stackSize == 0) var3.putStack((ItemStack)null);
			else var3.onSlotChanged();
		}
		return var2;
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return inv.isUseableByPlayer(player);
	}
}
