package com.austinv11.peripheralsplusplus.turtles.peripherals;

import com.austinv11.peripheralsplusplus.reference.Config;
import com.austinv11.peripheralsplusplus.utils.Util;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.ITurtleAccess;
import net.minecraft.block.BlockSign;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;

import java.util.ArrayList;

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
        switch (method) {
            case 0:
                return getSignText(turtle.getPosition().offset(turtle.getDirection()));
            case 1:
                return getSignText(turtle.getPosition().up());
            case 2:
                return getSignText(turtle.getPosition().down());
        }
        throw new LuaException();
    }

    private Object[] getSignText(BlockPos pos) throws LuaException {
        IBlockState blockFacing = turtle.getWorld().getBlockState(pos);
        if (blockFacing.getBlock() instanceof BlockSign) {
            TileEntitySign tileEntitySign = (TileEntitySign) turtle.getWorld().getTileEntity(pos);
            if (tileEntitySign == null)
                throw new LuaException("No sign found.");
            ArrayList<String> lines = new ArrayList<>();
            for (ITextComponent line : tileEntitySign.signText)
                lines.add(line.getUnformattedText());
            return new Object[] {Util.arrayToMap(lines.toArray())};
        }
        throw new LuaException("No sign found.");
    }

    @Override
    public boolean equals(IPeripheral other) {
        return (other == this);
    }
}
