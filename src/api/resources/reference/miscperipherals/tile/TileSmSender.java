package miscperipherals.tile;

import miscperipherals.peripheral.PeripheralSmSender;
import net.minecraft.entity.player.EntityPlayer;

public class TileSmSender extends TilePeripheralWrapper {
	public TileSmSender() {
		super(PeripheralSmSender.class);
	}
	
	@Override
	public boolean onBlockActivated(EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		return ((PeripheralSmSender) peripheral).onBlockActivated(player, side, hitX, hitY, hitZ);
	}
}
