package com.austinv11.peripheralsplusplus.init;

import com.austinv11.peripheralsplusplus.reference.Config;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class Recipes {

	public static void init(){
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.upgradeBase), "rsr", "sis", "rsr", 'r', "dustRedstone", 's', "stone", 'i', "ingotIron"));
		if (Config.enableChatBox)
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.chatBox), "gng", "ndn", "gng", 'g', "ingotGold", 'n', new ItemStack(Blocks.noteblock), 'd', "gemDiamond"));
		if (Config.enablePlayerSensor)
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.playerSensor), "grg", "ede", "grg", 'g', "ingotGold", 'r', "dustRedstone", 'e', new ItemStack(Items.ender_eye), 'd', "gemDiamond"));
		if (Config.enableRFCharger && Loader.isModLoaded("ThermalExpansion"))
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.rfCharger), "rlr", "lcl", "rer", 'r', "dustRedstone", 'l', "ingotLead", 'c', new ItemStack(GameRegistry.findItem("ThermalExpansion", "capacitor"), 1, 2), 'e', new ItemStack(GameRegistry.findItem("ThermalExpansion", "material"), 1, 3)));
		if (Config.enableNavigationTurtle)
			GameRegistry.addShapelessRecipe(new ItemStack(ModItems.upgradeCompass), new ItemStack(ModItems.upgradeBase), new ItemStack(Items.compass));
	}
}
