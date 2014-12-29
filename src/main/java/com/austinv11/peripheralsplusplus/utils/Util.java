package com.austinv11.peripheralsplusplus.utils;

import com.austinv11.peripheralsplusplus.reference.Reference;
import com.google.common.collect.Multimap;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.oredict.OreDictionary;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Util {

	public static HashMap<Integer, Object> iterableToMap(EnumSet<EnumPlantType> iterable) {
		HashMap<Integer,Object> map = new HashMap<Integer,Object>();
		Iterator<EnumPlantType> types = iterable.iterator();
		for (int i = 0; i < iterable.size(); i++) {
			map.put(i, types.next());
		}
		return map;
	}

	public static HashMap<Integer,Integer> arrayToMap(int[] array) {
		HashMap<Integer,Integer> map = new HashMap<Integer,Integer>();
		for (int i = 0; i < array.length; i++) {
			map.put(i, array[i]);
		}
		return map;
	}

	public static HashMap<Integer,Object> arrayToMap(Object[] array) {
		HashMap<Integer,Object> map = new HashMap<Integer,Object>();
		for (int i = 0; i < array.length; i++) {
			map.put(i, array[i]);
		}
		return map;
	}

	public static HashMap<Integer, String> getEntries(ItemStack stack) {
		int[] ids = OreDictionary.getOreIDs(stack);
		HashMap<Integer, String> entries = new HashMap<Integer,String>();
		for (int i = 0; i < ids.length; i++) {
			entries.put(i, OreDictionary.getOreName(ids[i]));
		}
		return entries;
	}

	public static boolean compare(ItemStack stack1, ItemStack stack2) {
		if (!(stack1 == null || stack2 == null))
			for (String key : getEntries(stack1).values()) {
				if (getEntries(stack2).containsValue(key))
					return true;
			}
		return false;
	}

	public static NBTTagCompound writeToBookNBT(String title, String author, List<String> pageText) {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setString("author", author);
		tag.setString("title", StatCollector.translateToLocal(title));
		NBTTagList list = new NBTTagList();
		for (String s : pageText)
			list.appendTag(new NBTTagString(StatCollector.translateToLocal(s)));
		tag.setTag("pages", list);
		return tag;
	}

	public static NBTTagCompound writeToBookNBT(String title, List<String> pageText) {
		return writeToBookNBT(title, Reference.MOD_NAME, pageText);
	}

	public static double getDamageAttribute(ItemStack item) {
		double val = 0;
		Multimap multimap = item.getItem().getAttributeModifiers(item);
		if (multimap.containsKey(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName()))
			for (Object o : multimap.get(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName()))
				if (o instanceof AttributeModifier) {
					val = ((AttributeModifier) o).getAmount();
					break;
				}
		return val;
	}

	public static String listToString(List<String> list) {
		String returnVal = "";
		for (Object o : list)
			returnVal = returnVal+String.valueOf(o);
		return returnVal;
	}

	public static String readFile(File file) throws IOException {
		FileReader reader = new FileReader(file);
		String result = "";
		int nextChar;
		while ((nextChar = reader.read()) != -1) {
			char newChar = (char)nextChar;
			result = result+newChar;
		}
		reader.close();
		return result;
	}

	public static String readFile(String filePath) throws IOException {
		return readFile(new File(filePath));
	}
}
