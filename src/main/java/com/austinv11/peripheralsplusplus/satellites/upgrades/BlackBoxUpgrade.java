package com.austinv11.peripheralsplusplus.satellites.upgrades;

import com.austinv11.peripheralsplusplus.api.satellites.ISatellite;
import com.austinv11.peripheralsplusplus.api.satellites.upgrades.ISatelliteUpgrade;
import com.austinv11.peripheralsplusplus.api.satellites.upgrades.SatelliteUpgradeType;
import com.austinv11.peripheralsplusplus.init.ModItems;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class BlackBoxUpgrade implements ISatelliteUpgrade {
	
	@Override
	public ResourceLocation getIcon() {
		return new ResourceLocation("t");
	}

	@Override
	public ItemStack getRecipe() {
		return new ItemStack(ModItems.blackBox);
	}

	@Override
	public void update(ISatellite satellite) {

	}

	@Override
	public SatelliteUpgradeType getType() {
		return SatelliteUpgradeType.MODIFIER;
	}

	@Override
	public int getUpgradeID() {
		return 0;
	}

	@Override
	public float getAddonWeight() {
		return 0.15F;
	}

	@Override
	public String getUnlocalisedName() {
		return "blackbox";
	}

	@Override
	public String[] getMethodNames() {
		return new String[0];
	}

	@Override
	public Object[] callMethod(ISatellite satellite, IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws LuaException, InterruptedException {
		return new Object[0];
	}

	@Override
	public void onConnect(ISatellite satellite, IComputerAccess computer) {

	}

	@Override
	public void onDisconnect(ISatellite satellite, IComputerAccess computer) {

	}
}
