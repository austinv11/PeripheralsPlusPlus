package com.austinv11.peripheralsplusplus.reference;

import java.util.ArrayList;
import java.util.List;

public class Config {
	public static boolean enableChatBox = true;
	public static boolean logCoords = true;
	public static double readRange = -1.0;
	public static double sayRange = 64;
	public static int sayRate = 1;
	public static boolean allowUnlimitedVertical = true;
	public static boolean enablePlayerSensor = true;
	public static boolean additionalMethods = true;
	public static double sensorRange = 64.0;
	public static boolean enableRFCharger = true;
	public static int fuelRF = 200;
	public static boolean enableNavigationTurtle = true;
	public static boolean enableXPTurtle = true;
	public static boolean enableBarrelTurtle = true;
	public static boolean enableOreDictionary = true;
	public static boolean oreDictionaryMessage = false;
	public static boolean enableShearTurtle = true;
	public static boolean enableAnalyzers = true;
	public static boolean enableTurtleTeleporter = true;
	public static double teleporterPenalty = 2.0;
	public static boolean enableEnvironmentScanner = true;
	public static boolean enableFeederTurtle = true;
	public static boolean enableSatellites = true;
	public static List<Integer> dimWhitelist = new ArrayList<Integer>();
	public static boolean enableVillagers = true;
	public static boolean enableProjectRedTurtles = true;
	public static boolean enableSpeaker = true;
	public static double speechRange = 64;
	public static boolean enableAPIs = true;
	public static boolean enablePeripheralContainer = true;
	public static int maxNumberOfPeripherals = 6;
	public static boolean enableMEBridge = true;
	public static boolean enableTankTurtle = true;
	public static int maxNumberOfMillibuckets = 10000;
	public static boolean enableSmartHelmet = true;
    public static double noteBlockRange = 16;
    public static boolean noteBlockEnabled = true;
	public static boolean enableReaderTurtle = true;
	public static boolean enableGardeningTurtle = true;

	public static void setWhitelist(int[] dims) {
		for (int i : dims)
			dimWhitelist.add(i);
	}
}
