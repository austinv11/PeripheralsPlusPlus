package com.austinv11.peripheralsplusplus.villagers;

import com.austinv11.collectiveframework.minecraft.utils.Colors;
import com.austinv11.collectiveframework.minecraft.utils.NBTHelper;
import com.austinv11.peripheralsplusplus.utils.Util;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.VillagerRegistry;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TradeHandler implements VillagerRegistry.IVillageTradeHandler {
	
	@Override
	public void manipulateTradesForVillager(EntityVillager villager, MerchantRecipeList recipeList, Random random) {
		ItemStack error = new ItemStack(Items.diamond);
		ItemStack emerald = new ItemStack(Items.emerald);
		error.setStackDisplayName(Colors.RED+"THIS IS A BUG, REPORT TO THE P++ AUTHOR ASAP");
		MerchantRecipe recipe = new MerchantRecipe(new ItemStack(Blocks.dirt), error);
		do {
			int trade = MathHelper.getRandomIntegerInRange(random, 0, 8);
			switch (trade) {
				case 0://Empty floppy disk + 3 emeralds = dungeon disk FIXME, must use colored disks
					int type = MathHelper.getRandomIntegerInRange(random, 0, 9);
					ItemStack floppy = getFloppyFromInt(type);
					emerald.stackSize = 3;
					recipe = new MerchantRecipe(new ItemStack(GameRegistry.findItem("ComputerCraft", "diskExpanded")), emerald, floppy);
					break;
				case 1://Normal comp + emerald = advanced comp
					recipe = new MerchantRecipe(new ItemStack(GameRegistry.findBlock("ComputerCraft", "CC-Computer")), emerald, new ItemStack(GameRegistry.findBlock("ComputerCraft", "CC-Computer"), 1, 16384));
					break;
				case 2://Normal monitor + emerald = advanced monitor
					recipe = new MerchantRecipe(new ItemStack(GameRegistry.findBlock("ComputerCraft", "CC-Peripheral"), 1, 2), emerald, new ItemStack(GameRegistry.findBlock("ComputerCraft", "CC-Peripheral"), 1, 4));
					break;
				case 3://Normal turtle + emerald = advanced turtle
					recipe = new MerchantRecipe(new ItemStack(GameRegistry.findBlock("ComputerCraft", "CC-Turtle")), emerald, new ItemStack(GameRegistry.findBlock("ComputerCraft", "CC-TurtleAdvanced")));
					break;
				case 4://Normal comp + 2 emeralds = normal turtle
					emerald.stackSize = 2;
					recipe = new MerchantRecipe(new ItemStack(GameRegistry.findBlock("ComputerCraft", "CC-Computer")), emerald, new ItemStack(GameRegistry.findBlock("ComputerCraft", "CC-Turtle")));
					break;
				case 5://Advanced comp + 2 emeralds = advanced turtle
					emerald.stackSize = 2;
					recipe = new MerchantRecipe(new ItemStack(GameRegistry.findBlock("ComputerCraft", "CC-Computer"), 1, 16384), emerald, new ItemStack(GameRegistry.findBlock("ComputerCraft", "CC-TurtleAdvanced")));
					break;
				case 6://64 emeralds = portable computer (an IPhone basically)
					emerald.stackSize = 64;
					ItemStack iPhone = new ItemStack(GameRegistry.findItem("ComputerCraft", "pocketComputer"), 1, 1);
					iPhone.setStackDisplayName(StatCollector.translateToLocal("item.peripheralsplusplus:iphone.name"));
					recipe = new MerchantRecipe(emerald, iPhone);
					break;
				case 7://Paper + emerald = book w/ lore TODO:More than 3 books
					recipe = new MerchantRecipe(new ItemStack(Items.book), emerald, getBookFromInt(MathHelper.getRandomIntegerInRange(random, 0, 2)));
					break;
			}
		} while (recipeList.contains(recipe));
		recipeList.add(recipe);
	}

	public static ItemStack getFloppyFromInt(int t) {
		ItemStack stack = new ItemStack(GameRegistry.findItem("ComputerCraft", "treasureDisk"));
		switch (t) {
			case 0:
				break;
			case 1:
				NBTHelper.setInteger(stack, "colour", 3368652);
				NBTHelper.setString(stack, "subPath", "fredthead/protector");
				NBTHelper.setString(stack, "title", "\"protector\" by fredthead");
				break;
			case 2:
				NBTHelper.setInteger(stack, "colour", 3368652);
				NBTHelper.setString(stack, "subPath", "GopherAtl/battleship");
				NBTHelper.setString(stack, "title", "\"battleship\" by GopherAtl");
				break;
			case 3:
				NBTHelper.setInteger(stack, "colour", 3368652);
				NBTHelper.setString(stack, "subPath", "GravityScore/LuaIDE");
				NBTHelper.setString(stack, "title", "\"LuaIDE\" by GravityScore");
				break;
			case 4:
				NBTHelper.setInteger(stack, "colour", 3368652);
				NBTHelper.setString(stack, "subPath", "JTK/maze3d");
				NBTHelper.setString(stack, "title", "\"maze3d\" by JTK");
				break;
			case 5:
				NBTHelper.setInteger(stack, "colour", 3368652);
				NBTHelper.setString(stack, "subPath", "Lyqyd/nsh");
				NBTHelper.setString(stack, "title", "\"nsh\" by Lyqyd");
				break;
			case 6:
				NBTHelper.setInteger(stack, "colour", 3368652);
				NBTHelper.setString(stack, "subPath", "nitrogenfingers/goldrunner");
				NBTHelper.setString(stack, "title", "\"goldrunner\" by nitrogenfingers");
				break;
			case 7:
				NBTHelper.setInteger(stack, "colour", 3368652);
				NBTHelper.setString(stack, "subPath", "nitrogenfingers/npaintpro");
				NBTHelper.setString(stack, "title", "\"npaintpro\" by nitrogenfingers");
				break;
			case 8:
				NBTHelper.setInteger(stack, "colour", 3368652);
				NBTHelper.setString(stack, "subPath", "vilsol/gameoflife");
				NBTHelper.setString(stack, "title", "\"gameoflife\" by vilsol");
				break;
			case 9:
				NBTHelper.setInteger(stack, "colour", 3368652);
				NBTHelper.setString(stack, "subPath", "TheOriginalBIT/tictactoe");
				NBTHelper.setString(stack, "title", "\"tictactoe\" by TheOriginalBIT");
				break;
		}
		stack.setItemDamage(0);
		return stack;
	}

	public static ItemStack getBookFromInt(int type) {
		ItemStack stack = new ItemStack(Items.written_book);
		switch (type) {
			case 0:
				stack.stackTagCompound = Util.writeToBookNBT("peripheralsplusplus.lore.1.title", Colors.MAGIC+"dan200", getTextFromInt(type));
				break;
			case 1:
				stack.stackTagCompound = Util.writeToBookNBT("peripheralsplusplus.lore.2.title", Colors.MAGIC+"dan200", getTextFromInt(type));
				break;
			case 2:
				stack.stackTagCompound = Util.writeToBookNBT("peripheralsplusplus.lore.3.title", Colors.MAGIC+"dan200", getTextFromInt(type));
				break;
		}
		return stack;
	}

	private static List<String> getTextFromInt(int type) {
		List<String> list = new ArrayList<String>();
		switch (type) {
			case 0:
				list.add("peripheralsplusplus.lore.1.header");
				list.add("peripheralsplusplus.lore.1.pg1");
				list.add("peripheralsplusplus.lore.1.pg2");
				list.add("peripheralsplusplus.lore.1.pg3");
				list.add("peripheralsplusplus.lore.1.pg4");
				break;
			case 1:
				list.add("peripheralsplusplus.lore.2.header");
				list.add("peripheralsplusplus.lore.2.pg1");
				list.add("peripheralsplusplus.lore.2.pg2");
				list.add("peripheralsplusplus.lore.2.pg3");
				list.add("peripheralsplusplus.lore.2.pg4");
				list.add("peripheralsplusplus.lore.2.pg5");
				list.add("peripheralsplusplus.lore.2.pg6");
				break;
			case 2:
				list.add("peripheralsplusplus.lore.3.header");
				list.add("peripheralsplusplus.lore.3.pg1");
				list.add("peripheralsplusplus.lore.3.pg2");
				list.add("peripheralsplusplus.lore.3.pg3");
				break;
		}
		return list;
	}
}
