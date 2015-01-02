package miscperipherals.peripheral;

import miscperipherals.core.LuaManager;
import miscperipherals.util.Util;
import net.minecraft.nbt.NBTTagCompound;
import dan200.computer.api.IComputerAccess;
import dan200.computer.api.IHostedPeripheral;
import dan200.turtle.api.ITurtleAccess;

public class PeripheralCompass implements IHostedPeripheral {
	private final ITurtleAccess turtle;
	
	public PeripheralCompass(ITurtleAccess turtle) {
		this.turtle = turtle;
	}
	
	@Override
	public String getType() {
		return "compass";
	}

	@Override
	public String[] getMethodNames() {
		return new String[] {"getFacing"};
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, int method, Object[] arguments) throws Exception {
		switch (method) {
			case 0: {
				return new Object[] {(Integer)Util.SIDE_TO_FACE[turtle.getFacingDir()]};
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
