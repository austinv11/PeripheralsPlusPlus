package miscperipherals.upgrade;

import miscperipherals.api.IUpgradeIcons;
import miscperipherals.peripheral.PeripheralAnvil;
import net.minecraft.block.Block;
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

public class UpgradeAnvil implements ITurtleUpgrade, IUpgradeIcons {
	private Icon[] anvilTops = new Icon[3]; 
	
	@Override
	public int getUpgradeID() {
		return 242;
	}

	@Override
	public String getAdjective() {
		return "Anvil";
	}

	@Override
	public TurtleUpgradeType getType() {
		return TurtleUpgradeType.Peripheral;
	}

	@Override
	public ItemStack getCraftingItem() {
		return new ItemStack(Block.anvil, 1, OreDictionary.WILDCARD_VALUE);
	}

	@Override
	public boolean isSecret() {
		return false;
	}

	@Override
	public Icon getIcon(ITurtleAccess turtle, TurtleSide side) {
		return Block.anvil.getBlockTextureFromSide(0);
	}

	@Override
	public IHostedPeripheral createPeripheral(ITurtleAccess turtle, TurtleSide side) {
		return new PeripheralAnvil(turtle, side);
	}

	@Override
	public boolean useTool(ITurtleAccess turtle, TurtleSide side, TurtleVerb verb, int direction) {
		return false;
	}

	@Override
	public void registerIcons(IconRegister reg) {
		anvilTops[0] = reg.registerIcon("anvil_top");
		anvilTops[1] = reg.registerIcon("anvil_top_damaged_1");
		anvilTops[2] = reg.registerIcon("anvil_top_damaged_2");
	}
}
