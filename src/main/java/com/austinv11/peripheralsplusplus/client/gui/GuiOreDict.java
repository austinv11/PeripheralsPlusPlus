package com.austinv11.peripheralsplusplus.client.gui;

import com.austinv11.peripheralsplusplus.reference.Reference;
import com.austinv11.peripheralsplusplus.tile.container.ContainerOreDict;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class GuiOreDict extends GuiContainer {
	private ResourceLocation inventoryImage = new ResourceLocation(Reference.MOD_ID.toLowerCase() + ":" + "textures/gui/guiOreDict.png");

	public GuiOreDict(EntityPlayer player, World world, BlockPos pos) {
		super(new ContainerOreDict((IInventory) world.getTileEntity(pos), player));
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(inventoryImage);
		drawTexturedModalRect((width - 176) / 2, (height - 166) / 2, 0, 0, 176, 166);
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
}
