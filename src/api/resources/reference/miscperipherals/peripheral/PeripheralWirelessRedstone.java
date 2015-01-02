package miscperipherals.peripheral;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import miscperipherals.core.LuaManager;
import miscperipherals.core.TickHandler;
import miscperipherals.safe.Reflector;
import net.minecraft.entity.EntityLiving;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import codechicken.core.Vector3;
import codechicken.wirelessredstone.core.WirelessReceivingDevice;
import codechicken.wirelessredstone.core.WirelessTransmittingDevice;
import dan200.computer.api.IComputerAccess;
import dan200.computer.api.IHostedPeripheral;
import dan200.turtle.api.ITurtleAccess;

public class PeripheralWirelessRedstone implements IHostedPeripheral, WirelessReceivingDevice, WirelessTransmittingDevice {
	private final ITurtleAccess turtle;
	private final Map<IComputerAccess, Boolean> computers = new WeakHashMap<IComputerAccess, Boolean>();
	private int frequency = 0;
	private boolean state = false;
	private WeakReference<World> worldCache;
	private boolean skipOn = false;
	
	public PeripheralWirelessRedstone(ITurtleAccess turtle) {
		this.turtle = turtle;
	}
	
	@Override
	public String getType() {
		return "wirelessRedstone";
	}

	@Override
	public String[] getMethodNames() {
		return new String[] {"setFreq","setFrequency","get","set","getFreq","getFrequency"};
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, int method, Object[] arguments) throws Exception {
		updateWorld();
		
		switch (method) {
			case 0:
			case 1: {
				if (arguments.length < 1) throw new Exception("too few arguments");
				else if (!(arguments[0] instanceof Double)) throw new Exception("bad argument #1 (expected number)");
				
				final Object rether = getRether();
				final int freq = (int)Math.floor((Double)arguments[0]);
				
				//if (!rether.canBroadcastOnFrequency("", freq)) return new Object[] {false};
				
				Future<Object> callback = TickHandler.addTickCallback(turtle.getWorld(), new Callable<Object>() {
					@Override
					public Object call() {
						skipOn = true;
						Reflector.invoke(rether, "removeReceivingDevice", Object.class, PeripheralWirelessRedstone.this);
						Reflector.invoke(rether, "removeTransmittingDevice", Object.class, PeripheralWirelessRedstone.this);
						frequency = freq;
						Reflector.invoke(rether, "addReceivingDevice", Object.class, PeripheralWirelessRedstone.this);
						skipOn = false;
						
						return null;
					}
				});
				
				callback.get();
				
				return new Object[] {true};
			}
			case 2: {
				return new Object[] {state};
			}
			case 3: {
				if (arguments.length < 1) throw new Exception("too few arguments");
				else if (!(arguments[0] instanceof Boolean)) throw new Exception("bad argument #1 (expected boolean)");
				final boolean state = (Boolean)arguments[0];
				
				Future<Object> callback = TickHandler.addTickCallback(turtle.getWorld(), new Callable<Object>() {
					@Override
					public Object call() {
						skipOn = true;
						Reflector.invoke(getRether(), state ? "addTransmittingDevice" : "removeTransmittingDevice", Object.class, PeripheralWirelessRedstone.this);
						skipOn = false;
						
						return null;
					}
				});
				callback.get();
			}
			case 4:
			case 5: {
				return new Object[] {frequency};
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
		
		updateWorld();
		
		computers.put(computer, true);
		
		Reflector.invoke(getRether(), "addReceivingDevice", Object.class, PeripheralWirelessRedstone.this);
	}

	@Override
	public void detach(IComputerAccess computer) {
		updateWorld();
		
		computers.remove(computer);
		Object rether = getRether();
		Reflector.invoke(rether, "removeReceivingDevice", Object.class, this);
		Reflector.invoke(rether, "removeTransmittingDevice", Object.class, this);
	}

	@Override
	public void update() {
		
	}

	@Override
	public Vector3 getPosition() {
		Vec3 pos = turtle.getPosition();
		return new Vector3(pos.xCoord, pos.yCoord, pos.zCoord);
	}

	@Override
	public int getDimension() {
		updateWorld();
		if (worldCache.get() == null) return 0;
		
		return worldCache.get().provider.dimensionId;
	}

	@Override
	public int getFreq() {
		return frequency;
	}

	@Override
	public EntityLiving getAttachedEntity() {
		return null;
	}

	@Override
	public void updateDevice(int freq, boolean on) {
		if (freq == frequency && !skipOn) {
			state = on;
			
			for (IComputerAccess computer : computers.keySet()) {
				computer.queueEvent("wireless_redstone", new Object[] {freq, on});
			}
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		frequency = nbttagcompound.getInteger("frequency");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		nbttagcompound.setInteger("frequency", frequency);
	}
	
	private Object getRether() {
		return Reflector.invoke("codechicken.wirelessredstone.core.RedstoneEther", "server", Object.class);
	}
	
	private void updateWorld() {
		if (worldCache == null || worldCache.get() == null) {
			try {
				worldCache = new WeakReference<World>(turtle.getWorld());
			} catch (Throwable e) {}
		}
	}
}
