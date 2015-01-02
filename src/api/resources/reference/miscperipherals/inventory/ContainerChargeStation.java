package miscperipherals.inventory;

import java.util.List;

import miscperipherals.tile.TileChargeStation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ICrafting;

public class ContainerChargeStation extends ContainerCommon {
	public TileChargeStation station;
	private int lastEnergy;
	
	public ContainerChargeStation(EntityPlayer player, TileChargeStation inventory) {
		super(player, inventory);
		
		this.station = inventory;
		
		addSlotToContainer(new SlotControlled(inventory, 0, 80, 41));
	}
	
	@Override
	public void addCraftingToCrafters(ICrafting var1) {
		super.addCraftingToCrafters(var1);
		var1.sendProgressBarUpdate(this, 0, (int)station.energy);
	}
	
	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		
		for (ICrafting crafter : (List<ICrafting>)crafters) {
			if (lastEnergy != station.energy) crafter.sendProgressBarUpdate(this, 0, (int)station.energy);
		}
		
		lastEnergy = (int)station.energy;
	}
	
	@Override
	public void updateProgressBar(int index, int value) {
		switch (index) {
			case 0: station.energy = value; break;
		}
	}
}
