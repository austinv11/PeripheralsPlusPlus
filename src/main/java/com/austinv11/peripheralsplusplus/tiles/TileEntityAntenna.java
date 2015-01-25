package com.austinv11.peripheralsplusplus.tiles;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityAntenna extends TileEntity {

	public static String publicName = "antenna";
	private  String name = "tileEntityAntenna";

	public TileEntityAntenna() {
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
}
