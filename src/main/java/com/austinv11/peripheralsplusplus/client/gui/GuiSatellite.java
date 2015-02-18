package com.austinv11.peripheralsplusplus.client.gui;

import com.austinv11.peripheralsplusplus.items.ItemSatellite;
import com.austinv11.peripheralsplusplus.reference.Reference;
import com.austinv11.peripheralsplusplus.tiles.containers.ContainerSatellite;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class GuiSatellite extends GuiContainer {
	
	private int x, y, z;
	private EntityPlayer player;
	private World world;
	private int sizeX, sizeY;
	private static ResourceLocation backgroundimage = new ResourceLocation(Reference.MOD_ID.toLowerCase() + ":" + "textures/gui/guiSatellite.png");
	private static ResourceLocation slot = new ResourceLocation(Reference.MOD_ID.toLowerCase() + ":" + "textures/gui/slot.png");
	private ItemSatellite.SatelliteInventory inventory;

	public GuiSatellite(EntityPlayer player, World world, int x, int y, int z) {
		super(new ContainerSatellite(player, ((ItemSatellite) player.getCurrentEquippedItem().getItem()).getInventoryFromStack(player), 176, 166));
		inventory = ((ItemSatellite) player.getCurrentEquippedItem().getItem()).getInventoryFromStack(player);
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
//		this.mc.getTextureManager().bindTexture(slot);
//		if (inventory.getSizeInventory() > 1)
//			if (inventory.getStackInSlot(1) != null)
//				for (int i = 0; i < inventory.getSizeInventory(); i++) {
//					if (i == 0 || i == 1)
//						continue;
//					else if (i < 10)
//						drawTexturedModalRect(x+3+(18*i), y+10, 0, 0, 30, 30);
//					else
//						drawTexturedModalRect(x+3+(18*i), (int) (y+10+(18*Math.floor(i/9))), 0, 0, 30, 30);
//				}
		fontRendererObj.drawString(StatCollector.translateToLocal("peripheralsplusplus.inv.satellite"), x+67, y+3, 0x313131);
		fontRendererObj.drawString(StatCollector.translateToLocal("peripheralsplusplus.inv.satellite.0"), x+5, y+22, 0x313131);
		fontRendererObj.drawString(StatCollector.translateToLocal("peripheralsplusplus.inv.satellite.1"), x+5, y+52, 0x313131);
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
}
