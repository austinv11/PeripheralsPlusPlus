package com.austinv11.peripheralsplusplus.smarthelmet;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.Gui;
import net.minecraft.nbt.NBTTagCompound;

import java.awt.*;

public class DrawRectangleCommand extends HelmetCommand {

	public int x1,y1,x2,y2;
	public Color color, color2;

	@SideOnly(Side.CLIENT)
	@Override
	public void call(Gui gui) {
//		GL11.glColor4f(color.getRed()/255, color.getGreen()/255, color.getBlue()/255, color.getAlpha()/255);
		if (color2 == null)
			Gui.drawRect(x1, y1, x2, y2, color.getRGB());
		else
			gui.drawGradientRect(x1, y1, x2, y2, color.getRGB(), color2.getRGB());
	}

	@Override
	public String getCommandName() {
		return "DrawRectangleCommand";
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		x1 = tagCompound.getInteger("x1");
		x2 = tagCompound.getInteger("x2");
		y1 = tagCompound.getInteger("y1");
		y2 = tagCompound.getInteger("y2");
		color = new Color(tagCompound.getInteger("rgb"));
		if (tagCompound.hasKey("rgb2"))
			color2 = new Color(tagCompound.getInteger("rgb2"));
	}

	@Override
	public void writeToNBT(NBTTagCompound tagCompound) {
		tagCompound.setInteger("x1", x1);
		tagCompound.setInteger("x2", x2);
		tagCompound.setInteger("y1", y1);
		tagCompound.setInteger("y2", y2);
		tagCompound.setInteger("rgb", color.getRGB());
		if (color2 != null)
			tagCompound.setInteger("rgb2", color2.getRGB());
	}
}
