package com.austinv11.peripheralsplusplus.init;

import com.austinv11.peripheralsplusplus.blocks.BlockTeleporter;
import com.austinv11.peripheralsplusplus.items.*;
import com.austinv11.peripheralsplusplus.reference.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.Locale;

import static net.minecraftforge.fml.common.registry.ForgeRegistries.ITEMS;

@GameRegistry.ObjectHolder(Reference.MOD_ID)
public class ModItems {

	public static final ItemPPP FEEDER_UPGRADE = new ItemFeederUpgrade();
	public static final ItemPPP TANK = new ItemTank();
	public static final Item SMART_HELMET = new ItemSmartHelmet();
	public static final ItemPPP NANO_SWARM = new ItemNanoSwarm();
	public static final ItemPPP CHUNK_LOADER_UPGRADE = new ItemChunkLoaderUpgrade();
    public static final ItemPPP PERM_CARD = new ItemPermissionsCard();
	public static final ItemPPP RESUPPLY_UPGRADE = new ItemResupplyUpgrade();
	public static final ItemPPP MOTION_DETECTOR = new ItemMotionDetector();
	public static final Item TURTLE = new ItemBlockTurtle(ModBlocks.TURTLE);
	public static final Item CHAT_BOX = getItemBlock(ModBlocks.CHAT_BOX);
	public static final Item PLAYER_SENSOR = getItemBlock(ModBlocks.PLAYER_SENSOR);
	public static final Item ORE_DICTIONARY = getItemBlock(ModBlocks.ORE_DICTIONARY);
	public static final Item ENVIRONMENT_SCANNER = getItemBlock(ModBlocks.ENVIRONMENT_SCANNER);
	public static final Item SPEAKER = getItemBlock(ModBlocks.SPEAKER);
	public static final Item RESUPPLY_STATION = getItemBlock(ModBlocks.RESUPPLY_STATION);
	public static final Item ANTENNA = getItemBlock(ModBlocks.ANTENNA);
	public static final Item RF_CHARGER = getItemBlock(ModBlocks.RF_CHARGER);
	public static final Item AI_CHAT_BOX = getItemBlock(ModBlocks.AI_CHAT_BOX);
	public static final Item ANALYZER_BEE = getItemBlock(ModBlocks.ANALYZER_BEE);
	public static final Item ANALYZER_TREE = getItemBlock(ModBlocks.ANALYZER_TREE);
	public static final Item ANALYZER_BUTTERFLY = getItemBlock(ModBlocks.ANALYZER_BUTTERFLY);
	public static final Item TELEPORTER = new ItemTeleporter(ModBlocks.TELEPORTER);
	public static final Item PERIPHERAL_CONTAINER = getItemBlock(ModBlocks.PERIPHERAL_CONTAINER);
	public static final Item ME_BRIDGE = getItemBlock(ModBlocks.ME_BRIDGE);
	public static final Item TIME_SENSOR = getItemBlock(ModBlocks.TIME_SENSOR);
	public static final Item INTERACTIVE_SORTER = getItemBlock(ModBlocks.INTERACTIVE_SORTER);
	public static final Item PLAYER_INTERFACE = getItemBlock(ModBlocks.PLAYER_INTERFACE);

	public static void register() {
		ITEMS.register(FEEDER_UPGRADE);
		ITEMS.register(TANK);
		ITEMS.register(SMART_HELMET);
		ITEMS.register(NANO_SWARM);
		ITEMS.register(CHUNK_LOADER_UPGRADE);
		ITEMS.register(PERM_CARD);
		ITEMS.register(RESUPPLY_UPGRADE);
		ITEMS.register(MOTION_DETECTOR);
		ITEMS.register(TURTLE);
		ITEMS.register(CHAT_BOX);
		ITEMS.register(PLAYER_SENSOR);
		ITEMS.register(ORE_DICTIONARY);
		ITEMS.register(ENVIRONMENT_SCANNER);
		ITEMS.register(SPEAKER);
		ITEMS.register(RESUPPLY_STATION);
		ITEMS.register(ANTENNA);
		ITEMS.register(RF_CHARGER);
		ITEMS.register(AI_CHAT_BOX);
		ITEMS.register(ANALYZER_BEE);
		ITEMS.register(ANALYZER_TREE);
		ITEMS.register(ANALYZER_BUTTERFLY);
		ITEMS.register(TELEPORTER);
		ITEMS.register(PERIPHERAL_CONTAINER);
		ITEMS.register(ME_BRIDGE);
		ITEMS.register(TIME_SENSOR);
		ITEMS.register(INTERACTIVE_SORTER);
		ITEMS.register(PLAYER_INTERFACE);
	}

	public static void registerModels() {
		registerModel(FEEDER_UPGRADE);
		registerModel(TANK);
		registerModel(SMART_HELMET);
		registerModel(NANO_SWARM);
		registerModel(CHUNK_LOADER_UPGRADE);
		registerModel(PERM_CARD);
		registerModel(RESUPPLY_UPGRADE);
		registerModel(MOTION_DETECTOR);
		registerModel(TURTLE);
		registerModel(CHAT_BOX);
		registerModel(PLAYER_SENSOR);
		registerModel(ORE_DICTIONARY);
		registerModel(ENVIRONMENT_SCANNER);
		registerModel(SPEAKER);
		registerModel(RESUPPLY_STATION);
		registerModel(ANTENNA);
		registerModel(RF_CHARGER);
		registerModel(AI_CHAT_BOX);
		registerModel(ANALYZER_BEE);
		registerModel(ANALYZER_TREE);
		registerModel(ANALYZER_BUTTERFLY);
		registerModel(PERIPHERAL_CONTAINER);
		registerModel(ME_BRIDGE);
		registerModel(TIME_SENSOR);
		registerModel(TELEPORTER, BlockTeleporter.TIER);
		registerModel(INTERACTIVE_SORTER);
		registerModel(PLAYER_INTERFACE);
	}

	private static void registerModel(Item item, IProperty property) {
		int registryIndex = 0;
		for (Object propertyValue : property.getAllowedValues()) {
			ModelLoader.setCustomModelResourceLocation(TELEPORTER, registryIndex++,
					new ModelResourceLocation(item.getRegistryName(),
							String.format("%s=%s",
									property.getName().toLowerCase(Locale.US),
									String.valueOf(propertyValue)))
			);
		}
	}

	private static void registerModel(Item item) {
		ModelLoader.setCustomModelResourceLocation(item, 0,
				new ModelResourceLocation(item.getRegistryName(), "inventory"));
	}

	private static Item getItemBlock(Block block) {
		return new ItemBlock(block).setRegistryName(block.getRegistryName())
				.setUnlocalizedName(block.getUnlocalizedName());
	}
}
