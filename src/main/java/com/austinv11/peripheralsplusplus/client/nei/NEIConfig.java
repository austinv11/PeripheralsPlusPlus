package com.austinv11.peripheralsplusplus.client.nei;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import com.austinv11.peripheralsplusplus.creativetab.PPPCreativeTab;
import com.austinv11.peripheralsplusplus.reference.Reference;
import cpw.mods.fml.common.registry.GameRegistry;
import dan200.computercraft.api.turtle.ITurtleUpgrade;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;

public class NEIConfig implements IConfigureNEI{
	
	@Override
	public void loadConfig() {
		List<ITurtleUpgrade> list = PPPCreativeTab.upgrades;
		ItemStack base = GameRegistry.findItemStack("ComputerCraft", "CC-TurtleExpanded", 1);
		if (base != null) {
			for (ITurtleUpgrade upgrade : upgrades) {
				ItemStack upg1 = base.copy();
				upg1.stackTagCompound = new NBTTagCompound();
				upg1.stackTagCompound.setShort("leftUpgrade", (short) upgrade.getUpgradeID());
				API.addItemListEntry(upg1);
			}
		}
		ItemStack base2 = GameRegistry.findItemStack("ComputerCraft", "CC-TurtleAdvanced", 1);
		if (base2 == null)
			return;
		for (ITurtleUpgrade upgrade : list) {
			ItemStack upg1 = base2.copy();
			upg1.stackTagCompound = new NBTTagCompound();
			upg1.stackTagCompound.setShort("leftUpgrade", (short) upgrade.getUpgradeID());
			API.addItemListEntry(upg1);
		}
	}

	@Override
	public String getName() {
		return Reference.MOD_NAME;
	}

	@Override
	public String getVersion() {
		return "1.0.0";
	}
}
