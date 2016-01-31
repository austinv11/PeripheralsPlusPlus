package com.austinv11.peripheralsplusplus.items;

import com.austinv11.collectiveframework.minecraft.utils.NBTHelper;
import com.austinv11.peripheralsplusplus.PeripheralsPlusPlus;
import com.austinv11.peripheralsplusplus.reference.Config;
import com.austinv11.peripheralsplusplus.reference.Reference;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class ItemPermissionsCard extends ItemPPP
{
    public ItemPermissionsCard()
    {
        super();
        this.setUnlocalizedName("permissionsCard");
        this.setMaxStackSize(1);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
    {
        if (Config.enablePlayerInterface)
        {
            NBTTagCompound compound = stack.getTagCompound();
            if (!player.isSneaking())
            {
                if (!world.isRemote)
                {
                    if (NBTHelper.getTag(stack, "profile") == null)
                    {
                        NBTTagCompound nbt = new NBTTagCompound();
                        NBTUtil.func_152460_a(nbt, player.getGameProfile());
                        NBTHelper.setTag(stack, "profile", nbt);

                        NBTHelper.setBoolean(stack, "getStacks", false);
                        NBTHelper.setBoolean(stack, "withdraw", false);
                        NBTHelper.setBoolean(stack, "deposit", false);
                        player.addChatComponentMessage(new ChatComponentTranslation("peripheralsplusplus.chat.permCard.set"));
                    }
                    else
                    {
                        player.addChatComponentMessage(new ChatComponentTranslation("peripheralsplusplus.chat.permCard.alreadySet"));
                    }
                }
            }
            else
            {
                if (compound == null || NBTHelper.getTag(stack, "profile") == null)
                {
                    if (!world.isRemote)
                    {
                        player.addChatComponentMessage(new ChatComponentTranslation("peripheralsplusplus.chat.permCard.notSet"));
                    }
                    return stack;
                }
                if (!NBTUtil.func_152459_a(NBTHelper.getCompoundTag(stack, "profile")).getId().toString().equals(player.getGameProfile().getId().toString()))
                {
                    if (!world.isRemote)
                    {
                        player.addChatComponentMessage(new ChatComponentTranslation("peripheralsplusplus.chat.permCard.wrongOwner"));
                    }
                    return stack;
                }

                player.openGui(PeripheralsPlusPlus.instance, Reference.GUIs.PERMCARD.ordinal(), world, (int) player.posX, (int) player.posY, (int) player.posZ);
            }
        }
        return stack;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack)
    {
        GameProfile username = NBTUtil.func_152459_a(NBTHelper.getCompoundTag(stack, "profile"));
        return StatCollector.translateToLocal("item.peripheralsplusplus:permissionsCard.name") + (NBTHelper.getTag(stack, "profile") == null ? "" : " - " + username.getName());
    }
}
