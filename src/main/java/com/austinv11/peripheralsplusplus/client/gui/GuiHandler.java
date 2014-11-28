package com.austinv11.peripheralsplusplus.client.gui;

import com.austinv11.peripheralsplusplus.entities.EntityRocket;
import com.austinv11.peripheralsplusplus.reference.Reference;
import com.austinv11.peripheralsplusplus.tiles.containers.ContainerAnalyzer;
import com.austinv11.peripheralsplusplus.tiles.containers.ContainerRocket;
import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.world.World;

public class GuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		if (id == Reference.GUIs.ANALYZER.ordinal()) {
			return new ContainerAnalyzer(player, (IInventory) world.getTileEntity(x, y, z), 176, 166);
		} else if (id == Reference.GUIs.ROCKET.ordinal()) {
			return new ContainerRocket(player, (EntityRocket) world.getEntityByID(x/*In this case, the entity id*/), 176, 166);
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		if (id == Reference.GUIs.ANALYZER.ordinal()) {
			return new GuiAnalyzer(player, world, x, y, z);
		} else if (id == Reference.GUIs.ROCKET.ordinal()) {
			return new GuiRocket(player, world, x/*In this case, the entity id*/, y, z);
		}
		return null;
	}
}
