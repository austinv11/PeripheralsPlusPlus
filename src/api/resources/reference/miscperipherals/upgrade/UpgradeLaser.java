package miscperipherals.upgrade;

import ic2.api.item.ElectricItem;
import ic2.api.item.Items;
import miscperipherals.api.IUpgradeToolIcons;
import miscperipherals.core.MiscPeripherals;
import miscperipherals.util.FakePlayer;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import dan200.computer.api.IHostedPeripheral;
import dan200.turtle.api.ITurtleAccess;
import dan200.turtle.api.ITurtleUpgrade;
import dan200.turtle.api.TurtleSide;
import dan200.turtle.api.TurtleUpgradeType;
import dan200.turtle.api.TurtleVerb;

public class UpgradeLaser implements ITurtleUpgrade, IUpgradeToolIcons {
	private static final int ENERGY_CONSUME_DIG = 100;
	private static final int ENERGY_CONSUME_ATTACK = 1250;
	
	private Icon icon;
	
	@Override
	public int getUpgradeID() {
		return 226;
	}

	@Override
	public String getAdjective() {
		return "Laser";
	}

	@Override
	public TurtleUpgradeType getType() {
		return TurtleUpgradeType.Tool;
	}

	@Override
	public ItemStack getCraftingItem() {
		ItemStack stack = Items.getItem("miningLaser").copy();
		stack.setItemDamage(OreDictionary.WILDCARD_VALUE);
		return stack;
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
	public IHostedPeripheral createPeripheral(ITurtleAccess turtle,	TurtleSide side) {
		return null;
	}

	@Override
	public boolean useTool(ITurtleAccess turtle, TurtleSide side, TurtleVerb verb, int direction) {
		float energyConsume = verb == TurtleVerb.Attack ? ENERGY_CONSUME_ATTACK : ENERGY_CONSUME_DIG;	
		if (!turtle.consumeFuel((int)Math.ceil(energyConsume / MiscPeripherals.instance.fuelEU))) return false;
		
		World world = turtle.getWorld();
		FakePlayer fakePlayer = FakePlayer.get(turtle);
		fakePlayer.alignToTurtle(turtle);
		
		int mode = verb == TurtleVerb.Attack ? 0 : 1;
		ItemStack laser = Items.getItem("miningLaser").copy();
		laser.stackTagCompound = new NBTTagCompound();
		ElectricItem.charge(laser, Integer.MAX_VALUE, Integer.MAX_VALUE, true, false);
		laser.stackTagCompound.setInteger("laserSetting", mode);
		laser.useItemRightClick(world, fakePlayer);
		
		return true;
	}

	@Override
	public void registerIcons(IconRegister reg) {
		//icon = reg.registerIcon("MiscPeripherals:laser_turtle");
		icon = reg.registerIcon("ic2:itemToolMiningLaser");
	}
}
