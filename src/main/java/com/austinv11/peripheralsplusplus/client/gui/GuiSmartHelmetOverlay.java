package com.austinv11.peripheralsplusplus.client.gui;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

import java.util.PriorityQueue;

public class GuiSmartHelmetOverlay extends Gui {

	public static PriorityQueue<Object> renderStack = new PriorityQueue<Object>();

	@SubscribeEvent
	public void renderOverlay(RenderGameOverlayEvent.Post event) {

	}
}
