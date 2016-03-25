package com.austinv11.peripheralsplusplus.client.gui;

import com.austinv11.peripheralsplusplus.reference.Reference;
import com.austinv11.peripheralsplusplus.tile.container.ContainerPlayerInterface;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class GuiPlayerInterface extends GuiPPP {

	public GuiPlayerInterface(EntityPlayer player, World world, BlockPos pos) {
		super(new ContainerPlayerInterface((IInventory) world.getTileEntity(pos), player), 176, 166);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		this.fontRendererObj.drawString("Player Interface", this.xSize / 2 - this.fontRendererObj.getStringWidth("Player Interface") / 2, 6, 4210752);
		this.fontRendererObj.drawString("Inventory", 8, this.ySize - 96 + 2, 4210752);
	}

	@Override
	public ResourceLocation getBackgroundImage() {
		return new ResourceLocation(Reference.MOD_ID.toLowerCase() + ":textures/gui/guiPlayerInterface.png");
	}
}
