package miscperipherals.external;

import miscperipherals.core.LuaManager;
import miscperipherals.safe.Reflector;
import miscperipherals.util.Util;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import thermalexpansion.api.tileentity.ITesseract;
import dan200.computer.api.IComputerAccess;
import dan200.computer.api.IHostedPeripheral;

public class ExtTesseract implements IHostedPeripheral {
	private final ITesseract tesseract;
	
	public ExtTesseract(TileEntity tesseract) {
		this.tesseract = (ITesseract) tesseract;
	}
	
	@Override
	public String getType() {
		return "tesseract";
	}

	@Override
	public String[] getMethodNames() {
		return new String[] {"getFrequency","getFreq","setFrequency","setFreq","getType","getOwner","getMode"};
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, int method, Object[] arguments) throws Exception {
		switch (method) {
			case 0:
			case 1: {
				return new Object[] {Reflector.getField(tesseract, "access", Byte.class).equals(0) ? Reflector.getField(tesseract, "frequency", Integer.class) : null};
			}
			case 2:
			case 3: {
				if (arguments.length < 1) throw new Exception("too few arguments");
				else if (!(arguments[0] instanceof Double)) throw new Exception("bad argument #1 (expected number)");
				
				int freq = (int) Math.floor((Double) arguments[0]);
				if (freq < -1 || freq > 999) throw new Exception("bad frequency " + freq + " (expected -1..999)");
				
				if (!Reflector.getField(tesseract, "access", Byte.class).equals(0)) return new Object[] {false};
				
				Reflector.setField(tesseract, "frequency", freq);
				
				return new Object[] {true};
			}
			case 4: {
				return new Object[] {Util.camelCase(Reflector.getField(tesseract, "type", String.class).replace('_', ' '))};
			}
			case 5: {
				return new Object[] {Reflector.getField(tesseract, "owner", String.class)};
			}
			case 6: {				
				return Reflector.getField(tesseract, "access", Byte.class).equals(0) ? new Object[] {Reflector.getField(tesseract, "mode", Byte.class)} : new Object[0];
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
