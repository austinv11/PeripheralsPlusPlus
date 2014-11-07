package com.austinv11.peripheralsplusplus.utils;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import dan200.computercraft.api.turtle.ITurtleUpgrade;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraftforge.client.event.TextureStitchEvent;

import java.util.ArrayList;
import java.util.List;

public class IconManager {

	public static List<Object> upgrades = new ArrayList<Object>();

	@SubscribeEvent
	public void onPreTextureStitch(TextureStitchEvent.Pre event) {
		registerTextures(event.map);
	}

	private void registerTextures(TextureMap map) {
		for (Object upgrade : upgrades) {
			if (upgrade instanceof IIconNeeded) {
				((IIconNeeded)upgrade).registerIcon(map);
			}
		}
	}

	public static interface IIconNeeded {
		public void registerIcon(IIconRegister register);
	}
}
