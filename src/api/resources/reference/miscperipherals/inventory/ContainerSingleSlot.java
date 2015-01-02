package miscperipherals.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class ContainerSingleSlot extends ContainerCommon {
	private Slot singleSlot;
	
	public ContainerSingleSlot(EntityPlayer player, IInventory inventory) {
		super(player, inventory);
		
		addSlotToContainer(singleSlot = new Slot(inventory, 0, 80, 35));
	}
}
