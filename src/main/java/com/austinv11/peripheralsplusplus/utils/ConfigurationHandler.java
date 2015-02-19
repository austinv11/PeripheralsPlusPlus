package com.austinv11.peripheralsplusplus.utils;

import com.austinv11.peripheralsplusplus.reference.Config;
import com.austinv11.peripheralsplusplus.reference.Reference;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class ConfigurationHandler {

	public static Configuration config;

	public static void init(File configFile){
		if (config == null) {
			config = new Configuration(configFile);
		}
		loadConfiguration();
	}

	@SubscribeEvent
	public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
		if (event.modID.equalsIgnoreCase(Reference.MOD_ID)) {
			loadConfiguration();
		}
	}

	private static void loadConfiguration(){
		try{//Load & read properties
			config.load();
			Config.enableChatBox = config.get("Chatbox", "enableChatBox", true, "If disabled, the recipe will be disabled and the current peripherals would cease to work").getBoolean(true);
			Config.logCoords = config.get("Chatbox", "logCoords", true, "Log the Chat Box peripheral's coordinates when it says a message, disabling this allows for 'naming' the chat box").getBoolean(true);
			Config.readRange = config.get("Chatbox", "readRange", -1.0, "Range for the Chat Box peripheral's reading. Negative values indicate infinite").getDouble(-1.0);
			Config.sayRange = config.get("Chatbox", "sayRange", 64.0, "Range for the Chat Box peripheral's say/tell function. Negative values indicate infinite").getDouble(64.0);
			Config.sayRate = config.get("Chatbox", "sayRate", 1, "Maximum number of messages per second a Chat Box peripheral can say").getInt(1);
			Config.allowUnlimitedVertical = config.get("Chatbox", "allowUnlimitedVertical", true, "Allow the Chat Box peripheral to send messages with unlimited vertical distance, but only if so the program chooses").getBoolean(true);
			Config.enablePlayerSensor = config.get("Player Sensor", "enablePlayerSensor", true, "If disabled, the recipe will be disabled and the current peripherals would cease to work").getBoolean(true);
			Config.additionalMethods = config.get("Player Sensor", "additionalMethods", true, "This enables the getNearbyPlayers and getAllPlayers functions").getBoolean(true);
			Config.sensorRange = config.get("Player Sensor", "sensorRange", 64.0, "The maximum range a player sensor could search for players").getDouble(64.0);
			Config.enableRFCharger = config.get("Turtle Chargers", "enableRFCharger", true, "If disabled, the recipe will be disabled").getBoolean(true);
			Config.fuelRF = config.get("Turtle Chargers", "fuelRF", 200, "Amount of RF per turtle fuel value").getInt(200);
			Config.enableNavigationTurtle = config.get("Navigational Turtle", "enableNavigationTurtle", true, "If disabled, the recipe will be disabled and the current peripherals would cease to work").getBoolean(true);
			Config.enableXPTurtle = config.get("XP Turtle", "enableXPTurtle", true, "If disabled, the recipe will be disabled and the current peripherals would cease to work").getBoolean(true);
			Config.enableBarrelTurtle = config.get("Barrel Turtle", "enableBarrelTurtle", true, "If disabled, the recipe will be disabled and the current peripherals would cease to work").getBoolean(true);
			Config.enableOreDictionary = config.get("OreDictionary Block", "enableOreDictionary", true, "If disabled, the recipe will be disabled and the current peripherals would cease to work").getBoolean(true);
			Config.oreDictionaryMessage = config.get("OreDictionary Block", "oreDictionaryMessage", false, "If enabled, the Ore Dictionary peripheral will display a chat message with the Ore Dictionary entries - useful for debugging").getBoolean(false);
			Config.enableShearTurtle = config.get("Shearing Turtle", "enableShearTurtle", true, "If disabled, the recipe will be disabled and the current peripherals would cease to work").getBoolean(true);
			Config.enableAnalyzers = config.get("Forestry Analyzers", "enableAnalyzers", true, "If disabled, the recipe will be disabled and the current peripherals would cease to work").getBoolean(true);
			Config.enableTurtleTeleporter = config.get("Turtle Teleporters", "enableTurtleTeleporter", true, "If disabled, the recipe will be disabled").getBoolean(true);
			Config.teleporterPenalty = config.get("Turtle Teleporters", "teleporterPenalty", 2.0, "Fuel penalty for using a Turtle Teleporter. For example, 1.0 is a 0% penalty, or 2.0 (default) is a 100% penalty").getDouble(2.0);
			Config.enableEnvironmentScanner = config.get("Environment Scanner", "enableEnvironmentScanner", true, "If disabled, the recipe will be disabled and the current peripherals would cease to work").getBoolean(true);
			Config.enableFeederTurtle = config.get("Feeder Turtle", "enableFeederTurtle", true, "If disabled, the recipe will be disabled and the current peripherals would cease to work").getBoolean(true);
			Config.enableSatellites = config.get("Satellites", "enableSatellites", true, "If disabled, the recipes will be disabled and the current peripherals would cease to work").getBoolean(true);
			Config.setWhitelist(config.get("Satellites", "dimWhitelist", new int[]{0}, "Whitelist for dimensions that could have a satellite launch in, input dimension ids (e.g. 0 is the overworld").getIntList());
			Config.enableVillagers = config.get("Villagers", "enableVillagers", true, "Whether to enable villagers from this mod").getBoolean(true);
			Config.enableProjectRedTurtles = config.get("Project Red Turtles", "enableProjectRedTurtles", true, "If disabled, the recipes will be disabled and the current peripherals would cease to work").getBoolean(true);
			Config.enableSpeaker = config.get("Speaker", "enableSpeaker", true, "If disabled, the recipes will be disabled and the current peripherals would cease to work").getBoolean(true);
			Config.speechRange = config.get("Speaker", "speechRange", 64.0, "Range for the Speaker peripheral's say/tell function. Negative values indicate infinite").getDouble(64.0);
			Config.enableAPIs = config.get("APIs", "enableAPIs", true, "This enables additional apis added by this mod, just note that if this is enabled, the *initial* computer startup is lengthened somewhat").getBoolean(true);
			Config.enablePeripheralContainer = config.get("Peripheral Container", "enablePeripheralContainer", true, "If disabled, the recipes will be disabled and the current peripherals would cease to work").getBoolean(true);
			Config.maxNumberOfPeripherals = config.get("Peripheral Container", "maxNumberOfPeripherals", 6, "The maximum number of peripherals the container is allows to contain").getInt(6);
			Config.enableMEBridge = config.get("ME Bridge", "enableMEBridge", true, "If disabled, the recipes will be disabled and the current peripherals would cease to work").getBoolean(true);
			Config.enableTankTurtle = config.get("Thirsty Turtle", "enableTankTurtle", true, "If disabled, the recipes will be disabled and the current peripherals would cease to work").getBoolean(true);
			Config.maxNumberOfMillibuckets = config.get("Thirsty Turtle", "maxNumberOfMillibuckets", 10000, "The maximimum mB that the Thirsty Turtle can store internally").getInt(10000);
			Config.enableSmartHelmet = config.get("Smart Helmet", "enableSmartHelmet", true, "If disabled, the recipes will be disabled and the current peripherals would cease to work").getBoolean(true);
            Config.noteBlockRange = config.get("NoteBlock", "noteBlockRange", 16.0, "Audible range for the noteblock").getDouble(16.0);
            Config.noteBlockEnabled = config.get("NoteBlock", "noteBlockEnabled", true, "If disabled, crafting recipes will be removed and existing items will stop functioning").getBoolean(true);
			Config.enableReaderTurtle = config.get("Sign Reading Turtle", "enableReaderTurtle", true, "If disabled, the recipe will be disabled and the current peripherals would cease to work").getBoolean();
			Config.enableGardeningTurtle = config.get("Gardening Turtle", "enabelGardeningTurtle", true, "If disabled, the recipe will be disabled the current peripherals would cease to work").getBoolean();
            Config.enableChunkLoading = config.get("Chunk Loading Turtle", "enableChunkLoadingTurtle", true, "If disabled, the recipe will be disabled. The current peripheral would cease to work").getBoolean();
		}catch (Exception e){//Log exception
			Logger.warn("Config exception!");
			Logger.warn(e.getStackTrace());
		}finally {//Save
			if (config.hasChanged()) {
				config.save();
			}
		}
	}
}
