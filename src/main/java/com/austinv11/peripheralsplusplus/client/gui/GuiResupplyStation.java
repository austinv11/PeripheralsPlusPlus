package com.austinv11.peripheralsplusplus.client.gui;

import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.IInventory;

public class GuiResupplyStation extends GuiChest {
	
	public GuiResupplyStation(IInventory playerInventory, IInventory chestInventory) {
		super(playerInventory, chestInventory);
	}
}
