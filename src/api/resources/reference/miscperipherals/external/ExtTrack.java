package miscperipherals.external;

import miscperipherals.core.LuaManager;
import mods.railcraft.api.tracks.ITrackInstance;
import net.minecraft.nbt.NBTTagCompound;
import dan200.computer.api.IComputerAccess;
import dan200.computer.api.IHostedPeripheral;

public abstract class ExtTrack implements IHostedPeripheral {
	protected final ITrackInstance track;
	
	public ExtTrack(ITrackInstance track) {
		this.track = track;
	}
	
	@Override
	public abstract String getType();

	@Override
	public abstract String[] getMethodNames();

	@Override
	public abstract Object[] callMethod(IComputerAccess computer, int method, Object[] arguments) throws Exception;

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
