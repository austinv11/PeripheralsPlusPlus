package com.austinv11.peripheralsplusplus.tile;

import com.austinv11.peripheralsplusplus.lua.LuaObjectPlayerInv;
import com.austinv11.peripheralsplusplus.reference.Config;
import com.austinv11.peripheralsplusplus.util.CCMethod;
import com.austinv11.peripheralsplusplus.util.IPlusPlusPeripheral;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;

import java.util.UUID;

public class TilePlayerInterface extends TileEntityInventory implements IPlusPlusPeripheral {
	public static final String name = "tilePlayerInterface";
	public EnumFacing outputSide;
	public EnumFacing inputSide;

	@CCMethod
	public LuaObjectPlayerInv getPlayerInv(Object[] arguments) throws LuaException {
		if (arguments.length != 1) {
			throw new LuaException("Wrong number of arguments. 1 expected.");
		}
		if (!(arguments[0] instanceof String)) {
			throw new LuaException("Bad argument #1 (expected string)");
		}
		// Check that the specified player exists
		for (EntityPlayer player : MinecraftServer.getServer().getConfigurationManager().playerEntityList) {
			if (player.getDisplayName().getUnformattedText().equals(arguments[0])) {
				// Check that the specified player has given permission for some sort of editing by putting their permissions card in the player interface
				if (hasPermissionsCardFor(player) || !Config.enableInterfacePermissions) {
					return new LuaObjectPlayerInv(player, this, getPermCardFor(player));
				} else {
					throw new LuaException("Missing permissions for player " + arguments[0]);
				}
			}
		}
		return null;
	}

	@CCMethod
	public void setOutputSide(Object[] arguments) throws LuaException {
		if (arguments.length != 1) {
			throw new LuaException("Wrong number of arguments. 1 expected.");
		}
		if (!(arguments[0] instanceof String)) {
			throw new LuaException("Bad argument #1 (expected string)");
		}
		outputSide = EnumFacing.valueOf(((String) arguments[0]).toUpperCase());
	}

	@CCMethod
	public void setInputSide(Object[] arguments) throws LuaException {
		if (arguments.length != 1) {
			throw new LuaException("Wrong number of arguments. 1 expected.");
		}
		if (!(arguments[0] instanceof String)) {
			throw new LuaException("Bad argument #1 (expected string)");
		}
		inputSide = EnumFacing.valueOf(((String) arguments[0]).toUpperCase());
	}

	@CCMethod
	public String getOutputSide(Object[] arguments) {
		return outputSide == null ? null : outputSide.name();
	}

	@CCMethod
	public String getInputSide(Object[] arguments) {
		return inputSide == null ? null : inputSide.name();
	}

	private boolean hasPermissionsCardFor(EntityPlayer player) {
		return getPermCardFor(player) != null;
	}

	private ItemStack getPermCardFor(EntityPlayer player) {
		for (ItemStack stack : inventory) {
			if (stack != null) {
				if (stack.hasTagCompound()) {
					UUID uuid = NBTUtil.readGameProfileFromNBT(stack.getTagCompound().getCompoundTag("profile")).getId();
					if (uuid.equals(player.getGameProfile().getId())) {
						return stack;
					}
				}
			}
		}
		return null;
	}

	@Override
	public String getType() {
		return "playerInterface";
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public int getSizeInventory() {
		return 9;
	}
}
