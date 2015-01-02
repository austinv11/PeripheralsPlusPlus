package miscperipherals.inventory;

import miscperipherals.tile.TileCrafter;
import miscperipherals.util.Util;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;

public class ContainerCrafter extends ContainerCommon {
	private TileCrafter crafter;
	SlotRO craftResult;
	
	public ContainerCrafter(EntityPlayer player, TileCrafter inventory) {
		super(player, inventory, 140);
		this.crafter = inventory;
		
		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				addSlotToContainer(new SlotRO(inventory.craftingInv, x + y * 3, 30 + x * 18, 17 + y * 18));
			}
		}
		
		addSlotToContainer(craftResult = new SlotRO(new InventoryBasic("", false, 1), 0, 124, 35));
		
		for (int x = 0; x < 9; x++) {
			for (int y = 0; y < 2; y++) {
				addSlotToContainer(new Slot(crafter, x + y * 9, 8 + x * 18, 90 + y * 18));				
			}
		}
	}
	
	@Override
	public void detectAndSendChanges() {
		craftResult.putStack(CraftingManager.getInstance().findMatchingRecipe(crafter.craftingInv, crafter.worldObj));
		
		super.detectAndSendChanges();
	}
	
	@Override
	public ItemStack slotClick(int par1, int par2, int par3, EntityPlayer par4EntityPlayer) {
		if (!par4EntityPlayer.worldObj.isRemote && par1 == craftResult.slotNumber) {
			for (int i = 0; i < crafter.getSizeInventory(); i++) {
				ItemStack slotstack = crafter.getStackInSlot(i);
				if (slotstack == null || Util.areStacksEqual(slotstack, craftResult.getStack())) {
					ItemStack craftResult = crafter.craft(slotstack);
					if (craftResult == null) break;
					
					if (slotstack == null) slotstack = craftResult.copy();
					else slotstack.stackSize += craftResult.stackSize;
					crafter.setInventorySlotContents(i, slotstack);
					
					detectAndSendChanges();
					break;
				}
			}
		}
		
		return super.slotClick(par1, par2, par3, par4EntityPlayer);
	}
}
