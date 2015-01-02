package miscperipherals.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerCommon extends Container {
	public EntityPlayer player;
	public IInventory inventory;
	
	public ContainerCommon(EntityPlayer player, IInventory inventory) {
		this(player, inventory, 84);
	}
	
	public ContainerCommon(EntityPlayer player, IInventory inventory, int inventoryBaseY) {
		this.player = player;
		this.inventory = inventory;
		
		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 9; x++) {
				addSlotToContainer(new Slot(player.inventory, x + y * 9 + 9, 8 + x * 18, inventoryBaseY + y * 18));
			}
		}
		
		for (int x = 0; x < 9; x++) {
			addSlotToContainer(new Slot(player.inventory, x, 8 + x * 18, inventoryBaseY + 58));
		}
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer var1) {
		return inventory.isUseableByPlayer(var1);
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int par1) {
        ItemStack var2 = null;
        Slot var3 = (Slot)this.inventorySlots.get(par1);

        if (var3 != null && var3.getHasStack()) {
            ItemStack var4 = var3.getStack();
            var2 = var4.copy();

            if (par1 < 1) {
                if (!this.mergeItemStack(var4, 1, this.inventorySlots.size(), true)) {
                    return null;
                }
            } else if (!this.mergeItemStack(var4, 0, 1, false)) return null;

            if (var4.stackSize == 0) var3.putStack((ItemStack)null);
            else var3.onSlotChanged();
        }

        return var2;
    }
}
