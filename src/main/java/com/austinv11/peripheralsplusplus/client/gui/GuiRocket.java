package com.austinv11.peripheralsplusplus.client.gui;

import com.austinv11.peripheralsplusplus.entities.EntityRocket;
import com.austinv11.peripheralsplusplus.reference.Reference;
import com.austinv11.peripheralsplusplus.tiles.containers.ContainerRocket;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class GuiRocket extends GuiContainer {

	private int sizeX, sizeY;
	private ResourceLocation backgroundimage = new ResourceLocation(Reference.MOD_ID.toLowerCase() + ":" + "textures/gui/guiRocket.png");

	public GuiRocket(EntityPlayer player, World world, int x, int y, int z) {
		super(new ContainerRocket(player, (EntityRocket)world.getEntityByID(x), 176, 166));
		sizeX = 176;
		sizeY = 166;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(backgroundimage);
		int x = (width - sizeX) / 2;
		int y = (height - sizeY) / 2;
		drawTexturedModalRect(x, y, 0, 0, sizeX, sizeY);
		fontRendererObj.drawString(StatCollector.translateToLocal("peripheralsplusplus.inv.rocket.0"), x+62, y+3, 0x313131);
		fontRendererObj.drawString(StatCollector.translateToLocal("peripheralsplusplus.inv.rocket.1"), x+85, y+23, 0x313131);
		fontRendererObj.drawString(StatCollector.translateToLocal("peripheralsplusplus.inv.rocket.2"), x+105, y+23, 0x313131);
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
}
