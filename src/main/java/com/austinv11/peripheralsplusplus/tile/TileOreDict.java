package com.austinv11.peripheralsplusplus.tile;

import com.austinv11.peripheralsplusplus.util.IPlusPlusPeripheral;
import com.austinv11.peripheralsplusplus.util.Util;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

public class TileOreDict extends TileEntityInventory implements IPlusPlusPeripheral {
	public static final String name = "tileOreDict";

	@Override
	public String[] getMethodNames() {
		return new String[] {"transmute"};
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws LuaException, InterruptedException {
		switch (method) {
			case 0:
				ItemStack stack = this.getStackInSlot(0);
				if (stack != null) {
					this.setInventorySlotContents(0, transmute(stack));
				}
				break;
		}
		return new Object[0];
	}

	private ItemStack transmute(ItemStack stack) {
		String[] entries = Util.getOreDictEntries(stack);
		entries:
		for (String entry : entries) {
			List<ItemStack> ores = OreDictionary.getOres(entry);
			for (int i = 0; i < ores.size(); i++) {
				if (ores.get(i).getItem() == stack.getItem()) {
					try {
						stack.setItem(ores.get(i + 1).getItem());
						break entries;
					} catch (IndexOutOfBoundsException e) {
						if (ores.get(0) != null) {
							stack.setItem(ores.get(0).getItem());
							break entries;
						}
					}
				}
			}
		}
		return stack;
	}

	@Override
	public String getType() {
		return "oreDict";
	}

	@Override
	public boolean equals(IPeripheral other) {
		return other == this;
	}

	@Override
	public void attach(IComputerAccess computer) {

	}

	@Override
	public void detach(IComputerAccess computer) {

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
