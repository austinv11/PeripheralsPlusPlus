package miscperipherals.tile;

import miscperipherals.peripheral.PeripheralGateReader;

public class TileGateReader extends TilePeripheralWrapper {
	public TileGateReader() {
		super(PeripheralGateReader.class, 9);
	}
	
	@Override
	public String getInventoryName() {
		return "Gate Reader";
	}
}
