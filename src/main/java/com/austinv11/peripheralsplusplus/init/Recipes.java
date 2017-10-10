package com.austinv11.peripheralsplusplus.init;

import com.austinv11.collectiveframework.minecraft.reference.ModIds;
import com.austinv11.peripheralsplusplus.recipe.ContainerRecipe;
import com.austinv11.peripheralsplusplus.recipe.ContainerRecipePocket;
import com.austinv11.peripheralsplusplus.reference.Config;
import com.austinv11.peripheralsplusplus.reference.Reference;
import com.austinv11.peripheralsplusplus.utils.TurtleUtil;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import java.util.ArrayList;
import java.util.List;

import static net.minecraftforge.fml.common.registry.ForgeRegistries.RECIPES;

public class Recipes {

	private static final ResourceLocation MOD_GROUP = new ResourceLocation(Reference.MOD_ID);

	public static void init(){
		if (Config.enableChatBox)
			RECIPES.register(new ShapedOreRecipe(
					new ResourceLocation(Reference.MOD_ID),
					new ItemStack(ModBlocks.CHAT_BOX),
					"gng",
					"ndn",
					"gng",
					'g', "ingotGold",
					'n', new ItemStack(Blocks.NOTEBLOCK),
					'd', "gemDiamond"
			).setRegistryName(Reference.MOD_ID, "recipe_chat_box"));
		if (Config.enablePlayerSensor)
			RECIPES.register(new ShapedOreRecipe(
					new ResourceLocation(Reference.MOD_ID),
					new ItemStack(ModBlocks.PLAYER_SENSOR),
					"grg",
					"ede",
					"grg",
					'g', "ingotGold",
					'r', "dustRedstone",
					'e', new ItemStack(Items.ENDER_EYE),
					'd', "gemDiamond"
			).setRegistryName(Reference.MOD_ID, "recipe_player_sensor"));
		if (Config.enableRFCharger) {
			String ingot = "ingotIron";
			ItemStack capacitor = new ItemStack(Blocks.REDSTONE_BLOCK);
			ItemStack coil = new ItemStack(Items.REDSTONE);
			if (Loader.isModLoaded(ModIds.THERMAL_EXPANSION) && Loader.isModLoaded(ModIds.THERMAL_FOUNDATION)) {
				ingot = "ingotLead";
				capacitor = GameRegistry.makeItemStack(ModIds.THERMAL_EXPANSION + ":capacitor", 0, 1, "");
				coil = GameRegistry.makeItemStack(ModIds.THERMAL_FOUNDATION + ":material", 515, 1, "");
			}
			RECIPES.register(new ShapedOreRecipe(
					MOD_GROUP,
					new ItemStack(ModBlocks.RF_CHARGER),
					"rlr",
					"lcl",
					"rer",
					'r', "dustRedstone",
					'l', ingot,
					'c', capacitor,
					'e', coil
			).setRegistryName(Reference.MOD_ID, "recipe_rf_charger"));
		}
		if (Config.enableOreDictionary)
			RECIPES.register(new ShapedOreRecipe(
					MOD_GROUP,
					new ItemStack(ModBlocks.ORE_DICTIONARY),
					"isi",
					"sbs",
					"isi",
					'i', "ingotIron",
					's', "stone",
					'b', new ItemStack(Items.BOOK)
			).setRegistryName(Reference.MOD_ID, "recipe_ore_dictionary"));
		if (Config.enableAnalyzers && Loader.isModLoaded(ModIds.FORESTRY)) {
			RECIPES.register(new ShapedOreRecipe(
					MOD_GROUP,
					new ItemStack(ModBlocks.ANALYZER_BEE),
					"grg",
					"rar",
					"grg",
					'g', "ingotGold",
					'r', "dustRedstone",
					'a', GameRegistry.makeItemStack(ModIds.FORESTRY + ":analyzer", 0, 1, "")
			).setRegistryName(Reference.MOD_ID, "recipe_analyzer_bee"));
			RECIPES.register(new ShapelessOreRecipe(
					MOD_GROUP,
					new ItemStack(ModBlocks.ANALYZER_BUTTERFLY),
					new ItemStack(ModBlocks.ANALYZER_BEE)
			).setRegistryName(Reference.MOD_ID, "recipe_analyzer_butterfly"));
			RECIPES.register(new ShapelessOreRecipe(
					MOD_GROUP,
					new ItemStack(ModBlocks.ANALYZER_TREE),
					new ItemStack(ModBlocks.ANALYZER_BUTTERFLY)
			).setRegistryName(Reference.MOD_ID, "recipe_analyzer_tree"));
			RECIPES.register(new ShapelessOreRecipe(
					MOD_GROUP,
					new ItemStack(ModBlocks.ANALYZER_BEE),
					new ItemStack(ModBlocks.ANALYZER_TREE)
			).setRegistryName(Reference.MOD_ID, "recipe_analyzer_bee_convert"));
		}
		if (Config.enableTurtleTeleporter) {
			RECIPES.register(new ShapedOreRecipe(
					MOD_GROUP,
					new ItemStack(ModBlocks.TELEPORTER),
					"pep",
					"pop",
					"pep",
					'p', new ItemStack(Items.ENDER_PEARL),
					'e', new ItemStack(Items.ENDER_EYE),
					'o', new ItemStack(Blocks.OBSIDIAN)
			).setRegistryName(Reference.MOD_ID, "recipe_teleporter"));
			RECIPES.register(new ShapedOreRecipe(
					MOD_GROUP,
					new ItemStack(ModBlocks.TELEPORTER, 1, 1),
					"ere",
					"EtE",
					"ere",
					'r', "dustRedstone",
					'e', new ItemStack(Items.ENDER_EYE),
					't', new ItemStack(ModBlocks.TELEPORTER),
					'E', "gemEmerald"
			).setRegistryName(Reference.MOD_ID, "recipe_teleporter_t2"));
		}
		if (Config.enableEnvironmentScanner)
			RECIPES.register(new ShapedOreRecipe(
					MOD_GROUP,
					new ItemStack(ModBlocks.ENVIRONMENT_SCANNER),
					"iei",
					"rmr",
					"iri",
					'i', "ingotIron",
					'e', new ItemStack(Items.ENDER_EYE),
					'r', "dustRedstone",
					'm', Items.MAP
			).setRegistryName(Reference.MOD_ID, "recipe_environment_scanner"));
		if (Config.enableFeederTurtle)
			RECIPES.register(new ShapedOreRecipe(
					MOD_GROUP,
					new ItemStack(ModItems.FEEDER_UPGRADE),
					"iwi",
					"wew",
					"iwi",
					'i', "ingotIron",
					'w', new ItemStack(Items.WHEAT),
					'e', new ItemStack(Items.ENDER_EYE)
			).setRegistryName(Reference.MOD_ID, "recipe_feeder"));
		if (Config.enableSpeaker)
			RECIPES.register(new ShapedOreRecipe(
					MOD_GROUP,
					new ItemStack(ModBlocks.SPEAKER),
					"gng",
					"nrn",
					"gng",
					'g',
					"ingotGold",
					'n', Blocks.NOTEBLOCK,
					'r', "blockRedstone"
			).setRegistryName(Reference.MOD_ID, "recipe_speaker"));
		if (Config.enablePeripheralContainer) {
			RECIPES.register(new ShapedOreRecipe(
					MOD_GROUP,
					new ItemStack(ModBlocks.PERIPHERAL_CONTAINER),
					"iii",
					"ici",
					"imi",
					'i', "ingotIron",
					'c', Blocks.CHEST,
					'm', GameRegistry.makeItemStack(ModIds.COMPUTERCRAFT + ":cable", 0, 1, ""))
					.setRegistryName(new ResourceLocation(Reference.MOD_ID, "recipe_peripheral_container")));
			RECIPES.register(new ContainerRecipe(MOD_GROUP)
					.setRegistryName(new ResourceLocation("recipe_peripheral_container_add_item")));
			RECIPES.register(new ContainerRecipePocket(MOD_GROUP)
					.setRegistryName(new ResourceLocation("recipe_peripheral_container_pocket_add_item_advanced")));
		}
		if (Config.enableMEBridge && Loader.isModLoaded(ModIds.APPLIED_ENGERGISTICS))
			RECIPES.register(new ShapelessOreRecipe(
					MOD_GROUP,
					new ItemStack(ModBlocks.ME_BRIDGE),
					GameRegistry.makeItemStack(ModIds.APPLIED_ENGERGISTICS + ":interface", 0, 1, ""),
					GameRegistry.makeItemStack(ModIds.COMPUTERCRAFT + ":cable", 1, 1, "")
			).setRegistryName(Reference.MOD_ID, "recipe_me_bridge"));
		if (Config.enableTankTurtle)
			RECIPES.register(new ShapedOreRecipe(
					MOD_GROUP,
					new ItemStack(ModItems.TANK),
					"ggg",
					"gmg",
					"ggg",
					'g', "blockGlass",
					'm', GameRegistry.makeItemStack(ModIds.COMPUTERCRAFT + ":cable", 1, 1, "")
			).setRegistryName(Reference.MOD_ID, "recipe_tank"));
		if (Config.enableSmartHelmet) {
			RECIPES.register(new ShapelessOreRecipe(
					MOD_GROUP,
					new ItemStack(ModItems.SMART_HELMET),
					GameRegistry.makeItemStack(ModIds.COMPUTERCRAFT + ":peripheral", 1, 1, ""),
					new ItemStack(Items.IRON_HELMET)
			).setRegistryName(Reference.MOD_ID, "recipe_smart_helmet"));
			RECIPES.register(new ShapelessOreRecipe(
					MOD_GROUP,
					new ItemStack(ModItems.SMART_HELMET),
					new ItemStack(ModItems.SMART_HELMET)
			).setRegistryName(Reference.MOD_ID, "recipe_smart_helmet_clear"));
		}
		RECIPES.register(new ShapedOreRecipe(
				MOD_GROUP,
				new ItemStack(ModBlocks.ANTENNA),
				"sms",
				" i ",
				"ppp",
				's', "stone",
				'm', GameRegistry.makeItemStack(ModIds.COMPUTERCRAFT + ":peripheral", 1, 1, ""),
				'i', "ingotIron",
				'p', new ItemStack(Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE)
		).setRegistryName(Reference.MOD_ID, "recipe_antenna"));
		RECIPES.register(new ShapelessOreRecipe(
				MOD_GROUP,
				new ItemStack(ModBlocks.TURTLE),
				TurtleUtil.getTurtle(true)
		).setRegistryName(Reference.MOD_ID, "recipe_turtle_from_advanced"));
		RECIPES.register(new ShapelessOreRecipe(
				MOD_GROUP,
				new ItemStack(ModBlocks.TURTLE),
				TurtleUtil.getTurtle(false)
		).setRegistryName(Reference.MOD_ID, "recipe_turtle"));
		RECIPES.register(new ShapelessOreRecipe(
				MOD_GROUP,
				new ItemStack(ModBlocks.TURTLE),
				GameRegistry.makeItemStack(ModIds.COMPUTERCRAFT + ":turtle_expanded", 0, 1, "")
		).setRegistryName(Reference.MOD_ID, "recipe_turtle_from_expanded"));
		if (Config.enableChunkyTurtle) {
			List<ItemStack> chunkLoaders = new ArrayList<>();
			if (Loader.isModLoaded(ModIds.CHICKEN_CHUNKS)) {
				chunkLoaders.add(GameRegistry.makeItemStack(ModIds.CHICKEN_CHUNKS +":chunk_loader",
						0, 1, ""));
				if (Config.chunkLoadingRadius == 0)
					chunkLoaders.add(GameRegistry.makeItemStack(ModIds.CHICKEN_CHUNKS + ":chunk_loader",
							1, 1, ""));
			}
			chunkLoaders.add(new ItemStack(Items.END_CRYSTAL));
			for (ItemStack itemStack : chunkLoaders)
				if (itemStack != null)
					RECIPES.register(new ShapelessOreRecipe(
							MOD_GROUP,
							new ItemStack(ModItems.CHUNK_LOADER_UPGRADE),
							itemStack,
							GameRegistry.makeItemStack(ModIds.COMPUTERCRAFT + ":peripheral", 1, 1, "")
					).setRegistryName(Reference.MOD_ID, "recipe_chunk_loader_" + itemStack.getUnlocalizedName()));
		}
		if (Config.enableInteractiveSorter)
			RECIPES.register(new ShapedOreRecipe(
					MOD_GROUP,
					new ItemStack(ModBlocks.INTERACTIVE_SORTER),
					"dpd",
					"pep",
					"dpd",
					'd', "gemDiamond",
					'p', new ItemStack(Blocks.PISTON),
					'e', new ItemStack(Items.ENDER_EYE)
			).setRegistryName(Reference.MOD_ID, "recipe_interactive_sorter"));
		RECIPES.register(new ShapelessOreRecipe(
				MOD_GROUP,
				new ItemStack(ModItems.PERM_CARD),
				new ItemStack(ModItems.PERM_CARD)
		).setRegistryName(Reference.MOD_ID, "recipe_perm_card"));
		if (Config.enableResupplyStation) {
			RECIPES.register(new ShapedOreRecipe(
					MOD_GROUP,
					new ItemStack(ModItems.RESUPPLY_UPGRADE),
					"grg",
					"rer",
					"grg",
					'g', "ingotGold",
					'r', "dustRedstone",
					'e', Blocks.ENDER_CHEST
			).setRegistryName(Reference.MOD_ID, "recipe_resupply_station_upgrade"));
			RECIPES.register(new ShapedOreRecipe(
					MOD_GROUP,
					new ItemStack(ModBlocks.RESUPPLY_STATION),
					"iri",
					"cuc",
					"iri",
					'i', "ingotIron",
					'r', "dustRedstone",
					'c', Blocks.CHEST,
					'u', ModItems.RESUPPLY_UPGRADE
			).setRegistryName(Reference.MOD_ID, "recipe_resupply_station"));
		}
        if (Config.enablePlayerInterface) {
			RECIPES.register(new ShapedOreRecipe(
					MOD_GROUP,
					new ItemStack(ModBlocks.PLAYER_INTERFACE),
					"ipi",
					"ini",
					"imi",
					'i', new ItemStack(Items.IRON_INGOT),
					'p', new ItemStack(ModItems.PERM_CARD),
					'n', new ItemStack(Items.NETHER_STAR),
					'm', GameRegistry.makeItemStack(ModIds.COMPUTERCRAFT + ":peripheral", 1, 1, "")
			).setRegistryName(Reference.MOD_ID, "recipe_player_interface"));
			RECIPES.register(new ShapelessOreRecipe(
					MOD_GROUP,
					new ItemStack(ModItems.PERM_CARD),
					new ItemStack(Items.EMERALD),
					new ItemStack(Items.IRON_INGOT),
					new ItemStack(Items.REDSTONE)
			).setRegistryName(Reference.MOD_ID, "recipe_player_interface_card"));
		}
		if (Config.enableMotionDetector)
			RECIPES.register(new ShapedOreRecipe(
					MOD_GROUP,
					new ItemStack(ModItems.MOTION_DETECTOR),
					"srs",
					"rer",
					"srs",
					's', "stone",
					'r', "dustRedstone",
					'e', Items.ENDER_PEARL
			).setRegistryName(Reference.MOD_ID, "recipe_motion_detector"));
        if (Config.enableAIChatBox)
            RECIPES.register(new ShapedOreRecipe(
            		MOD_GROUP,
            		new ItemStack(ModBlocks.AI_CHAT_BOX),
					" r ",
					"cbc",
					" s ",
					'r', new ItemStack(Items.REDSTONE),
					'c', new ItemStack(Items.COMPARATOR),
					'b', new ItemStack(ModBlocks.CHAT_BOX),
					's', new ItemStack(Items.SLIME_BALL)
			).setRegistryName(Reference.MOD_ID, "recipe_ai_chat_box"));
        if (Config.enableNanoBots)
        	RECIPES.register(new ShapedOreRecipe(
        			MOD_GROUP,
					new ItemStack(ModItems.NANO_SWARM),
					"rrr",
					"rir",
					"rrr",
					'r', "dustRedstone",
					'i', "ingotIron"
			).setRegistryName(Reference.MOD_ID, "recipe_nano_bots"));
        if (Config.enableTimeSensor)
        	RECIPES.register(new ShapedOreRecipe(
        			MOD_GROUP,
					new ItemStack(ModBlocks.TIME_SENSOR),
					"wrw",
					"wcw",
					"waw",
					'r', "dustRedstone",
					'w', "plankWood",
					'c', new ItemStack(Items.CLOCK),
					'a', GameRegistry.makeItemStack(ModIds.COMPUTERCRAFT + ":cable", 0, 1, "")
			).setRegistryName(Reference.MOD_ID, "recipe_time_sensor"));
	}
}
