package com.austinv11.peripheralsplusplus.tiles;

import com.austinv11.collectiveframework.minecraft.utils.NBTHelper;
import com.austinv11.peripheralsplusplus.lua.LuaObjectPlayerInv;
import com.austinv11.peripheralsplusplus.reference.Config;
import com.mojang.authlib.GameProfile;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;

import java.util.UUID;

public class TileEntityPlayerInterface extends MountedTileEntityInventory {
    public static final String publicName = "playerInterfacePPP";
    public EnumFacing outputSide;
    public EnumFacing inputSide;

    public TileEntityPlayerInterface() {
        super();
        this.invName = "PlayerInterface";
    }
    
    @Override
    public int getSize() {
        return 8;
    }
    
    @Override
    public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws LuaException, InterruptedException {
        if (Config.enablePlayerInterface)
        {
            if (method == 0)
            {
                if (arguments.length != 1)
                {
                    throw new LuaException("Wrong number of arguments. 1 expected.");
                }
                if (!(arguments[0] instanceof String))
                {
                    throw new LuaException("Bad argument #1 (expected string)");
                }
                // Check that the specified player exists
                for (WorldServer worldServer : DimensionManager.getWorlds())
                    for (EntityPlayer player : worldServer.playerEntities) {
                        if (player.getDisplayNameString().equals(arguments[0])) {
                            // Check that the specified player has given permission for some sort of
                            // editing by putting their permissions card in the player interface
                            if (hasPermissionsCardFor(player) || !Config.enableInterfacePermissions) {
                                return new Object[]{new LuaObjectPlayerInv(player, this,
                                        getPermCardFor(player))};
                            } else {
                                throw new LuaException("Missing permissions for player " + arguments[0]);
                            }
                        }
                    }
                throw new LuaException("Player not found");
            }
            else if (method == 1 || method == 2)
            {
                if (arguments.length != 1)
                {
                    throw new LuaException("Wrong number of arguments. 1 expected.");
                }
                if (!(arguments[0] instanceof String))
                {
                    throw new LuaException("Bad argument #1 (expected string)");
                }
                if (method == 1)
                {
                    outputSide = EnumFacing.valueOf(((String) arguments[0]).toUpperCase());
                }
                else
                {
                    inputSide = EnumFacing.valueOf(((String) arguments[0]).toUpperCase());
                }
            }
            else if (method == 3)
            {
                if (outputSide == null)
                    return new Object[0];
                return new Object[]{outputSide.toString()};
            }
            else if (method == 4)
            {
                if (inputSide == null)
                    return new Object[0];
                return new Object[]{inputSide.toString()};
            }
        }
        else
        {
            throw new LuaException("Player Interfaces have been disabled");
        }
        return new Object[0];
    }

    @Override
    public boolean equals(IPeripheral other) {
        return this == other;
    }

    @Override
    public String[] getMethodNames() {
        return new String[]{"getPlayerInv", "setOutputSide", "setInputSide", "getOutputSide", "getInputSide"};
    }

    @Override
    public String getType() {
        return publicName;
    }

    private boolean hasPermissionsCardFor(EntityPlayer player) {
        return !getPermCardFor(player).isEmpty();
    }

    private ItemStack getPermCardFor(EntityPlayer player) {
        for (ItemStack stack : items) {
            if (stack != null) {
                if (stack.hasTagCompound()) {
                    GameProfile profile = NBTUtil.readGameProfileFromNBT(NBTHelper.getCompoundTag(stack, "profile"));
                    if (profile == null)
                        return ItemStack.EMPTY;
                    UUID uuid = profile.getId();
                    if (uuid.equals(player.getGameProfile().getId())) {
                        return stack;
                    }
                }
            }
        }
        return ItemStack.EMPTY;
    }
}
