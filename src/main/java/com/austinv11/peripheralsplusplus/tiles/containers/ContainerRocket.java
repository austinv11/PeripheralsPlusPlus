package com.austinv11.peripheralsplusplus.tiles.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerRocket extends Container {

	private EntityPlayer player;
	private IInventory inv;
	private final int slotX = 80;
	private final int slotY = 34;

	public ContainerRocket(EntityPlayer player, IInventory inv, int xSize, int ySize) {
		this.player = player;
		this.inv = inv;
		inv.openInventory();
		layout(xSize,ySize);
	}

	protected void layout(int xSize, int ySize) {
		addSlotToContainer(new Slot(inv,0,slotX,slotY));
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
	public ItemStack transferStackInSlot(EntityPlayer p_82846_1_, int p_82846_2_) {
		ItemStack var2 = null;
		Slot var3 = (Slot)this.inventorySlots.get(p_82846_2_);
		if (var3 != null && var3.getHasStack()) {
			ItemStack var4 = var3.getStack();
			var2 = var4.copy();
			if (p_82846_2_ < 1) {
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
	public boolean canInteractWith(EntityPlayer p_75145_1_) {
		return inv.isUseableByPlayer(p_75145_1_);
	}
}
