package com.austinv11.peripheralsplusplus.turtles.peripherals;

import com.austinv11.peripheralsplusplus.reference.Config;
import com.austinv11.peripheralsplusplus.utils.Logger;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.ITurtleAccess;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSign;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.Facing;
import sun.rmi.log.LogHandler;

public class PeripheralSignReader extends MountedPeripheral
{
    private ITurtleAccess turtle;

    public PeripheralSignReader(ITurtleAccess turtle) {
        this.turtle = turtle;
    }

    @Override
    public String getType()
    {
        return "signReader";
    }

    @Override
    public String[] getMethodNames() { return new String[] {"read"}; }

    @Override
    public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws LuaException, InterruptedException
    {
        if (!Config.enableReaderTurtle)
            throw new LuaException("Sign Reading Turtles have been disabled");
        if (method == 0)
        {
            int blockX = turtle.getPosition().posX+ Facing.offsetsXForSide[turtle.getDirection()];
            int blockY = turtle.getPosition().posY+Facing.offsetsYForSide[turtle.getDirection()];
            int blockZ = turtle.getPosition().posZ+Facing.offsetsZForSide[turtle.getDirection()];

            Block blockFacing = turtle.getWorld().getBlock(blockX, blockY, blockZ);

            if (blockFacing instanceof BlockSign)
            {
                Logger.info("Sign Stuff");
                TileEntitySign tileEntitySign = (TileEntitySign) turtle.getWorld().getTileEntity(blockX, blockY, blockZ);
                return new Object[] {tileEntitySign.signText[0], tileEntitySign.signText[1], tileEntitySign.signText[2], tileEntitySign.signText[3]};
            }
        }
        return new Object[0];
    }

    @Override
    public boolean equals(IPeripheral other)
    {
        return (other == this);
    }
}
