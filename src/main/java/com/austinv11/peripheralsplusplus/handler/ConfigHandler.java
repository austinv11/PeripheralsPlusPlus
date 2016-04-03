package com.austinv11.peripheralsplusplus.handler;

import com.austinv11.peripheralsplusplus.reference.Config;
import com.austinv11.peripheralsplusplus.reference.Reference;
import com.austinv11.peripheralsplusplus.util.Logger;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;

public class ConfigHandler {
	public static Configuration config;
	private static final String ENABLE_COMMENT = "Enable/Disable this feature.";

	public static void init(File configFile) {
		if (config == null) {
			config = new Configuration(configFile);
			loadConfig();
		}
	}

	@SubscribeEvent
	public void onConfigChange(ConfigChangedEvent.OnConfigChangedEvent event) {
		if (event.modID.equals(Reference.MOD_ID)) {
			loadConfig();
		}
	}

	private static void loadConfig() {
		try {
			config.load();
			config.addCustomCategoryComment("Blocks", "");
			config.addCustomCategoryComment("Items", "");
			config.addCustomCategoryComment("Misc", "");

			Config.enableChatBox = config.getBoolean("enableChatBox", "Blocks", true, ENABLE_COMMENT);
			Config.enableEnvScanner = config.getBoolean("enableEnvScanner", "Blocks", true, ENABLE_COMMENT);
			Config.enableOreDict = config.getBoolean("enableOreDict", "Blocks", true, ENABLE_COMMENT);
			Config.enablePlayerSensor = config.getBoolean("enablePlayerSensor", "Blocks", true, ENABLE_COMMENT);
			Config.enableSorter = config.getBoolean("enableSensor", "Blocks", true, ENABLE_COMMENT);
			Config.enableIronNoteBlock = config.getBoolean("enableIronNoteBlock", "Blocks", true, ENABLE_COMMENT);

			Config.commandDiscriminator = config.getString("commandDiscriminator", "Misc", "\\", "The character the chat messages must start with the determine if it is a command for Chat Boxes.");
			Config.chatBoxMaxRange = config.get("Misc", "chatBoxMaxRange", 256, "Max say/tell range for the Chat Box.").getInt();

			Config.playerSensorMaxRange = config.get("Misc", "playerSensorMaxRange", "Max searching range for the Player Sensor").getInt();

			Config.enableInterfacePermissions = config.getBoolean("Misc", "enableInterfacePermissions", true, "Enable the requirement of permissions for the player interface.");
		} catch (Exception e) {
			Logger.error(e);
		} finally {
			if (config.hasChanged()) {
				config.save();
			}
		}
	}
}
