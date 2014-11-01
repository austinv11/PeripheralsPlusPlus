package com.austinv11.peripheralsplusplus.tiles;

import com.austinv11.peripheralsplusplus.reference.Config;
import com.austinv11.peripheralsplusplus.utils.ChatUtil;
import com.austinv11.peripheralsplusplus.utils.Logger;
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
		return "oreDictionary";
	}

	private boolean isTurtle() {
		return !(turtle == null);
	}

	@Override
	public String[] getMethodNames() {
		if (isTurtle())
			return new String[]{"getEntries"/*getEntries() - returns table w/ all oredict entries*/, "combineStacks"/*combineStacks(slotNum,slotNum2) - returns boolean and places new stack in selected slot*/, "transmute"/*transmute() - transmutes selected items to next oreDict possibility*/};
		return new String[0];
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws LuaException, InterruptedException {
		return new Object[0];
	}

	@Override
	public void attach(IComputerAccess computer) {
		//Logger.info(":D");
		if (!isTurtle())
			computers.put(computer, true);
	}

	@Override
	public void detach(IComputerAccess computer) {
		if (!isTurtle())
			computers.remove(computer);
	}

	@Override
	public boolean equals(IPeripheral other) {
		return (other == this);
	}

	private HashMap<Integer, String> getEntries(ItemStack stack) {
		int[] ids = OreDictionary.getOreIDs(stack);
		HashMap<Integer, String> entries = new HashMap<Integer,String>();
		for (int i = 0; i < ids.length; i++) {
			entries.put(i, OreDictionary.getOreName(ids[i]));
		}
		return entries;
	}

	public void blockActivated(EntityPlayer player) {
		if (player.getHeldItem() != null) {
			for (IComputerAccess computer : computers.keySet()) {
				computer.queueEvent("oreDict", new Object[]{getEntries(player.getHeldItem())});
			}
			if (Config.oreDictionaryMessage)
				ChatUtil.sendMessage(player.getDisplayName(), this, new ChatComponentText(getEntries(player.getHeldItem()).entrySet().toString()), 100, true);
		}
	}
}
