package miscperipherals.tile;

import net.minecraft.entity.player.EntityPlayer;

public class TileNuclearReaderT2 extends TileNuclearReader {
	public TileNuclearReaderT2() {
		super(9);
	}
	
	@Override
	public String getInventoryName() {
		return "Adv Nuclear Information Reader";
	}
	
	@Override
	public int getGuiId() {
		return -1;
	}
	
	@Override
	public boolean onBlockActivated(EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		player.displayGUIChest(this);
		return true;
	}
}
