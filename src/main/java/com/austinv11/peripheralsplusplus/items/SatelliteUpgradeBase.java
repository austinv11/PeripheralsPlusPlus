package com.austinv11.peripheralsplusplus.items;

import com.austinv11.peripheralsplusplus.api.satellites.upgrades.ISatelliteUpgrade;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;

public abstract class SatelliteUpgradeBase extends PPPItem{

	public SatelliteUpgradeBase() {
		super();
		this.setUnlocalizedName("satelliteUpgradeBase");
		this.setMaxStackSize(1);
	}

	@Override
	public abstract String getUnlocalizedName();

	@Override
	public abstract String getUnlocalizedName(ItemStack item);

	@Override
	@SideOnly(Side.CLIENT)
	public abstract void registerIcons(IIconRegister register);

	public abstract ISatelliteUpgrade getUpgrade();
}
