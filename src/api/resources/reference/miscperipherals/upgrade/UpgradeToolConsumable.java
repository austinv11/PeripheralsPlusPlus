package miscperipherals.upgrade;

import java.util.Arrays;

import miscperipherals.util.FakePlayer;
import miscperipherals.util.SlotStack;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import dan200.turtle.api.ITurtleAccess;

public abstract class UpgradeToolConsumable extends UpgradeTool {
	protected ItemStack currentItem;
	
	@Override
	public boolean canAttack(ITurtleAccess turtle, Entity ent) {
		return super.canAttack(turtle, ent) && consumeItem(turtle, false);
	}
	
	@Override
	public boolean attack(final ITurtleAccess turtle, Entity ent) {
		boolean ret = super.attack(turtle, ent);
		if (ret) consumeItem(turtle, true);
		return ret;
	}
	
	@Override
	public int getDamage(ITurtleAccess turtle, Entity ent) {
		SlotStack slotstack = findFirstConsumable(turtle);
		if (slotstack != null) return slotstack.stack.getDamageVsEntity(ent);
		else return 0;
	}
	
	@Override
	public boolean canDig(ITurtleAccess turtle, int x, int y, int z, int side) {
		ItemStack item = getCraftingItem();
		if (item != null) {
			int id = turtle.getWorld().getBlockId(x, y, z);
			if (Block.blocksList[id] != null) {
				SlotStack slotstack = findFirstConsumable(turtle);
				if (slotstack != null) return isToolEffective(slotstack.stack, id, turtle.getWorld().getBlockMetadata(x, y, z));
				else return false;
			}
		}
		
		return false;
	}
	
	@Override
	public boolean dig(ITurtleAccess turtle, int x, int y, int z, int side) {
		boolean ret = super.dig(turtle, x, y, z, side);
		if (ret) consumeItem(turtle, true);
		return ret;
	}
	
	protected boolean isToolEffective(ItemStack stack, int id, int meta) {
		return stack.getItem().canHarvestBlock(Block.blocksList[id]);
	}
	
	public Iterable<ItemStack> getConsumedItems() {
		return Arrays.asList(getCraftingItem());
	}
	
	public boolean consumeItem(ITurtleAccess turtle, boolean doConsume) {
		SlotStack slotstack = findFirstConsumable(turtle);
		if (slotstack != null) {
			if (doConsume) {
				FakePlayer player = FakePlayer.get(turtle.getWorld());
				player.alignToTurtle(turtle);
				slotstack.stack.damageItem(1, player);
				if (slotstack.stack.stackSize <= 0) slotstack.stack = null;
				turtle.setSlotContents(slotstack.slot, slotstack.stack);
			}
			
			return true;
		}
		
		return false;
	}
	
	SlotStack findFirstConsumable(ITurtleAccess turtle) {
		Iterable<ItemStack> consumedItems = getConsumedItems();
		
		for (int i = 0; i < turtle.getInventorySize(); i++) {
			ItemStack slotstack = turtle.getSlotContents(i);
			if (slotstack == null) continue;
			
			for (ItemStack stack : consumedItems) {
				if (stack.itemID == slotstack.itemID) {
					return new SlotStack(currentItem = slotstack, i);
				}
			}
		}
		
		return null;
	}
}
