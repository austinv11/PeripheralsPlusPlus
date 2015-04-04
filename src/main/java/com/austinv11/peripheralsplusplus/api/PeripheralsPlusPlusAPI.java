package com.austinv11.peripheralsplusplus.api;

import com.austinv11.collectiveframework.minecraft.utils.NBTHelper;
import com.austinv11.peripheralsplusplus.PeripheralsPlusPlus;
import com.austinv11.peripheralsplusplus.api.satellites.upgrades.ISatelliteUpgrade;
import com.austinv11.peripheralsplusplus.items.ItemSatellite;
import com.austinv11.peripheralsplusplus.items.SatelliteUpgradeBase;
import com.austinv11.peripheralsplusplus.reference.Reference;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class PeripheralsPlusPlusAPI {

	/**
	 * Registers the given satellite upgrade
	 * @param upgrade The upgrade to register
	 */
	public static void registerSatelliteUpgrade(final ISatelliteUpgrade upgrade) {
		PeripheralsPlusPlus.UPGRADE_REGISTRY.put(upgrade.getUpgradeID(), upgrade);
		PeripheralsPlusPlus.SATELLITE_UPGRADE_REGISTRY.add(new SatelliteUpgradeBase() {
			@Override
			public String getUnlocalizedName() {
				return upgrade.getUnlocalisedName();
			}

			@Override
			public String getUnlocalizedName(ItemStack item) {
				return upgrade.getUnlocalisedName();
			}

			@Override
			public void registerIcons(IIconRegister register) {
				itemIcon = register.registerIcon(upgrade.getIcon().toString());
			}

			@Override
			public ISatelliteUpgrade getUpgrade() {
				return upgrade;
			}
		});
		PeripheralsPlusPlus.SATELLITE_UPGRADE_ID_REGISTRY.add(upgrade.getUpgradeID());
	}

	/**
	 * Attempts to recover the satellite upgrades from a satellite item itemstack
	 * @param stack The satellite itemstack
	 * @return The upgrades (or null if none are found)
	 */
	public static List<ISatelliteUpgrade> getUpgradesFromItemStack(ItemStack stack) {
		if (!(stack.getItem() instanceof ItemSatellite))
			return null;
		List<ISatelliteUpgrade> upgrades = new ArrayList<ISatelliteUpgrade>();
		int slots = NBTHelper.getInt(stack, "slots");
		for (int i = 0; i < slots; i++) {
			ItemStack upgrade = ItemStack.loadItemStackFromNBT(NBTHelper.getCompoundTag(stack, "slot:"+i));
			if (upgrade != null)
				upgrades.add(getUpgradeFromItem(upgrade.getItem()));
		}
		return upgrades.isEmpty() ? null : upgrades;
	}

	/**
	 * Attempts to find a satellite upgrade via its upgrade id
	 * @param id The upgrade id
	 * @return The upgrade (if found)
	 */
	public static ISatelliteUpgrade getUpgradeFromId(int id) {
		return PeripheralsPlusPlus.UPGRADE_REGISTRY.get(id);
	}

	/**
	 * Returns the item representing a satellite upgrade
	 * @param upgrade The satellite upgrade
	 * @return The item representing the satellite upgrade
	 */
	public static Item getItemFromUpgrade(ISatelliteUpgrade upgrade) {
		return GameRegistry.findItem(Reference.MOD_ID, upgrade.getUnlocalisedName());
	}

	/**
	 * Returns the satellite upgrade that an item represents
	 * @param item The satellite upgrade item
	 * @return The satellite upgrade (or null if no upgrade is found)
	 */
	public static ISatelliteUpgrade getUpgradeFromItem(Item item) {
		return item instanceof SatelliteUpgradeBase ? ((SatelliteUpgradeBase)item).getUpgrade() : null;
	}
}
