package miscperipherals.external;

import miscperipherals.core.LuaManager;
import miscperipherals.module.ModulePortalGun;
import miscperipherals.safe.Reflector;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import dan200.computer.api.IComputerAccess;
import dan200.computer.api.IHostedPeripheral;

public class ExtAFP implements IHostedPeripheral {
	private final TileEntity afp;
	
	public ExtAFP(TileEntity afp) {
		this.afp = afp;
	}
	
	@Override
	public String getType() {
		return "aerialFaithPlate";
	}

	@Override
	public String[] getMethodNames() {
		return new String[] {"setHPower", "setVPower", "getPower", "setPowered", "getPowered"};
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, int method, Object[] arguments) throws Exception {
		switch (method) {
			case 0: {
				if (arguments.length < 1) throw new Exception("too few arguments");
				else if (!(arguments[0] instanceof Double)) throw new Exception("bad argument #1 (expected number)");
				
				double value = (Double)arguments[0];
				double min = (Reflector.getField(afp, "set", Integer.class) == 2 ? 1.0D : 0.0D);
				double max = 5.0D;
				if (value < min || value > max) throw new Exception("bad value (expected "+min+"-"+max+")");
				
				ModulePortalGun.setValueWithPair(afp, "strHori", value);
				return callMethod(computer, 2, arguments);
			}
			case 1: {
				if (arguments.length < 1) throw new Exception("too few arguments");
				else if (!(arguments[0] instanceof Double)) throw new Exception("bad argument #1 (expected number)");
				
				double value = (Double)arguments[0];
				double min = (Reflector.getField(afp, "set", Integer.class) <= 1 ? 1.0D : 0.0D);
				double max = 5.0D;
				if (value < min || value > max) throw new Exception("bad value (expected "+min+"-"+max+")");
				
				ModulePortalGun.setValueWithPair(afp, "strVert", value);
				return callMethod(computer, 2, arguments);
			}
			case 2: {
				return new Object[] {Reflector.getField(afp, "strHori", Object.class), Reflector.getField(afp, "strVert", Object.class)};
			}
			case 3: {
				if (arguments.length < 1) throw new Exception("too few arguments");
				else if (!(arguments[0] instanceof Boolean)) throw new Exception("bad argument #1 (expected boolean)");
				
				ModulePortalGun.setValueWithPair(afp, "needsPower", arguments[0]);
				return new Object[0];
			}
			case 4: {
				return new Object[] {Reflector.getField(afp, "needsPower", Object.class)};
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
}
