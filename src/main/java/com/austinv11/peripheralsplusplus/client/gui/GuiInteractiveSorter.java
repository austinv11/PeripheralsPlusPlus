package com.austinv11.peripheralsplusplus.client.gui;

import com.austinv11.peripheralsplusplus.reference.Reference;
import com.austinv11.peripheralsplusplus.tiles.containers.ContainerInteractiveSorter;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class GuiInteractiveSorter extends GuiContainer {
	
	private int x, y, z;
	private EntityPlayer player;
	private World world;
	private int sizeX, sizeY;
	private ResourceLocation backgroundimage = new ResourceLocation(Reference.MOD_ID.toLowerCase() + ":" +
			"textures/gui/analyzer.png");
	
	public GuiInteractiveSorter(EntityPlayer player, World world, int x, int y, int z) {
		super(new ContainerInteractiveSorter(player, (IInventory)world.getTileEntity(new BlockPos(x, y, z)),
				176, 166));
		this.x = x;
		this.y = y;
		this.z = z;
		this.player = player;
		this.world = world;
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
		fontRenderer.drawString(I18n.translateToLocal(Reference.MOD_ID + ".inv.interactiveSorter"), x+42, y+3, 0x313131);
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
}
