package miscperipherals.network;

import miscperipherals.inventory.ContainerChargeStation;
import miscperipherals.inventory.ContainerCrafter;
import miscperipherals.inventory.ContainerSingleSlot;
import miscperipherals.tile.TileChargeStation;
import miscperipherals.tile.TileCrafter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {
	public static final int CHARGE_STATION = 0;
	public static final int CRAFTER = 1;
	public static final int SINGLE_SLOT = 2;
	
	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world,	int x, int y, int z) {
		switch (id) {
			case CHARGE_STATION: return new ContainerChargeStation(player, (TileChargeStation)world.getBlockTileEntity(x, y, z));
			case CRAFTER: return new ContainerCrafter(player, (TileCrafter)world.getBlockTileEntity(x, y, z));
			case SINGLE_SLOT: return new ContainerSingleSlot(player, (IInventory)world.getBlockTileEntity(x, y, z));
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		return getServerGuiElement(id, player, world, x, y, z);
	}
}
