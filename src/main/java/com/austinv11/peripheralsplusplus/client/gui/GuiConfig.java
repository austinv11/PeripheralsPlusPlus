package com.austinv11.peripheralsplusplus.client.gui;

import com.austinv11.peripheralsplusplus.reference.Reference;
import com.austinv11.peripheralsplusplus.utils.ConfigurationHandler;
import cpw.mods.fml.client.config.GuiConfig;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;

public class GuiConfig extends cpw.mods.fml.client.config.GuiConfig {
	public GuiConfig(GuiScreen guiScreen){
		super(guiScreen, new ConfigElement(ConfigurationHandler.config.getCategory(Configuration.CATEGORY_GENERAL)).getChildElements(), Reference.MOD_ID, false/*requireWorldRestart*/, false/*requireMCRestart*/, cpw.mods.fml.client.config.GuiConfig.getAbridgedConfigPath(ConfigurationHandler.config.toString())/*title*/);
	}
}
