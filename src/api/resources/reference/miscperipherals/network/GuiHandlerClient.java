package miscperipherals.network;

import miscperipherals.gui.GuiChargeStation;
import miscperipherals.gui.GuiCrafter;
import miscperipherals.gui.GuiSingleSlot;
import miscperipherals.inventory.ContainerChargeStation;
import miscperipherals.inventory.ContainerCrafter;
import miscperipherals.inventory.ContainerSingleSlot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiHandlerClient extends GuiHandler {
	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world,	int x, int y, int z) {
		switch (id) {
			case CHARGE_STATION: return new GuiChargeStation((ContainerChargeStation)getServerGuiElement(id, player, world, x, y, z));
			case CRAFTER: return new GuiCrafter((ContainerCrafter)getServerGuiElement(id, player, world, x, y, z));
			case SINGLE_SLOT: return new GuiSingleSlot((ContainerSingleSlot)getServerGuiElement(id, player, world, x, y, z));
		}
		return null;
	}
}
