package miscperipherals.gui;

import miscperipherals.inventory.ContainerChargeStation;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiChargeStation extends GuiContainer {
	private ContainerChargeStation container;
	
	public GuiChargeStation(ContainerChargeStation par1Container) {
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
		mc.renderEngine.bindTexture("/mods/MiscPeripherals/textures/gui/chargestation.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		
		int top = (width - xSize) / 2;
		int left = (height - ySize) / 2;
		drawTexturedModalRect(top, left, 0, 0, xSize, ySize);
		
		int energy = (int)(14 * ((float)container.station.energy / (float)container.station.getMaxCharge()));
		if (energy > 0) drawTexturedModalRect(top + 80, (left + 36) - energy, 176, 12 - energy, 14, energy + 2);
		
		if (isPointInRegion(80, 36 - 14, 14, 14, var2, var3)) {
			drawCreativeTabHoveringText("Stored: "+(int)container.station.energy+" / "+container.station.getMaxCharge(), var2, var3);
		}
	}
}
