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
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;

import java.util.HashMap;

public class PeripheralBarrel extends MountedPeripheral {

	private int MAX_SIZE = 4096;
	private int STACK_SIZE = 64;
	private Item ITEM_TYPE_STORED;
    private int ITEM_META_STORED = 0;
	private int CURRENT_USAGE = 0;
	private ITurtleAccess turtle;
	private TurtleSide side;
	public boolean changed = false;
	private NBTTagCompound itemStoredTag;

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
        {
            ITEM_TYPE_STORED = ForgeRegistries.ITEMS.getValue(new ResourceLocation(tag.getString("itemID")));
            ITEM_META_STORED = tag.getInteger("stackMeta");
            if (tag.hasKey("itemTag"))
            	itemStoredTag = tag.getCompoundTag("itemTag");
        }
		checkUsageStats();
	}

	private void checkUsageStats() {
		if (CURRENT_USAGE <= 0 || ITEM_TYPE_STORED == null) {
			CURRENT_USAGE = 0;
			STACK_SIZE = 64;
			MAX_SIZE = 64 * STACK_SIZE;
			ITEM_TYPE_STORED = null;
            ITEM_META_STORED = 0;
            itemStoredTag = null;
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
		return new String[]{"get", "put", "getUnlocalizedName", "getLocalizedName", "getItemID", "getAmount",
				"getOreDictEntries", "getNbtTag"};
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
                amount = (int) (double) (Double) arguments[0];
			}
			if (CURRENT_USAGE < amount)
				amount = CURRENT_USAGE;
			if (ITEM_TYPE_STORED == null)
				return new Object[]{0};
			ItemStack slot = turtle.getInventory().getStackInSlot(turtle.getSelectedSlot());
			Item itemStored = ITEM_TYPE_STORED;
			int stackCount = amount;
			if (!slot.isEmpty()) {
				ItemStack compareStack = new ItemStack(itemStored, stackCount, ITEM_META_STORED);
				compareStack.setTagCompound(itemStoredTag);
				if (!slot.isItemEqual(compareStack) || !areItemStackTagsEqual(slot, compareStack))
					throw new LuaException("Item mismatch");
				if (amount + slot.getCount() > STACK_SIZE)
					amount = STACK_SIZE - slot.getCount();
				stackCount = amount + slot.getCount();
			}
			ItemStack stack = new ItemStack(itemStored, stackCount, ITEM_META_STORED);
			stack.setTagCompound(itemStoredTag);
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
                amount = (int) (double) (Double) arguments[0];
			}
			if (turtle.getInventory().getStackInSlot(turtle.getSelectedSlot()).isEmpty())
				return new Object[]{0};
			ItemStack items = turtle.getInventory().getStackInSlot(turtle.getSelectedSlot()).copy();
			if (amount > items.getCount())
				amount = items.getCount();
			if (amount > (MAX_SIZE - CURRENT_USAGE))
				amount = MAX_SIZE - CURRENT_USAGE;
			if (ITEM_TYPE_STORED != null) {
				ItemStack temp = new ItemStack(ITEM_TYPE_STORED, 1, ITEM_META_STORED);
				temp.setTagCompound(itemStoredTag);
				if (!temp.isItemEqual(items) || !areItemStackTagsEqual(temp, items))
					throw new LuaException("Item mismatch");
			} else {
				Item type = items.getItem();
				ITEM_TYPE_STORED = type;
                ITEM_META_STORED = items.getItemDamage();
                itemStoredTag = items.getTagCompound();
				STACK_SIZE = type.getItemStackLimit(items);
				MAX_SIZE = 64 * STACK_SIZE;
			}
			CURRENT_USAGE = CURRENT_USAGE + amount;
			ItemStack newStack = new ItemStack(items.getItem());
            newStack.setItemDamage(ITEM_META_STORED);
			if (items.getCount() - amount <= 0) {
				newStack = ItemStack.EMPTY;
			} else {
				newStack.setCount(items.getCount() - amount);
			}
			turtle.getInventory().setInventorySlotContents(turtle.getSelectedSlot(), newStack);
			changed = true;
			return new Object[]{amount};
		} else if (method == 2) {
			if (ITEM_TYPE_STORED != null)
				return new Object[]{ITEM_TYPE_STORED.getUnlocalizedName()};
		} else if (method == 3) {
			if (ITEM_TYPE_STORED != null)
				return new Object[]{new ItemStack(ITEM_TYPE_STORED).getDisplayName()};
		} else if (method == 4) {
			if (ITEM_TYPE_STORED != null)
				return new Object[]{ForgeRegistries.ITEMS.getKey(ITEM_TYPE_STORED) != null ?
						ForgeRegistries.ITEMS.getKey(ITEM_TYPE_STORED).toString() : null};
		} else if (method == 5) {
			int amount = 0;
			if (ITEM_TYPE_STORED != null)
				amount = CURRENT_USAGE;
			return new Object[]{amount};
		} else if (method == 6) {
			int[] ids = OreDictionary.getOreIDs(new ItemStack(ITEM_TYPE_STORED));
			HashMap<Integer, String> entries = new HashMap<>();
			for (int i = 0; i < ids.length; i++) {
				entries.put(i, OreDictionary.getOreName(ids[i]));
			}
			return new Object[]{entries};
		} else if (method == 7)
			if (itemStoredTag != null)
				return new Object[]{itemStoredTag.toString()};
		return new Object[0];
	}

	private boolean areItemStackTagsEqual(ItemStack itemFirst, ItemStack itemSecond) {
		return itemFirst.getTagCompound() == null && itemSecond.getTagCompound() == null ||
				itemFirst.getTagCompound() != null && itemSecond.getTagCompound() != null &&
						itemFirst.getTagCompound().equals(itemSecond.getTagCompound());
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
			ResourceLocation itemId = ForgeRegistries.ITEMS.getKey(ITEM_TYPE_STORED);
			tag.setString("itemID", itemId == null ? "" : itemId.toString());
            tag.setInteger("stackMeta", ITEM_META_STORED);
            if (itemStoredTag != null)
            	tag.setTag("itemTag", itemStoredTag);
		}
		turtle.updateUpgradeNBTData(side);
		changed = false;
	}
}
