package com.austinv11.peripheralsplusplus.tile.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class ContainerPlayerInterface extends ContainerPPP {

	public ContainerPlayerInterface(IInventory inventory, EntityPlayer player) {
		super(inventory, player);
	}

	@Override
	public void layout() {
		for (int i = 0; i < 9; i++) {
			addSlotToContainer(new Slot(this.inventory, i, 8 + i * 18, 34));
		}
		addPlayerInvSlots();
	}
}
