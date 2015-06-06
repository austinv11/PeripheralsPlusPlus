package com.austinv11.peripheralsplusplus.hooks;

import cpw.mods.fml.common.Loader;
import dan200.computercraft.api.media.IMediaProvider;
import dan200.computercraft.api.peripheral.IPeripheralProvider;
import dan200.computercraft.api.redstone.IBundledRedstoneProvider;
import dan200.computercraft.api.turtle.ITurtleUpgrade;
import net.minecraft.item.ItemStack;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is the entrance point for any registry-related objects including peripherals and APIs 
 */
public class ComputerCraftRegistry {
	
	public static HashMap<Integer, IPocketComputerUpgrade> pocketUpgrades = new HashMap<Integer, IPocketComputerUpgrade>();
	public static HashMap<ItemStack, Integer> craftingRecipes = new HashMap<ItemStack, Integer>();
	private static Field peripheralProviders;
	private static Field bundledRedstoneProviders;
	private static Field mediaProviders;
	private static Field turtleUpgrades;
	private static Object proxy;
	
	public static List<IPeripheralProvider> getPeripheralProviders() throws ComputerCraftNotFoundException {
		checkCC();
		if (peripheralProviders == null)
			try {
				peripheralProviders = Class.forName("dan200.computercraft.ComputerCraft").getField("peripheralProviders");
				peripheralProviders.setAccessible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		try {
			return (List<IPeripheralProvider>) peripheralProviders.get(null);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return new ArrayList<IPeripheralProvider>();
	}
	
	public static List<IBundledRedstoneProvider> getBundledRedstoneProviders() throws ComputerCraftNotFoundException {
		checkCC();
		if (bundledRedstoneProviders == null)
			try {
				bundledRedstoneProviders = Class.forName("dan200.computercraft.ComputerCraft").getField("bundledRedstoneProviders");
				bundledRedstoneProviders.setAccessible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		try {
			return (List<IBundledRedstoneProvider>) bundledRedstoneProviders.get(null);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return new ArrayList<IBundledRedstoneProvider>();
	}
	
	public static List<IMediaProvider> getMediaProviders() throws ComputerCraftNotFoundException {
		checkCC();
		if (mediaProviders == null)
			try {
				mediaProviders = Class.forName("dan200.computercraft.ComputerCraft").getField("mediaProviders");
				mediaProviders.setAccessible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		try {
			return (List<IMediaProvider>) mediaProviders.get(null);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return new ArrayList<IMediaProvider>();
	}
	
	public static Map<Integer, ITurtleUpgrade> getTurtleUpgrades() throws ComputerCraftNotFoundException {
		checkCC();
		if (turtleUpgrades == null)
			try {
				turtleUpgrades = Class.forName("dan200.computercraft.shared.proxy.CCTurtleProxyCommon").getDeclaredField("m_turtleUpgrades");
				turtleUpgrades.setAccessible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		if (proxy == null)
			try {
				proxy = Class.forName("dan200.computercraft.ComputerCraft").getField("turtleProxy").get(null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		try {
			return (Map<Integer, ITurtleUpgrade>) turtleUpgrades.get(proxy);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return new HashMap<Integer, ITurtleUpgrade>();
	}
	
	/**
	 * Use this to register pocket computer upgrades
	 * @param upgrade The upgrade
	 * @throws ComputerCraftNotFoundException
	 * @throws InvalidUpgradeIDException
	 */
	public static void registerPocketUpgrade(IPocketComputerUpgrade upgrade) throws ComputerCraftNotFoundException, InvalidUpgradeIDException {
		checkCC();
		if (upgrade.getUpgradeID() == 1 || pocketUpgrades.containsKey(upgrade.getUpgradeID()))
			throw new InvalidUpgradeIDException(upgrade);
		pocketUpgrades.put(upgrade.getUpgradeID(), upgrade);
		craftingRecipes.put(upgrade.getCraftingItem(), upgrade.getUpgradeID());
	}
	
	private static void checkCC() throws ComputerCraftNotFoundException {
		if (!Loader.isModLoaded("ComputerCraft"))
			throw new ComputerCraftNotFoundException();
	}
}
