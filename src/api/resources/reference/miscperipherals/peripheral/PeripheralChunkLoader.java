package miscperipherals.peripheral;

import miscperipherals.core.LuaManager;
import miscperipherals.core.MiscPeripherals;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Vec3;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.ForgeChunkManager.Type;
import dan200.computer.api.IComputerAccess;
import dan200.computer.api.IHostedPeripheral;
import dan200.turtle.api.ITurtleAccess;

public class PeripheralChunkLoader implements IHostedPeripheral {
	private final ITurtleAccess turtle;
	private IComputerAccess computer;
	public boolean ticketCreated = false;
	private Ticket ticket;
	private int oldChunkX = Integer.MIN_VALUE;
	private int oldChunkZ = Integer.MIN_VALUE;
	private double oldXCoord = Integer.MIN_VALUE;
	private double oldYCoord = Integer.MIN_VALUE;
	private double oldZCoord = Integer.MIN_VALUE;
	
	public PeripheralChunkLoader(ITurtleAccess turtle) {
		this.turtle = turtle;
	}
	
	@Override
	public String getType() {
		return "chunkLoader";
	}

	@Override
	public String[] getMethodNames() {
		return new String[0];
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, int method, Object[] arguments) throws Exception {
		return new Object[0];
	}

	@Override
	public boolean canAttachToSide(int side) {
		return true;
	}

	@Override
	public void attach(IComputerAccess computer) {
		LuaManager.mount(computer);
		
		this.computer = computer;
	}

	@Override
	public void detach(IComputerAccess computer) {
		this.computer = computer;
		ForgeChunkManager.releaseTicket(ticket);
	}

	@Override
	public void update() {
		if (!MiscPeripherals.proxy.isServer()) return;
		
		Vec3 pos = turtle.getPosition();
		
		if (pos == null) {
			MiscPeripherals.log.warning("Turtle position is null! "+turtle);
			return;
		}
		
		if (ticketCreated && (pos.xCoord != oldXCoord || pos.yCoord != oldYCoord || pos.zCoord != oldZCoord)) {
			if (ticket == null) {
				MiscPeripherals.log.warning("Null ticket when moving chunkloaded turtle "+(computer == null ? "[unknown]" : computer.getID())+" at ("+(int)pos.xCoord+","+(int)pos.yCoord+","+(int)pos.zCoord+")!");
			} else {
				ForgeChunkManager.releaseTicket(ticket);
				
				ticketCreated = false;
				oldXCoord = pos.xCoord;
				oldYCoord = pos.yCoord;
				oldZCoord = pos.zCoord;
			}
		}
		
		if (!ticketCreated) {
			ticketCreated = true;
			ticket = ForgeChunkManager.requestTicket(MiscPeripherals.instance, turtle.getWorld(), Type.NORMAL);
			if (ticket == null) {
				MiscPeripherals.log.warning("Chunk loading limit exceeded, not chunkloading turtle "+(computer == null ? "[unknown]" : computer.getID())+" at ("+(int)pos.xCoord+","+(int)pos.yCoord+","+(int)pos.zCoord+")!");
				return;
			}
			
			int width = (MiscPeripherals.instance.chunkLoaderRadius * 2) + 1;
			ticket.setChunkListDepth(width * width);
		}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		
	}
	
	private void updatePos(int x, int y, int z) {
		if (ticket != null) {
			NBTTagCompound modData = ticket.getModData();
			modData.setInteger("turtleX", x);
			modData.setInteger("turtleY", y);
			modData.setInteger("turtleZ", z);
			
			int chunkX = x >> 4;
			int chunkZ = z >> 4;
			
			if (oldChunkX != chunkX || oldChunkZ != chunkZ) {
				int radius = MiscPeripherals.instance.chunkLoaderRadius;
				for (int cx = chunkX - radius; cx <= chunkX + radius; cx++) {
					for (int cz = chunkZ - radius; cz <= chunkZ + radius; cz++) {
						ForgeChunkManager.forceChunk(ticket, new ChunkCoordIntPair(cx, cz));
					}
				}
				
				oldChunkX = chunkX;
				oldChunkZ = chunkZ;
			}
		}
	}
}
