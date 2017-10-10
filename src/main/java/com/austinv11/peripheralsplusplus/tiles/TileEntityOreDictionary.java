package com.austinv11.peripheralsplusplus.tiles;

import com.austinv11.peripheralsplusplus.reference.Config;
import com.austinv11.peripheralsplusplus.utils.ChatUtil;
import com.austinv11.peripheralsplusplus.utils.Util;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.ITurtleAccess;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;

import java.util.HashMap;

public class TileEntityOreDictionary extends MountedTileEntity {

	public static String publicName = "oreDictionary";
	private String name = "tileEntityOreDictionary";
	private HashMap<IComputerAccess, Boolean> computers = new HashMap<IComputerAccess,Boolean>();
	private ITurtleAccess turtle = null;

	public TileEntityOreDictionary() {
		super();
	}

	public TileEntityOreDictionary(ITurtleAccess turtle) {
		super();
		this.turtle = turtle;
	}

	public String getName() {
		return name;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
		return nbttagcompound;
	}

	@Override
	public String getType() {
		return publicName;
	}

	private boolean isTurtle() {
		return !(turtle == null);
	}

	@Override
	public String[] getMethodNames() {
		if (isTurtle())
			return new String[]{"getEntries"/*getEntries() - returns table w/ all oredict entries*/, "combineStacks"/*combineStacks(slotNum,slotNum2) - returns boolean and places new stack in selected slot*/, "transmute"/*transmute() - transmutes selected items to next oreDict possibility*/, "doItemsMatch"/*doItemsMatch(slotNum, slotNum2) - returns whether the two items match*/};
		return new String[0];
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws LuaException, InterruptedException {
		if (!Config.enableOreDictionary)
			throw new LuaException("Ore Dictionaries have been disabled");
		try {
			if (method == 0) {
				ItemStack slot = turtle.getInventory().getStackInSlot(turtle.getSelectedSlot());
				if (slot.isEmpty())
					throw new LuaException("Empty slot");
				return new Object[]{Util.getOreDictEntries(slot)};
			} else if (method == 1) {
				if (!(arguments.length > 0) || !(arguments[0] instanceof Double))
					throw new LuaException("Bad argument #1 (expected number)");
				if (!(arguments.length > 1) || !(arguments[1] instanceof Double))
					throw new LuaException("Bad argument #2 (expected number)");
				int argSlot1 = ((int)(double)arguments[0]) - 1;
				int argSlot2 = ((int)(double)arguments[1]) - 1;
				if (argSlot1 == argSlot2)
					return new Object[]{true};
				ItemStack slot = turtle.getInventory().getStackInSlot(turtle.getSelectedSlot()).copy();
				ItemStack stack1 = turtle.getInventory().getStackInSlot(argSlot1).copy();
				ItemStack stack2 = turtle.getInventory().getStackInSlot(argSlot2).copy();
				if (stack1.isEmpty() || stack2.isEmpty())
					throw new LuaException("One or more selected slots have nil items");
				if (!Util.compareItemStacksViaOreDict(stack1, stack2))
					throw new LuaException("Stacks are not equivalent");
				if (!Util.compareItemStacksViaOreDict(stack1, slot) && !slot.isEmpty())
					throw new LuaException("The destination slot is incompatible");
				// First slot is selected
				if (argSlot1 == turtle.getSelectedSlot()) {
					// Second slot to output/first
					combineSlots(turtle.getSelectedSlot(), argSlot2);
					return new Object[]{true};
				}
				// Second slot is selected
				else if (argSlot2 == turtle.getSelectedSlot()) {
					// First slot to output/second
					combineSlots(turtle.getSelectedSlot(), argSlot1);
				}
				// Neither slot is selected
				else {
					// First slot to output
					combineSlots(turtle.getSelectedSlot(), argSlot1);
					// Second slot to first slot
					combineSlots(argSlot1, argSlot2);
					// First slot to output
					combineSlots(turtle.getSelectedSlot(), argSlot1);
				}
				return new Object[]{true};
			} else if (method == 2) {
				ItemStack slot = turtle.getInventory().getStackInSlot(turtle.getSelectedSlot()).copy();
				if (slot.isEmpty())
					throw new LuaException("Selected slot's item is nil");
				ItemStack newStack = transmute(slot);
				if (newStack.isEmpty() || newStack.isItemEqual(slot))
					return new Object[]{false};
				newStack.setCount(slot.getCount());
				turtle.getInventory().setInventorySlotContents(turtle.getSelectedSlot(), newStack);
				return new Object[]{true};
			} else if (method == 3) {
				if (!(arguments.length > 0) || !(arguments[0] instanceof Double))
					throw new LuaException("Bad argument #1 (expected number)");
				if (!(arguments.length > 1) || !(arguments[1] instanceof Double))
					throw new LuaException("Bad argument #2 (expected number)");
				double arg1 = (Double) arguments[0];
				double arg2 = (Double) arguments[1];
				arg1--;
				arg2--;
				return new Object[]{Util.compareItemStacksViaOreDict(turtle.getInventory().getStackInSlot((int) arg1), turtle.getInventory().getStackInSlot((int) arg2))};
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return new Object[0];
	}

	private void combineSlots(int outputSlotNum, int fillerSlotNum) {
		ItemStack output = turtle.getInventory().getStackInSlot(outputSlotNum);
		ItemStack filler = turtle.getInventory().getStackInSlot(fillerSlotNum);
		int freeAmount = output.getMaxStackSize() -  output.getCount();
		if (output.isEmpty()) {
			output = filler.copy();
			output.setCount(0);
		}
		if (filler.getCount() > freeAmount) {
			output.setCount(output.getCount() + freeAmount);
			filler.setCount(filler.getCount() - freeAmount);
		}
		else {
			output.setCount(output.getCount() + filler.getCount());
			filler.setCount(0);
		}
		turtle.getInventory().setInventorySlotContents(outputSlotNum, output);
		turtle.getInventory().setInventorySlotContents(fillerSlotNum, filler);
	}

	@Override
	public void attach(IComputerAccess computer) {
		if (!isTurtle())
			computers.put(computer, true);
		super.attach(computer);
	}

	@Override
	public void detach(IComputerAccess computer) {
		if (!isTurtle())
			computers.remove(computer);
		super.detach(computer);
	}

	@Override
	public boolean equals(IPeripheral other) {
		return (other == this);
	}

	private ItemStack transmute(ItemStack item) {
		HashMap<Integer, String> oreDictEntries = Util.getOreDictEntries(item);
		boolean returnNextItem = false;
		for (String oreDictName : oreDictEntries.values()) {
			NonNullList<ItemStack> otherDictEntries = OreDictionary.getOres(oreDictName);
			for (ItemStack stack : otherDictEntries) {
				if (returnNextItem)
					return stack.copy();
				if (stack.isItemEqual(item))
					returnNextItem = true;
			}
			if (returnNextItem)
				return otherDictEntries.get(0).copy();
		}
		return ItemStack.EMPTY;
	}

	public boolean blockActivated(EntityPlayer player) {
		if (!player.getHeldItemMainhand().isEmpty()) {
			for (IComputerAccess computer : computers.keySet()) {
				computer.queueEvent("oreDict", new Object[]{Util.getOreDictEntries(player.getHeldItemMainhand())});
			}
			if (Config.oreDictionaryMessage)
				ChatUtil.sendMessage(player.getDisplayNameString(), this,
                        Util.getOreDictEntries(player.getHeldItemMainhand()).entrySet().toString(), 100, true);
			return true;
		}
		return false;
	}
}
