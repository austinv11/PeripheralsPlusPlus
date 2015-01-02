package miscperipherals.upgrade;

import ic2.api.item.Items;

import java.util.ArrayList;
import java.util.List;

import miscperipherals.api.IUpgradeIcons;
import miscperipherals.safe.Reflector;
import miscperipherals.util.FakePlayer;
import miscperipherals.util.Util;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.Icon;
import net.minecraft.util.MovingObjectPosition;
import dan200.computer.api.IHostedPeripheral;
import dan200.turtle.api.ITurtleAccess;
import dan200.turtle.api.ITurtleUpgrade;
import dan200.turtle.api.TurtleSide;
import dan200.turtle.api.TurtleUpgradeType;
import dan200.turtle.api.TurtleVerb;

public class UpgradeTreetap implements ITurtleUpgrade, IUpgradeIcons {
	private Icon icon;
	
	@Override
	public int getUpgradeID() {
		return 222;
	}

	@Override
	public String getAdjective() {
		return "Treetap";
	}

	@Override
	public TurtleUpgradeType getType() {
		return TurtleUpgradeType.Tool;
	}

	@Override
	public ItemStack getCraftingItem() {
		ItemStack item = Items.getItem("treetap").copy();
		item.setItemDamage(0);
		return item;
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
		return null;
	}

	@Override
	public boolean useTool(ITurtleAccess turtle, TurtleSide side, TurtleVerb verb, int direction) {
		switch (verb) {
			case Attack: {
				FakePlayer player = FakePlayer.get(turtle);
				player.alignToTurtle(turtle);
				player.setHeldItem(getCraftingItem());
				MovingObjectPosition mop = Util.rayTrace(player, 1.5D);
				if (mop == null || mop.typeOfHit != EnumMovingObjectType.ENTITY) return false;
				
				return mop.entityHit.attackEntityFrom(DamageSource.causePlayerDamage(player), 1);
			}
			case Dig: {
				MovingObjectPosition mop = Util.rayTraceBlock(turtle, direction);
				
				if (turtle.getWorld().getBlockId(mop.blockX, mop.blockY, mop.blockZ) == Items.getItem("rubberWood").itemID) {
					List<ItemStack> stacks = new ArrayList<ItemStack>(1);
					
					Boolean ret = Reflector.invoke("ic2.core.item.tool.ItemTreetap", "attemptExtract", Boolean.class, null, turtle.getWorld(), mop.blockX, mop.blockY, mop.blockZ, Util.OPPOSITE[direction], stacks);
					if (ret == null) return false;
					for (ItemStack stack : stacks) {
						Util.storeOrDrop(turtle, stack);
					}
					
					return ret;
				}
			}
		}
		
		return false;
	}

	@Override
	public void registerIcons(IconRegister reg) {
		//icon = reg.registerIcon("MiscPeripherals:treetap_turtle");
		icon = reg.registerIcon("ic2:itemTreetap");
	}
}
