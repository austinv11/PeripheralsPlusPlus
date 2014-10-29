package com.austinv11.peripheralsplusplus.tiles;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;
import com.austinv11.peripheralsplusplus.reference.Config;
import com.austinv11.peripheralsplusplus.utils.Logger;
import dan200.computercraft.api.turtle.ITurtleAccess;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.List;

public class TileEntityRFCharger extends TileEntity implements IEnergyHandler{
	private EnergyStorage storage = new EnergyStorage(400000);//Leadstone

	public static String publicName = "rfCharger";
	private String name = "tileEntityrfCharger";

	public TileEntityRFCharger() {
		super();
	}

	public String getName() {
		return name;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		storage.readFromNBT(nbttagcompound);
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
		storage.writeToNBT(nbttagcompound);
	}

	@Override
	public void updateEntity() {
		List<ITurtleAccess> turtles = new ArrayList<ITurtleAccess>(6);
		ForgeDirection[] dirs = {ForgeDirection.DOWN, ForgeDirection.EAST, ForgeDirection.NORTH, ForgeDirection.SOUTH, ForgeDirection.UP, ForgeDirection.WEST};
		for (int i = 0; i < 6; i++) {
			int x = this.xCoord + dirs[i].offsetX;
			int y = this.yCoord + dirs[i].offsetY;
			int z = this.yCoord + dirs[i].offsetZ;
			if (!getWorldObj().blockExists(x, y, z))
				continue;
			Block t = getWorldObj().getBlock(x, y, z);
			TileEntity te = getWorldObj().getTileEntity(x, y, z);
			if (t != null)
				Logger.info(t.getUnlocalizedName());
			if (te instanceof ITurtleAccess)
				turtles.add((ITurtleAccess) te);
		}
		int rate = ((int) Math.floor((float) 6 / (float) turtles.size())) * Config.fuelRF;
		for (ITurtleAccess turtle : turtles) {
			if (storage.getEnergyStored() >= Config.fuelRF)
				storage.extractEnergy(addFuel(turtle, rate), false);
		}
	}

	private int addFuel(ITurtleAccess turtle, int rate) {
		if (Integer.MAX_VALUE - rate <= turtle.getFuelLevel()) {
			int added = Integer.MAX_VALUE - turtle.getFuelLevel();
			turtle.consumeFuel(-added);
			return added;
		}else {
			turtle.consumeFuel(-rate);
			return rate;
		}
	}

	@Override
	public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
		return storage.receiveEnergy(maxReceive, simulate);
	}

	@Override
	public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) {
		return 0;
	}

	@Override
	public int getEnergyStored(ForgeDirection from) {
		return storage.getEnergyStored();
	}

	@Override
	public int getMaxEnergyStored(ForgeDirection from) {
		return storage.getMaxEnergyStored();
	}

	@Override
	public boolean canConnectEnergy(ForgeDirection from) {
		return true;
	}
}
