package com.austinv11.peripheralsplusplus.client.gui;

import com.austinv11.peripheralsplusplus.reference.Reference;
import com.austinv11.peripheralsplusplus.tile.container.ContainerPPP;
import net.minecraft.util.ResourceLocation;

public class GuiSingleSlot extends GuiPPP {
	private String name;

	public GuiSingleSlot(ContainerPPP container, String name) {
		super(container, 176, 166);
		this.name = name;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		this.fontRendererObj.drawString(name, this.xSize / 2 - this.fontRendererObj.getStringWidth(name) / 2, 6, 4210752);
		this.fontRendererObj.drawString("Inventory", 8, this.ySize - 96 + 2, 4210752);
	}

	@Override
	public ResourceLocation getBackgroundImage() {
		return new ResourceLocation(Reference.MOD_ID.toLowerCase() + ":textures/gui/guiSingleSlot.png");
	}
}
