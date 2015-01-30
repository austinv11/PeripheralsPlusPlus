package com.austinv11.peripheralsplusplus.tiles;

import com.austinv11.peripheralsplusplus.reference.Config;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.ITurtleAccess;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntityEnvironmentScanner extends MountedTileEntity {

	public static String publicName = "environmentScanner";
	private String name = "tileEntityEnvironmentScanner";
	private boolean isRaining = false;
	private String biome;
	private String temp;
	private boolean isSnow;
	private ITurtleAccess turtle;

	public TileEntityEnvironmentScanner() {
		super();
	}

	public TileEntityEnvironmentScanner(ITurtleAccess turtle) {
		this.turtle = turtle;
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
		if (worldObj != null) {
			isRaining = worldObj.isRaining();
			biome = worldObj.getBiomeGenForCoords(xCoord, zCoord).biomeName;
			temp = worldObj.getBiomeGenForCoords(xCoord, zCoord).getTempCategory().name();
			isSnow = worldObj.getBiomeGenForCoords(xCoord, zCoord).getEnableSnow();
		}
		if (turtle != null) {
			this.setWorldObj(turtle.getWorld());
			this.xCoord = turtle.getPosition().posX;
			this.zCoord = turtle.getPosition().posZ;
		}
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
	public boolean equals(IPeripheral other) {
		return (this == other);
	}
}
