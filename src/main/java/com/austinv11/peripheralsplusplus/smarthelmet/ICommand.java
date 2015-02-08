package com.austinv11.peripheralsplusplus.smarthelmet;

import net.minecraft.client.gui.Gui;
import net.minecraft.nbt.NBTTagCompound;

public abstract class ICommand {

	public abstract void call(Gui gui);

	public abstract String getCommandName();

	public abstract void readFromNBT(NBTTagCompound tagCompound);

	public abstract void writeToNBT(NBTTagCompound tagCompound);

	public static ICommand getCommandFromName(String name) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
		return (ICommand) Class.forName("com.austinv11.peripheralsplusplus.smarthelmet."+name).newInstance();
	}
}
