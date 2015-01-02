package miscperipherals.peripheral;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;

import miscperipherals.core.LuaManager;
import miscperipherals.core.TickHandler;
import miscperipherals.tile.TilePeripheralWrapper;
import miscperipherals.util.BFMachine;
import miscperipherals.util.BFMachine.BFEventHandler;
import miscperipherals.util.BFMachine.BFException;
import miscperipherals.util.Positionable;
import net.minecraft.nbt.NBTTagCompound;
import dan200.computer.api.IComputerAccess;
import dan200.computer.api.IHostedPeripheral;
import dan200.turtle.api.ITurtleAccess;

public class PeripheralAccelerator implements IHostedPeripheral {
	private final Positionable positionable;
	private List<TickAlarm> alarms = new ArrayList<TickAlarm>();
	private List<TickAlarm> alarmsToRemove = new ArrayList<TickAlarm>();
	private Random rnd = new Random();
	
	public PeripheralAccelerator(ITurtleAccess turtle) {
		this.positionable = new Positionable.PositionableTurtle(turtle);
	}
	
	public PeripheralAccelerator(TilePeripheralWrapper tile) {
		this.positionable = new Positionable.PositionableTile(tile);
	}
	
	@Override
	public String getType() {
		return "hardwareAccelerator";
	}
	
	@Override
	public String[] getMethodNames() {
		return new String[] {"tickAlarm", "bf"};
	}
	
	@Override
	public Object[] callMethod(final IComputerAccess computer, int method, Object[] arguments) throws Exception {
		switch (method) {
			case 0: {
				if (arguments.length < 1) throw new Exception("too few arguments");
				else if (!(arguments[0] instanceof Double)) throw new Exception("bad argument #1 (expected number)");
				
				int ticks = (int)Math.floor((Double)arguments[0]);
				if (ticks < 1) throw new Exception("bad tick time "+ticks+" (expected 1-)");
				
				double id = rnd.nextLong();
				final TickAlarm alarm = new TickAlarm(computer, ticks, id);
				TickHandler.addTickCallback(positionable.getWorld(), new Callable<Object>() {
					@Override
					public Object call() throws Exception {
						alarms.add(alarm);
						return null;
					}
				}).get();
				
				return new Object[] {id};
			}
			case 1: {
				if (arguments.length < 1) throw new Exception("too few arguments");
				else if (!(arguments[0] instanceof String)) throw new Exception("bad argument #1 (expected string)");
				else if (arguments.length > 1 && !(arguments[1] instanceof String)) throw new Exception("bad argument #2 (expected string)");
				
				final double id = rnd.nextLong();
				
				BFMachine machine = new BFMachine((String) arguments[0]);
				if (arguments.length > 1) machine.setInput((String) arguments[1]);
				machine.addEventHandler(new BFEventHandler() {
					@Override
					public void onOutput(String output, String add) {
						
					}
					
					@Override
					public void onInsta(int value) {
						computer.queueEvent("bf_insta", new Object[] {id, value});
					}
					
					@Override
					public void onError(BFException e) {
						computer.queueEvent("bf_error", new Object[] {id, e.getMessage()});
					}
					
					@Override
					public void onEnd(String output) {
						computer.queueEvent("bf_end", new Object[] {id, output});
					}
				});
				new Thread(machine, "MiscPeripherals BF Executor [" + computer + "]").start();
				
				return new Object[] {id};
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
	}
	
	@Override
	public void detach(IComputerAccess computer) {
		
	}
	
	@Override
	public void update() {
		alarmsToRemove.clear();
		for (TickAlarm alarm : alarms) {
			if (!alarm.tick()) alarmsToRemove.add(alarm);
		}
		alarms.removeAll(alarmsToRemove);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		
	}
	
	private static class TickAlarm {
		private final IComputerAccess computer;
		private int ticks;
		private final double id;
		
		public TickAlarm(IComputerAccess computer, int ticks, double id) {
			this.computer = computer;
			this.ticks = ticks;
			this.id = id;
		}
		
		public boolean tick() {
			if (--ticks <= 0) {
				computer.queueEvent("tickAlarm", new Object[] {id});
				return false;
			} else return true;
		}
	}
}
