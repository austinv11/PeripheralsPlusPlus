package com.austinv11.peripheralsplusplus.recipe;

import com.austinv11.peripheralsplusplus.blocks.BlockPeripheralContainer;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import javax.annotation.Nullable;

public class ContainedPeripheral {
    private IPeripheral peripheral;
    private ResourceLocation blockResourceLocation;

    public ContainedPeripheral(ResourceLocation blockResourceLocation, IPeripheral peripheral) {
        this.blockResourceLocation = blockResourceLocation;
        this.peripheral = peripheral;
    }

    public ContainedPeripheral(NBTTagCompound tagCompound) {
        blockResourceLocation = new ResourceLocation(tagCompound.getString("id"));
        try {
            Class<?> clazz = Class.forName(tagCompound.getString("class"));
            peripheral = (IPeripheral) clazz.newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            peripheral = getPeripheralForBlock(ForgeRegistries.BLOCKS.getValue(blockResourceLocation));
            if (peripheral instanceof TileEntity && tagCompound.hasKey("tileEntity")) {
                ((TileEntity)peripheral).readFromNBT(tagCompound.getCompoundTag("tileEntity"));
            }
        }
    }

    @Nullable
    static IPeripheral getPeripheralForBlock(Block block) {
        if (!(block instanceof BlockPeripheralContainer)) {
            IPeripheral peripheral = null;
            if (block instanceof IPeripheral)
                peripheral = (IPeripheral) block;
            if (peripheral == null && block instanceof ITileEntityProvider) {
                TileEntity ent = null;
                try {
                    ent = block.createTileEntity(null, block.getDefaultState());
                } catch (NullPointerException ignore) {}
                if (ent != null) {
                    if (ent instanceof IPeripheral)
                        peripheral = (IPeripheral) ent;
                    // TODO get registered providers via reflection
                }
            }
            return peripheral;
        }
        return null;
    }

    public ResourceLocation getBlockResourceLocation() {
        return blockResourceLocation;
    }

    public NBTTagCompound toNbt() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("id", blockResourceLocation.toString());
        tag.setString("class", peripheral.getClass().toString());
        if (getPeripheral() instanceof TileEntity) {
            NBTTagCompound tileEntityTag = new NBTTagCompound();
            ((TileEntity)getPeripheral()).writeToNBT(tileEntityTag);
            tag.setTag("tileEntity", tileEntityTag);
        }
        return tag;
    }

    public IPeripheral getPeripheral() {
        return peripheral;
    }
}
