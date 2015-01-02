package miscperipherals.upgrade;

import miscperipherals.api.IUpgradeIcons;
import miscperipherals.peripheral.PeripheralBarrel;
import miscperipherals.safe.ReflectionStore;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import dan200.computer.api.IHostedPeripheral;
import dan200.turtle.api.ITurtleAccess;
import dan200.turtle.api.ITurtleUpgrade;
import dan200.turtle.api.TurtleSide;
import dan200.turtle.api.TurtleUpgradeType;
import dan200.turtle.api.TurtleVerb;

public class UpgradeBarrel implements ITurtleUpgrade, IUpgradeIcons {
	private Icon[] icons = new Icon[15];
	
	@Override
	public int getUpgradeID() {
		return 243;
	}

	@Override
	public String getAdjective() {
		return "Barrel";
	}

	@Override
	public TurtleUpgradeType getType() {
		return TurtleUpgradeType.Peripheral;
	}

	@Override
	public ItemStack getCraftingItem() {
		return ReflectionStore.barrel_item == null ? new ItemStack(Block.chest) : ReflectionStore.barrel_item.copy();
	}

	@Override
	public boolean isSecret() {
		return false;
	}

	@Override
	public Icon getIcon(ITurtleAccess turtle, TurtleSide side) {
		if (turtle == null) return icons[0];
		
		IHostedPeripheral peripheral = turtle.getPeripheral(side);
		if (!(peripheral instanceof PeripheralBarrel)) return icons[0];
		
		PeripheralBarrel barrel = (PeripheralBarrel)peripheral;
		if (barrel.clientAmount == 0) return icons[0];
		else if (barrel.clientAmount == barrel.maxSize) return icons[15];
		else return icons[1 + ((int)Math.ceil((double)barrel.clientAmount / (double)barrel.maxSize) * 14)];
	}

	@Override
	public IHostedPeripheral createPeripheral(ITurtleAccess turtle, TurtleSide side) {
		return new PeripheralBarrel(turtle);
	}
	
	@Override
	public boolean useTool(ITurtleAccess turtle, TurtleSide side, TurtleVerb verb, int direction) {
		return false;
	}

	@Override
	public void registerIcons(IconRegister reg) {
		for (int i = 0; i < icons.length; i++) icons[i] = reg.registerIcon("MiscPeripherals:barrel"+i);
	}
}
