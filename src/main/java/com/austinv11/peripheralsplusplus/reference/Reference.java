package com.austinv11.peripheralsplusplus.reference;

public class Reference {
	public static final String MOD_ID = "PeripheralsPlusPlus";
	public static final String MOD_NAME = "Peripherals++";
	public static final String VERSION = "@VERSION@";
	public static final String SERVER_PROXY_CLASS = "com.austinv11.peripheralsplusplus.proxy.CommonProxy";
	public static final String CLIENT_PROXY_CLASS = "com.austinv11.peripheralsplusplus.proxy.ClientProxy";
	public static final String GUI_FACTORY_CLASS = "com.austinv11.peripheralsplusplus.client.gui.GUIFactory";
	public static final int CHAT_BOX_UPGRADE = 101;
	public static final int PLAYER_SENSOR_UPGRADE = 102;
	public static final int COMPASS_UPGRADE = 103;
	public static final int XP_UPGRADE = 104;
	public static final int BARREL_UPGRADE = 105;
	public static final int ORE_DICTIONARY_UPGRADE = 106;
	public static final int SHEAR_UPGRADE = 107;
	public static final int ENVIRONMENT_UPGRADE = 108;
	public static final int FEEDER_UPGRADE = 109;
	public static final int BASE_PROJ_RED_UPGRADE = 110;
	public static final int SPEAKER_UPGRADE = 125;
	public static final int TANK_UPGRADE = 126; //TODO Remember to update the id list on the wiki
    public static final int NOTE_BLOCK_UPGRADE = 127;
	public static final int SIGN_READER_UPGRADE = 128;
	public static final int GARDEN_UPGRADE = 129;
	public static final int RIDABLE_UPGRADE = 130;

	public static enum GUIs {
		ANALYZER,ROCKET,SATELLITE,HELMET;
	}

	public static class Colors {
		public static final String BLACK = "§0";
		public static final String DARK_BLUE = "§1";
		public static final String DARK_GREEN = "§2";
		public static final String DARK_AQUA = "§3";
		public static final String DARK_RED = "§4";
		public static final String DARK_PURPLE = "§5";
		public static final String GOLD = "§6";
		public static final String GRAY = "§7";
		public static final String DARK_GRAY = "§8";
		public static final String BLUE = "§9";
		public static final String GREEN = "§a";
		public static final String AQUA = "§b";
		public static final String RED = "§c";
		public static final String LIGHT_PURPLE = "§d";
		public static final String YELLOW = "§e";
		public static final String WHITE = "§f";
		public static final String MAGIC = "§k";
		public static final String BOLD = "§l";
		public static final String STRIKETHROUGH = "§m";
		public static final String UNDERLINE = "§n";
		public static final String ITALIC = "§o";
		public static final String RESET = "§r";
	}
}
