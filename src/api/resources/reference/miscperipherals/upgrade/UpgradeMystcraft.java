package miscperipherals.upgrade;

import miscperipherals.api.IUpgradeIcons;
import miscperipherals.peripheral.PeripheralMystcraft;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;

import com.xcompwiz.mystcraft.api.MystObjects;

import dan200.computer.api.IHostedPeripheral;
import dan200.turtle.api.ITurtleAccess;
import dan200.turtle.api.ITurtleUpgrade;
import dan200.turtle.api.TurtleSide;
import dan200.turtle.api.TurtleUpgradeType;
import dan200.turtle.api.TurtleVerb;

public class UpgradeMystcraft implements ITurtleUpgrade, IUpgradeIcons {
	private Icon icon;
	
	@Override
	public int getUpgradeID() {
		return 213;
	}

	@Override
	public String getAdjective() {
		return "Mystcraft";
	}

	@Override
	public TurtleUpgradeType getType() {
		return TurtleUpgradeType.Peripheral;
	}

	@Override
	public ItemStack getCraftingItem() {
		return new ItemStack(MystObjects.notebook);
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
		return new PeripheralMystcraft(turtle);
	}

	@Override
	public boolean useTool(ITurtleAccess turtle, TurtleSide side, TurtleVerb verb, int direction) {
		return false;
	}
	
	@Override
	public void registerIcons(IconRegister reg) {
		icon = reg.registerIcon("MiscPeripherals:generic");
	}
}
