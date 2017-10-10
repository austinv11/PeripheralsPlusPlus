package com.austinv11.peripheralsplusplus.tiles;

import com.austinv11.collectiveframework.minecraft.utils.Location;
import com.austinv11.peripheralsplusplus.reference.Config;
import com.austinv11.peripheralsplusplus.utils.Util;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.ITurtleAccess;
import net.minecraft.nbt.NBTTagCompound;

import java.util.HashMap;

public class TileEntityPlayerSensor extends MountedTileEntity {

	public static String publicName = "playerSensor";
	private String name = "tileEntityPlayerSensor";
	private HashMap<IComputerAccess,Boolean> computers = new HashMap<IComputerAccess,Boolean>();
	private Location location;
	private ITurtleAccess turtle;

	public TileEntityPlayerSensor() {
		super();
	}

	public TileEntityPlayerSensor(ITurtleAccess turtle) {
		location = new Location(turtle.getPosition().getX(),turtle.getPosition().getY(), turtle.getPosition().getZ(),
                turtle.getWorld());
		this.turtle = turtle;
		this.setPos(turtle.getPosition());
		this.setWorld(turtle.getWorld());
	}

    @Override
    public void validate() {
        super.validate();

        if(world != null)
            location = new Location(this);
    }

    public String getName() {
		return name;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
		return nbttagcompound;
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
			try {
                if (arguments.length > 0 && !(arguments[0] instanceof Double))
                    throw new LuaException("Bad argument #1 (expected number)");
                synchronized (this) {
                    double range = Config.sensorRange;
                    if (arguments.length > 0)
                        range = (Double) arguments[0];

					HashMap<String,Double> map = new Location(this).getPlayers(range);
                    HashMap<Integer,HashMap<String,Object>> returnVal = new HashMap<Integer,HashMap<String,Object>>();
                    int i = 1;
                    for (String player : map.keySet()) {
                        HashMap<String,Object> table = new HashMap<String,Object>();
                        table.put("player", player);
                        table.put("distance", map.get(player));
                        returnVal.put(i, table);
                        i++;
                    }
                    return new Object[]{returnVal};
                }
			}catch (Exception e) {
				e.printStackTrace();
			}
		}else if (method == 1) {
			if (arguments.length > 0 && !(arguments[0] instanceof Boolean))
				throw new LuaException("Bad argument #1 (expected boolean)");
			boolean inWorld = false;
			if (arguments.length > 0)
				inWorld = (Boolean) arguments[0];
			synchronized (this) {
				HashMap<Integer,String> map = new HashMap<Integer,String>();
				int i = 1;
				for (String p : Util.getPlayers(inWorld ? getWorld() : null)) {
					map.put(i, p);
					i++;
				}
				return new Object[]{map};
			}
		}
		return new Object[0];
	}

	@Override
	public void attach(IComputerAccess computer) {
		computers.put(computer, true);
		super.attach(computer);
	}

	@Override
	public void detach(IComputerAccess computer) {
		computers.remove(computer);
		super.detach(computer);
	}

	@Override
	public boolean equals(IPeripheral other) {//FIXME idk what I'm doing
		return (other == this);
	}

	public void update() {
	    setPos(turtle.getPosition());
	}

	public void blockActivated(String player) {
		for (IComputerAccess computer : computers.keySet()) {
			computer.queueEvent("player", new Object[]{player});
		}
	}
}
