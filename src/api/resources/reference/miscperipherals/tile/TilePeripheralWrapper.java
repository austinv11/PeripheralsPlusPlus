package miscperipherals.tile;

import net.minecraft.nbt.NBTTagCompound;
import dan200.computer.api.IComputerAccess;
import dan200.computer.api.IHostedPeripheral;
import dan200.computer.api.IPeripheral;

public class TilePeripheralWrapper extends TileInventory implements IPeripheral {
	public final IHostedPeripheral peripheral;
	
	public TilePeripheralWrapper(Class<? extends IHostedPeripheral> peripheral) {
		this(peripheral, 0);
	}
	
	public TilePeripheralWrapper(Class<? extends IHostedPeripheral> peripheral, int slots) {
		super(slots);
		
		try {
			this.peripheral = peripheral.getConstructor(TilePeripheralWrapper.class).newInstance(this);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public boolean canUpdate() {
		return true;
	}
	
	@Override
	public void updateEntity() {
		super.updateEntity();
		
		peripheral.update();
	}

	@Override
	public String getType() {
		return peripheral.getType();
	}

	@Override
	public String[] getMethodNames() {
		return peripheral.getMethodNames();
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, int method, Object[] arguments) throws Exception {
		return peripheral.callMethod(computer, method, arguments);
	}

	@Override
	public boolean canAttachToSide(int side) {
		return peripheral.canAttachToSide(side);
	}

	@Override
	public void attach(IComputerAccess computer) {
		peripheral.attach(computer);
	}

	@Override
	public void detach(IComputerAccess computer) {
		peripheral.detach(computer);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		peripheral.readFromNBT(tag);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		peripheral.writeToNBT(tag);
	}
}
