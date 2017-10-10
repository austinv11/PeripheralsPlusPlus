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
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class ItemPermissionsCard extends ItemPPP
{
    public ItemPermissionsCard()
    {
        super();
        this.setUnlocalizedName("permissions_card");
        this.setRegistryName(Reference.MOD_ID, "permissions_card");
        this.setMaxStackSize(1);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
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
                        NBTUtil.writeGameProfile(nbt, player.getGameProfile());
                        NBTHelper.setTag(stack, "profile", nbt);

                        NBTHelper.setBoolean(stack, "getStacks", false);
                        NBTHelper.setBoolean(stack, "withdraw", false);
                        NBTHelper.setBoolean(stack, "deposit", false);
                        player.sendMessage(new TextComponentTranslation(
                                "peripheralsplusplus.chat.permCard.set"));
                    }
                    else
                    {
                        player.sendMessage(new TextComponentTranslation(
                                "peripheralsplusplus.chat.permCard.alreadySet"));
                        return new ActionResult<>(EnumActionResult.FAIL, stack);
                    }
                }
            }
            else
            {
                if (compound == null || NBTHelper.getTag(stack, "profile") == null)
                {
                    if (!world.isRemote)
                    {
                        player.sendMessage(new TextComponentTranslation(
                                "peripheralsplusplus.chat.permCard.notSet"));
                    }
                    return new ActionResult<>(EnumActionResult.FAIL, stack);
                }
                GameProfile gameProfile = NBTUtil.readGameProfileFromNBT(NBTHelper.getCompoundTag(stack,
                        "profile"));
                if (gameProfile == null || !gameProfile.getId().equals(player.getGameProfile().getId()))
                {
                    if (!world.isRemote)
                    {
                        player.sendMessage(new TextComponentTranslation(
                                "peripheralsplusplus.chat.permCard.wrongOwner"));
                    }
                    return new ActionResult<>(EnumActionResult.FAIL, stack);
                }

                player.openGui(PeripheralsPlusPlus.instance, Reference.GUIs.PERMCARD.ordinal(), world,
                        (int) player.posX, (int) player.posY, (int) player.posZ);
            }
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack)
    {
        GameProfile username = NBTUtil.readGameProfileFromNBT(NBTHelper.getCompoundTag(stack, "profile"));
        return I18n.translateToLocal("item.peripheralsplusplus:permissions_card.name") +
                (NBTHelper.getTag(stack, "profile") == null ? "" : " - " +
                        (username.getName() == null ? "" : username.getName()));
    }
}
