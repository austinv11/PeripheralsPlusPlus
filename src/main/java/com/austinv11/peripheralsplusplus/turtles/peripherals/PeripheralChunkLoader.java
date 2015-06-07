package com.austinv11.peripheralsplusplus.turtles.peripherals;

import com.austinv11.peripheralsplusplus.PeripheralsPlusPlus;
import com.austinv11.peripheralsplusplus.reference.Config;
import com.austinv11.peripheralsplusplus.utils.ReflectionHelper;
import com.austinv11.peripheralsplusplus.utils.TurtleUtil;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.ITurtleAccess;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;

import java.util.LinkedList;
import java.util.List;

public class PeripheralChunkLoader extends MountedPeripheral {
	
	private ITurtleAccess turtle;
	private boolean ticketCreated = false;
	private ForgeChunkManager.Ticket ticket;
	private IComputerAccess computer;
	private ChunkCoordinates oldPos;
	private int oldChunkX;
	private int oldChunkZ;
	
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
	public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws LuaException, InterruptedException {
		return new Object[0];
	}
	
	@Override
	public void attach(IComputerAccess computer) {
		this.computer = computer;
	}
	
	@Override
	public void detach(IComputerAccess computer) {
		if (ticket != null)
			ForgeChunkManager.releaseTicket(ticket);
	}
	
	@Override
	public boolean equals(IPeripheral other) {
		return false;
	}
	
	public void update() {
		if (Config.enableChunkyTurtle) {
			if (turtle.getWorld().isRemote)
				return;
			
			ChunkCoordinates pos = turtle.getPosition();
			
			if (!ticketCreated) {
				ticketCreated = true;
				ticket = ForgeChunkManager.requestTicket(PeripheralsPlusPlus.instance, turtle.getWorld(), ForgeChunkManager.Type.NORMAL);
				if (ticket == null) {
					PeripheralsPlusPlus.LOGGER.warn("Chunk loading limit exceeded, not chunkloading turtle "+(computer == null ? "[unknown]" : computer.getID())+" at ("+pos.posX+","+pos.posY+","+pos.posZ+")!");
					return;
				}
				
				int width = (Config.chunkLoadingRadius*2)+1+(Config.aggressiveChunkLoading && Config.chunkLoadingRadius == 0 ? 2 : 0);//The extra 2 is for another possible 2 chunks loaded
				ticket.setChunkListDepth(width*width);
				
			} else if (pos.posX != oldPos.posX || pos.posY != oldPos.posY || pos.posZ != oldPos.posZ) {
				updatePos();
				turtle.consumeFuel(Config.chunkyMovementPenalty);
				if (ticket == null) {
					PeripheralsPlusPlus.LOGGER.warn("Null ticket when moving chunkloaded turtle "+(computer == null ? "[unknown]" : computer.getID())+" at ("+pos.posX+","+pos.posY+","+pos.posZ+")!");
				} else {
					ForgeChunkManager.releaseTicket(ticket);
					ticketCreated = false;
				}
			}
			
			oldPos = pos;
		}
	}
	
	private void updatePos() {
		ChunkCoordinates pos = turtle.getPosition();
		if (ticket != null) {
			NBTTagCompound modData = ticket.getModData();
			modData.setInteger("turtleX", pos.posX);
			modData.setInteger("turtleY", pos.posY);
			modData.setInteger("turtleZ", pos.posZ);
			
			int chunkX = pos.posX >> 4;
			int chunkZ = pos.posZ >> 4;
			
			if (oldChunkX != chunkX || oldChunkZ != chunkZ) {
				int radius = Config.chunkLoadingRadius;
				for (int cx = chunkX-radius; cx <= chunkX+radius; cx++) {
					for (int cz = chunkZ-radius; cz <= chunkZ+radius; cz++) {
						ForgeChunkManager.forceChunk(ticket, new ChunkCoordIntPair(cx, cz));
					}
				}
				
				oldChunkX = chunkX;
				oldChunkZ = chunkZ;
			}
			
			if (Config.aggressiveChunkLoading && Config.chunkLoadingRadius == 0) {
				if (chunkX != (pos.posX+1) >> 4) {
					ForgeChunkManager.forceChunk(ticket, new ChunkCoordIntPair((pos.posX+1) >> 4, chunkZ));
				} else if (chunkX != (pos.posX-1) >> 4) {
					ForgeChunkManager.forceChunk(ticket, new ChunkCoordIntPair((pos.posX-1) >> 4, chunkZ));
				}
				if (chunkZ != (pos.posZ+1) >> 4) {
					ForgeChunkManager.forceChunk(ticket, new ChunkCoordIntPair(chunkX, (pos.posZ+1) >> 4));
				} else if (chunkZ != (pos.posZ-1) >> 4) {
					ForgeChunkManager.forceChunk(ticket, new ChunkCoordIntPair(chunkX, (pos.posZ-1) >> 4));
				}
			}
		}
	}
	
	public static class TurtleChunkLoadingCallback implements ForgeChunkManager.OrderedLoadingCallback {
		
		@Override
		public void ticketsLoaded(List<ForgeChunkManager.Ticket> tickets, World world) {
			for (ForgeChunkManager.Ticket ticket : tickets) {
				int x = ticket.getModData().getInteger("turtleX");
				int y = ticket.getModData().getInteger("turtleY");
				int z = ticket.getModData().getInteger("turtleZ");
				
				TileEntity te = world.getTileEntity(x, y, z);
				ITurtleAccess turtle = null;
				try {
					if (te != null)
						turtle = ReflectionHelper.getTurtle(te);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (turtle != null) {
					ForgeChunkManager.forceChunk(ticket, new ChunkCoordIntPair(x >> 4, z >> 4));
					
					PeripheralChunkLoader loader = TurtleUtil.getPeripheral((ITurtleAccess) te, PeripheralChunkLoader.class);
					if (loader != null) 
						loader.ticketCreated = true;
				}
			}
		}
		
		@Override
		public List<ForgeChunkManager.Ticket> ticketsLoaded(List<ForgeChunkManager.Ticket> tickets, World world, int maxTicketCount) {
			List<ForgeChunkManager.Ticket> ret = new LinkedList<ForgeChunkManager.Ticket>();
			List<ChunkCoordinates> known = new LinkedList<ChunkCoordinates>();
			
			for (ForgeChunkManager.Ticket ticket : tickets) {
				int x = ticket.getModData().getInteger("turtleX");
				int y = ticket.getModData().getInteger("turtleY");
				int z = ticket.getModData().getInteger("turtleZ");
				
				ChunkCoordinates coords = new ChunkCoordinates(x, y, z);
				if (known.contains(coords)) 
					continue;
				else 
					known.add(coords);
				
				TileEntity te = world.getTileEntity(x, y, z);
				ITurtleAccess turtle = null;
				try {
					if (te != null)
						turtle = ReflectionHelper.getTurtle(te);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (turtle != null && TurtleUtil.getPeripheral((ITurtleAccess)te, PeripheralChunkLoader.class) != null) {
					ret.add(ticket);
				}
			}
			
			int removed = tickets.size() - known.size();
			if (removed > 0)
				PeripheralsPlusPlus.LOGGER.info("Removed "+removed+" bugged duplicate chunk loading tickets from world "+world.provider.dimensionId);
			
			return ret;
		}
	}
}
