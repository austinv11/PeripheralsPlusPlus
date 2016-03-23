package com.austinv11.peripheralsplusplus.client.gui;

import com.austinv11.peripheralsplusplus.tile.container.ContainerSorter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class GuiSorter extends GuiSingleSlot {
	public GuiSorter(EntityPlayer player, World world, BlockPos pos) {
		super(new ContainerSorter((IInventory) world.getTileEntity(pos), player), "Interactive Sorter");
	}
}
