package miscperipherals.upgrade;

import miscperipherals.api.IUpgradeIcons;
import miscperipherals.peripheral.PeripheralAlchemist;
import miscperipherals.safe.ReflectionStore;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import dan200.computer.api.IHostedPeripheral;
import dan200.turtle.api.ITurtleAccess;
import dan200.turtle.api.ITurtleUpgrade;
import dan200.turtle.api.TurtleSide;
import dan200.turtle.api.TurtleUpgradeType;
import dan200.turtle.api.TurtleVerb;

public class UpgradeAlchemist implements ITurtleUpgrade, IUpgradeIcons {
	private Icon icon;
	
	@Override
	public int getUpgradeID() {
		return 220;
	}

	@Override
	public String getAdjective() {
		return "Alchemist";
	}

	@Override
	public TurtleUpgradeType getType() {
		return TurtleUpgradeType.Peripheral;
	}

	@Override
	public ItemStack getCraftingItem() {
		return ReflectionStore.miniumStone != null ? new ItemStack(ReflectionStore.miniumStone, 1, 0) : new ItemStack(Item.appleGold);
	}

	@Override
	public boolean isSecret() {
		return false;
	}

	@Override
	public Icon getIcon(ITurtleAccess turtle, TurtleSide side) {
		return icon;
	}

	@Override
	public IHostedPeripheral createPeripheral(ITurtleAccess turtle, TurtleSide side) {
		return new PeripheralAlchemist(turtle);
	}

	@Override
	public boolean useTool(ITurtleAccess turtle, TurtleSide side, TurtleVerb verb, int direction) {
		return false;
	}

	@Override
	public void registerIcons(IconRegister reg) {
		icon = reg.registerIcon("MiscPeripherals:minium");
	}
}
