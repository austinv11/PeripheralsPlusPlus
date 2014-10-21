package com.austinv11.peripheralsplusplus.client.gui;

import com.austinv11.peripheralsplusplus.reference.Reference;
import com.austinv11.peripheralsplusplus.utils.ConfigurationHandler;
import cpw.mods.fml.client.config.GuiConfig;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;

public class ConfigGUI extends GuiConfig{
	public ConfigGUI(GuiScreen guiScreen){
		super(guiScreen/*parent gui*/, new ConfigElement(ConfigurationHandler.config.getCategory(Configuration.CATEGORY_GENERAL)).getChildElements()/*list of elements*/, Reference.MOD_ID/*modid*/, false/*requireWorldRestart*/, false/*requireMCRestart*/, GuiConfig.getAbridgedConfigPath(ConfigurationHandler.config.toString())/*title*/);
	}
}
