package com.austinv11.peripheralsplusplus.smarthelmet;

import com.austinv11.peripheralsplusplus.client.gui.GuiHelmet;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

//Gui only
public class AddButtonCommand extends HelmetCommand {

	public int id, x, y, width, height;
	public String message;

	@SideOnly(Side.CLIENT)
	@Override
	public void call(Gui gui) {
		GuiHelmet screen = (GuiHelmet) gui;
		screen.addButton(new GuiButton(id, x, y, width, height, message));
	}

	@Override
	public String getCommandName() {
		return "AddButtonCommand";
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		id = tagCompound.getInteger("id");
		x = tagCompound.getInteger("x");
		y = tagCompound.getInteger("y");
		width = tagCompound.getInteger("width");
		height = tagCompound.getInteger("height");
		message = tagCompound.getString("message");
	}

	@Override
	public void writeToNBT(NBTTagCompound tagCompound) {
		tagCompound.setInteger("id", id);
		tagCompound.setInteger("x", x);
		tagCompound.setInteger("y", y);
		tagCompound.setInteger("width", width);
		tagCompound.setInteger("height", height);
		tagCompound.setString("message", message);
	}
}
