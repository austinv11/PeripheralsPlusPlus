package com.austinv11.peripheralsplusplus.smarthelmet;

import com.austinv11.peripheralsplusplus.client.gui.GuiHelmet;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.nbt.NBTTagCompound;

//Gui only
public class AddTextFieldCommand extends HelmetCommand {

	public int id, x, y, width, height;
	public String message = "@NULL@";

	@SideOnly(Side.CLIENT)
	@Override
	public void call(Gui gui) {
		GuiHelmet screen = (GuiHelmet)gui;
		if (message.equals("@NULL@")) {
			screen.addTextField(id, new GuiTextField(Minecraft.getMinecraft().fontRenderer, x, y, width, height));
		} else {
			GuiTextField field = new GuiTextField(Minecraft.getMinecraft().fontRenderer, x, y, width, height);
			field.setText(message);
			screen.addTextField(id, field);
		}
	}

	@Override
	public String getCommandName() {
		return "AddTextFieldCommand";
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
