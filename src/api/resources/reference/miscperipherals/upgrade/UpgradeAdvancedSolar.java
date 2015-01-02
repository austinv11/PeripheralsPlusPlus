package miscperipherals.upgrade;

import miscperipherals.api.IUpgradeIcons;
import miscperipherals.peripheral.PeripheralAdvancedSolar;
import miscperipherals.safe.ReflectionStore;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import dan200.computer.api.IHostedPeripheral;
import dan200.turtle.api.ITurtleAccess;
import dan200.turtle.api.TurtleSide;

public class UpgradeAdvancedSolar extends UpgradeSolar implements IUpgradeIcons {
	public static final String[] NAME = {"Advanced", "Hybrid", "Ultimate Hybrid"};
	public static final int[] OUTPUT_DAY = {ReflectionStore.advGenDay == null ? 8 : ReflectionStore.advGenDay, ReflectionStore.hGenDay == null ? 64 : ReflectionStore.hGenDay, ReflectionStore.uhGenDay == null ? 512 : ReflectionStore.uhGenDay};
	public static final int[] OUTPUT_NIGHT = {ReflectionStore.advGenNight == null ? 1 : ReflectionStore.advGenNight, ReflectionStore.hGenNight == null ? 8 : ReflectionStore.hGenNight, ReflectionStore.uhGenNight == null ? 64 : ReflectionStore.uhGenNight};
	private final int type;
	private Icon[] icons = new Icon[3];
	
	public UpgradeAdvancedSolar(int type) {
		super();
		
		this.type = type;
	}
	
	@Override
	public int getUpgradeID() {
		return 236 + type;
	}
	
	@Override
	public String getAdjective() {
		return NAME[type]+" Solar";
	}
	
	@Override
	public ItemStack getCraftingItem() {
		return new ItemStack(ReflectionStore.blockAdvSolarPanel, 1, type);
	}
	
	@Override
	public Icon getIcon(ITurtleAccess turtle, TurtleSide side) {
		return icons[type];
	}
	
	@Override
	public IHostedPeripheral createPeripheral(ITurtleAccess turtle, TurtleSide side) {
		return new PeripheralAdvancedSolar(turtle, side, type);
	}
	
	@Override
	public void registerIcons(IconRegister reg) {
		icons[0] = reg.registerIcon("MiscPeripherals:solar_adv");
		icons[1] = reg.registerIcon("MiscPeripherals:solar_h");
		icons[2] = reg.registerIcon("MiscPeripherals:solar_uh");
	}
}
