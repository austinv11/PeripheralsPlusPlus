package com.austinv11.peripheralsplusplus.turtles.peripherals;

import com.austinv11.peripheralsplusplus.PeripheralsPlusPlus;
import com.austinv11.peripheralsplusplus.reference.Config;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.ITurtleAccess;

import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;

import java.util.ArrayList;
import java.util.List;

public class PeripheralChunkLoader extends MountedPeripheral {

	private ITurtleAccess turtle;
	private ForgeChunkManager.Ticket ticket;
	private boolean attached = false;

	private ChunkPos pos;

	public PeripheralChunkLoader(ITurtleAccess turtle) {
		this.turtle = turtle;
		this.pos = new ChunkPos(turtle.getPosition());
	}

	@Override
	public void attach(IComputerAccess computer) {
		super.attach(computer);
		attached = true;
	}

	public void update() {
		if (attached && !turtle.getWorld().isRemote) {
			if (ticket == null || posChanged()) {
				this.pos = new ChunkPos(turtle.getPosition());
				updateTicket();
			}
		}
	}

	@Override
	public void detach(IComputerAccess computer) {
		super.detach(computer);
		synchronized (this) {
			ForgeChunkManager.releaseTicket(ticket);
		}
		ticket = null;
	}

	public void updateTicket() {
		ForgeChunkManager.releaseTicket(ticket);
		ticket = ForgeChunkManager.requestTicket(PeripheralsPlusPlus.instance, turtle.getWorld(), ForgeChunkManager.Type.NORMAL);
		for (ChunkPos coordIntPair : getChunksInRadius(Config.chunkLoadingRadius)) {
			ForgeChunkManager.forceChunk(ticket, coordIntPair);
		}
	}

	public ArrayList<ChunkPos> getChunksInRadius(int radius) {
		ArrayList<ChunkPos> chunkList = new ArrayList<>();
		for (int chunkX = pos.getXStart() - radius; chunkX <= pos.getXStart() + radius; chunkX++) {
			for (int chunkZ = pos.getZStart() - radius; chunkZ <= pos.getZStart() + radius; chunkZ++) {
				chunkList.add(new ChunkPos(chunkX, chunkZ));
			}
		}
		return chunkList;
	}

	public boolean posChanged() {
		return !(new ChunkPos(turtle.getPosition())).equals(pos);
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
	public boolean equals(IPeripheral other) {
		return (this == other);
	}

	public static class LoaderHandler implements ForgeChunkManager.LoadingCallback {
		@Override
		public void ticketsLoaded(List<ForgeChunkManager.Ticket> tickets, World world) {

		}
	}
}
