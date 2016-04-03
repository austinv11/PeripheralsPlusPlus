package com.austinv11.peripheralsplusplus.client.gui;

import com.austinv11.peripheralsplusplus.PeripheralsPlusPlus;
import com.austinv11.peripheralsplusplus.network.PacketPermCardChanged;
import com.austinv11.peripheralsplusplus.reference.Reference;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

public class GuiPermCard extends GuiScreen {
	private ItemStack permCard;
	private ResourceLocation backgroundImage = new ResourceLocation(Reference.MOD_ID.toLowerCase() + ":" + "textures/gui/guiPermCard.png");
	private int sizeX, sizeY;
	private boolean canGetStacks, canWithdraw, canDeposit;

	public GuiPermCard(ItemStack card) {
		super();
		this.permCard = card;
		this.canGetStacks = permCard.getTagCompound().getBoolean("getStacks");
		this.canWithdraw = permCard.getTagCompound().getBoolean("withdraw");
		this.canDeposit = permCard.getTagCompound().getBoolean("deposit");
		sizeX = 132;
		sizeY = 166;
	}

	@Override
	public void drawScreen(int par1, int par2, float par3) {
		this.drawDefaultBackground();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(backgroundImage);
		int x = (width - sizeX) / 2;
		int y = (height - sizeY) / 2;
		drawTexturedModalRect(x, y, 0, 0, sizeX, sizeY);
		// Have to draw the buttons before the text because mojang
		drawTexturedModalRect(x + 10, y + 40, this.canGetStacks ? 0 : 9, 166, 9, 9);
		drawTexturedModalRect(x + 10, y + 60, this.canWithdraw ? 0 : 9, 166, 9, 9);
		drawTexturedModalRect(x + 10, y + 80, this.canDeposit ? 0 : 9, 166, 9, 9);
		fontRendererObj.drawString(StatCollector.translateToLocal("peripheralsplusplus.inv.permCard"), x + 22, y + 5, 0x313131);
		fontRendererObj.drawString(StatCollector.translateToLocal("peripheralsplusplus.inv.permCard.perms"), x + 32, y + 20, 0x313131);
		fontRendererObj.drawString(StatCollector.translateToLocal("peripheralsplusplus.inv.permCard.get"), x + 25, y + 40, 0x313131);
		fontRendererObj.drawString(StatCollector.translateToLocal("peripheralsplusplus.inv.permCard.withdraw"), x + 25, y + 60, 0x313131);
		fontRendererObj.drawString(StatCollector.translateToLocal("peripheralsplusplus.inv.permCard.deposit"), x + 25, y + 80, 0x313131);
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int par3) {
		int x = (width - sizeX) / 2;
		int y = (height - sizeY) / 2;
		if (mouseX >= x + 10 && mouseX < x + 20) {
			if (mouseY >= y + 40 && mouseY < y + 50) {
				canGetStacks = !canGetStacks;
			}

			if (mouseY >= y + 60 && mouseY < y + 70) {
				canWithdraw = !canWithdraw;
			}

			if (mouseY >= y + 80 && mouseY < y + 90) {
				canDeposit = !canDeposit;
			}
		}
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	public void onGuiClosed() {
		PeripheralsPlusPlus.NETWORK.sendToServer(new PacketPermCardChanged(canGetStacks, canWithdraw, canDeposit));
	}
}
