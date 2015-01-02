package miscperipherals.peripheral;

import java.util.Map;
import java.util.WeakHashMap;

import miscperipherals.api.IEnergyMeterData;
import miscperipherals.core.LuaManager;
import miscperipherals.tile.TilePeripheralWrapper;
import miscperipherals.util.Positionable;
import miscperipherals.util.Util;
import miscperipherals.util.Positionable.PositionableTurtle;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Facing;
import net.minecraft.util.Vec3;
import dan200.computer.api.IComputerAccess;
import dan200.computer.api.IHostedPeripheral;
import dan200.turtle.api.ITurtleAccess;

public class PeripheralEnergyMeter implements IHostedPeripheral {
	private final Positionable positionable;
	private Map<IComputerAccess, Boolean> computers = new WeakHashMap<IComputerAccess, Boolean>();
	private int ticker = 0;
	
	private IEnergyMeterData source;
	private NBTTagCompound data;
	private State state = State.Idle;
	private int x, y, z, side;
	private int direction = -1;
	private Object[] arguments;
	
	public PeripheralEnergyMeter(ITurtleAccess turtle) {
		this.positionable = new Positionable.PositionableTurtle(turtle);
	}
	
	public PeripheralEnergyMeter(TilePeripheralWrapper tile) {
		this.positionable = new Positionable.PositionableTile(tile);
	}
	
	@Override
	public String getType() {
		return "energyMeter";
	}

	@Override
	public String[] getMethodNames() {
		return new String[] {"start", "startUp", "startDown", "stop", "getState"};
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, int method, Object[] arguments) throws Exception {
		switch (method) {
			case 0:
			case 1:
			case 2: {
				if (state == State.Idle) {
					if (method == 1) direction = 1;
					else if (method == 2) direction = 0;
					else direction = -1;
					
					state = State.Start;
					return new Object[] {true};
				}
				
				return new Object[] {false};
			}
			case 3: {
				if (state == State.Monitoring) {
					state = State.Stop;
					return new Object[] {true};
				}
				
				return new Object[] {false};
			}
			case 4: {
				return new Object[] {Util.camelCase(state.toString().replace('_', ' '))};
			}
		}
		
		return new Object[0];
	}

	@Override
	public boolean canAttachToSide(int side) {
		return true;
	}

	@Override
	public void attach(IComputerAccess computer) {
		LuaManager.mount(computer);
		
		computers.put(computer, true);
	}

	@Override
	public void detach(IComputerAccess computer) {
		computers.remove(computer);
	}

	@Override
	public void update() {
		switch (state) {
			case Start: {
				start();
				break;
			}
			case Stop: {
				stop();
				break;
			}
			case Monitoring: {
				if (source != null && ticker % 20 == 0) {
					Map<String, Object> ret = source.getData(positionable.getWorld(), x, y, z, side, data, arguments);
					if (ret == null || ret.isEmpty()) {
						stop();
						break;
					}
					
					for (IComputerAccess computer : computers.keySet()) {
						computer.queueEvent("energy_measure", new Object[] {ret});
					}
				}
			}
			// shut up Eclipse
			case Idle: {}
		}
		
		ticker++;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		
	}
	
	private void start() {
		if (arguments == null) arguments = new Object[0];		
		data = new NBTTagCompound();
		
		Vec3 pos = positionable.getPosition();
		if (direction < 0 || !(positionable instanceof PositionableTurtle)) direction = positionable.getFacing();
		x = (int) Math.floor(pos.xCoord) + Facing.offsetsXForSide[direction];
		y = (int) Math.floor(pos.yCoord) + Facing.offsetsYForSide[direction];
		z = (int) Math.floor(pos.zCoord) + Facing.offsetsZForSide[direction];
		side = Util.OPPOSITE[direction];
		
		for (IEnergyMeterData handler : IEnergyMeterData.handlers) {
			if (handler.canHandle(positionable.getWorld(), x, y, z, side, arguments)) {
				source = handler;
				break;
			}
		}
		
		if (source == null) {
			state = State.Idle;
			return;
		}
		
		source.start(positionable.getWorld(), x, y, z, side, data, arguments);
		state = State.Monitoring;
	}
	
	private void stop() {
		source.cleanup(positionable.getWorld(), x, y, z, side, data, arguments);
		state = State.Idle;
		
		x = y = z = side = 0;
		direction = -1;
		arguments = null;
		data = null;
		source = null;
	}
	
	private static enum State {
		Idle, Monitoring, Start, Stop;
	}
}
