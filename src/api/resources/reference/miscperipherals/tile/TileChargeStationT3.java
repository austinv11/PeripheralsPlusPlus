package miscperipherals.tile;

import miscperipherals.core.MiscPeripherals;
import miscperipherals.util.Util;
import net.minecraftforge.common.ForgeDirection;

public class TileChargeStationT3 extends TileChargeStation {
	public TileChargeStationT3() {
		super(3);
	}
	
	@Override
	public int[] getSides() {
		if (!MiscPeripherals.instance.chargeStationMultiCharge) return super.getSides();
		
		int facing = getFacing();
		ForgeDirection dir = ForgeDirection.getOrientation(facing);
		return new int[] {facing, Util.OPPOSITE[facing], dir.getRotation(ForgeDirection.EAST).ordinal(), dir.getRotation(ForgeDirection.WEST).ordinal()};
	}
}
