package com.austinv11.peripheralsplusplus.client.gui;

import com.austinv11.peripheralsplusplus.items.ItemSmartHelmet;
import com.austinv11.peripheralsplusplus.smarthelmet.ICommand;
import com.austinv11.peripheralsplusplus.utils.NBTHelper;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.UUID;

@SideOnly(Side.CLIENT)
public class GuiSmartHelmetOverlay extends Gui {

	public static HashMap<UUID,ArrayDeque<ICommand>> renderStack = new HashMap<UUID,ArrayDeque<ICommand>>();

	@SubscribeEvent
	public void renderOverlay(RenderGameOverlayEvent.Post event) {
		if (/*event.isCanceled() || */event.type != RenderGameOverlayEvent.ElementType.FOOD)
			return;
		if (Minecraft.getMinecraft().thePlayer.getCurrentArmor(3) != null && Minecraft.getMinecraft().thePlayer.getCurrentArmor(3).getItem() instanceof ItemSmartHelmet)
			if (NBTHelper.hasTag(Minecraft.getMinecraft().thePlayer.getCurrentArmor(3), "identifier")) {
				UUID uuid = UUID.fromString(NBTHelper.getString(Minecraft.getMinecraft().thePlayer.getCurrentArmor(3), "identifier"));
				if (renderStack.containsKey(uuid)) {
					ArrayDeque<ICommand> commands = new ArrayDeque<ICommand>(renderStack.get(uuid));
					while (!commands.isEmpty())
						commands.poll().call(this);
				}
			}
	}
}
