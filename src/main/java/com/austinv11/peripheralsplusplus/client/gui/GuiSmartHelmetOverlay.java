package com.austinv11.peripheralsplusplus.client.gui;

import com.austinv11.collectiveframework.minecraft.utils.NBTHelper;
import com.austinv11.peripheralsplusplus.items.ItemSmartHelmet;
import com.austinv11.peripheralsplusplus.smarthelmet.HelmetCommand;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.UUID;

public class GuiSmartHelmetOverlay extends Gui {

	public static HashMap<UUID,ArrayDeque<HelmetCommand>> renderStack = new HashMap<UUID,ArrayDeque<HelmetCommand>>();

	@SubscribeEvent
	public void renderOverlay(RenderGameOverlayEvent.Post event) {
		if (/*event.isCanceled() || */event.getType() != RenderGameOverlayEvent.ElementType.CROSSHAIRS)
			return;
		EntityPlayer player = Minecraft.getMinecraft().player;
		Iterable<ItemStack> armor = player.getArmorInventoryList();
		for (ItemStack armorPiece : armor) {
			if (armorPiece.getItem() instanceof ItemSmartHelmet &&
					NBTHelper.hasTag(armorPiece, "identifier")) {
				UUID uuid = UUID.fromString(NBTHelper.getString(armorPiece, "identifier"));
				if (renderStack.containsKey(uuid)) {
					ArrayDeque<HelmetCommand> commands = new ArrayDeque<HelmetCommand>(renderStack.get(uuid));
					while (!commands.isEmpty())
						commands.poll().call(this);
				}
				break;
			}
		}
	}
}
