package miscperipherals.external;

import miscperipherals.core.LuaManager;
import miscperipherals.module.ModulePortalGun;
import miscperipherals.safe.Reflector;
import miscperipherals.util.Util;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import dan200.computer.api.IComputerAccess;
import dan200.computer.api.IHostedPeripheral;

public class ExtPortalSpawner implements IHostedPeripheral {
	private static final BiMap<String, String> GUNS = HashBiMap.create();
	private final TileEntity spawner;
	
	public ExtPortalSpawner(TileEntity spawner) {
		this.spawner = spawner;
	}
	
	@Override
	public String getType() {
		return "portalSpawner";
	}

	@Override
	public String[] getMethodNames() {
		return new String[] {"setGun", "setColor", "getPortal", "setPowered", "getPowered"};
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, int method, Object[] arguments) throws Exception {
		switch (method) {
			case 0: {
				if (arguments.length < 1) throw new Exception("too few arguments");
				else if (!(arguments[0] instanceof String)) throw new Exception("bad argument #1 (expected string)");
				
				String gun = ((String)arguments[0]).toLowerCase();
				if (!GUNS.containsKey(gun)) throw new Exception("unknown gun "+gun+" (expected "+Util.join("/", GUNS.keySet())+")");
				
				ModulePortalGun.setValueWithPair(spawner, "spOwner", GUNS.get(gun));
				return callMethod(computer, 2, arguments);
			}
			case 1: {
				if (arguments.length < 1) throw new Exception("too few arguments");
				else if (!(arguments[0] instanceof Boolean)) throw new Exception("bad argument #1 (expected boolean)");
				
				boolean secondary = (Boolean)arguments[0];
				ModulePortalGun.setValueWithPair(spawner, "spColour", secondary ? 2 : 1, "spPair");
				return callMethod(computer, 2, arguments);
			}
			case 2: {
				return new Object[] {GUNS.inverse().get(Reflector.getField(spawner, "spOwner", Object.class)), Reflector.getField(spawner, "spColour", Integer.class) == 2};
			}
			case 3: {
				if (arguments.length < 1) throw new Exception("too few arguments");
				else if (!(arguments[0] instanceof Boolean)) throw new Exception("bad argument #1 (expected boolean)");
				
				boolean powered = (Boolean)arguments[0];
				ModulePortalGun.setValueWithPair(spawner, "spawnerPowered", powered, "spPair");
				return new Object[0];
			}
			case 4: {
				return new Object[] {Reflector.getField(spawner, "spawnerPowered", Object.class)};
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
		
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		
	}
	
	static {
		GUNS.put("normal", "def");
		GUNS.put("atlas", "coopA");
		GUNS.put("pbody", "coopB");
		GUNS.put("bacon", "_A");
		GUNS.put("potato", "_B");
	}
}
