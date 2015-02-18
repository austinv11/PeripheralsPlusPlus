package com.austinv11.peripheralsplusplus.smarthelmet;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.Gui;
import net.minecraft.nbt.NBTTagCompound;

public abstract class HelmetCommand {

	@SideOnly(Side.CLIENT)
	public abstract void call(Gui gui);

	public abstract String getCommandName();

	public abstract void readFromNBT(NBTTagCompound tagCompound);

	public abstract void writeToNBT(NBTTagCompound tagCompound);

	public static HelmetCommand getCommandFromName(String name) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
		return (HelmetCommand) Class.forName("com.austinv11.peripheralsplusplus.smarthelmet."+name).newInstance();
	}
}
