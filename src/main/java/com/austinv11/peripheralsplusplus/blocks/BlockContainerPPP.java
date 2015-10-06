package com.austinv11.peripheralsplusplus.blocks;

import com.austinv11.peripheralsplusplus.reference.Reference;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.Random;

public abstract class BlockContainerPPP extends BlockContainer
{
    public BlockContainerPPP(Material material)
    {
        super(material);
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int meta)
    {
        dropItems(world, x, y, z);
        super.breakBlock(world, x, y, z, block, meta);
    }

    @Override
    public String getUnlocalizedName(){//Formats the name
        return String.format("tile.%s%s", Reference.MOD_ID.toLowerCase()+":", getUnwrappedUnlocalizedName(getUnwrappedUnlocalizedName(super.getUnlocalizedName())));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister){//Registers the block icon(s)
        blockIcon = iconRegister.registerIcon(String.format("%s", getUnwrappedUnlocalizedName(this.getUnlocalizedName())));
    }

    protected String getUnwrappedUnlocalizedName(String unlocalizedName){//Removes the "item." from the item name
        return unlocalizedName.substring(unlocalizedName.indexOf(".") + 1);
    }

    private void dropItems(World world, int x, int y, int z)
    {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        IInventory inventory = (IInventory) tileEntity;

        for (int i = 0; i < inventory.getSizeInventory(); i++)
        {
            ItemStack itemStack = inventory.getStackInSlot(i);

            if (itemStack != null)
            {
                Random random = new Random();
                float dX = random.nextFloat() * 0.8F + 0.1F;
                float dY = random.nextFloat() * 0.8F + 0.1F;
                float dZ = random.nextFloat() * 0.8F + 0.1F;

                EntityItem entityItem = new EntityItem(world, x + dX, y + dY, z + dZ, itemStack.copy());
                if (itemStack.hasTagCompound())
                {
                    entityItem.getEntityItem().setTagCompound((NBTTagCompound) itemStack.getTagCompound().copy());
                }

                float motionFactor = 0.05F;
                entityItem.motionX = random.nextGaussian() * motionFactor;
                entityItem.motionY = random.nextGaussian() * motionFactor + 0.2F;
                entityItem.motionZ = random.nextGaussian() * motionFactor;
                world.spawnEntityInWorld(entityItem);
                itemStack.stackSize = 0;
            }
        }
    }
}
