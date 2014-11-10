package com.austinv11.peripheralsplusplus.tiles;

import com.austinv11.peripheralsplusplus.reference.Config;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityEnvironmentScanner extends TileEntity implements IPeripheral {

	public static String publicName = "environmentScanner";
	private String name = "tileEntityEnvironmentScanner";
	private boolean isRaining = false;
	private String biome;
	private String temp;
	private boolean isSnow;

	public TileEntityEnvironmentScanner() {
		super();
	}

	public String getName() {
		return name;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
	}

	@Override
	public void updateEntity() {
		isRaining = worldObj.isRaining();
		biome = worldObj.getBiomeGenForCoords(xCoord,zCoord).biomeName;
		temp = worldObj.getBiomeGenForCoords(xCoord,zCoord).getTempCategory().name();
		isSnow = worldObj.getBiomeGenForCoords(xCoord,zCoord).getEnableSnow();
	}

	@Override
	public String getType() {
		return publicName;
	}

	@Override
	public String[] getMethodNames() {
		return new String[]{"isRaining", "getBiome", "getTemperature", "getTemp", "isSnow"};
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws LuaException, InterruptedException {
		if (!Config.enableEnvironmentScanner)
			throw new LuaException("Environment Scanners have been disabled");
		switch (method) {
			case 0:
				return new Object[]{isRaining};
			case 1:
				return new Object[]{biome};
			case 2:
			case 3:
				return new Object[]{temp};
			case 4:
				return new Object[]{isSnow};
		}
		return new Object[0];
	}

	@Override
	public void attach(IComputerAccess computer) {}

	@Override
	public void detach(IComputerAccess computer) {}

	@Override
	public boolean equals(IPeripheral other) {
		return (this == other);
	}
}
