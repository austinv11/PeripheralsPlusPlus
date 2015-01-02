package miscperipherals.peripheral;

import java.util.Random;

import miscperipherals.core.LuaManager;
import miscperipherals.core.MiscPeripherals;
import miscperipherals.util.Util;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenDesert;
import net.minecraftforge.common.ForgeDirection;
import dan200.computer.api.IComputerAccess;
import dan200.computer.api.IHostedPeripheral;
import dan200.turtle.api.ITurtleAccess;
import dan200.turtle.api.TurtleSide;

public class PeripheralSolar implements IHostedPeripheral {
	final ITurtleAccess turtle;
	private final TurtleSide side;
	private int euBuffer = 0;
	private int ticker = new Random().nextInt(20);
	boolean sunVisible = false;
	boolean skyVisible = false;
	
	public PeripheralSolar(ITurtleAccess turtle, TurtleSide side) {
		this.turtle = turtle;
		this.side = side;
	}
	
	@Override
	public String getType() {
		return "solar";
	}

	@Override
	public String[] getMethodNames() {
		return new String[] {"getProduction"};
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, int method, Object[] arguments) throws Exception {
		switch (method) {
			case 0: {
				return new Object[] {getProduction()};
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
		if (!MiscPeripherals.proxy.isServer()) return;
		
		if (++ticker >= 20) {
			Vec3 pos = turtle.getPosition();
			if (pos != null) {
				ticker = 0;
				
				World world = turtle.getWorld();
				if (world != null) {
					int x = (int)Math.floor(pos.xCoord);
					int y = (int)Math.floor(pos.yCoord);
					int z = (int)Math.floor(pos.zCoord);
					
					if (MiscPeripherals.instance.sideSensitive) {
						ForgeDirection dir = Util.getDirectionFromTurtleSide(side, turtle.getFacingDir());
						x += dir.offsetX;
						y += dir.offsetY;
						z += dir.offsetZ;
					}
					
					skyVisible = !world.provider.hasNoSky && world.canBlockSeeTheSky(x, y, z);
					sunVisible = world.isDaytime() && skyVisible && (world.getWorldChunkManager().getBiomeGenAt(x, z) instanceof BiomeGenDesert || (!world.isRaining() && !world.isThundering()));
				}
			}
		}
		
		if (canUpdate()) {
			euBuffer += getProduction();
			while (euBuffer >= MiscPeripherals.instance.fuelEU) {
				euBuffer -= MiscPeripherals.instance.fuelEU * Util.addFuel(turtle, 1);
			}
		}
	}
	
	boolean canUpdate() {
		return sunVisible;
	}
	
	int getProduction() {
		return 1;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		
	}
}
