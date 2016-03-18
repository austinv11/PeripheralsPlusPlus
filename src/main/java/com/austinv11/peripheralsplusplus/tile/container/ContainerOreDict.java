package com.austinv11.peripheralsplusplus.tile.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerOreDict extends ContainerPPP {

	public ContainerOreDict(IInventory inventory, EntityPlayer player) {
		super(inventory, player);
		this.layout();
	}

	@Override
	public void layout() {
		addSlotToContainer(new Slot(this.inventory, 0, 80, 34));
		addPlayerInvSlots();
	}
}
