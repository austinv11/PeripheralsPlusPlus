package com.austinv11.peripheralsplusplus.client.gui;

import com.austinv11.peripheralsplusplus.reference.Reference;
import com.austinv11.peripheralsplusplus.tiles.containers.ContainerAnalyzer;
import com.austinv11.peripheralsplusplus.tiles.containers.ContainerInteractiveSorter;
import com.austinv11.peripheralsplusplus.tiles.containers.ContainerPlayerInterface;
import com.austinv11.peripheralsplusplus.tiles.containers.ContainerResupplyStation;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.world.World;

public class GuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		if (id == Reference.GUIs.ANALYZER.ordinal()) {
			return new ContainerAnalyzer(player, (IInventory) world.getTileEntity(x, y, z), 176, 166);
		} else if (id == Reference.GUIs.INTERACTIVE_SORTER.ordinal()) {
			return new ContainerInteractiveSorter(player, (IInventory) world.getTileEntity(x, y, z), 176, 166);
        } else if (id == Reference.GUIs.PLAYER_INTERFACE.ordinal()) {
            return new ContainerPlayerInterface(player, (IInventory) world.getTileEntity(x, y, z), 176, 133);
        } else if (id == Reference.GUIs.PERMCARD.ordinal()) {
            return null;
        } else if (id == Reference.GUIs.RESUPPLY_STATION.ordinal()) {
			return new ContainerResupplyStation(player.inventory, (IInventory)world.getTileEntity(x, y, z));
		}
        return null;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		if (id == Reference.GUIs.ANALYZER.ordinal()) {
			return new GuiAnalyzer(player, world, x, y, z);
		} else if (id == Reference.GUIs.HELMET.ordinal()) {
			return new GuiHelmet();
		} else if (id == Reference.GUIs.INTERACTIVE_SORTER.ordinal()) {
			return new GuiInteractiveSorter(player, world, x, y, z);
        } else if (id == Reference.GUIs.PLAYER_INTERFACE.ordinal()) {
            return new GuiPlayerInterface(player, x, y, z);
        } else if (id == Reference.GUIs.PERMCARD.ordinal()) {
            return new GuiPermCard(player.getCurrentEquippedItem());
        } else if (id == Reference.GUIs.RESUPPLY_STATION.ordinal()) {
			return new GuiResupplyStation(player.inventory, (IInventory)world.getTileEntity(x, y, z));
		}
        return null;
	}
}
