package com.austinv11.peripheralsplusplus.tiles;

import com.austinv11.peripheralsplusplus.mount.DynamicMount;
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
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.oredict.OreDictionary;

import java.util.HashMap;

public class TileEntityOreDictionary extends TileEntity implements IPeripheral {

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
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
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
				return new Object[]{Util.getEntries(slot)};
			} else if (method == 1) {
				if (!(arguments.length > 0) || !(arguments[0] instanceof Double))
					throw new LuaException("Bad argument #1 (expected number)");
				if (!(arguments.length > 1) || !(arguments[1] instanceof Double))
					throw new LuaException("Bad argument #2 (expected number)");
				double arg1 = (Double) arguments[0];
				double arg2 = (Double) arguments[1];
				arg1--;
				arg2--;
				ItemStack slot = turtle.getInventory().getStackInSlot(turtle.getSelectedSlot());
				ItemStack stack1 = turtle.getInventory().getStackInSlot((int)arg1);
				ItemStack stack2 = turtle.getInventory().getStackInSlot((int)arg2);
				if (stack1 == null || stack2 == null)
					throw new LuaException("One or more selected slots have nil items");
				if (!Util.compare(stack1, stack2))
					return new Object[]{false};
				if (!Util.compare(stack1, slot) && slot != null)
					throw new LuaException("The destination slot is incompatible");
				int maxMoveSize = 0;
				if (slot != null) {
					maxMoveSize = slot.getMaxStackSize() - slot.stackSize;
				}else {
					maxMoveSize = stack1.getMaxStackSize() - stack1.stackSize;
				}
				int move1 = stack1.stackSize;
				int move2 = stack2.stackSize;
				if (move1 + move2 > maxMoveSize) {
					if (move1 >= maxMoveSize)
						move1 = maxMoveSize;
					move2 = maxMoveSize - move1;
				}
				stack1.stackSize = stack1.stackSize - move1;
				stack2.stackSize = stack2.stackSize - move2;
				if (slot != null) {
					slot.stackSize = move1 + move2;
					turtle.getInventory().setInventorySlotContents(turtle.getSelectedSlot(), slot.copy());
					if (stack1.stackSize <= 0) {
						turtle.getInventory().setInventorySlotContents((int) arg1, null);
					}else {
						turtle.getInventory().setInventorySlotContents((int) arg1, stack1.copy());
					}
					if (stack2.stackSize <= 0) {
						turtle.getInventory().setInventorySlotContents((int) arg2, null);
					}else {
						turtle.getInventory().setInventorySlotContents((int) arg2, stack2.copy());
					}
				}else {
					ItemStack newStack = new ItemStack(stack1.getItem());
					newStack.stackSize = move1 + move2;
					turtle.getInventory().setInventorySlotContents(turtle.getSelectedSlot(), newStack.copy());
					if (stack1.stackSize <= 0) {
						turtle.getInventory().setInventorySlotContents((int) arg1, null);
					}else {
						turtle.getInventory().setInventorySlotContents((int) arg1, stack1.copy());
					}
					if (stack2.stackSize <= 0) {
						turtle.getInventory().setInventorySlotContents((int) arg2, null);
					}else {
						turtle.getInventory().setInventorySlotContents((int) arg2, stack2.copy());
					}
				}
				return new Object[]{true};
			} else if (method == 2) {
				ItemStack slot = turtle.getInventory().getStackInSlot(turtle.getSelectedSlot());
				if (slot == null)
					throw new LuaException("Selected slot's item is nil");
				ItemStack newStack = transmute(slot);
				if (newStack == null || newStack.isItemEqual(slot))
					return new Object[]{false};
				newStack.stackSize = slot.stackSize;
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
				return new Object[]{Util.compare(turtle.getInventory().getStackInSlot((int) arg1), turtle.getInventory().getStackInSlot((int) arg2))};
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return new Object[0];
	}

	@Override
	public void attach(IComputerAccess computer) {
		//Logger.info(":D");
		if (!isTurtle())
			computers.put(computer, true);
		computer.mount(DynamicMount.DIRECTORY, new DynamicMount(this));
	}

	@Override
	public void detach(IComputerAccess computer) {
		if (!isTurtle())
			computers.remove(computer);
		computer.unmount(DynamicMount.DIRECTORY);
	}

	@Override
	public boolean equals(IPeripheral other) {
		return (other == this);
	}

	private ItemStack transmute(ItemStack item) {
		HashMap<Integer, String> entries = Util.getEntries(item);
		int i = 0;
		boolean test = false;
		for (String v : entries.values()) {
			for (ItemStack stack : OreDictionary.getOres(v)) {
				if (test)
					return stack.copy();
				if (stack.isItemEqual(item)) {
					test = true;
				}
				i++;
			}
			if (test)
				return OreDictionary.getOres(v).get(0).copy();
			i = 0;
		}
		return null;
	}

	public void blockActivated(EntityPlayer player) {
		if (player.getHeldItem() != null) {
			for (IComputerAccess computer : computers.keySet()) {
				computer.queueEvent("oreDict", new Object[]{Util.getEntries(player.getHeldItem())});
			}
			if (Config.oreDictionaryMessage)
				ChatUtil.sendMessage(player.getDisplayName(), this, new ChatComponentText(Util.getEntries(player.getHeldItem()).entrySet().toString()), 100, true);
		}
	}
}
