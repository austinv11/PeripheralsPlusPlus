package miscperipherals.tile;

import java.util.Map;
import java.util.WeakHashMap;

import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.common.FMLCommonHandler;
import dan200.computer.api.IComputerAccess;
import dan200.computer.api.IPeripheral;

public class TilePlayerDetector extends Tile implements IPeripheral {
	private Map<IComputerAccess, Boolean> computers = new WeakHashMap<IComputerAccess, Boolean>();
	public String player;
	private int comparatorTicker;
	
	@Override
	public String getType() {
		return "playerDetector";
	}

	@Override
	public String[] getMethodNames() {
		return new String[0];
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, int method, Object[] arguments) throws Exception {
		return new Object[0];
	}

	@Override
	public boolean canAttachToSide(int side) {
		return true;
	}

	@Override
	public void attach(IComputerAccess computer) {
		computers.put(computer, true);
	}

	@Override
	public void detach(IComputerAccess computer) {
		computers.remove(computer);
	}
	
	@Override
	public boolean onBlockActivated(EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		for (IComputerAccess computer : computers.keySet()) {
			computer.queueEvent("player", new String[] {player.getEntityName()});
		}
		
		comparatorTicker = 10;
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		
		return true;
	}
	
	@Override
	public int getComparator(int side) {
		return comparatorTicker > 0 ? 15 : 0;
	}
	
	@Override
	public boolean canUpdate() {
		return !FMLCommonHandler.instance().getEffectiveSide().isClient();
	}
	
	@Override
	public void updateEntity() {
		if (comparatorTicker > 0 && --comparatorTicker <= 0) worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}
}
