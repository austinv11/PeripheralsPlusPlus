package miscperipherals.peripheral;

import miscperipherals.safe.Reflector;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import dan200.computer.api.IComputerAccess;
import dan200.computer.api.IHostedPeripheral;
import dan200.turtle.api.ITurtleAccess;
import dan200.turtle.api.ITurtleUpgrade;
import dan200.turtle.api.TurtleSide;

public class PeripheralChunkLoaderModem extends PeripheralChunkLoader {
	public PeripheralChunkLoaderModem(ITurtleAccess turtle, TurtleSide side) {
		super(turtle);
	}
	
	@Override
	public String getType() {
		return super.getType();
	}
	
	@Override
	public String[] getMethodNames() {
		return super.getMethodNames();
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, int method, Object[] arguments) throws Exception {
		return super.callMethod(computer, method, arguments);
	}

	@Override
	public boolean canAttachToSide(int side) {
		return super.canAttachToSide(side);
	}
	
	@Override
	public void attach(IComputerAccess computer) {
		super.attach(computer);
	}
	
	@Override
	public void detach(IComputerAccess computer) {
		super.detach(computer);
	}
	
	@Override
	public void update() {
		super.update();
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
	}
}
