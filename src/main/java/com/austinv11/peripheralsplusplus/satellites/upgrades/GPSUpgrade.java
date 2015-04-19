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

public class GPSUpgrade implements ISatelliteUpgrade {
	
	@Override
	public ResourceLocation getIcon() {
		return new ResourceLocation("");//TODO
	}
	
	@Override
	public ItemStack getRecipe() {
		return new ItemStack(ModItems.positionalUnit);
	}
	
	@Override
	public boolean doesRecipeRetainNBT() {
		return false;
	}
	
	@Override
	public void update(ISatellite satellite) {}
	
	@Override
	public SatelliteUpgradeType getType() {
		return SatelliteUpgradeType.MAIN;
	}
	
	@Override
	public int getUpgradeID() {
		return 0;
	}
	
	@Override
	public float getAddonWeight() {
		return 0;
	}
	
	@Override
	public String getUnlocalisedName() {
		return "gps";
	}
	
	@Override
	public String[] getMethodNames() {
		return new String[]{"locate"};
	}
	
	@Override
	public Object[] callMethod(ISatellite satellite, IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws LuaException, InterruptedException {
		return new Object[0];
	}
	
	@Override
	public void onConnect(ISatellite satellite, IComputerAccess computer) {}
	
	@Override
	public void onDisconnect(ISatellite satellite, IComputerAccess computer) {}
}
