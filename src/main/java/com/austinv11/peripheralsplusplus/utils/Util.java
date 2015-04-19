package com.austinv11.peripheralsplusplus.utils;

import com.austinv11.collectiveframework.utils.FileUtils;
import com.austinv11.collectiveframework.utils.StringUtils;
import com.austinv11.peripheralsplusplus.reference.Reference;
import com.google.common.collect.Multimap;
import com.google.gson.Gson;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.oredict.OreDictionary;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Util {

	public static HashMap<Integer, Object> iteratorToMap(Iterator iterator) {
		HashMap<Integer,Object> map = new HashMap<Integer,Object>();
		int i = 1;
		while (iterator.hasNext()) {
			map.put(i+1, iterator.next());
			i++;
		}
		return map;
	}

	public static HashMap<Integer, Object> EnumSetToMap(EnumSet<EnumPlantType> iterable) {
		HashMap<Integer,Object> map = new HashMap<Integer,Object>();
		Iterator<EnumPlantType> types = iterable.iterator();
		for (int i = 0; i < iterable.size(); i++) {
			map.put(i+1, types.next());
		}
		return map;
	}

	public static HashMap<Integer, Object> collectionToMap(Collection iterable) {
		HashMap<Integer,Object> map = new HashMap<Integer,Object>();
		Iterator<Object> types = iterable.iterator();
		for (int i = 0; i < iterable.size(); i++) {
			map.put(i+1, types.next());
		}
		return map;
	}

	public static HashMap<Integer,Integer> arrayToMap(int[] array) {
		HashMap<Integer,Integer> map = new HashMap<Integer,Integer>();
		for (int i = 0; i < array.length; i++) {
			map.put(i+1, array[i]);
		}
		return map;
	}

	public static HashMap<Integer,Object> arrayToMap(Object[] array) {
		HashMap<Integer,Object> map = new HashMap<Integer,Object>();
		for (int i = 0; i < array.length; i++) {
			map.put(i+1, array[i]);
		}
		return map;
	}

	public static HashMap<Integer, String> getEntries(ItemStack stack) {
		int[] ids = OreDictionary.getOreIDs(stack);
		HashMap<Integer, String> entries = new HashMap<Integer,String>();
		for (int i = 0; i < ids.length; i++) {
			entries.put(i+1, OreDictionary.getOreName(ids[i]));
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
		for (String s : list)
			returnVal = returnVal+s+"\n";
		return returnVal;
	}

	public static boolean checkFileVersion(String dir, JSONFileList json) throws IOException{
		File file = new File(dir+"/index.json");
		if (!file.exists())
			return true;
		Gson gson = new Gson();
		String localJson = StringUtils.stringFromList(FileUtils.readAll(file));
		JSONFileList localFile = gson.fromJson(localJson, JSONFileList.class);
		return !localFile.ver.equals(json.ver);
	}

	public static Object keyFromVal(HashMap map, Object val) {
		for (Object key : map.keySet())
			if (map.get(key).equals(val))
				return key;
		return null;
	}

	public static EntityPlayer getPlayer(String ign) {
		List<EntityPlayer> players = MinecraftServer.getServer().getConfigurationManager().playerEntityList;
		for (EntityPlayer p : players) {
			if (p.getDisplayName().equalsIgnoreCase(ign))
				return p;
		}
		return null;
	}

	public static String[] stringToArray(String array) {
		String[] array_ = array.replace("]", "").replace("[", "").split(",");
		for (int i = 0; i < array_.length; i++)
			array_[i] = array_[i].trim();
		return array_;
	}
	
	public static List<String> getPlayers(World world) {
		List<String> list = new ArrayList<String>();
		if (world != null) {
			for (EntityPlayer player : (Iterable<EntityPlayer>) world.playerEntities)
				list.add(player.getDisplayName());
		}else {
			for (String player : MinecraftServer.getServer().getAllUsernames())
				list.add(player);
		}
		return list;
	}
}
