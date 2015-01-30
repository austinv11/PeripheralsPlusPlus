package com.austinv11.peripheralsplusplus.turtles.peripherals;

import com.austinv11.peripheralsplusplus.reference.Config;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.TurtleSide;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;

import java.util.HashMap;

public class PeripheralBarrel extends MountedPeripheral {

	private int MAX_SIZE = 4096;
	private int STACK_SIZE = 64;
	private Item ITEM_TYPE_STORED;
	private int CURRENT_USAGE = 0;
	private ITurtleAccess turtle;
	private TurtleSide side;
	public boolean changed = false;

	public PeripheralBarrel(ITurtleAccess turtle, TurtleSide side) {
		this.turtle = turtle;
		this.side = side;
		NBTTagCompound tag = turtle.getUpgradeNBTData(side);
		if (tag.getInteger("maxSize") > 0)
			MAX_SIZE = tag.getInteger("maxSize");
		if (tag.getInteger("stackSize") > 0)
			STACK_SIZE = tag.getInteger("stackSize");
		CURRENT_USAGE = tag.getInteger("currentUsage");
		if (tag.getBoolean("isKnown"))
			ITEM_TYPE_STORED = Item.getItemById(tag.getInteger("itemID"));
		checkUsageStats();
	}

	private void checkUsageStats() {
		if (CURRENT_USAGE <= 0 || ITEM_TYPE_STORED == null) {
			CURRENT_USAGE = 0;
			STACK_SIZE = 64;
			MAX_SIZE = 64 * STACK_SIZE;
			ITEM_TYPE_STORED = null;
		}
	}

	private String getUnwrappedUnlocalizedName(String unlocalizedName){//Removes the "item." from the item name
		return unlocalizedName.substring(unlocalizedName.indexOf(".")+1);
	}

	@Override
	public String getType() {
		return "barrel";
	}

	@Override
	public String[] getMethodNames() {
		return new String[]{"get", "put", "getUnlocalizedName", "getLocalizedName", "getItemID", "getAmount", "getOreDictEntries"};
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws LuaException, InterruptedException {
		if (!Config.enableBarrelTurtle)
			throw new LuaException("Barrel Turtles have been disabled");
		if (method == 0) {
			int amount = STACK_SIZE;
			if (arguments.length > 0) {
				if (!(arguments[0] instanceof Double))
					throw new LuaException("Bad argument #1 (expected number)");
				double temp = (Double) arguments[0];
				amount = (int) temp;
			}
			if (CURRENT_USAGE < amount)
				amount = CURRENT_USAGE;
			if (ITEM_TYPE_STORED == null)
				return new Object[]{0};
			ItemStack slot = turtle.getInventory().getStackInSlot(turtle.getSelectedSlot());
			Item itemStored = ITEM_TYPE_STORED;
			int stackCount = amount;
			if (slot != null) {
				if (!slot.isItemEqual(new ItemStack(itemStored)))
					throw new LuaException("Item mismatch");
				if (amount + slot.stackSize > STACK_SIZE)
					amount = STACK_SIZE - slot.stackSize;
				stackCount = amount + slot.stackSize;
			}
			ItemStack stack = new ItemStack(itemStored);
			stack.stackSize = stackCount;
			CURRENT_USAGE = CURRENT_USAGE - amount;
			checkUsageStats();
			turtle.getInventory().setInventorySlotContents(turtle.getSelectedSlot(), stack.copy());
			changed = true;
			return new Object[]{amount};
		} else if (method == 1) {
			int amount = 64;
			if (arguments.length > 0) {
				if (!(arguments[0] instanceof Double))
					throw new LuaException("Bad argument #1 (expected number)");
				double temp = (Double) arguments[0];
				amount = (int) temp;
			}
			if (turtle.getInventory().getStackInSlot(turtle.getSelectedSlot()) == null)
				return new Object[]{0};
			ItemStack items = turtle.getInventory().getStackInSlot(turtle.getSelectedSlot()).copy();
			if (amount > items.stackSize)
				amount = items.stackSize;
			if (amount > (MAX_SIZE - CURRENT_USAGE))
				amount = MAX_SIZE - CURRENT_USAGE;
			if (ITEM_TYPE_STORED != null) {
				ItemStack temp = new ItemStack(ITEM_TYPE_STORED);
				if (!temp.isItemEqual(items))
					return new Object[]{0};
			} else {
				Item type = items.getItem();
				ITEM_TYPE_STORED = type;
				STACK_SIZE = type.getItemStackLimit(items);
				MAX_SIZE = 64 * STACK_SIZE;
			}
			CURRENT_USAGE = CURRENT_USAGE + amount;
			ItemStack newStack = new ItemStack(items.getItem());
			if (items.stackSize - amount <= 0) {
				newStack = null;
			} else {
				newStack.stackSize = items.stackSize - amount;
			}
			turtle.getInventory().setInventorySlotContents(turtle.getSelectedSlot(), newStack);
			changed = true;
			return new Object[]{amount};
		} else if (method == 2) {
			if (ITEM_TYPE_STORED != null)
				return new Object[]{Item.itemRegistry.getNameForObject(ITEM_TYPE_STORED)};
		} else if (method == 3) {
			if (ITEM_TYPE_STORED != null)
				return new Object[]{new ItemStack(ITEM_TYPE_STORED).getDisplayName()};
		} else if (method == 4) {
			if (ITEM_TYPE_STORED != null)
				return new Object[]{Item.getIdFromItem(ITEM_TYPE_STORED)};
		} else if (method == 5) {
			int amount = 0;
			if (ITEM_TYPE_STORED != null)
				amount = CURRENT_USAGE;
			return new Object[]{amount};
		} else if (method == 6) {
			int[] ids = OreDictionary.getOreIDs(new ItemStack(ITEM_TYPE_STORED));
			HashMap<Integer, String> entries = new HashMap<Integer,String>();
			for (int i = 0; i < ids.length; i++) {
				entries.put(i, OreDictionary.getOreName(ids[i]));
			}
			return new Object[]{entries};
		}
		return new Object[0];
	}

	@Override
	public boolean equals(IPeripheral other) {
		return (this == other);
	}

	public void update() {
		NBTTagCompound tag = turtle.getUpgradeNBTData(side);
		tag.setInteger("maxSize", MAX_SIZE);
		tag.setInteger("stackSize", STACK_SIZE);
		tag.setInteger("currentUsage", CURRENT_USAGE);
		if (ITEM_TYPE_STORED == null) {
			tag.setBoolean("isKnown", false);
		}else {
			tag.setBoolean("isKnown", true);
			tag.setInteger("itemID", Item.getIdFromItem(ITEM_TYPE_STORED));
		}
		turtle.updateUpgradeNBTData(side);
		changed = false;
	}
}
