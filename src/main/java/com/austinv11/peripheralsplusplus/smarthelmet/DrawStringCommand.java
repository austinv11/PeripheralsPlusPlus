package com.austinv11.peripheralsplusplus.smarthelmet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class DrawStringCommand extends HelmetCommand {

	public String message;
	public int x,y,color;
	public boolean shadow;

	@SideOnly(Side.CLIENT)
	@Override
	public void call(Gui gui) {
		Minecraft.getMinecraft().fontRenderer.drawString(message, x, y, color, shadow);
	}

	@Override
	public String getCommandName() {
		return "DrawStringCommand";
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		message = tagCompound.getString("message");
		x = tagCompound.getInteger("x");
		y = tagCompound.getInteger("y");
		color = tagCompound.getInteger("color");
		shadow = tagCompound.getBoolean("shadow");
	}

	@Override
	public void writeToNBT(NBTTagCompound tagCompound) {
		tagCompound.setString("message", message);
		tagCompound.setInteger("x", x);
		tagCompound.setInteger("y", y);
		tagCompound.setInteger("color", color);
		tagCompound.setBoolean("shadow", shadow);
	}
}
