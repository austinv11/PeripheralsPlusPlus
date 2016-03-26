package com.austinv11.peripheralsplusplus.tile;

import com.austinv11.peripheralsplusplus.util.CCMethod;
import com.austinv11.peripheralsplusplus.util.IPlusPlusPeripheral;
import com.austinv11.peripheralsplusplus.util.Util;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

import java.util.Arrays;
import java.util.HashMap;

public class TileSorter extends TileEntityInventory implements IPlusPlusPeripheral {
	public static final String name = "tileSorter";

	@CCMethod
	public boolean pull(Object[] arguments) throws LuaException {
		if (arguments.length < 3)
			throw new LuaException("Wrong number of arguments. Expected 3.");
		if (!(arguments[0] instanceof String))
			throw new LuaException("Bad argument #1. Expected string.");
		if (!(arguments[1] instanceof Double))
			throw new LuaException("Bad argument #2. Expected number.");
		if (!(arguments[2] instanceof Double))
			throw new LuaException("Bad argument #3. Expected number.");

		EnumFacing direction = EnumFacing.byName((String) arguments[0]);
		TileEntity tile = getWorld().getTileEntity(new BlockPos(getPos().getX() + direction.getFrontOffsetX(), getPos().getY() + direction.getFrontOffsetY(), getPos().getZ() + direction.getFrontOffsetZ()));
		if (!(tile instanceof IInventory))
			return false;

		int slotIndex = (int) (double) (Double) arguments[1];
		int amount = (int) (double) (Double) arguments[2];

		ItemStack pullStack = ((IInventory) tile).getStackInSlot(slotIndex);
		if (pullStack == null)
			return false;

		if (amount > pullStack.stackSize)
			amount = pullStack.stackSize;

		if (this.getStackInSlot(0) == null) {
			this.setInventorySlotContents(0, new ItemStack(pullStack.getItem(), amount));
			((IInventory) tile).setInventorySlotContents(slotIndex, new ItemStack(pullStack.getItem(), pullStack.stackSize - amount));
		} else {
			if (this.getStackInSlot(0).getItem() != pullStack.getItem()) {
				return false;
			}
			int remaining = this.getStackInSlot(0).getMaxStackSize() - this.getStackInSlot(0).stackSize;

			int size = this.getStackInSlot(0).stackSize;
			this.setInventorySlotContents(0, new ItemStack(pullStack.getItem(), amount >= remaining ? 64 : this.getStackInSlot(0).stackSize + amount));
			((IInventory) tile).setInventorySlotContents(slotIndex, new ItemStack(pullStack.getItem(), pullStack.stackSize - (this.getStackInSlot(0).stackSize - size)));
			if (((IInventory) tile).getStackInSlot(slotIndex).stackSize <= 0) {
				((IInventory) tile).setInventorySlotContents(slotIndex, null);
			}
		}
		return true;
	}

	@CCMethod
	public boolean push(Object[] arguments) throws LuaException {
		if (arguments.length < 3)
			throw new LuaException("Wrong number of arguments. Expected 3.");
		if (!(arguments[0] instanceof String))
			throw new LuaException("Bad argument #1. Expected string.");
		if (!(arguments[1] instanceof Double))
			throw new LuaException("Bad argument #2. Expected number.");
		if (!(arguments[2] instanceof Double))
			throw new LuaException("Bad argument #3. Expected number.");

		EnumFacing direction = EnumFacing.byName((String) arguments[0]);
		TileEntity tile = getWorld().getTileEntity(new BlockPos(getPos().getX() + direction.getFrontOffsetX(), getPos().getY() + direction.getFrontOffsetY(), getPos().getZ() + direction.getFrontOffsetZ()));
		if (!(tile instanceof IInventory))
			return false;

		int toSlot = (int) (double) (Double) arguments[1];
		int amount = (int) (double) (Double) arguments[2];

		ItemStack pushStack = this.getStackInSlot(0);
		ItemStack invStack = ((IInventory) tile).getStackInSlot(toSlot);

		if (pushStack == null)
			return false;

		if (amount > pushStack.stackSize)
			amount = pushStack.stackSize;

		if (invStack != null) {
			if (invStack.getItem() != pushStack.getItem()) {
				return false;
			}

			try {
				int remaining = ((IInventory) tile).getStackInSlot(0).getMaxStackSize() - ((IInventory) tile).getStackInSlot(0).stackSize;

				int size = ((IInventory) tile).getStackInSlot(0).stackSize;

				((IInventory) tile).setInventorySlotContents(0, new ItemStack(invStack.getItem(), amount >= remaining ? 64 : ((IInventory) tile).getStackInSlot(0).stackSize + amount));
				this.setInventorySlotContents(0, new ItemStack(pushStack.getItem(), pushStack.stackSize - (((IInventory) tile).getStackInSlot(toSlot).stackSize - size)));
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else {
			((IInventory) tile).setInventorySlotContents(toSlot, new ItemStack(pushStack.getItem(), amount));
			this.decrStackSize(0, amount);
		}
		return true;
	}

	@CCMethod
	public HashMap<String, Object> analyze(Object[] arguments) {
		ItemStack curStack = this.getStackInSlot(0);
		HashMap<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("name", curStack.getUnlocalizedName());
		returnMap.put("size", curStack.stackSize);
		returnMap.put("meta", curStack.getMetadata());
		returnMap.put("nbt", curStack.serializeNBT().toString());
		returnMap.put("entries", Util.listToIndexedMap(Arrays.asList(Util.getOreDictEntries(curStack))));

		return returnMap;
	}

	@CCMethod
	public boolean isInventoryPresent(Object[] arguments) throws LuaException {
		if (arguments.length < 1)
			throw new LuaException("Wrong number of arguments. 1 expected.");
		if (!(arguments[0] instanceof String))
			throw new LuaException("Bad argument #1. Expected string.");

		EnumFacing direction = EnumFacing.byName((String) arguments[0]);
		TileEntity tile = getWorld().getTileEntity(new BlockPos(getPos().getX() + direction.getFrontOffsetX(), getPos().getY() + direction.getFrontOffsetY(), getPos().getZ() + direction.getFrontOffsetZ()));

		return (tile != null && tile instanceof IInventory);
	}

	@Override
	public String getType() {
		return "sorter";
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public int getSizeInventory() {
		return 1;
	}
}
