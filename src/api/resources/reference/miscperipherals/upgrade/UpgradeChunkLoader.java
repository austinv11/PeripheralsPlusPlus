package miscperipherals.upgrade;

import miscperipherals.api.IUpgradeIcons;
import miscperipherals.core.MiscPeripherals;
import miscperipherals.peripheral.PeripheralChunkLoader;
import miscperipherals.safe.Reflector;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import dan200.computer.api.IHostedPeripheral;
import dan200.turtle.api.ITurtleAccess;
import dan200.turtle.api.ITurtleUpgrade;
import dan200.turtle.api.TurtleSide;
import dan200.turtle.api.TurtleUpgradeType;
import dan200.turtle.api.TurtleVerb;

public class UpgradeChunkLoader implements ITurtleUpgrade, IUpgradeIcons {
	private Icon[] icons = new Icon[2];
	
	@Override
	public int getUpgradeID() {
		return 225;
	}

	@Override
	public String getAdjective() {
		return "Chunk Loader";
	}

	@Override
	public TurtleUpgradeType getType() {
		return TurtleUpgradeType.Peripheral;
	}

	@Override
	public ItemStack getCraftingItem() {
		return new ItemStack(MiscPeripherals.instance.itemAlpha, 1, 4);
	}

	@Override
	public boolean isSecret() {
		return false;
	}

	@Override
	public Icon getIcon(ITurtleAccess turtle, TurtleSide side) {
		if (turtle != null) {
			IHostedPeripheral peripheral = turtle.getPeripheral(side);
			if (peripheral.getClass().getName().equals("miscperipherals.peripheral.PeripheralChunkLoaderModem")) {
				IHostedPeripheral modem = Reflector.getField(peripheral, "modem", IHostedPeripheral.class);
				if (modem != null) {
					Boolean active = Reflector.invoke(modem, "isActive", Boolean.class);
					if (active != null && active) return icons[1];
				}
			}
		}
		
		return icons[0];
	}

	@Override
	public IHostedPeripheral createPeripheral(ITurtleAccess turtle, TurtleSide side) {
		/*try {
			return new PeripheralChunkLoaderModem(turtle, side);
		} catch (Throwable e) {*/
			return new PeripheralChunkLoader(turtle);
		//}
	}

	@Override
	public boolean useTool(ITurtleAccess turtle, TurtleSide side, TurtleVerb verb, int direction) {
		return false;
	}

	@Override
	public void registerIcons(IconRegister reg) {
		icons[0] = reg.registerIcon("MiscPeripherals:chunkLoader_turtle");
		icons[1] = reg.registerIcon("MiscPeripherals:chunkLoader_turtle_active");
	}
}
