package com.austinv11.peripheralsplusplus.utils;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraftforge.client.event.TextureStitchEvent;

import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class IconManager {

	public static List<IIconNeeded> upgrades = new ArrayList<IIconNeeded>();

	@SubscribeEvent
	public void onPreTextureStitch(TextureStitchEvent.Pre event) {
		registerTextures(event.map);
	}

	private void registerTextures(TextureMap map) {
		for (IIconNeeded upgrade : upgrades) {
			upgrade.registerIcon(map);
		}
	}

	public static interface IIconNeeded {
		public void registerIcon(IIconRegister register);
	}
}
