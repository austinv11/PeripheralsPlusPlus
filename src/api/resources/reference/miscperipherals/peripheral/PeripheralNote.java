package miscperipherals.peripheral;

import java.io.InputStream;

import miscperipherals.core.LuaManager;
import miscperipherals.core.MiscPeripherals;
import miscperipherals.tile.TilePeripheralWrapper;
import miscperipherals.util.Positionable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import cpw.mods.fml.common.network.PacketDispatcher;
import dan200.computer.api.IComputerAccess;
import dan200.computer.api.IHostedPeripheral;
import dan200.turtle.api.ITurtleAccess;

public class PeripheralNote implements IHostedPeripheral {
	private static final int MAX_INSTRUMENT = 4;
	private static final int MAX_NOTE = 24;
	private static final int MAX_TICKER = 5;
	
	private final Positionable positionable;
	private int ticker = 0;
	private InputStream is;
	
	public PeripheralNote(ITurtleAccess turtle) {
		this.positionable = new Positionable.PositionableTurtle(turtle);
	}
	
	public PeripheralNote(TilePeripheralWrapper tile) {
		this.positionable = new Positionable.PositionableTile(tile);
	}
	
	@Override
	public String getType() {
		return "note";
	}

	@Override
	public String[] getMethodNames() {
		return new String[] {"playNote", "playSound"};
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, int method, Object[] arguments) throws Exception {
		switch (method) {
			case 0: {
				if (arguments.length < 2) throw new Exception("too few arguments");
				else if (!(arguments[0] instanceof Double)) throw new Exception("bad argument #1 (expected number)");
				else if (!(arguments[1] instanceof Double)) throw new Exception("bad argument #2 (expected number)");
				
				int instrument = ((Double)arguments[0]).intValue();
				if (instrument < 0 || instrument > MAX_INSTRUMENT) throw new Exception("bad instrument "+instrument+" (expected 0-"+MAX_INSTRUMENT+")");
				
				int note = ((Double)arguments[1]).intValue();
				if (note < 0 || note > MAX_NOTE) throw new Exception("bad note "+note+" (expected 0-"+MAX_NOTE+")");
				
				if (++ticker > MAX_TICKER) throw new Exception("too many notes (over "+MAX_TICKER+" per tick)");
				
				Vec3 position = positionable.getPosition();
				World world = positionable.getWorld();
				if (position == null || world == null) return new Object[0];
				
				ByteArrayDataOutput os = ByteStreams.newDataOutput();
				os.writeDouble(position.xCoord);
				os.writeDouble(position.yCoord);
				os.writeDouble(position.zCoord);
				os.writeByte(instrument);
				os.writeByte(note);
				playNote(world, position.xCoord, position.yCoord, position.zCoord, instrument, note);
				PacketDispatcher.sendPacketToAllAround(position.xCoord + 0.5D, position.yCoord + 0.5D, position.zCoord + 0.5D, 64.0D, world.provider.dimensionId, PacketDispatcher.getTinyPacket(MiscPeripherals.instance, (short)0, os.toByteArray()));
				
				return new Object[0];
			}
			case 1: {
				if (arguments.length < 1) throw new Exception("too few arguments");
				else if (!(arguments[0] instanceof String)) throw new Exception("bad argument #1 (expected string)");
				
				String sound = (String)arguments[0];
				
				double volume = 1.0F;
				if (arguments.length > 1) {
					if (!(arguments[1] instanceof Double)) throw new Exception("bad argument #2 (expected number)");
					else volume = (Double)arguments[1];
				}
				
				double pitch = 1.0F;
				if (arguments.length > 2) {
					if (!(arguments[2] instanceof Double)) throw new Exception("bad argument #3 (expected number)");
					else volume = (Double)arguments[2];
				}
				
				Vec3 position = positionable.getPosition();
				positionable.getWorld().playSoundEffect(position.xCoord + 0.5D, position.yCoord + 0.5D, position.zCoord + 0.5D, sound, (float)volume, (float)pitch);
				
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
		ticker = 0;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		
	}
	
	/**
	 * Modified BlockNote.onBlockEventReceived
	 */
	public static void playNote(World world, double x, double y, double z, int instrument, int note) {
		float inflate = (float)Math.pow(2.0D, (note - 12) / 12.0D);
        String instrumentName;

        switch (instrument) {
        	case 1: instrumentName = "bd"; break;
        	case 2: instrumentName = "snare"; break;
        	case 3: instrumentName = "hat"; break;
        	case 4: instrumentName = "bassattack"; break;
        	default: instrumentName = "harp"; break;
        }

        world.playSoundEffect(x + 0.5D, y + 0.5D, z + 0.5D, "note." + instrumentName, 3.0F, inflate);
        world.spawnParticle("note", x + 0.5D, y + 1.2D, z + 0.5D, (double)note / 24.0D, 0.0D, 0.0D);
	}
}
