package com.austinv11.peripheralsplusplus.client.gui;

import com.austinv11.peripheralsplusplus.tile.container.ContainerPPP;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public abstract class GuiPPP extends GuiContainer {
	public int xSize, ySize;

	public abstract ResourceLocation getBackgroundImage();

	public GuiPPP(ContainerPPP container, int xSize, int ySize) {
		super(container);
		this.xSize = xSize;
		this.ySize = ySize;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(getBackgroundImage());
		drawTexturedModalRect((width - xSize) / 2, (height - ySize) / 2, 0, 0, xSize, ySize);
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
}
