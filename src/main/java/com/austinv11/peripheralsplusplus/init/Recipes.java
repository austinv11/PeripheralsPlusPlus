package com.austinv11.peripheralsplusplus.init;

import com.austinv11.peripheralsplusplus.PeripheralsPlusPlus;
import com.austinv11.peripheralsplusplus.recipe.UpgradeRecipe;
import com.austinv11.peripheralsplusplus.reference.Config;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class Recipes {

	public static void init(){
		//Hehehe inside jokes ftw!
		ItemStack cDust = new ItemStack(Items.redstone);
		cDust.setStackDisplayName(StatCollector.translateToLocal("item.peripheralsplusplus:dustRedstone.name"));
		ItemStack tIngot = new ItemStack(Items.iron_ingot);
		tIngot.setStackDisplayName(StatCollector.translateToLocal("item.peripheralsplusplus:ingotIron.name"));
		GameRegistry.addShapelessRecipe(cDust, new ItemStack(Items.redstone));
		GameRegistry.addShapelessRecipe(tIngot, new ItemStack(Items.iron_ingot));
		if (Config.enableChatBox)
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.chatBox), "gng", "ndn", "gng", 'g', "ingotGold", 'n', new ItemStack(Blocks.noteblock), 'd', "gemDiamond"));
		if (Config.enablePlayerSensor)
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.playerSensor), "grg", "ede", "grg", 'g', "ingotGold", 'r', "dustRedstone", 'e', new ItemStack(Items.ender_eye), 'd', "gemDiamond"));
		if (Config.enableRFCharger)
			if (Loader.isModLoaded("ThermalExpansion"))
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.rfCharger), "rlr", "lcl", "rer", 'r', "dustRedstone", 'l', "ingotLead", 'c', new ItemStack(GameRegistry.findItem("ThermalExpansion", "capacitor"), 1, 2), 'e', new ItemStack(GameRegistry.findItem("ThermalExpansion", "material"), 1, 3)));
			else if (Loader.isModLoaded("BuildCraft|Core"))
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.rfCharger), "rlr", "lcl", "rer", 'r', "dustRedstone", 'l', "gearIron", 'c', "gearGold", 'e', new ItemStack(GameRegistry.findItem("BuildCraft|Transport", "item.buildcraftPipe.pipepowergold"))));
		if (Config.enableOreDictionary)
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.oreDictionary), "isi", "sbs", "isi", 'i', "ingotIron", 's', "stone", 'b', new ItemStack(Items.book)));
		if (Config.enableAnalyzers && Loader.isModLoaded("Forestry")) {
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.beeAnalyzer), "grg", "rar", "grg", 'g', "ingotGold", 'r', "dustRedstone", 'a', new ItemStack(GameRegistry.findItem("Forestry", "beealyzer"))));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.butterflyAnalyzer), "grg", "rar", "grg", 'g', "ingotGold", 'r', "dustRedstone", 'a', new ItemStack(GameRegistry.findItem("Forestry", "flutterlyzer"))));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.treeAnalyzer), "grg", "rar", "grg", 'g', "ingotGold", 'r', "dustRedstone", 'a', new ItemStack(GameRegistry.findItem("Forestry", "treealyzer"))));
		}
		if (Config.enableTurtleTeleporter) {
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.teleporter), "pep", "pop", "pep", 'p', new ItemStack(Items.ender_pearl), 'e', new ItemStack(Items.ender_eye), 'o', new ItemStack(Blocks.obsidian)));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.teleporterT2), "ere", "EtE", "ere", 'r', "dustRedstone", 'e', new ItemStack(Items.ender_eye), 't', new ItemStack(ModBlocks.teleporter), 'E', "gemEmerald"));
		}
		if (Config.enableEnvironmentScanner)
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.environmentScanner), "iei", "rmr", "iri", 'i', "ingotIron", 'e', new ItemStack(Items.ender_eye), 'r', "dustRedstone", 'm', Items.map));
		if (Config.enableFeederTurtle)
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.feederUpgrade), "iwi", "wew", "iwi", 'i', "ingotIron", 'w', new ItemStack(Items.wheat), 'e', new ItemStack(Items.ender_eye)));
		if (Config.enableSatellites) {
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.satellite), "ded", "ptp", "iii", 'd', new ItemStack(Blocks.daylight_detector), 'e', new ItemStack(Items.ender_eye), 'p', new ItemStack(Blocks.heavy_weighted_pressure_plate), 't', new ItemStack(GameRegistry.findBlock("ComputerCraft", "CC-TurtleAdvanced")), 'i', "ingotIron"));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.rocket), " r ", " b ", "ibi", 'r', new ItemStack(Blocks.redstone_torch), 'b', new ItemStack(Blocks.iron_block), 'i', new ItemStack(Blocks.iron_bars)));
			for (int i = 0; i < PeripheralsPlusPlus.SATELLITE_UPGRADE_ID_REGISTRY.size(); i++)
				GameRegistry.addRecipe(PeripheralsPlusPlus.SATELLITE_UPGRADE_REGISTRY.get(PeripheralsPlusPlus.SATELLITE_UPGRADE_ID_REGISTRY.get(i)).getUpgrade().getRecipe());
			GameRegistry.addRecipe(new UpgradeRecipe());
			GameRegistry.addShapelessRecipe(new ItemStack(ModItems.satellite), new ItemStack(ModItems.satellite));
		}
		if (Config.enableSpeaker)
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.speaker), "gng", "nrn", "gng", 'g', "ingotGold", 'n', Blocks.noteblock, 'r', "blockRedstone"));
	}
}
