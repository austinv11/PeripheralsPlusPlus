package miscperipherals.peripheral;

import miscperipherals.core.LuaManager;
import miscperipherals.core.MiscPeripherals;
import miscperipherals.tile.TilePeripheralWrapper;
import miscperipherals.util.Positionable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Vec3;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import cpw.mods.fml.common.network.PacketDispatcher;
import dan200.computer.api.IComputerAccess;
import dan200.computer.api.IHostedPeripheral;
import dan200.turtle.api.ITurtleAccess;

public class PeripheralSpeaker implements IHostedPeripheral {
	private static final int MAX_REQS = 1;
	
	private final Positionable positionable;
	private int ticker = 0;
	private int reqs = 0;
	
	public PeripheralSpeaker(ITurtleAccess turtle) {
		this.positionable = new Positionable.PositionableTurtle(turtle);
	}
	
	public PeripheralSpeaker(TilePeripheralWrapper tile) {
		this.positionable = new Positionable.PositionableTile(tile);
	}
	
	@Override
	public String getType() {
		return "speaker";
	}

	@Override
	public String[] getMethodNames() {
		return new String[] {"speak"};
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, int method, Object[] arguments) throws Exception {
		switch (method) {
			case 0: {
				if (arguments.length < 1) throw new Exception("too few arguments");
				else if (!(arguments[0] instanceof String)) throw new Exception("bad argument #1 (expected string)");
				else if (arguments.length > 1 && !(arguments[1] instanceof Double)) throw new Exception("bad argument #2 (expected number)");
				
				String text = (String) arguments[0];
				if (text.length() > 255) text = text.substring(0, 255);
				
				double speed = arguments.length > 1 ? (Double)arguments[1] : 0.0D;
				if (speed < -1.0D || speed > 1.0D) throw new Exception("bad speed " + speed + " (expected -1..1)");
				
				if (++reqs > MAX_REQS) throw new Exception("too many requests (over "+MAX_REQS+" per second)");
				
				Vec3 pos = positionable.getPosition();
				ByteArrayDataOutput os = ByteStreams.newDataOutput();
				os.writeDouble(pos.xCoord);
				os.writeDouble(pos.yCoord);
				os.writeDouble(pos.zCoord);
				os.writeUTF(text);
				os.writeDouble(speed);
				
				PacketDispatcher.sendPacketToAllAround(pos.xCoord, pos.yCoord, pos.zCoord, 64.0D, positionable.getWorld().provider.dimensionId, PacketDispatcher.getTinyPacket(MiscPeripherals.instance, (short)8, os.toByteArray()));
				
				return new Object[0];
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
		if (++ticker % 20 == 0) reqs = 0;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		
	}
}
