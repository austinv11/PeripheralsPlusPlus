package com.austinv11.peripheralsplusplus.blocks;

import com.austinv11.peripheralsplusplus.reference.Reference;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public abstract class BlockContainerPPP extends BlockContainer
{
    public BlockContainerPPP(Material material)
    {
        super(material);
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        dropItems(world, pos, state);
        super.breakBlock(world, pos, state);
    }

    @Override
    public String getUnlocalizedName(){//Formats the name
        return String.format("tile.%s%s", Reference.MOD_ID.toLowerCase()+":", getUnwrappedUnlocalizedName(getUnwrappedUnlocalizedName(super.getUnlocalizedName())));
    }

    protected String getUnwrappedUnlocalizedName(String unlocalizedName){//Removes the "item." from the item name
        return unlocalizedName.substring(unlocalizedName.indexOf(".") + 1);
    }

    private void dropItems(World world, BlockPos pos, IBlockState state)
    {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity == null)
            return;
        IInventory inventory = (IInventory) tileEntity;

        for (int i = 0; i < inventory.getSizeInventory(); i++)
        {
            ItemStack itemStack = inventory.getStackInSlot(i);

            if (!itemStack.isEmpty())
            {
                Random random = new Random();
                float dX = random.nextFloat() * 0.8F + 0.1F;
                float dY = random.nextFloat() * 0.8F + 0.1F;
                float dZ = random.nextFloat() * 0.8F + 0.1F;

                EntityItem entityItem = new EntityItem(world, pos.getX() + dX, pos.getY() + dY,
                        pos.getZ() + dZ, itemStack.copy());
                if (itemStack.hasTagCompound())
                {
                    entityItem.getItem().setTagCompound(itemStack.getTagCompound().copy());
                }

                float motionFactor = 0.05F;
                entityItem.motionX = random.nextGaussian() * motionFactor;
                entityItem.motionY = random.nextGaussian() * motionFactor + 0.2F;
                entityItem.motionZ = random.nextGaussian() * motionFactor;
                world.spawnEntity(entityItem);
                itemStack.setCount(0);
            }
        }
    }
}
