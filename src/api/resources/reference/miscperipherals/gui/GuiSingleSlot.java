package miscperipherals.gui;

import miscperipherals.inventory.ContainerSingleSlot;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

public class GuiSingleSlot extends GuiContainer {
	private ContainerSingleSlot container;
	
	public GuiSingleSlot(ContainerSingleSlot par1Container) {
		super(par1Container);
		this.container = par1Container;
	}
	
	@Override
	public void drawGuiContainerForegroundLayer(int a, int b) {
		super.drawGuiContainerForegroundLayer(a, b);
		String name = container.inventory.getInvName();
		fontRenderer.drawString(name, (xSize - fontRenderer.getStringWidth(name)) / 2, 6, 0x404040);
		fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, (ySize - 96) + 2, 0x404040);
	}

	@Override
	public void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
		mc.renderEngine.bindTexture("/mods/MiscPeripherals/textures/gui/singleslot.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		
		int top = (width - xSize) / 2;
		int left = (height - ySize) / 2;
		drawTexturedModalRect(top, left, 0, 0, xSize, ySize);
	}
}
