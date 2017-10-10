package com.austinv11.peripheralsplusplus.smarthelmet;

import com.austinv11.peripheralsplusplus.utils.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;
import java.util.HashMap;
import java.util.Stack;

public class MessageCommand extends HelmetCommand {

	public static Stack<String> messageStack = new Stack<String>();
	public static HashMap<String, Stack<String>> messageStacks = new HashMap<String,Stack<String>>();
	public String player;

	@SideOnly(Side.CLIENT)
	@Override
	public void call(Gui gui) {
		Stack<String> messages = (Stack<String>) messageStack.clone();
		int y = 1;
		while (!messages.empty()) {
			gui.drawString(Minecraft.getMinecraft().fontRenderer, messages.pop(), 1, y, Color.white.getRGB());
			y += 8;
		}
	}

	@Override
	public String getCommandName() {
		return "MessageCommand";
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		messageStack.clear();
		String[] array = Util.stringToArray(tagCompound.getString("stack"));
		for (String s : array)
			messageStack.push(s);
	}

	@Override
	public void writeToNBT(NBTTagCompound tagCompound) {
		Stack<String> messages = messageStacks.get(player);
		tagCompound.setString("stack", messages.toString());
	}
}
