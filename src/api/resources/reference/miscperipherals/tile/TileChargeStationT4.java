package miscperipherals.tile;

import miscperipherals.core.MiscPeripherals;

public class TileChargeStationT4 extends TileChargeStation {
	public TileChargeStationT4() {
		super(4);
	}
	
	@Override
	public int[] getSides() {
		if (!MiscPeripherals.instance.chargeStationMultiCharge) return super.getSides();
		
		return new int[] {0,1,2,3,4,5};
	}
}
