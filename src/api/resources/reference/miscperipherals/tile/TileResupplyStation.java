package miscperipherals.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class TileResupplyStation extends TileInventory {
	public TileResupplyStation() {
		super(54);
	}
	
	@Override
	public String getInventoryName() {
		return "Resupply Station";
	}
	
	@Override
	public boolean onBlockActivated(EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		player.displayGUIChest(this);
		return true;
	}
	
	public int resupply(ItemStack item, int amount) {
		int resupplied = 0;
		
		for (int i = 0; i < getSizeInventory(); i++) {
			ItemStack stack = getStackInSlot(i);
			if (stack != null && stack.itemID == item.itemID && stack.getItemDamage() == item.getItemDamage()) {
				int add = Math.min(amount - resupplied, stack.stackSize);
				
				stack.stackSize -= add;
				if (stack.stackSize <= 0) stack = null;
				setInventorySlotContents(i, stack);
				
				resupplied += add;
				
				if (resupplied >= amount) return amount;
			}
		}
		
		return resupplied;
	}
}
