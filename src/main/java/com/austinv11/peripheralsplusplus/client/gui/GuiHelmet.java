package com.austinv11.peripheralsplusplus.client.gui;

import com.austinv11.peripheralsplusplus.items.ItemSmartHelmet;
import com.austinv11.peripheralsplusplus.smarthelmet.HelmetCommand;
import com.austinv11.peripheralsplusplus.utils.NBTHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.opengl.GL11;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.UUID;

@SideOnly(Side.CLIENT)
public class GuiHelmet extends GuiScreen {

	public static HashMap<UUID,ArrayDeque<HelmetCommand>> renderStack = new HashMap<UUID,ArrayDeque<HelmetCommand>>();

	@Override
	public void drawScreen(int mouseX, int mouseY, float renderPartialTicks) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		if (Minecraft.getMinecraft().thePlayer.getCurrentArmor(3) != null && Minecraft.getMinecraft().thePlayer.getCurrentArmor(3).getItem() instanceof ItemSmartHelmet)
			if (NBTHelper.hasTag(Minecraft.getMinecraft().thePlayer.getCurrentArmor(3), "identifier")) {
				UUID uuid = UUID.fromString(NBTHelper.getString(Minecraft.getMinecraft().thePlayer.getCurrentArmor(3), "identifier"));
				if (renderStack.containsKey(uuid)) {
					ArrayDeque<HelmetCommand> commands = new ArrayDeque<HelmetCommand>(renderStack.get(uuid));
					while (!commands.isEmpty())
						commands.poll().call(this);
				}
			}
		super.drawScreen(mouseX, mouseY, renderPartialTicks);
	}

	@Override
	public boolean doesGuiPauseGame() {
		return true;
	}
}
