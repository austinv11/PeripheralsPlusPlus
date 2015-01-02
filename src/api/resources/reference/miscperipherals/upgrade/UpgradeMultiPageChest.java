package miscperipherals.upgrade;

import miscperipherals.peripheral.PeripheralChest;
import miscperipherals.safe.ReflectionStore;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.ItemStack;
import dan200.computer.api.IHostedPeripheral;
import dan200.turtle.api.ITurtleAccess;
import dan200.turtle.api.TurtleSide;

public class UpgradeMultiPageChest extends UpgradeChest {
	@Override
	public int getUpgradeID() {
		return 207;
	}
	
	@Override
	public String getAdjective() {
		return "Multi Page";
	}
	
	@Override
	public ItemStack getCraftingItem() {
		return ReflectionStore.chestBlock == null ? null : new ItemStack(ReflectionStore.chestBlock);
	}

	@Override
	public boolean isSecret() {
		return true;
	}
	
	@Override
	public IHostedPeripheral createPeripheral(ITurtleAccess turtle, TurtleSide side) {
		return new PeripheralChest(turtle, 16);
	}
	
	@Override
	public void registerIcons(IconRegister reg) {
		icon = reg.registerIcon("MiscPeripherals:chest_multiPage");
	}
}
