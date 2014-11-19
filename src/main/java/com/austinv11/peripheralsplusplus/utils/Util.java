package com.austinv11.peripheralsplusplus.utils;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.oredict.OreDictionary;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;

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
}
