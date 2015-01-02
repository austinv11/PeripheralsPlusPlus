package miscperipherals.tile;

import miscperipherals.core.MiscPeripherals;
import miscperipherals.util.Util;

public class TileChargeStationT2 extends TileChargeStation {
	public TileChargeStationT2() {
		super(2);
	}
	
	@Override
	public int[] getSides() {
		if (!MiscPeripherals.instance.chargeStationMultiCharge) return super.getSides();
		
		int facing = getFacing();
		return new int[] {facing, Util.OPPOSITE[facing]};
	}
}
