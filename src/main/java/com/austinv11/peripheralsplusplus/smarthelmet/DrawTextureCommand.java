package com.austinv11.peripheralsplusplus.smarthelmet;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.Gui;
import net.minecraft.nbt.NBTTagCompound;

public class DrawTextureCommand extends ICommand {

	@SideOnly(Side.CLIENT)
	@Override
	public void call(Gui gui) {

	}

	@Override
	public String getCommandName() {
		return "DrawTextureCommand";
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {

	}

	@Override
	public void writeToNBT(NBTTagCompound tagCompound) {

	}
}
