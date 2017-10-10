package com.austinv11.peripheralsplusplus.tiles;


import com.austinv11.collectiveframework.minecraft.tiles.NetworkedTileEntity;
import com.austinv11.peripheralsplusplus.reference.Config;
import com.austinv11.peripheralsplusplus.utils.ReflectionHelper;
import dan200.computercraft.api.turtle.ITurtleAccess;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class TileEntityRFCharger extends NetworkedTileEntity implements IEnergyStorage, ITickable {
	private EnergyStorage storage = new EnergyStorage(80000);//Leadstone Capacitor

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
		if (nbttagcompound.hasKey("capacity")) {
            int capacity = nbttagcompound.getInteger("capacity");
            int energy = nbttagcompound.getInteger("energy");
            storage = new EnergyStorage(capacity, capacity, capacity, energy);
        }
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
		nbttagcompound.setInteger("capacity", storage.getMaxEnergyStored());
		nbttagcompound.setInteger("energy", storage.getEnergyStored());
		return nbttagcompound;
	}

	@Override
	public void update() {
		if (!getWorld().isRemote) {
			List<ITurtleAccess> turtles = new ArrayList<ITurtleAccess>(6);
			for (EnumFacing direction : EnumFacing.values()) {
			    BlockPos pos = getPos().offset(direction);
				if (getWorld().isAirBlock(pos))
					continue;
				TileEntity te = getWorld().getTileEntity(pos);
				if (te != null) {
					try {
						ITurtleAccess turtle = ReflectionHelper.getTurtle(te);
						if (turtle != null) {
							turtles.add(turtle);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			int rate = ((int) Math.floor((float) 6/(float) turtles.size()));
			for (ITurtleAccess turtle : turtles) {
				if (storage.getEnergyStored() >= rate)
					storage.extractEnergy(addFuel(turtle, rate)*Config.fuelRF, false);
			}
		}
	}

	private int addFuel(ITurtleAccess turtle, int rate) {
		if (turtle.getFuelLimit() > turtle.getFuelLevel()) {
			turtle.setFuelLevel(rate+turtle.getFuelLevel());//Bad fix for a crash with turtle.addFuel()
			return rate;
		}
		return 0;
	}

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        return storage.receiveEnergy(maxReceive, simulate);
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        return storage.extractEnergy(maxExtract, simulate);
    }

    @Override
    public int getEnergyStored() {
        return storage.getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored() {
        return storage.getMaxEnergyStored();
    }

    @Override
    public boolean canExtract() {
        return storage.canExtract();
    }

    @Override
    public boolean canReceive() {
        return storage.canReceive();
    }

	public void showFuel(EntityPlayer player) {
		player.sendMessage(new TextComponentString(String.format("Energy: %d/%dRF",
				getEnergyStored(), getMaxEnergyStored())));
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		return capability.equals(CapabilityEnergy.ENERGY) || super.hasCapability(capability, facing);
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		if (hasCapability(capability, facing))
			return CapabilityEnergy.ENERGY.cast(this);
		return super.getCapability(capability, facing);
	}
}
