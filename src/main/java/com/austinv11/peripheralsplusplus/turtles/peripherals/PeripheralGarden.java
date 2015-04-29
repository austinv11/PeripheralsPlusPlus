package com.austinv11.peripheralsplusplus.turtles.peripherals;

import com.austinv11.peripheralsplusplus.reference.Config;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.ITurtleAccess;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCocoa;
import net.minecraft.block.IGrowable;
import net.minecraft.init.Items;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Facing;

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
    public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws LuaException, InterruptedException {
        if (!Config.enableGardeningTurtle)
            throw new LuaException("Gardening Turtles have been disabled!");
        if (method == 0) {
            int blockX = turtle.getPosition().posX + Facing.offsetsXForSide[turtle.getDirection()];
            int blockY = turtle.getPosition().posY + Facing.offsetsYForSide[turtle.getDirection()];
            int blockZ = turtle.getPosition().posZ + Facing.offsetsZForSide[turtle.getDirection()];

            Block blockFacing = turtle.getWorld().getBlock(blockX, blockY, blockZ);

            if (blockFacing instanceof IGrowable) {
                return new Object[]{Math.floor((turtle.getWorld().getBlockMetadata(blockX, blockY, blockZ) / (blockFacing instanceof BlockCocoa ? 8.0F : 7.0F)) * 100.0F)};
            }
        } else if (method == 1) {
            int blockX = turtle.getPosition().posX;
            int blockY = turtle.getPosition().posY + +1;
            int blockZ = turtle.getPosition().posZ;

            Block blockFacing = turtle.getWorld().getBlock(blockX, blockY, blockZ);

            if (blockFacing instanceof IGrowable) {
                return new Object[]{Math.floor((turtle.getWorld().getBlockMetadata(blockX, blockY, blockZ) / (blockFacing instanceof BlockCocoa ? 8.0F : 7.0F)) * 100.0F)};
            }
        } else if (method == 2) {
            int blockX = turtle.getPosition().posX;
            int blockY = turtle.getPosition().posY + -1;
            int blockZ = turtle.getPosition().posZ;

            Block blockFacing = turtle.getWorld().getBlock(blockX, blockY, blockZ);

            if (blockFacing instanceof IGrowable) {
                return new Object[]{Math.floor((turtle.getWorld().getBlockMetadata(blockX, blockY, blockZ) / (blockFacing instanceof BlockCocoa ? 8.0F : 7.0F)) * 100.0F)};
            }
        } else if (method == 3) {
			boolean success = false;
            if (turtle.getInventory().getStackInSlot(turtle.getSelectedSlot()) != null && turtle.getInventory().getStackInSlot(turtle.getSelectedSlot()).isItemEqual(new ItemStack(Items.dye, 1, 15))) {
                ItemStack bonemeal = turtle.getInventory().getStackInSlot(turtle.getSelectedSlot());
                int blockX = turtle.getPosition().posX + Facing.offsetsXForSide[turtle.getDirection()];
                int blockY = turtle.getPosition().posY + Facing.offsetsYForSide[turtle.getDirection()];
                int blockZ = turtle.getPosition().posZ + Facing.offsetsZForSide[turtle.getDirection()];

                Block blockFacing = turtle.getWorld().getBlock(blockX, blockY, blockZ);

                if (blockFacing instanceof IGrowable) {
					success = ItemDye.applyBonemeal(bonemeal, turtle.getWorld(), blockX, blockY, blockZ, null);
                    if (!turtle.getWorld().isRemote) {
                        turtle.getWorld().playAuxSFX(2005, blockX, blockY, blockZ, 0);
                    }
                }
            }
			return new Object[]{success};
        } else if (method == 4) {
			boolean success = false;
			if (turtle.getInventory().getStackInSlot(turtle.getSelectedSlot()) != null && turtle.getInventory().getStackInSlot(turtle.getSelectedSlot()).isItemEqual(new ItemStack(Items.dye, 1, 15))) {
                ItemStack bonemeal = turtle.getInventory().getStackInSlot(turtle.getSelectedSlot());
                int blockX = turtle.getPosition().posX;
                int blockY = turtle.getPosition().posY + +1;
                int blockZ = turtle.getPosition().posZ;

                Block blockFacing = turtle.getWorld().getBlock(blockX, blockY, blockZ);

                if (blockFacing instanceof IGrowable) {
					success = ItemDye.applyBonemeal(bonemeal, turtle.getWorld(), blockX, blockY, blockZ, null);
                    if (!turtle.getWorld().isRemote) {
                        turtle.getWorld().playAuxSFX(2005, blockX, blockY, blockZ, 0);
                    }
                }
            }
			return new Object[]{success};
        } else if (method == 5) {
			boolean success = false;
            if (turtle.getInventory().getStackInSlot(turtle.getSelectedSlot()) != null && turtle.getInventory().getStackInSlot(turtle.getSelectedSlot()).isItemEqual(new ItemStack(Items.dye, 1, 15))) {
                ItemStack bonemeal = turtle.getInventory().getStackInSlot(turtle.getSelectedSlot());
                int blockX = turtle.getPosition().posX;
                int blockY = turtle.getPosition().posY - 1;
                int blockZ = turtle.getPosition().posZ;

                Block blockFacing = turtle.getWorld().getBlock(blockX, blockY, blockZ);

                if (blockFacing instanceof IGrowable) {
					success = ItemDye.applyBonemeal(bonemeal, turtle.getWorld(), blockX, blockY, blockZ, null);
                    if (!turtle.getWorld().isRemote) {
                        turtle.getWorld().playAuxSFX(2005, blockX, blockY, blockZ, 0);
                    }
                }
            }
			return new Object[]{success};
        }

        return new Object[0];
    }

    @Override
    public boolean equals(IPeripheral other) {
        return (other == this);
    }
}
