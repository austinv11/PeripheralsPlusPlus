package com.austinv11.peripheralsplusplus.tiles;

import com.austinv11.peripheralsplusplus.reference.Config;
import com.austinv11.peripheralsplusplus.utils.Location;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.ITurtleAccess;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import java.util.HashMap;

public class TileEntityPlayerSensor extends TileEntity implements IPeripheral {

	public static String publicName = "playerSensor";
	private String name = "tileEntityPlayerSensor";
	private HashMap<IComputerAccess,Boolean> computers = new HashMap<IComputerAccess,Boolean>();
	private Location location;
	private ITurtleAccess turtle;

	public TileEntityPlayerSensor() {
		super();
		location = new Location(xCoord,zCoord,yCoord,worldObj);
	}

	public TileEntityPlayerSensor(ITurtleAccess turtle) {
		location = new Location(turtle.getPosition().posX,turtle.getPosition().posY, turtle.getPosition().posZ, turtle.getWorld());
		this.turtle = turtle;
		this.xCoord = turtle.getPosition().posX;
		this.yCoord = turtle.getPosition().posY;
		this.zCoord = turtle.getPosition().posZ;
		this.setWorldObj(turtle.getWorld());
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
		if (Config.additionalMethods)
			return new String[] {"getNearbyPlayers"/*params: (optional) range returns:table containing players (in table with 'player' and 'distance' keys)*/, "getAllPlayers"/*params: (optional) limitToCurrentWorld returns:table containing all players*/};
		return new String[0];
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws LuaException, InterruptedException {
		if (!Config.enablePlayerSensor)
			throw new LuaException("Player sensors have been disabled");
		if (!Config.additionalMethods)
			throw new LuaException("Additional methods for player sensors have been disabled");
		if (method == 0) {
			//try {
			if (arguments.length > 0 && !(arguments[0] instanceof Double))
				throw new LuaException("Bad argument #1 (expected number)");
			double range = Config.sensorRange;
			if (arguments.length > 0)
				range = (Double) arguments[0];
			HashMap<String,Double> map = location.getPlayers(this, range);
			HashMap<Integer, HashMap<String, Object>> returnVal = new HashMap<Integer, HashMap<String, Object>>();
			int i = 1;
			for (String player : map.keySet()) {
				HashMap<String, Object> table = new HashMap<String,Object>();
				table.put("player", player);
				table.put("distance", map.get(player));
				returnVal.put(i, table);
				i++;
			}
			return new Object[]{returnVal};
			/*}catch (Exception e) {
				e.printStackTrace();
			}*/
		}else if (method == 1) {
			if (arguments.length > 0 && !(arguments[0] instanceof Boolean))
				throw new LuaException("Bad argument #1 (expected boolean)");
			boolean inWorld = false;
			if (arguments.length > 0)
				inWorld = (Boolean) arguments[0];
			HashMap<Integer, String> map = new HashMap<Integer,String>();
			int i = 1;
			for (String p : location.getPlayers(inWorld)) {
				map.put(i, p);
				i++;
			}
			return new Object[]{map};
		}
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

	public void update() {
		this.xCoord = turtle.getPosition().posX;
		this.yCoord = turtle.getPosition().posY;
		this.zCoord = turtle.getPosition().posZ;
	}

	public void blockActivated(String player) {
		for (IComputerAccess computer : computers.keySet()) {
			computer.queueEvent("player", new Object[]{player});
		}
	}
}
