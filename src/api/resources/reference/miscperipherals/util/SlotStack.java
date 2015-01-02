package miscperipherals.util;

import net.minecraft.item.ItemStack;

public class SlotStack {
	public ItemStack stack;
	public int slot;
	
	public SlotStack(ItemStack stack, int slot) {
		this.stack = stack;
		this.slot = slot;
	}
}