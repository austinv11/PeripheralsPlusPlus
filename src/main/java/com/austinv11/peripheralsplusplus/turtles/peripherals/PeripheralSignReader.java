package com.austinv11.peripheralsplusplus.turtles.peripherals;

import com.austinv11.peripheralsplusplus.reference.Config;
import com.austinv11.peripheralsplusplus.utils.Util;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.ITurtleAccess;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSign;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.Facing;

public class PeripheralSignReader extends MountedPeripheral
{
    private ITurtleAccess turtle;

    public PeripheralSignReader(ITurtleAccess turtle) {
        this.turtle = turtle;
    }

    @Override
    public String getType() {
        return "signReader";
    }

    @Override
    public String[] getMethodNames() {
		return new String[]{"read", "readUp", "readDown"};
	}

    @Override
    public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws LuaException, InterruptedException {
        if (!Config.enableReaderTurtle)
            throw new LuaException("Sign Reading Turtles have been disabled");
        if (method == 0) {
            int blockX = turtle.getPosition().posX+ Facing.offsetsXForSide[turtle.getDirection()];
            int blockY = turtle.getPosition().posY+Facing.offsetsYForSide[turtle.getDirection()];
            int blockZ = turtle.getPosition().posZ+Facing.offsetsZForSide[turtle.getDirection()];

            Block blockFacing = turtle.getWorld().getBlock(blockX, blockY, blockZ);

            if (blockFacing instanceof BlockSign) {
                TileEntitySign tileEntitySign = (TileEntitySign) turtle.getWorld().getTileEntity(blockX, blockY, blockZ);
                return new Object[] {Util.arrayToMap(tileEntitySign.signText)};
            }
        } else if (method == 1) {
			int blockX = turtle.getPosition().posX;
			int blockY = turtle.getPosition().posY+1;
			int blockZ = turtle.getPosition().posZ;

			Block blockFacing = turtle.getWorld().getBlock(blockX, blockY, blockZ);

			if (blockFacing instanceof BlockSign) {
				TileEntitySign tileEntitySign = (TileEntitySign) turtle.getWorld().getTileEntity(blockX, blockY, blockZ);
				return new Object[] {Util.arrayToMap(tileEntitySign.signText)};
			}
		} else if (method == 2) {
			int blockX = turtle.getPosition().posX;
			int blockY = turtle.getPosition().posY-1;
			int blockZ = turtle.getPosition().posZ;

			Block blockFacing = turtle.getWorld().getBlock(blockX, blockY, blockZ);

			if (blockFacing instanceof BlockSign) {
				TileEntitySign tileEntitySign = (TileEntitySign) turtle.getWorld().getTileEntity(blockX, blockY, blockZ);
				return new Object[]{Util.arrayToMap(tileEntitySign.signText)};
			}
		}
        return new Object[0];
    }

    @Override
    public boolean equals(IPeripheral other) {
        return (other == this);
    }
}
