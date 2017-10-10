package com.austinv11.peripheralsplusplus.init;

import com.austinv11.peripheralsplusplus.blocks.*;
import com.austinv11.peripheralsplusplus.reference.Reference;
import net.minecraft.block.BlockContainer;
import net.minecraftforge.fml.common.registry.GameRegistry;

import static net.minecraftforge.fml.common.registry.ForgeRegistries.BLOCKS;

@GameRegistry.ObjectHolder(Reference.MOD_ID)
public class ModBlocks {
	public static final BlockPppBase CHAT_BOX = new BlockChatBox();
	public static final BlockPppBase AI_CHAT_BOX = new BlockAIChatBox();
	public static final BlockPppBase PLAYER_SENSOR = new BlockPlayerSensor();
	public static final BlockPppBase RF_CHARGER = new BlockRFCharger();
	public static final BlockPppBase ORE_DICTIONARY = new BlockOreDictionary();
	public static final BlockContainer ANALYZER_BEE = new BlockAnalyzerBee();
	public static final BlockContainer ANALYZER_TREE = new BlockAnalyzerTree();
	public static final BlockContainer ANALYZER_BUTTERFLY = new BlockAnalyzerButterfly();
	public static final BlockTeleporter TELEPORTER = new BlockTeleporter();
	public static final BlockPppBase ENVIRONMENT_SCANNER = new BlockEnvironmentScanner();
	public static final BlockSpeaker SPEAKER = new BlockSpeaker();
	public static final BlockAntenna ANTENNA = new BlockAntenna();
	public static final BlockPppBase PERIPHERAL_CONTAINER = new BlockPeripheralContainer();
	public static final BlockPppBase ME_BRIDGE = new BlockMEBridge();
	public static final BlockTurtle TURTLE = new BlockTurtle();
	public static final BlockPppBase TIME_SENSOR = new BlockTimeSensor();
	public static final BlockContainer INTERACTIVE_SORTER = new BlockInteractiveSorter();
    public static final BlockContainer PLAYER_INTERFACE = new BlockPlayerInterface();
	public static final BlockContainer RESUPPLY_STATION = new BlockResupplyStation();

	public static void register(){
		BLOCKS.register(CHAT_BOX);
		BLOCKS.register(AI_CHAT_BOX);
		BLOCKS.register(PLAYER_SENSOR);
		BLOCKS.register(RF_CHARGER);
		BLOCKS.register(ORE_DICTIONARY);
		BLOCKS.register(ANALYZER_BEE);
		BLOCKS.register(ANALYZER_TREE);
		BLOCKS.register(ANALYZER_BUTTERFLY);
		BLOCKS.register(TELEPORTER);
		BLOCKS.register(ENVIRONMENT_SCANNER);
		BLOCKS.register(SPEAKER);
		BLOCKS.register(ANTENNA);
		BLOCKS.register(PERIPHERAL_CONTAINER);
		BLOCKS.register(ME_BRIDGE);
		BLOCKS.register(TURTLE);
		BLOCKS.register(TIME_SENSOR);
		BLOCKS.register(INTERACTIVE_SORTER);
        BLOCKS.register(PLAYER_INTERFACE);
		BLOCKS.register(RESUPPLY_STATION);
    }
}
