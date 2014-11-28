package com.austinv11.peripheralsplusplus.client.gui;

import com.austinv11.peripheralsplusplus.entities.EntityRocket;
import com.austinv11.peripheralsplusplus.reference.Reference;
import com.austinv11.peripheralsplusplus.tiles.containers.ContainerAnalyzer;
import com.austinv11.peripheralsplusplus.tiles.containers.ContainerRocket;
import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import java.util.List;

public class GuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		if (id == Reference.GUIs.ANALYZER.ordinal()) {
			return new ContainerAnalyzer(player, (IInventory) world.getTileEntity(x, y, z), 176, 166);
		} else if (id == Reference.GUIs.ROCKET.ordinal()) {
			return new ContainerRocket(player, getRocketEntity(player, world, x, y, z), 176, 166);
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		if (id == Reference.GUIs.ANALYZER.ordinal()) {
			return new GuiAnalyzer(player, world, x, y, z);
		} else if (id == Reference.GUIs.ROCKET.ordinal()) {
			return new GuiRocket(player, world, x, y, z);
		}
		return null;
	}

	public IInventory getRocketEntity(EntityPlayer player, World world, int x, int y, int z) {

	}
}
