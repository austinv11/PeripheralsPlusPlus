package com.austinv11.peripheralsplusplus.turtles.peripherals;

import com.austinv11.peripheralsplusplus.reference.Config;
import com.google.common.collect.ImmutableMap;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.ITurtleAccess;
import net.minecraft.block.IGrowable;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;
import java.util.Map;

public class PeripheralGarden extends MountedPeripheral {
    
    private ITurtleAccess turtle;

    public PeripheralGarden(ITurtleAccess turtle) {
        this.turtle = turtle;
    }

    @Override
    public String getType() {
        return "gardener";
    }

    @Override
    public String[] getMethodNames() {
        return new String[]{"getGrowth", "getGrowthUp", "getGrowthDown", "fertilize", "fertilizeUp", "fertilizeDown"};
    }

    @Override
    public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments)
            throws LuaException, InterruptedException {
        if (!Config.enableGardeningTurtle)
            throw new LuaException("Gardening Turtles have been disabled!");
        switch (method) {
            case 0:
                return getGrowth(turtle.getPosition().offset(turtle.getDirection()));
            case 1:
                return getGrowth(turtle.getPosition().up());
            case 2:
                return getGrowth(turtle.getPosition().down());
            case 3:
                return fertilize(turtle.getPosition().offset(turtle.getDirection()));
            case 4:
                return fertilize(turtle.getPosition().up());
            case 5:
                return fertilize(turtle.getPosition().down());
        }
        throw new LuaException();
    }

    private Object[] fertilize(BlockPos pos) {
        boolean success = false;
        if (turtle.getInventory().getStackInSlot(turtle.getSelectedSlot())
                .isItemEqual(new ItemStack(Items.DYE, 1, 15))) {
            ItemStack bonemeal = turtle.getInventory().getStackInSlot(turtle.getSelectedSlot());
            IBlockState blockFacing = turtle.getWorld().getBlockState(pos);
            if (blockFacing.getBlock() instanceof IGrowable)
                success = ItemDye.applyBonemeal(bonemeal, turtle.getWorld(), pos);
        }
        return new Object[]{success};
    }

    private Object[] getGrowth(BlockPos pos) throws LuaException {
        IBlockState blockFacing = turtle.getWorld().getBlockState(pos);
        if (blockFacing.getBlock() instanceof IGrowable) {
            ImmutableMap<IProperty<?>, Comparable<?>> properties = blockFacing.getProperties();
            for (Map.Entry<IProperty<?>, Comparable<?>> entry : properties.entrySet()) {
                if (entry.getKey().getName().equalsIgnoreCase("age") ||
                        entry.getKey().getName().equalsIgnoreCase("stage")) {
                    if (entry.getKey().getClass().isAssignableFrom(PropertyInteger.class)) {
                        Map<String, Object> growth = new HashMap<>();
                        growth.put("age", entry.getValue());
                        Object[] allowedValues = entry.getKey().getAllowedValues().toArray();
                        growth.put("min", allowedValues[0]);
                        growth.put("max", allowedValues[allowedValues.length - 1]);
                        growth.put("percent", Float.valueOf(String.valueOf(entry.getValue())) /
                                ((int)allowedValues[allowedValues.length -1]));
                        return new Object[]{growth};
                    }
                    else
                        throw new LuaException("IGrowable \"age\" property is not of the type PropertyInteger");
                }
            }
            throw new LuaException("IGrowable does not contain an \"age\" property.");
        }
        throw new LuaException("Block is not an instance of IGrowable");
    }

    @Override
    public boolean equals(IPeripheral other) {
        return (other == this);
    }
}
