package com.austinv11.peripheralsplusplus.tiles;

import com.austinv11.peripheralsplusplus.reference.Config;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.HashMap;

public class TileEntityPlayerSensor extends TileEntity implements IPeripheral {

	public static String publicName = "playerSensor";
	private String name = "tileEntityChatBox";
	private HashMap<IComputerAccess,Boolean> computers = new HashMap<IComputerAccess,Boolean>();

	public TileEntityPlayerSensor() {
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
	public String getType() {
		return name;
	}

	@Override
	public String[] getMethodNames() {
		if (Config.additionalMethods)
			return new String[] {"getNearbyPlayers", "getAllPlayers"};
		return new String[0];
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws LuaException, InterruptedException {
		if (!Config.enablePlayerSensor)
			throw new LuaException("Player sensors have been disabled");
		if (!Config.additionalMethods)
			throw new LuaException("Additional methods for player sensors have been disabled");
		return new Object[0];
	}

	@Override
	public void attach(IComputerAccess computer) {
		computers.put(computer, true);
	}

	@Override
	public void detach(IComputerAccess computer) {
		computers.remove(computer);
	}

	@Override
	public boolean equals(IPeripheral other) {//FIXME idk what I'm doing
		return (other == this);
	}

	public void blockActivated(String player) {
		for (IComputerAccess computer : computers.keySet()) {
			computer.queueEvent("player", new Object[]{player});
		}
	}
}
