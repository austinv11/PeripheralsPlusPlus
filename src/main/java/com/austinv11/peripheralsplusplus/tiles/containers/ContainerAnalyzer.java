package com.austinv11.peripheralsplusplus.tiles.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class ContainerAnalyzer extends Container {

	private EntityPlayer player;
	private IInventory inv;
	private final int slotX = 12;
	private final int slotY = 8;

	public ContainerAnalyzer(EntityPlayer player, IInventory inv, int xSize, int ySize) {
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
		addSlotToContainer(new Slot(inv,1,slotX,slotY));
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
	public boolean canInteractWith(EntityPlayer p_75145_1_) {
		return inv.isUseableByPlayer(p_75145_1_);
	}
}
