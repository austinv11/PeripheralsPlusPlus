package com.austinv11.peripheralsplusplus.tiles.containers;

import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;

public class ContainerResupplyStation extends ContainerChest {
	
	public ContainerResupplyStation(IInventory playerInventory, IInventory chestInventory) {
		super(playerInventory, chestInventory);
	}
}
