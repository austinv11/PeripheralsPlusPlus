package com.austinv11.peripheralsplusplus.tiles;

import appeng.api.AEApi;
import appeng.api.networking.IGridHost;
import appeng.api.networking.IGridNode;
import appeng.api.util.AECableType;
import com.austinv11.peripheralsplusplus.blocks.MEBridge;
import com.austinv11.peripheralsplusplus.reference.Config;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityMEBridge extends TileEntity implements IPeripheral, IGridHost {

	public static String publicName = "meBridge";
	private String name = "tileEntityMEBridge";

	public TileEntityMEBridge() {
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
		return new String[] {"listAll", "listItems", "listCraft", "retrieve", "craft"};
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws LuaException, InterruptedException {
		if (!Config.enableMEBridge)
			throw new LuaException("ME bridges have been disabled");
		return new Object[0];
	}

	@Override
	public void attach(IComputerAccess computer) {}

	@Override
	public void detach(IComputerAccess computer) {}

	@Override
	public boolean equals(IPeripheral other) {//FIXME idk what I'm doing
		return (other == this);
	}

	@Override
	public IGridNode getGridNode(ForgeDirection dir) {
		return AEApi.instance().createGridNode(new MEBridge());
	}

	@Override
	public AECableType getCableConnectionType(ForgeDirection dir) {
		return AECableType.GLASS;
	}

	@Override
	public void securityBreak() {}
}
