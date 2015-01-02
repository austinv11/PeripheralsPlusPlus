package miscperipherals.peripheral;

import miscperipherals.core.LuaManager;
import miscperipherals.core.MiscPeripherals;
import miscperipherals.safe.Reflector;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Vec3;
import dan200.computer.api.IComputerAccess;
import dan200.computer.api.IHostedPeripheral;
import dan200.turtle.api.ITurtleAccess;

public class PeripheralOreScanner implements IHostedPeripheral {
	private final ITurtleAccess turtle;
	private final boolean advanced;
	
	public PeripheralOreScanner(ITurtleAccess turtle, boolean advanced) {
		this.turtle = turtle;
		this.advanced = advanced;
	}
	
	@Override
	public String getType() {
		return "oreScanner";
	}

	@Override
	public String[] getMethodNames() {
		return new String[] {"scan"};
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, int method, Object[] arguments) throws Exception {
		switch (method) {
			case 0: {
				float energyConsume = advanced ? 250 : 50;
				if (!turtle.consumeFuel((int)Math.ceil(energyConsume / MiscPeripherals.instance.fuelEU))) return new Object[] {null};
				
				Vec3 pos = turtle.getPosition();
				Integer scan = Reflector.invoke("ic2.core.item.tool.ItemScanner", "valueOfArea", Integer.class, turtle.getWorld(), (int)Math.floor(pos.xCoord), (int)Math.floor(pos.yCoord), (int)Math.floor(pos.zCoord), advanced);
				return new Object[] {scan == null ? 0 : scan};
			}
		}
		return new Object[0];
	}

	@Override
	public boolean canAttachToSide(int side) {
		return false;
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
