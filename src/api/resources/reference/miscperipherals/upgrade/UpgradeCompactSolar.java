package miscperipherals.upgrade;

import miscperipherals.peripheral.PeripheralCompactSolar;
import miscperipherals.safe.Reflector;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import dan200.computer.api.IHostedPeripheral;
import dan200.turtle.api.ITurtleAccess;
import dan200.turtle.api.TurtleSide;

public class UpgradeCompactSolar extends UpgradeSolar {
	public static final String[] NAME = {"LV", "MV", "HV"};
	public static final int[] OUTPUT = {8, 64, 512};
	private final int type;
	
	public UpgradeCompactSolar(int type) {
		super();
		
		this.type = type;
	}
	
	@Override
	public int getUpgradeID() {
		return 232 + type;
	}
	
	@Override
	public String getAdjective() {
		return NAME[type]+" Solar";
	}
	
	@Override
	public ItemStack getCraftingItem() {
		return new ItemStack(Reflector.getField("cpw.mods.compactsolars.CompactSolars", "compactSolarBlock", Block.class), 1, type);
	}
	
	@Override
	public IHostedPeripheral createPeripheral(ITurtleAccess turtle, TurtleSide side) {
		return new PeripheralCompactSolar(turtle, side, type);
	}
}
