package com.austinv11.peripheralsplusplus.items;

import com.austinv11.peripheralsplusplus.api.satellites.upgrades.ISatelliteUpgrade;
import com.austinv11.peripheralsplusplus.api.satellites.upgrades.SatelliteUpgradeType;
import com.austinv11.peripheralsplusplus.reference.Reference;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.List;

public abstract class SatelliteUpgradeBase extends ItemPPP {

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

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack p_77624_1_, EntityPlayer p_77624_2_, List p_77624_3_, boolean p_77624_4_) {
		if (getUpgrade().getType() == SatelliteUpgradeType.MODIFIER)
			p_77624_3_.add(Reference.Colors.RESET+Reference.Colors.GRAY+"Upgrade wWight: "+String.format("%.2f", getUpgrade().getAddonWeight()*100)+"%");
	}
}
