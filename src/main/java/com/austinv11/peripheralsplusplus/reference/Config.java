package com.austinv11.peripheralsplusplus.reference;

import com.austinv11.collectiveframework.minecraft.config.Description;

@com.austinv11.collectiveframework.minecraft.config.Config(fileName = "PeripheralsPlusPlus.cfg",
		exclude = {"enableAPIs", "ENABLE_CONFIG_MESSAGE", "INSTANCE"})
public class Config {

	public static final Config INSTANCE = new Config();
	
	private static final String ENABLE_CONFIG_MESSAGE = "If disabled, the recipe will be disabled and the current peripherals would cease to work";
	
	@Description(category = "Chatbox", comment = ENABLE_CONFIG_MESSAGE)
	public static boolean enableChatBox = true;
	
	@Description(category = "Chatbox", comment = "Log the Chat Box peripheral's coordinates when it says a message, disabling this allows for 'naming' the chat box")
	public static boolean logCoords = true;
	
	@Description(category = "Chatbox", comment = "Range for the Chat Box peripheral's reading. Negative values indicate infinite")
	public static double readRange = -1.0;
	
	@Description(category = "Chatbox", comment = "Range for the Chat Box peripheral's say/tell function. Negative values indicate infinite")
	public static double sayRange = 64;
	
	@Description(category = "Chatbox", comment = "Maximum number of messages per second a Chat Box peripheral can say")
	public static int sayRate = 1;
	
	@Description(category = "Chatbox", comment = "Allow the Chat Box peripheral to send messages with unlimited vertical distance, but only if so the program chooses")
	public static boolean allowUnlimitedVertical = true;
	
	@Description(category = "Chatbox", comment = "If the character combination is detected at the beginning of a chat message, it will not be shown in chat-like a command and it'll only be accessible by chatboxes, disable it with a space")
	public static String chatboxCommandPrefix = "\\";
	
	@Description(category = "Player Sensor", comment = ENABLE_CONFIG_MESSAGE)
	public static boolean enablePlayerSensor = true;
	
	@Description(category = "Player Sensor", comment = "This enables the getNearbyPlayers and getAllPlayers functions")
	public static boolean additionalMethods = true;
	
	@Description(category = "Player Sensor", comment = "The maximum range a player sensor could search for players")
	public static double sensorRange = 64.0;
	
	@Description(category = "RF Turtle Charger", comment = "If disabled, the recipe will be disabled")
	public static boolean enableRFCharger = true;
	
	@Description(category = "RF Turtle Charger", comment = "Amount of RF per turtle fuel value")
	public static int fuelRF = 200;
	
	@Description(category = "Navigational Turtle", comment = ENABLE_CONFIG_MESSAGE)
	public static boolean enableNavigationTurtle = true;
	
	@Description(category = "XP Turtle", comment = ENABLE_CONFIG_MESSAGE)
	public static boolean enableXPTurtle = true;
	
	@Description(category = "Barrel Turtle", comment = ENABLE_CONFIG_MESSAGE)
	public static boolean enableBarrelTurtle = true;
	
	@Description(category = "Ore Dictionary", comment = ENABLE_CONFIG_MESSAGE)
	public static boolean enableOreDictionary = true;
	
	@Description(category = "Ore Dictionary", comment = "If enabled, the Ore Dictionary peripheral will display a chat message with the Ore Dictionary entries - useful for debugging")
	public static boolean oreDictionaryMessage = false;
	
	@Description(category = "Shearing Turtle", comment = ENABLE_CONFIG_MESSAGE)
	public static boolean enableShearTurtle = true;
	
	@Description(category = "Forestry Analyzers", comment = ENABLE_CONFIG_MESSAGE)
	public static boolean enableAnalyzers = true;
	
	@Description(category = "Turtle Teleporters", comment = "If disabled, the recipe will be disabled")
	public static boolean enableTurtleTeleporter = true;
	
	@Description(category = "Turtle Teleporters", comment = "Fuel penalty for using a Turtle Teleporter. For example, 1.0 is a 0% penalty, or 2.0 (default) is a 100% penalty")
	public static double teleporterPenalty = 2.0;
	
	@Description(category = "Environment Scanner", comment = ENABLE_CONFIG_MESSAGE)
	public static boolean enableEnvironmentScanner = true;
	
	@Description(category = "Feeder Turtle", comment = ENABLE_CONFIG_MESSAGE)
	public static boolean enableFeederTurtle = true;
	
	@Description(category = "Villagers", comment = "Whether to enable villagers from this mod")
	public static boolean enableVillagers = true;
	
	@Description(category = "RedPower-Like Turtles", comment = ENABLE_CONFIG_MESSAGE)
	public static boolean enableRedPowerLikeTurtles = true;
	
	@Description(category = "Speaker", comment = ENABLE_CONFIG_MESSAGE)
	public static boolean enableSpeaker = true;
	
	@Description(category = "Speaker", comment = "Range for the Speaker peripheral's say/tell function. Negative values indicate infinite")
	public static double speechRange = 64;
	
	@Description(category = "APIs", comment = "This enables additional apis added by this mod, just note that if this is enabled, the *initial* computer startup is lengthened somewhat")
	public static boolean enableAPIs = true;
	
	@Description(category = "Peripheral Container", comment = ENABLE_CONFIG_MESSAGE)
	public static boolean enablePeripheralContainer = true;
	
	@Description(category = "Peripheral Container", comment = "The maximum number of peripherals the container is allows to contain")
	public static int maxNumberOfPeripherals = 6;
	
	@Description(category = "ME Bridge", comment = ENABLE_CONFIG_MESSAGE)
	public static boolean enableMEBridge = true;
	
	@Description(category = "Tank Turtle", comment = ENABLE_CONFIG_MESSAGE)
	public static boolean enableTankTurtle = true;
	
	@Description(category = "Tank Turtle", comment = "The maximum mB that the Thirsty Turtle can store internally")
	public static int maxNumberOfMillibuckets = 10000;
	
	@Description(category = "Smart Helmet", comment = ENABLE_CONFIG_MESSAGE)
	public static boolean enableSmartHelmet = true;
	
	@Description(category = "Sign Reading Turtle", comment = ENABLE_CONFIG_MESSAGE)
	public static boolean enableReaderTurtle = true;
	
	@Description(category = "Gardening Turtle", comment = ENABLE_CONFIG_MESSAGE)
	public static boolean enableGardeningTurtle = true;
	
	@Description(category = "Ridable Turtle", comment = ENABLE_CONFIG_MESSAGE)
	public static boolean enableRidableTurtle = true;
	
	@Description(category = "Ridable Turtle", comment = "The amount of fuel used after each turtle movement")
	public static int fuelPerTurtleMovement = 1;
	
	@Description(category = "Nano Bots", comment = ENABLE_CONFIG_MESSAGE)
	public static boolean enableNanoBots = true;
	
	@Description(category = "Nano Bots", comment = "The amount of instructions allowed per nano swarm used on an entity, negative denotes infinite")
	public static int numberOfInstructions = 8;
	
	@Description(category = "Nano Bots", comment = "The amount of seconds before a key is 'released', negative denotes infinite")
	public static double secondsBeforeReversal = 5.0;
	
	@Description(category = "Flinging Turtle", comment = ENABLE_CONFIG_MESSAGE)
	public static boolean enableFlingingTurtle = true;

	@Description(category = "Time Sensor", comment = ENABLE_CONFIG_MESSAGE)
	public static boolean enableTimeSensor = true;
	
	@Description(category = "Chunky Turtle", comment = ENABLE_CONFIG_MESSAGE)
	public static boolean enableChunkyTurtle = true;
	
	@Description(category = "Chunky Turtle", comment = "Radius of chunks that will be loaded. 0 for 1x1, 1 for 3x3, 2 for 5x5, etc. The maximum allowed by the default chunkloading settings is 5x5, edit config/forgeChunkLoading.cfg to change those")
	public static int chunkLoadingRadius = 1;
	
	@Description(category = "Interactive Sorter", comment = ENABLE_CONFIG_MESSAGE)
	public static boolean enableInteractiveSorter = true;
	
	@Description(category = "Resupply Station", comment = ENABLE_CONFIG_MESSAGE)
	public static boolean enableResupplyStation = true;

    @Description(category = "Player Interface", comment = ENABLE_CONFIG_MESSAGE)
    public static boolean enablePlayerInterface = true;
	
	@Description(category = "Motion Detector", comment = ENABLE_CONFIG_MESSAGE)
	public static boolean enableMotionDetector = true;

    @Description(category = "AI Chat Box", comment = ENABLE_CONFIG_MESSAGE)
    public static boolean enableAIChatBox = true;

    @Description(category = "Player Interface", comment = "If disabled, the peripheral will require no permission cards to function and will be allowed to access all inventories regardless of permissions.")
    public static boolean enableInterfacePermissions = true;

    @Description(category = "AI Chat Box", comment = "Cleverbot API key. This needs to be set (on the server if not playing single player) to use the service.")
	public static String cleverbotApiKey = "";
}
