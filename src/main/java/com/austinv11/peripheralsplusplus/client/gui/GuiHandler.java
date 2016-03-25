package com.austinv11.peripheralsplusplus.client.gui;

import com.austinv11.peripheralsplusplus.reference.Reference;
import com.austinv11.peripheralsplusplus.tile.container.ContainerOreDict;
import com.austinv11.peripheralsplusplus.tile.container.ContainerPlayerInterface;
import com.austinv11.peripheralsplusplus.tile.container.ContainerSorter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == Reference.GUIs.ORE_DICT.ordinal()) {
			return new ContainerOreDict((IInventory) world.getTileEntity(new BlockPos(x, y, z)), player);
		} else if (ID == Reference.GUIs.SORTER.ordinal()) {
			return new ContainerSorter((IInventory) world.getTileEntity(new BlockPos(x, y, z)), player);
		} else if (ID == Reference.GUIs.PLAYER_INTERFACE.ordinal()) {
			return new ContainerPlayerInterface((IInventory) world.getTileEntity(new BlockPos(x, y, z)), player);
		} else if (ID == Reference.GUIs.PERM_CARD.ordinal()) {
			return null;
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == Reference.GUIs.ORE_DICT.ordinal()) {
			return new GuiOreDict(player, world, new BlockPos(x, y, z));
		} else if (ID == Reference.GUIs.SORTER.ordinal()) {
			return new GuiSorter(player, world, new BlockPos(x, y, z));
		} else if (ID == Reference.GUIs.PLAYER_INTERFACE.ordinal()) {
			return new GuiPlayerInterface(player, world, new BlockPos(x, y, z));
		} else if (ID == Reference.GUIs.PERM_CARD.ordinal()) {
			return new GuiPermCard(player.getCurrentEquippedItem());
		}
		return null;
	}
}
