package com.austinv11.peripheralsplusplus.tiles;

import com.austinv11.peripheralsplusplus.blocks.BlockAntenna;
import com.austinv11.peripheralsplusplus.lua.LuaObjectClientControl;
import com.austinv11.peripheralsplusplus.lua.LuaObjectServerControl;
import com.austinv11.peripheralsplusplus.reference.Config;
import com.austinv11.peripheralsplusplus.utils.Util;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TileEntityEnderNetModem extends MountedNetworkedTileEntity {

	public static String publicName = "enderNetModem";
	private  String name = "tileEntityEnderNetModem";
	private boolean isServer = false;
	private boolean wasServer = false;
	private static HashMap<Integer, IComputerAccess> endernet_connected_computers = new HashMap<Integer,IComputerAccess>();
	private HashMap<Integer, IComputerAccess> connected_computers = new HashMap<Integer,IComputerAccess>();

	public TileEntityEnderNetModem() {
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
		return publicName;
	}

	@Override
	public String[] getMethodNames() {
		return new String[]{"broadcastMessage",/*Broadcasts a message (essentially long range rednet)*/
				"openSocket",/*Opens a server, returns I/O handle for it. Only works if it has a proper connected*/
				"openConnection",/*Connects to server, returns a "limited" I/O handle for it*/
				"whois"/*Performs a search of all ips (computer ids) and domains (based on what you search)*/};
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws LuaException, InterruptedException {
		if (!Config.additionalMethods)
			throw new LuaException("Satellites and associated systems have been disabled");
		switch (method) {
			case 0:
				if (arguments.length < 1)
					throw new LuaException("Too few arguments");
				if (!(arguments[0] instanceof String))
					throw new LuaException("Bad argument #1 (expected string)");
				for (IComputerAccess comp : endernet_connected_computers.values())
					if (comp.getID() != computer.getID())
						comp.queueEvent("endernetMessage", new Object[]{(String)arguments[0], computer.getID()});
				return new Object[0];
			case 1:
				if (arguments.length < 1)
					throw new LuaException("Too few arguments");
				if (!(arguments[0] instanceof String))
					throw new LuaException("Bad argument #1 (expected string)");
				if (isServer) {
					if (LuaObjectServerControl.domains.containsKey(computer.getID()))
						throw new LuaException("Domain '"+(String)arguments[0]+"' already exists");
					return new Object[]{new LuaObjectServerControl((String) arguments[0], false, computer)};
				}
				else

					throw new LuaException("The computer is not connected to a valid server host");
			case 2:
				if (arguments.length < 1)
					throw new LuaException("Too few arguments");
				if (!(arguments[0] instanceof String))
					throw new LuaException("Bad argument #1 (expected string)");
				return new Object[]{new LuaObjectClientControl((String)arguments[0], computer)};
			case 3:
				if (arguments.length < 1)
					throw new LuaException("Too few arguments");
				if (!(arguments[0] instanceof String) && !(arguments[0] instanceof Double))
					throw new LuaException("Bad argument #1 (expected string or number)");
				if (arguments[0] instanceof Double)
					return new Object[]{domainsFromInt((int)(double)(Double)arguments[0])};
				else
					return new Object[]{domainsFromString((String)arguments[0])};
		}
		return new Object[0];
	}

	private HashMap<Integer, Object> domainsFromInt(int id) {
		List<String> domains = new ArrayList<String>();
		if (LuaObjectServerControl.domains.containsKey(id)) {
			domains.add(LuaObjectServerControl.domains.get(id));
			String domain = domains.get(0);
			if (LuaObjectServerControl.subdomains.containsKey(domain))
				for (String subdomain : LuaObjectServerControl.subdomains.get(domain))
					domains.add(subdomain+"."+domain);
			return Util.collectionToMap(domains);
		}
		return null;
	}

	private HashMap<Integer, Object> domainsFromString(String id) {
		List<Object> domains = new ArrayList<Object>();
		for (String domain : LuaObjectServerControl.domains.values())
			if (domain.contains(id)) {
				domains.add(Util.keyFromVal(LuaObjectServerControl.domains, domain));
				domains.add(domain);
				for (String sub : LuaObjectServerControl.subdomains.get(domain))
					domains.add(sub+"."+domain);
			}
		return domains.isEmpty() ? null : Util.collectionToMap(domains);
	}

	@Override
	public void updateEntity() {
		if (worldObj != null) {
			checkMultiblockStatus();
			if (isServer && getBlockMetadata() == 0) {
				worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 1, 2);
			} else if (!isServer && getBlockMetadata() != 0) {
				worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 0, 2);
			}
		}
		if (!isServer && wasServer)
			for (IComputerAccess computer : connected_computers.values())
				if (LuaObjectServerControl.domains.containsKey(computer.getID())) {
					String domain = LuaObjectServerControl.domains.get(computer.getID());
					LuaObjectServerControl.domains.remove(computer.getID());
					if (LuaObjectServerControl.subdomains.containsKey(domain))
						LuaObjectServerControl.subdomains.remove(domain);
				}
	}

	private void checkMultiblockStatus() {
		if (!worldObj.isAirBlock(xCoord, yCoord+1, zCoord))
			isServer = worldObj.getBlock(xCoord, yCoord+1, zCoord) instanceof BlockAntenna;
		if (!wasServer && isServer)
			wasServer = true;
	}

	@Override
	public void attach(IComputerAccess computer) {
		if (!endernet_connected_computers.keySet().contains(computer.getID()))
			endernet_connected_computers.put(computer.getID(), computer);
		connected_computers.put(computer.getID(), computer);
		super.attach(computer);
	}

	@Override
	public void detach(IComputerAccess computer) {
		if (endernet_connected_computers.keySet().contains(computer.getID()))
			endernet_connected_computers.remove(computer.getID());
		if (isServer)
			if (LuaObjectServerControl.domains.containsKey(computer.getID())) {
				String domain = LuaObjectServerControl.domains.get(computer.getID());
				LuaObjectServerControl.domains.remove(computer.getID());
				if (LuaObjectServerControl.subdomains.containsKey(domain))
					LuaObjectServerControl.subdomains.remove(domain);
			}
		connected_computers.remove(computer.getID());
		computer.unmount("servers");
		super.detach(computer);
	}

	@Override
	public boolean equals(IPeripheral other) {
		return (other == this);
	}
}
