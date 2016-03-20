package com.austinv11.peripheralsplusplus.init;

import com.austinv11.peripheralsplusplus.reference.Config;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class Recipes {
	public static void init() {
		if (Config.enableChatBox)
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.blockChatBox),
					"GNG",
					"NDN",
					"GNG",
					'G', "ingotGold", 'N', new ItemStack(Blocks.noteblock), 'D', "gemDiamond"));

		if (Config.enablePlayerSensor)
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.blockPlayerSensor),
					"GRG",
					"EDE",
					"GRG",
					'G', "ingotGold", 'R', "dustRedstone", 'E', new ItemStack(Items.ender_eye), 'D', "gemDiamond"));

		if (Config.enableEnvScanner)
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.blockEnvScanner),
					"IEI",
					"RMR",
					"IRI",
					'I', "ingotIron", 'E', new ItemStack(Items.ender_eye), 'R', "dustRedstone", 'M', Items.map));

		if (Config.enableOreDict)
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.blockOreDict),
					"ISI",
					"SBS",
					"ISI",
					'I', "ingotIron", 'S', "stone", 'B', new ItemStack(Items.book)));
	}
}
