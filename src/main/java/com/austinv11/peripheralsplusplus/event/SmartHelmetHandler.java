package com.austinv11.peripheralsplusplus.event;

import com.austinv11.peripheralsplusplus.PeripheralsPlusPlus;
import com.austinv11.peripheralsplusplus.items.ItemSmartHelmet;
import com.austinv11.peripheralsplusplus.network.InputEventPacket;
import com.austinv11.peripheralsplusplus.utils.NBTHelper;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.util.UUID;

@SideOnly(Side.CLIENT)
public class SmartHelmetHandler {

	@SubscribeEvent
	public void onClick(InputEvent.MouseInputEvent event) {
		if (checkSmartHelmetStatus()) {
			PeripheralsPlusPlus.NETWORK.sendToServer(new InputEventPacket(UUID.fromString(Minecraft.getMinecraft().thePlayer.getCurrentArmor(3).getTagCompound().getString("identifier")), Mouse.getEventButton(), Mouse.getEventButtonState(), "mouseInput", Minecraft.getMinecraft().thePlayer.getDisplayName()));
		}
	}

	@SubscribeEvent
	public void onKeyPressed(InputEvent.KeyInputEvent event) {
		if (checkSmartHelmetStatus()) {
			PeripheralsPlusPlus.NETWORK.sendToServer(new InputEventPacket(UUID.fromString(Minecraft.getMinecraft().thePlayer.getCurrentArmor(3).getTagCompound().getString("identifier")), Keyboard.getEventKey(), Keyboard.getEventKeyState(), "keyInput", Minecraft.getMinecraft().thePlayer.getDisplayName()));
		}
	}

	private boolean checkSmartHelmetStatus() {
		ItemStack helmet = Minecraft.getMinecraft().thePlayer.getCurrentArmor(3);
		if (helmet != null)
			if (helmet.getItem() instanceof ItemSmartHelmet)
				if (NBTHelper.hasTag(helmet, "identifier"))
					return true;
		return false;
	}
}
