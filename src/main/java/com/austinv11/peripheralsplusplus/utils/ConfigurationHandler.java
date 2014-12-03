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
			boolean enableChatBox = config.get("Chatbox", "enableChatBox", true, "If disabled, the recipe will be disabled and the current peripherals would cease to work").getBoolean(true);
			boolean logCoords = config.get("Chatbox", "logCoords", true, "Log the Chat Box peripheral's coordinates when it says a message, disabling this allows for 'naming' the chat box").getBoolean(true);
			double readRange = config.get("Chatbox", "readRange", -1.0, "Range for the Chat Box peripheral's reading. Negative values indicate infinite").getDouble(-1.0);
			double sayRange = config.get("Chatbox", "sayRange", 64.0, "Range for the Chat Box peripheral's say/tell function. Negative values indicate infinite").getDouble(64.0);
			int sayRate = config.get("Chatbox", "sayRate", 1, "Maximum number of messages per second a Chat Box peripheral can say").getInt(1);
			boolean allowUnlimitedVertical = config.get("Chatbox", "allowUnlimitedVertical", true, "Allow the Chat Box peripheral to send messages with unlimited vertical distance, but only if so the program chooses").getBoolean(true);
			boolean enablePlayerSensor = config.get("Player Sensor", "enablePlayerSensor", true, "If disabled, the recipe will be disabled and the current peripherals would cease to work").getBoolean(true);
			boolean additionalMethods = config.get("Player Sensor", "additionalMethods", true, "This enables the getNearbyPlayers and getAllPlayers functions").getBoolean(true);
			double sensorRange = config.get("Player Sensor", "sensorRange", 64.0, "The maximum range a player sensor could search for players").getDouble(64.0);
			boolean enableRFCharger = config.get("Turtle Chargers", "enableRFCharger", true, "If disabled, the recipe will be disabled").getBoolean(true);
			int fuelRF = config.get("Turtle Chargers", "fuelRF", 200, "Amount of RF per turtle fuel value").getInt(200);
			boolean enableNavigationTurtle = config.get("Navigational Turtle", "enableNavigationTurtle", true, "If disabled, the recipe will be disabled and the current peripherals would cease to work").getBoolean(true);
			boolean enableXPTurtle = config.get("XP Turtle", "enableXPTurtle", true, "If disabled, the recipe will be disabled and the current peripherals would cease to work").getBoolean(true);
			boolean enableBarrelTurtle = config.get("Barrel Turtle", "enableBarrelTurtle", true, "If disabled, the recipe will be disabled and the current peripherals would cease to work").getBoolean(true);
			boolean enableOreDictionary = config.get("OreDictionary Block", "enableOreDictionary", true, "If disabled, the recipe will be disabled and the current peripherals would cease to work").getBoolean(true);
			boolean oreDictionaryMessage = config.get("OreDictionary Block", "oreDictionaryMessage", false, "If enabled, the Ore Dictionary peripheral will display a chat message with the Ore Dictionary entries - useful for debugging").getBoolean(false);
			boolean enableShearTurtle = config.get("Shearing Turtle", "enableShearTurtle", true, "If disabled, the recipe will be disabled and the current peripherals would cease to work").getBoolean(true);
			boolean enableAnalyzers = config.get("Forestry Analyzers", "enableAnalyzers", true, "If disabled, the recipe will be disabled and the current peripherals would cease to work").getBoolean(true);
			boolean enableTurtleTeleporter = config.get("Turtle Teleporters", "enableTurtleTeleporter", true, "If disabled, the recipe will be disabled").getBoolean(true);
			double teleporterPenalty = config.get("Turtle Teleporters", "teleporterPenalty", 2.0, "Fuel penalty for using a Turtle Teleporter. For example, 1.0 is a 0% penalty, or 2.0 (default) is a 100% penalty").getDouble(2.0);
			boolean enableEnvironmentScanner = config.get("Environment Scanner", "enableEnvironmentScanner", true, "If disabled, the recipe will be disabled and the current peripherals would cease to work").getBoolean(true);
			boolean enableFeederTurtle = config.get("Feeder Turtle", "enableFeederTurtle", true, "If disabled, the recipe will be disabled and the current peripherals would cease to work").getBoolean(true);
			boolean enableSatellites = config.get("Satellites", "enableSatellites", true, "If disabled, the recipes will be disabled and the current peripherals would cease to work").getBoolean(true);
			int[] dims = config.get("Satellites", "dimWhitelist", new int[] {0}, "Whitelist for dimensions that could have a satellite launch in, input dimension ids (e.g. 0 is the overworld").getIntList();
			boolean enableVillagers = config.get("Villagers", "enableVillagers", true, "Whether to enable villagers from this mod").getBoolean(true);
			boolean enableProjectRedTurtles = config.get("Project Red Turtles", "enableProjectRedTurtles", true, "If disabled, the recipes will be disabled and the current peripherals would cease to work").getBoolean(true);
			reSyncConfig(enableChatBox, logCoords, readRange, sayRange, sayRate, allowUnlimitedVertical,
					enablePlayerSensor, additionalMethods, sensorRange, enableRFCharger, fuelRF, enableNavigationTurtle,
					enableXPTurtle, enableBarrelTurtle, enableOreDictionary, oreDictionaryMessage, enableShearTurtle,
					enableAnalyzers, enableTurtleTeleporter, teleporterPenalty, enableEnvironmentScanner, enableFeederTurtle,
					enableSatellites, dims, enableVillagers, enableProjectRedTurtles);
		}catch (Exception e){//Log exception
			Logger.warn("Config exception!");
			Logger.warn(e.getStackTrace());
		}finally {//Save
			if (config.hasChanged()) {
				config.save();
			}
		}
	}

	private static void reSyncConfig(boolean v0, boolean v1, double v2, double v3, int v4, boolean v5, boolean v6, boolean v7, double v8, boolean v9,
									 int v10, boolean v11, boolean v12, boolean v13, boolean v14, boolean v15, boolean v16, boolean v17,
									 boolean v18, double v19, boolean v20, boolean v21, boolean v22, int[] v23, boolean v24, boolean v25){
		Config.enableChatBox = v0;
		Config.logCoords = v1;
		Config.readRange = v2;
		Config.sayRange = v3;
		Config.sayRate = v4;
		Config.allowUnlimitedVertical = v5;
		Config.enablePlayerSensor = v6;
		Config.additionalMethods = v7;
		Config.sensorRange = v8;
		Config.enableRFCharger = v9;
		Config.fuelRF = v10;
		Config.enableNavigationTurtle = v11;
		Config.enableXPTurtle = v12;
		Config.enableBarrelTurtle = v13;
		Config.enableOreDictionary = v14;
		Config.oreDictionaryMessage = v15;
		Config.enableShearTurtle = v16;
		Config.enableAnalyzers = v17;
		Config.enableTurtleTeleporter = v18;
		Config.teleporterPenalty = v19;
		Config.enableEnvironmentScanner = v20;
		Config.enableFeederTurtle = v21;
		Config.enableSatellites = v22;
		Config.setWhitelist(v23);
		Config.enableVillagers = v24;
		Config.enableProjectRedTurtles = v25;
	}
}
