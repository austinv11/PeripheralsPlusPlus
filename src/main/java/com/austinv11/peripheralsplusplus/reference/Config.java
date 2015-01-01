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

	public static void setWhitelist(int[] dims) {
		for (int i : dims)
			dimWhitelist.add(i);
	}
}
