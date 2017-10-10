package com.austinv11.peripheralsplusplus.entities;

import com.austinv11.peripheralsplusplus.mount.DynamicMount;
import com.austinv11.peripheralsplusplus.utils.IPlusPlusPeripheral;
import dan200.computercraft.api.peripheral.IComputerAccess;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public abstract class MountedEntity extends Entity implements IPlusPlusPeripheral {
    public MountedEntity(World worldIn) {
        super(worldIn);
    }

    @Override
    public void attach(@Nonnull IComputerAccess computer) {
        computer.mount(DynamicMount.DIRECTORY, new DynamicMount(this));
    }

    @Override
    public void detach(@Nonnull IComputerAccess computer) {
        computer.mount(DynamicMount.DIRECTORY, new DynamicMount(this));
        computer.unmount(DynamicMount.DIRECTORY);
    }
}
