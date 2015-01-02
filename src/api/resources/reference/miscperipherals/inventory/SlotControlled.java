package miscperipherals.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotControlled extends Slot {
	public SlotControlled(IInventory par1iInventory, int par2, int par3, int par4) {
		super(par1iInventory, par2, par3, par4);
	}

	public boolean isItemValid(ItemStack stack) {
		if (stack == null) return true;
		return inventory.isStackValidForSlot(slotNumber, stack);
	}
}
