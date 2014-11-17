package com.austinv11.peripheralsplusplus.api;

import com.austinv11.peripheralsplusplus.PeripheralsPlusPlus;
import com.austinv11.peripheralsplusplus.api.satellites.upgrades.ISatelliteUpgrade;
import com.austinv11.peripheralsplusplus.items.SatelliteUpgradeBase;
import com.austinv11.peripheralsplusplus.reference.Reference;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class PeripheralsPlusPlusAPI {

	/**
	 * Registers the given satellite upgrade
	 * @param upgrade The upgrade to register
	 */
	public static void registerSatelliteUpgrade(final ISatelliteUpgrade upgrade) {
		PeripheralsPlusPlus.instance.UPGRADE_REGISTY.add(upgrade.getUpgradeID(), upgrade);
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

	public static Item getItemFromUpgrade(ISatelliteUpgrade upgrade) {
		return GameRegistry.findItem(Reference.MOD_ID, upgrade.getName());
	}
}
