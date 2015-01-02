package miscperipherals.peripheral;

import java.util.Random;

import miscperipherals.safe.Reflector;
import miscperipherals.upgrade.UpgradeCompactSolar;
import dan200.turtle.api.ITurtleAccess;
import dan200.turtle.api.TurtleSide;

public class PeripheralCompactSolar extends PeripheralSolar {
	private static final Random random = new Random();
	private final int type;
	private final int prodRate;
	
	public PeripheralCompactSolar(ITurtleAccess turtle, TurtleSide side, int type) {
		super(turtle, side);
		
		this.type = type;
		
		Integer prodRate = Reflector.getField("cpw.mods.compactsolars.CompactSolars", "productionRate", Integer.class);
		if (prodRate != null) this.prodRate = prodRate;
		else this.prodRate = 1;
	}
	
	@Override
	boolean canUpdate() {
		return super.canUpdate() && (prodRate == 1 || random.nextInt(prodRate) == 0);
	}
	
	@Override
	int getProduction() {
		return UpgradeCompactSolar.OUTPUT[type];
	}
}
