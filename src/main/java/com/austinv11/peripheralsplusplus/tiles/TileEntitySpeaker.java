package com.austinv11.peripheralsplusplus.tiles;

import com.austinv11.peripheralsplusplus.mount.DynamicMount;
import com.austinv11.peripheralsplusplus.reference.Config;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.ITurtleAccess;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntitySpeaker extends TileEntity implements IPeripheral {

	public static String publicName = "speaker";
	private String name = "tileEntitySpeaker";
	private ITurtleAccess turtle;

	public TileEntitySpeaker() {
		super();
	}

	public TileEntitySpeaker(ITurtleAccess turtle) {
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
		return new String[]{"speak"};
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws LuaException, InterruptedException {
		if (!Config.enableSpeaker)
			throw new LuaException("Speakers have been disabled");
		if (method == 0) {

		}
		return new Object[0];
	}

	@Override
	public void attach(IComputerAccess computer) {
		computer.mount(DynamicMount.DIRECTORY, new DynamicMount(this));
	}

	@Override
	public void detach(IComputerAccess computer) {
		computer.unmount(DynamicMount.DIRECTORY);
	}

	@Override
	public boolean equals(IPeripheral other) {
		return (this == other);
	}
}
