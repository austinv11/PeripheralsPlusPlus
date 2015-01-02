package miscperipherals.tile;

import miscperipherals.peripheral.PeripheralFireworks;
import net.minecraft.entity.player.EntityPlayer;

public class TileFireworks extends TilePeripheralWrapper {
	public TileFireworks() {
		super(PeripheralFireworks.class, 54);
	}
	
	@Override
	public boolean onBlockActivated(EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		player.displayGUIChest(this);
		return true;
	}
	
	@Override
	public String getInventoryName() {
		return "Firework Launcher";
	}
}
