package com.austinv11.peripheralsplusplus.blocks;

import com.austinv11.peripheralsplusplus.PeripheralsPlusPlus;
import com.austinv11.peripheralsplusplus.creativetab.CreativeTabPPP;
import com.austinv11.peripheralsplusplus.reference.Reference;
import com.austinv11.peripheralsplusplus.tiles.TileEntityPlayerInterface;
import com.google.common.collect.Lists;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralProvider;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.ArrayList;

public class BlockPlayerInterface extends BlockContainer implements IPeripheralProvider
{

    public BlockPlayerInterface()
    {
        super(Material.rock);
        this.setBlockName("playerInterface");
        this.setCreativeTab(CreativeTabPPP.PPP_TAB);
        this.setHardness(4f);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int p_149915_2_)
    {
        return new TileEntityPlayerInterface();
    }

    @Override
    public IPeripheral getPeripheral(World world, int x, int y, int z, int side)
    {
        return (IPeripheral) world.getTileEntity(x, y, z);
    }

    @Override
    public String getUnlocalizedName()
    {//Formats the name
        return String.format("tile.%s%s", Reference.MOD_ID.toLowerCase() + ":", getUnwrappedUnlocalizedName(getUnwrappedUnlocalizedName(super.getUnlocalizedName())));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister)
    {//Registers the block icon(s)
        blockIcon = iconRegister.registerIcon(String.format("%s", getUnwrappedUnlocalizedName(this.getUnlocalizedName())));
    }

    protected String getUnwrappedUnlocalizedName(String unlocalizedName)
    {//Removes the "item." from the item name
        return unlocalizedName.substring(unlocalizedName.indexOf(".") + 1);
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune)
    {
        ArrayList<ItemStack> items = Lists.newArrayList();
        ItemStack stack = new ItemStack(this, 1, metadata);
        items.add(stack);
        TileEntityPlayerInterface playerInterface = (TileEntityPlayerInterface) world.getTileEntity(x, y, z);
        ;
        if (playerInterface.getStackInSlot(0) != null)
            items.add(playerInterface.getStackInSlot(0));
        return items;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int meta, float hitX, float hitY, float hitZ)
    {
        TileEntity te = world.getTileEntity(x, y, z);
        if (!world.isRemote)
        {
            if (te != null)
            {
                player.openGui(PeripheralsPlusPlus.instance, Reference.GUIs.PLAYERINTERFACE.ordinal(), world, x, y, z);
            }
        }
        return true;
    }
}
