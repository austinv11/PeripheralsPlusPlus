package com.austinv11.peripheralsplusplus.client.gui;

import com.austinv11.peripheralsplusplus.tile.container.ContainerOreDict;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class GuiOreDict extends GuiSingleSlot {
	public GuiOreDict(EntityPlayer player, World world, BlockPos pos) {
		super(new ContainerOreDict((IInventory) world.getTileEntity(pos), player), "Ore Dictionary");
	}
}
