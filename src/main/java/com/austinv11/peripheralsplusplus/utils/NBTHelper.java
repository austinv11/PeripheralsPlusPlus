package com.austinv11.peripheralsplusplus.utils;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.common.util.Constants;

import java.util.List;

public class NBTHelper {
	public static boolean hasTag(ItemStack itemStack, String keyName){
		return itemStack != null && itemStack.stackTagCompound != null && itemStack.stackTagCompound.hasKey(keyName);
	}

	public static void removeTag(ItemStack itemStack, String keyName){
		if (itemStack.stackTagCompound != null){
			itemStack.stackTagCompound.removeTag(keyName);
		}
	}

	/**
	 * Initializes the NBT Tag Compound for the given ItemStack if it is null
	 *
	 * @param itemStack
	 *         The ItemStack for which its NBT Tag Compound is being checked for initialization
	 */
	private static void initNBTTagCompound(ItemStack itemStack){
		if (itemStack.stackTagCompound == null){
			itemStack.setTagCompound(new NBTTagCompound());
		}
	}

	public static void setLong(ItemStack itemStack, String keyName, long keyValue){
		initNBTTagCompound(itemStack);
		itemStack.stackTagCompound.setLong(keyName, keyValue);
	}

	// String
	public static String getString(ItemStack itemStack, String keyName){
		initNBTTagCompound(itemStack);
		if (!itemStack.stackTagCompound.hasKey(keyName)){
			setString(itemStack, keyName, "");
		}
		return itemStack.stackTagCompound.getString(keyName);
	}

	public static void setString(ItemStack itemStack, String keyName, String keyValue){
		initNBTTagCompound(itemStack);
		itemStack.stackTagCompound.setString(keyName, keyValue);
	}

	// boolean
	public static boolean getBoolean(ItemStack itemStack, String keyName){
		initNBTTagCompound(itemStack);
		if (!itemStack.stackTagCompound.hasKey(keyName)){
			setBoolean(itemStack, keyName, false);
		}
		return itemStack.stackTagCompound.getBoolean(keyName);
	}

	public static void setBoolean(ItemStack itemStack, String keyName, boolean keyValue){
		initNBTTagCompound(itemStack);
		itemStack.stackTagCompound.setBoolean(keyName, keyValue);
	}

	// byte
	public static byte getByte(ItemStack itemStack, String keyName){
		initNBTTagCompound(itemStack);
		if (!itemStack.stackTagCompound.hasKey(keyName)){
			setByte(itemStack, keyName, (byte) 0);
		}
		return itemStack.stackTagCompound.getByte(keyName);
	}

	public static void setByte(ItemStack itemStack, String keyName, byte keyValue){
		initNBTTagCompound(itemStack);
		itemStack.stackTagCompound.setByte(keyName, keyValue);
	}

	// short
	public static short getShort(ItemStack itemStack, String keyName){
		initNBTTagCompound(itemStack);
		if (!itemStack.stackTagCompound.hasKey(keyName)){
			setShort(itemStack, keyName, (short) 0);
		}
		return itemStack.stackTagCompound.getShort(keyName);
	}

	public static void setShort(ItemStack itemStack, String keyName, short keyValue){
		initNBTTagCompound(itemStack);
		itemStack.stackTagCompound.setShort(keyName, keyValue);
	}

	// int
	public static int getInt(ItemStack itemStack, String keyName){
		initNBTTagCompound(itemStack);
		if (!itemStack.stackTagCompound.hasKey(keyName))
		{
			setInteger(itemStack, keyName, 0);
		}
		return itemStack.stackTagCompound.getInteger(keyName);
	}

	public static void setInteger(ItemStack itemStack, String keyName, int keyValue){
		initNBTTagCompound(itemStack);
		itemStack.stackTagCompound.setInteger(keyName, keyValue);
	}

	// long
	public static long getLong(ItemStack itemStack, String keyName){
		initNBTTagCompound(itemStack);
		if (!itemStack.stackTagCompound.hasKey(keyName)){
			setLong(itemStack, keyName, 0);
		}
		return itemStack.stackTagCompound.getLong(keyName);
	}

	// float
	public static float getFloat(ItemStack itemStack, String keyName){
		initNBTTagCompound(itemStack);
		if (!itemStack.stackTagCompound.hasKey(keyName)){
			setFloat(itemStack, keyName, 0);
		}
		return itemStack.stackTagCompound.getFloat(keyName);
	}

	public static void setFloat(ItemStack itemStack, String keyName, float keyValue){
		initNBTTagCompound(itemStack);
		itemStack.stackTagCompound.setFloat(keyName, keyValue);
	}

	// double
	public static double getDouble(ItemStack itemStack, String keyName){
		initNBTTagCompound(itemStack);
		if (!itemStack.stackTagCompound.hasKey(keyName)){
			setDouble(itemStack, keyName, 0);
		}
		return itemStack.stackTagCompound.getDouble(keyName);
	}

	public static void setDouble(ItemStack itemStack, String keyName, double keyValue){
		initNBTTagCompound(itemStack);
		itemStack.stackTagCompound.setDouble(keyName, keyValue);
	}

	public static NBTTagList getList(ItemStack itemStack, String keyName, int type) {
		initNBTTagCompound(itemStack);
		if (!itemStack.stackTagCompound.hasKey(keyName)){
			setList(itemStack, keyName, new NBTTagList());
		}
		return itemStack.stackTagCompound.getTagList(keyName, type);
	}

	public static void setList(ItemStack itemStack, String keyName, NBTBase tag) {
		initNBTTagCompound(itemStack);
		itemStack.stackTagCompound.setTag(keyName, tag);
	}

	public static void addInfo(ItemStack itemStack, List<String> text) {
		initNBTTagCompound(itemStack);
		if (hasTag(itemStack, "display")) {
			NBTTagCompound display = itemStack.stackTagCompound.getCompoundTag("display");
			NBTTagList list = display.getTagList("Lore", Constants.NBT.TAG_STRING);
			for (String s : text)
				list.appendTag(new NBTTagString(s));
			display.setTag("Lore", list);
			itemStack.stackTagCompound.setTag("display", display);
		}else {
			NBTTagCompound display = new NBTTagCompound();
			NBTTagList list = new NBTTagList();
			for (String s : text)
				list.appendTag(new NBTTagString(s));
			display.setTag("Lore", list);
			itemStack.stackTagCompound.setTag("display", display);
		}
	}

	public static void setInfo(ItemStack itemStack, List<String> text) {
		initNBTTagCompound(itemStack);
		NBTTagCompound display = new NBTTagCompound();
		NBTTagList list = new NBTTagList();
		for (String s : text)
			list.appendTag(new NBTTagString(s));
		display.setTag("Lore", list);
		itemStack.stackTagCompound.setTag("display", display);
	}

	public static void removeInfo(ItemStack itemStack) {
		if (hasTag(itemStack, "display")) {
			NBTTagCompound display = itemStack.stackTagCompound.getCompoundTag("display");
			display.removeTag("Lore");
			itemStack.stackTagCompound.setTag("display", display);
		}
	}

	public static void setTag(ItemStack itemStack, String keyName, NBTBase tag) {
		initNBTTagCompound(itemStack);
		itemStack.stackTagCompound.setTag(keyName, tag);
	}

	public static NBTBase getTag(ItemStack itemStack, String keyName) {
		initNBTTagCompound(itemStack);
		return itemStack.stackTagCompound.getTag(keyName);
	}

	public static void setIntArray(ItemStack itemStack, String keyName, int[] array) {
		initNBTTagCompound(itemStack);
		itemStack.stackTagCompound.setIntArray(keyName, array);
	}

	public static int[] getIntArray(ItemStack itemStack, String keyName) {
		initNBTTagCompound(itemStack);
		return itemStack.stackTagCompound.getIntArray(keyName);
	}

	public static NBTTagCompound getCompoundTag(ItemStack itemStack, String keyName) {
		initNBTTagCompound(itemStack);
		return itemStack.stackTagCompound.getCompoundTag(keyName);
	}
}
