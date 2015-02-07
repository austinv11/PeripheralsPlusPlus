package com.austinv11.peripheralsplusplus.client.gui;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

import java.util.PriorityQueue;

@SideOnly(Side.CLIENT)
public class GuiSmartHelmetOverlay extends Gui {

	public static PriorityQueue<Object> renderStack = new PriorityQueue<Object>();
	public static int scaledHeight, scaledWidth;

	@SubscribeEvent
	public void renderOverlay(RenderGameOverlayEvent.Post event) {
		scaledHeight = event.resolution.getScaledHeight();
		scaledWidth = event.resolution.getScaledWidth();
	}
}
