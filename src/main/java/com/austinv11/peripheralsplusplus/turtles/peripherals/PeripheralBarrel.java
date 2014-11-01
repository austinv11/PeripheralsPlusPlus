package com.austinv11.peripheralsplusplus.turtles.peripherals;

import com.austinv11.peripheralsplusplus.reference.Config;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.TurtleSide;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class PeripheralBarrel implements IPeripheral {

	private int MAX_SIZE = 4096;
	private int STACK_SIZE = 64;
	private Object ITEM_TYPE_STORED = null;
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
		if (tag.getBoolean("isKnown")) {
			if (tag.getBoolean("isBlock")) {
				ITEM_TYPE_STORED = Block.getBlockById(tag.getInteger("itemID"));
			}else {
				ITEM_TYPE_STORED = Item.getItemById(tag.getInteger("itemID"));
			}
		}
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
		return new String[]{"get", "put", "getItem"};
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws LuaException, InterruptedException {
		if (!Config.enableBarrelTurtle)
			throw new LuaException("Barrel Turtles have been disabled");
		if (method == 0) {
			int amount = STACK_SIZE;
			if (arguments.length > 0) {
				if (!(arguments[0] instanceof Integer))
					throw new LuaException("Bad argument #1 (expected number)");
				amount = (Integer) arguments[0];
			}
			if (CURRENT_USAGE < amount)
				amount = CURRENT_USAGE;
			if (ITEM_TYPE_STORED == null)
				return new Object[]{0};
			ItemStack slot = turtle.getInventory().getStackInSlot(turtle.getSelectedSlot());
			if (ITEM_TYPE_STORED instanceof Block) {
				Block blockStored = (Block) ITEM_TYPE_STORED;
				if (slot != null) {
					if (!slot.isItemEqual(new ItemStack(blockStored)))
						throw new LuaException("Block mismatch");
					amount = amount - slot.stackSize;
				}
				ItemStack stack = new ItemStack(blockStored);
				stack.stackSize = amount + slot.stackSize;
				CURRENT_USAGE = CURRENT_USAGE - amount;
				checkUsageStats();
				turtle.getInventory().setInventorySlotContents(turtle.getSelectedSlot(), stack);
				changed = true;
				return new Object[]{amount};
			}else if (ITEM_TYPE_STORED instanceof Item) {
				Item itemStored = (Item) ITEM_TYPE_STORED;
				if (slot != null) {
					if (!slot.isItemEqual(new ItemStack(itemStored)))
						throw new LuaException("Item mismatch");
					amount = amount - slot.stackSize;
				}
				ItemStack stack = new ItemStack(itemStored);
				stack.stackSize = amount + slot.stackSize;
				CURRENT_USAGE = CURRENT_USAGE - amount;
				checkUsageStats();
				turtle.getInventory().setInventorySlotContents(turtle.getSelectedSlot(), stack);
				changed = true;
				return new Object[]{amount};
			}
			throw new LuaException("Unknown storage exception - please tell the mod author");
		}else if (method == 1) {
			int amount = 64;
			if (arguments.length > 0) {
				if (!(arguments[0] instanceof Integer))
					throw new LuaException("Bad argument #1 (expected number)");
				amount = (Integer) arguments[0];
			}
			ItemStack items = turtle.getInventory().getStackInSlot(turtle.getSelectedSlot());
			if (amount > items.stackSize)
				amount = items.stackSize;
			if (amount > (MAX_SIZE - CURRENT_USAGE))
				amount = MAX_SIZE - CURRENT_USAGE;
			if (ITEM_TYPE_STORED != null) {
				if (ITEM_TYPE_STORED instanceof Block) {
					ItemStack temp = new ItemStack((Block) ITEM_TYPE_STORED);
					if (!temp.isItemEqual(items))
						throw new LuaException("Block mismatch");
				}else if (ITEM_TYPE_STORED instanceof Item) {
					ItemStack temp = new ItemStack((Item) ITEM_TYPE_STORED);
					if (!temp.isItemEqual(items))
						throw new LuaException("Item mismatch");
				}
				throw new LuaException("Unknown storage exception - please tell the mod author");
			}else {
				if (items.getItem() instanceof ItemBlock) {
					Block type = Block.getBlockFromItem(items.getItem());
					ITEM_TYPE_STORED = type;
					STACK_SIZE = 64;
					MAX_SIZE = 64 * STACK_SIZE;
				}else {
					Item type = items.getItem();
					ITEM_TYPE_STORED = type;
					STACK_SIZE = type.getItemStackLimit(items);
					MAX_SIZE = 64 * STACK_SIZE;
				}
			}
			CURRENT_USAGE = CURRENT_USAGE + amount;
			ItemStack newStack = new ItemStack(items.getItem());
			if (items.stackSize - amount <= 0) {
				newStack = null;
			}else {
				newStack.stackSize = items.stackSize - amount;
			}
			turtle.getInventory().setInventorySlotContents(turtle.getSelectedSlot(), newStack);
			changed = true;
			return new Object[]{amount};
		}else if (method == 2) {
			if (ITEM_TYPE_STORED != null) {
				if (ITEM_TYPE_STORED instanceof Block) {
					return new Object[] {getUnwrappedUnlocalizedName(((Block)ITEM_TYPE_STORED).getUnlocalizedName())};
				}else if (ITEM_TYPE_STORED instanceof Item){
					return new Object[] {getUnwrappedUnlocalizedName(((Item)ITEM_TYPE_STORED).getUnlocalizedName())};
				}
				throw new LuaException("Unknown storage exception - please tell the mod author");
			}
		}
		return new Object[0];
	}

	@Override
	public void attach(IComputerAccess computer) {}

	@Override
	public void detach(IComputerAccess computer) {}

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
			if (ITEM_TYPE_STORED instanceof Block) {
				tag.setBoolean("isBlock", true);
				tag.setInteger("itemID", Block.getIdFromBlock((Block)ITEM_TYPE_STORED));
			}else {
				tag.setBoolean("isBlock", false);
				tag.setInteger("itemID", Item.getIdFromItem((Item)ITEM_TYPE_STORED));
			}
		}
	}
}
