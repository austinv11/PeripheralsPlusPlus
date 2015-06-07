package com.austinv11.peripheralsplusplus.turtles.peripherals;

import com.austinv11.peripheralsplusplus.blocks.BlockResupplyStation;
import com.austinv11.peripheralsplusplus.reference.Config;
import com.austinv11.peripheralsplusplus.tiles.TileEntityResupplyStation;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.ITurtleAccess;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class PeripheralResupply extends MountedPeripheral {
	
	private ITurtleAccess turtle;
	private ChunkCoordinates linkedStation;
	
	public PeripheralResupply(ITurtleAccess turtle) {
		this.turtle = turtle;
	}
	
	@Override
	public String getType() {
		return "resupply";
	}
	
	@Override
	public String[] getMethodNames() {
		return new String[]{"link", "resupply"};
	}
	
	@Override
	public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws LuaException, InterruptedException {
		if (!Config.enableResupplyStation)
			throw new LuaException("The resupply station has been disabled");
		switch (method) {
			case 0:
				if (arguments.length < 1 || (arguments.length > 1 && arguments.length < 3))
					throw new LuaException("Too few arguments");
				if (!(arguments[0] instanceof String) && !(arguments[0] instanceof Double))
					throw new LuaException("Bad argument #1 (expected string or number)");
				if (arguments.length > 1) {
					if (!(arguments[0] instanceof Double))
						throw new LuaException("Bad argument #1 (expected number)");
					if (!(arguments[1] instanceof Double))
						throw new LuaException("Bad argument #2 (expected number)");
					if (!(arguments[2] instanceof Double))
						throw new LuaException("Bad argument #3 (expected number)");
					linkedStation = new ChunkCoordinates(((Double)arguments[0]).intValue(), 
							((Double)arguments[1]).intValue(), ((Double)arguments[2]).intValue());
					return new Object[]{true};
				}
				ForgeDirection dir;
				if (arguments[0] instanceof String)
					dir = ForgeDirection.valueOf(((String) arguments[0]).toUpperCase());
				else
					dir = ForgeDirection.getOrientation((int) (double) (Double) arguments[0]);
				ChunkCoordinates newLink = new ChunkCoordinates(turtle.getPosition().posX+dir.offsetX, 
						turtle.getPosition().posY+dir.offsetY, turtle.getPosition().posZ+dir.offsetZ);
				World turtleWorld = turtle.getWorld();
				if (!turtleWorld.blockExists(newLink.posX, newLink.posY, newLink.posZ) || turtleWorld.isAirBlock(newLink.posX,
						newLink.posY, newLink.posZ) || !(turtleWorld.getBlock(newLink.posX, newLink.posY, newLink.posZ) 
						instanceof BlockResupplyStation))
					return new Object[]{false};
				linkedStation = newLink;
				return new Object[]{true};
			
			case 1:
				if (linkedStation == null)
					throw new LuaException("A station has not been linked!");
				World world = turtle.getWorld();
				if (!world.blockExists(linkedStation.posX, linkedStation.posY, linkedStation.posZ) || world.isAirBlock(linkedStation.posX,
						linkedStation.posY, linkedStation.posZ) || !(world.getBlock(linkedStation.posX, linkedStation.posY, linkedStation.posZ)
						instanceof BlockResupplyStation))
					throw new LuaException("The linked station is nonexistant!");
				if (arguments.length > 0 && !(arguments[0] instanceof Double))
					throw new LuaException("Bad argument #1 (expected number)");
				if (arguments.length > 1 && !(arguments[1] instanceof String) && !(arguments[1] instanceof Double))
					throw new LuaException("Bad argument #2 (expected string or number)");
				if (arguments.length > 2 && !(arguments[2] instanceof Double))
					throw new LuaException("Bad argument #3 (expected number)");
				int slot = turtle.getSelectedSlot();
				if (arguments.length > 0)
					slot = ((Double)arguments[0]).intValue()-1;
				String id = null;
				if (arguments.length > 1 && arguments[1] instanceof String)
					id = (String)arguments[1];
				else {
					if (turtle.getInventory().getStackInSlot(slot) == null)
						return new Object[]{false};
					if (turtle.getInventory().getStackInSlot(slot).getItem() instanceof ItemBlock) {
						Block itemBlock = Block.getBlockFromItem(turtle.getInventory().getStackInSlot(slot).getItem());
						id = Block.blockRegistry.getNameForObject(itemBlock);
					} else {
						Item item = turtle.getInventory().getStackInSlot(slot).getItem();
						id = Item.itemRegistry.getNameForObject(item);
					}
				}
				int meta = arguments.length > 2 ? ((Double)arguments[2]).intValue() : turtle.getInventory().getStackInSlot(slot) == null ?
						0 : turtle.getInventory().getStackInSlot(slot).getItemDamage();
				synchronized (this) {
					TileEntityResupplyStation station = (TileEntityResupplyStation) world.getTileEntity(linkedStation.posX, 
							linkedStation.posY, linkedStation.posZ);
					return new Object[]{station.resupply(turtle, slot, id, meta)};
				}
		}
		return new Object[0];
	}
	
	@Override
	public boolean equals(IPeripheral other) {
		return this == other;
	}
}
