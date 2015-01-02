package miscperipherals.upgrade;

import miscperipherals.util.FakePlayer;
import miscperipherals.util.Util;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.Icon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import cpw.mods.fml.common.registry.GameRegistry;
import dan200.computer.api.IHostedPeripheral;
import dan200.turtle.api.ITurtleAccess;
import dan200.turtle.api.ITurtleUpgrade;
import dan200.turtle.api.TurtleSide;
import dan200.turtle.api.TurtleUpgradeType;
import dan200.turtle.api.TurtleVerb;

public class UpgradeWrench implements ITurtleUpgrade {
	public static ItemStack wrench;
	
	public UpgradeWrench() {
		if ((wrench = GameRegistry.findItemStack("OmniTools", "OmniWrench", 1)) == null) wrench = new ItemStack(Item.axeGold);
	}
	
	@Override
	public int getUpgradeID() {
		return 221;
	}

	@Override
	public String getAdjective() {
		return "Wrench";
	}

	@Override
	public TurtleUpgradeType getType() {
		return TurtleUpgradeType.Tool;
	}

	@Override
	public ItemStack getCraftingItem() {
		return wrench;
	}

	@Override
	public boolean isSecret() {
		return false;
	}

	@Override
	public Icon getIcon(ITurtleAccess turtle, TurtleSide side) {
		return getCraftingItem().getIconIndex();
	}

	@Override
	public IHostedPeripheral createPeripheral(ITurtleAccess turtle,	TurtleSide side) {
		return null;
	}

	@Override
	public boolean useTool(ITurtleAccess turtle, TurtleSide side, TurtleVerb verb, int direction) {
		ItemStack item = getCraftingItem();
		
		switch (verb) {
			case Attack: {
				World world = turtle.getWorld();
				FakePlayer player = FakePlayer.get(world);
				player.alignToTurtle(turtle);
				player.setHeldItem(item);
				MovingObjectPosition mop = Util.rayTrace(player, 1.5D);
				if (mop == null || mop.typeOfHit != EnumMovingObjectType.ENTITY) return false;
				
				return mop.entityHit.interact(player);
			}
			case Dig: {
				World world = turtle.getWorld();
				FakePlayer player = FakePlayer.get(world);
				player.alignToTurtle(turtle);
				player.setHeldItem(item);
				MovingObjectPosition mop = Util.rayTraceBlock(turtle, direction);
				
				int id = world.getBlockId(mop.blockX, mop.blockY, mop.blockZ);
				Block block = Block.blocksList[id];
				if (block != null && block.onBlockActivated(world, mop.blockX, mop.blockY, mop.blockZ, player, mop.sideHit, 0.0F, 0.0F, 0.0F)) {
					return true;
				}
				
				return item.getItem().onItemUse(item, player, world, mop.blockX, mop.blockY, mop.blockZ, mop.sideHit, 0.0F, 0.0F, 0.0F);
			}
		}
		
		return false;
	}
}
