package com.austinv11.peripheralsplusplus.turtles.peripherals;

import codechicken.core.asm.CodeChickenCoreModContainer;
import codechicken.lib.world.ChunkExtension;
import com.austinv11.peripheralsplusplus.reference.Config;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.ITurtleAccess;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ChunkLoader;
import net.minecraftforge.event.world.ChunkDataEvent;

/**
 * CommuMod - A Minecraft Modification
 * Copyright (C) 2015 Cyb3rWarri0r8
 * <p/>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
public class PeripheralChunkLoader extends MountedPeripheral {
    ITurtleAccess turtleAccess;

    public PeripheralChunkLoader(ITurtleAccess turtleAccess)
    {
        this.turtleAccess = turtleAccess;
    }

    @Override
    public String getType() {
        return "chunk loader";
    }

    @Override
    public String[] getMethodNames() {
        return new String[]{"loadChunk"};
    }

    @Override
    public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws LuaException, InterruptedException {
        if(method == 0)
        {
            if(Config.enableChunkLoading){
                return new Object[0];
            }
        }

        return new Object[0];
    }

    @Override
    public boolean equals(IPeripheral other) {
        return (other == this);
    }


}
