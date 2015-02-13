package com.austinv11.peripheralsplusplus.smarthelmet;

import net.minecraft.client.gui.Gui;
import net.minecraft.nbt.NBTTagCompound;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class DrawRectangleCommand extends ICommand {

	public int x1,y1,x2,y2;
	public Color color;

	@Override
	public void call(Gui gui) {
		GL11.glColor4f(color.getRed()/255, color.getGreen()/255, color.getBlue()/255, color.getAlpha()/255);
		GL11.glRecti(x1, y1, x2, y2);
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
	}

	@Override
	public void writeToNBT(NBTTagCompound tagCompound) {
		tagCompound.setInteger("x1", x1);
		tagCompound.setInteger("x2", x2);
		tagCompound.setInteger("y1", y1);
		tagCompound.setInteger("y2", y2);
		tagCompound.setInteger("rgb", color.getRGB());
	}
}
