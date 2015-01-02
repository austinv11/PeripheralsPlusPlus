package miscperipherals.tile;

import miscperipherals.peripheral.PeripheralEnergyMeter;

public class TileEnergyMeter extends TilePeripheralWrapper {
	public TileEnergyMeter() {
		super(PeripheralEnergyMeter.class);
	}
}
