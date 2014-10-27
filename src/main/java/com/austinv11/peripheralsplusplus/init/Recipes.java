package com.austinv11.peripheralsplusplus.init;

import com.austinv11.peripheralsplusplus.reference.Config;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class Recipes {

	public static void init(){
		if (Config.enableChatBox)
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.chatBox), "gng", "ndn", "gng", 'g', "ingotGold", 'n', new ItemStack(Blocks.noteblock), 'd', "gemDiamond"));
		if (Config.enablePlayerSensor)
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.playerSensor), "grg", "ede", "grg", 'g', "ingotGold", 'r', "dustRedstone", 'e', new ItemStack(Items.ender_eye), 'd', "gemDiamond"));
		if (Config.enableMEBridge)
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.meBridge), "dpd", "pip", "dpd", 'd', "gemDiamond", 'p', new ItemStack(Blocks.piston), 'i', new ItemStack(Item.getItemFromBlock(Block.getBlockFromName("appliedenergistics2:tile.BlockInterface")))));
	}
}
