package miscperipherals.core;

import java.util.LinkedList;
import java.util.List;

import miscperipherals.peripheral.PeripheralChunkLoader;
import miscperipherals.util.Util;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.OrderedLoadingCallback;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import dan200.turtle.api.ITurtleAccess;

public class ChunkLoadingCallback implements OrderedLoadingCallback {
	@Override
	public void ticketsLoaded(List<Ticket> tickets, World world) {
		for (Ticket ticket : tickets) {
			int x = ticket.getModData().getInteger("turtleX");
			int y = ticket.getModData().getInteger("turtleY");
			int z = ticket.getModData().getInteger("turtleZ");
			
			TileEntity te = world.getBlockTileEntity(x, y, z);
			if (te instanceof ITurtleAccess) {
				ForgeChunkManager.forceChunk(ticket, new ChunkCoordIntPair(x >> 4, z >> 4));
				
				PeripheralChunkLoader loader = Util.getPeripheral((ITurtleAccess)te, PeripheralChunkLoader.class);
				if (loader != null) loader.ticketCreated = true;
			}
		}
	}
	
	@Override
	public List<Ticket> ticketsLoaded(List<Ticket> tickets, World world, int maxTicketCount) {
		List<Ticket> ret = new LinkedList<Ticket>();
		List<ChunkCoordinates> known = new LinkedList<ChunkCoordinates>();
		
		for (Ticket ticket : tickets) {
			int x = ticket.getModData().getInteger("turtleX");
			int y = ticket.getModData().getInteger("turtleY");
			int z = ticket.getModData().getInteger("turtleZ");
			
			ChunkCoordinates coords = new ChunkCoordinates(x, y, z);
			if (known.contains(coords)) continue;
			else known.add(coords);
			
			TileEntity te = world.getBlockTileEntity(x, y, z);
			if (te instanceof ITurtleAccess && Util.getPeripheral((ITurtleAccess)te, PeripheralChunkLoader.class) != null) {
				ret.add(ticket);
			}
		}
		
		int removed = tickets.size() - known.size();
		if (removed > 0) MiscPeripherals.log.info("Removed "+removed+" bugged duplicate chunk loading tickets from world "+world.provider.dimensionId);
		
		return ret;
	}
}
