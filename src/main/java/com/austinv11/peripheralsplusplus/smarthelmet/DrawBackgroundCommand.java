package com.austinv11.peripheralsplusplus.smarthelmet;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.nbt.NBTTagCompound;

//Gui only
public class DrawBackgroundCommand extends HelmetCommand {

	public boolean defaultBackground = true;

	@SideOnly(Side.CLIENT)
	@Override
	public void call(Gui gui) {
		GuiScreen screen = (GuiScreen) gui;
		if (defaultBackground)
			screen.drawBackground(0);
		else
			screen.drawGradientRect(0, 0, screen.width, screen.height, -1072689136, -804253680);
	}

	@Override
	public String getCommandName() {
		return "DrawBackgroundCommand";
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		defaultBackground = tagCompound.getBoolean("defaultBackground");
	}

	@Override
	public void writeToNBT(NBTTagCompound tagCompound) {
		tagCompound.setBoolean("defaultBackground", defaultBackground);
	}
}
