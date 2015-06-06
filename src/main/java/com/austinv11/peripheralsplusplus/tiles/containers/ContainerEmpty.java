package com.austinv11.peripheralsplusplus.tiles.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

// Empty container class for GuiContainer because Panda is lazy
public class ContainerEmpty extends Container
{
    @Override
    public boolean canInteractWith(EntityPlayer player)
    {
        return true;
    }
}
