package miscperipherals.upgrade;

import java.util.ArrayList;
import java.util.List;

import miscperipherals.core.MiscPeripherals;
import miscperipherals.safe.Reflector;
import miscperipherals.util.FakePlayer;
import miscperipherals.util.SlotStack;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import dan200.turtle.api.ITurtleAccess;
import dan200.turtle.api.TurtleSide;
import forestry.api.arboriculture.IToolGrafter;

public class UpgradeGrafter extends UpgradeToolConsumable {
	private static final List<ItemStack> GRAFTERS = new ArrayList<ItemStack>(3);
	
	@Override
	public int getUpgradeID() {
		return 209;
	}

	@Override
	public String getAdjective() {
		return "Grafter";
	}

	@Override
	public ItemStack getCraftingItem() {
		try {
			return GRAFTERS.get(0);
		} catch (Throwable e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Icon getIcon(ITurtleAccess turtle, TurtleSide side) {
		try {
			return getCraftingItem().getIconIndex();
		} catch (Throwable e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * The API is a pain in the back if not impossible to be used by a non-player	
	 */
	@Override
	public boolean canDig(ITurtleAccess turtle, int x, int y, int z, int side) {
		if (!consumeItem(turtle, false)) return false;
		
		Block block = Block.blocksList[turtle.getWorld().getBlockId(x, y, z)];
		if (block != null) return block.getClass().getName().equals("forestry.arboriculture.gadgets.BlockLeaves");
		else return false;
	}
	
	@Override
	public Iterable<ItemStack> getBlockDrops(ITurtleAccess turtle, int x, int y, int z, int side) {
		World world = turtle.getWorld();
		FakePlayer player = FakePlayer.get(world);
		player.alignToTurtle(turtle);
		player.setHeldItem(currentItem);
		
		SlotStack slotstack = findFirstConsumable(turtle);
		if (slotstack == null || !(slotstack.stack.getItem() instanceof IToolGrafter)) return new ArrayList<ItemStack>(0);
		
		IToolGrafter grafter = (IToolGrafter) slotstack.stack.getItem();
		return (ArrayList<ItemStack>) Reflector.invoke(Block.blocksList[world.getBlockId(x, y, z)], "getLeafDrop", ArrayList.class, world, x, y, z, world.getBlockMetadata(x, y, z), grafter.getSaplingModifier(player.getHeldItem(), world, player, x, y, z));
	}
	
	@Override
	public Iterable<ItemStack> getConsumedItems() {
		return GRAFTERS;
	}
	
	public static void registerGrafter(ItemStack stack) {
		if (stack != null) GRAFTERS.add(stack);
		else {
			StackTraceElement ste = new Exception().getStackTrace()[1];
			MiscPeripherals.log.warning(ste.getClassName() + ":" + ste.getLineNumber() + " attempted to register a null grafter!");
		}
	}
}
