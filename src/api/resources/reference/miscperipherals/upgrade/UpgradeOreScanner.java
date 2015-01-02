package miscperipherals.upgrade;

import ic2.api.item.Items;
import miscperipherals.api.IUpgradeIcons;
import miscperipherals.peripheral.PeripheralOreScanner;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraftforge.oredict.OreDictionary;
import dan200.computer.api.IHostedPeripheral;
import dan200.turtle.api.ITurtleAccess;
import dan200.turtle.api.ITurtleUpgrade;
import dan200.turtle.api.TurtleSide;
import dan200.turtle.api.TurtleUpgradeType;
import dan200.turtle.api.TurtleVerb;

public class UpgradeOreScanner implements ITurtleUpgrade, IUpgradeIcons {
	public final boolean advanced;
	private Icon[] icons = new Icon[2];
	
	public UpgradeOreScanner(boolean advanced) {
		this.advanced = advanced;
	}
	
	@Override
	public int getUpgradeID() {
		return advanced ? 212 : 211;
	}

	@Override
	public String getAdjective() {
		return (advanced ? "Advanced " : "")+"Ore Scanner";
	}

	@Override
	public TurtleUpgradeType getType() {
		return TurtleUpgradeType.Peripheral;
	}

	@Override
	public ItemStack getCraftingItem() {
		ItemStack item = Items.getItem(advanced ? "ovScanner" : "odScanner");
		item.setItemDamage(OreDictionary.WILDCARD_VALUE);
		return item;
	}

	@Override
	public boolean isSecret() {
		return false;
	}

	@Override
	public Icon getIcon(ITurtleAccess turtle, TurtleSide side) {
		return icons[advanced ? 1 : 0];
	}

	@Override
	public IHostedPeripheral createPeripheral(ITurtleAccess turtle, TurtleSide side) {
		return new PeripheralOreScanner(turtle, advanced);
	}

	@Override
	public boolean useTool(ITurtleAccess turtle, TurtleSide side, TurtleVerb verb, int direction) {
		return false;
	}

	@Override
	public void registerIcons(IconRegister reg) {
		icons[0] = reg.registerIcon("MiscPeripherals:oreScanner");
		icons[1] = reg.registerIcon("MiscPeripherals:oreScanner_adv");
	}
}
