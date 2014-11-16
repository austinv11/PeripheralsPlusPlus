package com.austinv11.peripheralsplusplus.api;

import com.austinv11.peripheralsplusplus.PeripheralsPlusPlus;
import com.austinv11.peripheralsplusplus.api.satellites.upgrades.ISatelliteUpgrade;
import com.austinv11.peripheralsplusplus.api.satellites.upgrades.SatelliteUpgradeType;
import com.austinv11.peripheralsplusplus.init.ModItems;
import com.austinv11.peripheralsplusplus.items.SatelliteUpgradeBase;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;

public class PeripheralsPlusPlusAPI {

	/**
	 * Registers the given satellite upgrade
	 * @param upgrade The upgrade to register
	 */
	public static void registerSatelliteUpgrade(final ISatelliteUpgrade upgrade) {
		PeripheralsPlusPlus.instance.UPGRADE_REGISTY.put(upgrade, (upgrade.getType() == SatelliteUpgradeType.MAIN));
		ModItems.SATELLITE_UPGRADE_REGISTRY.add(new SatelliteUpgradeBase() {
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
	}
}
