package miscperipherals.upgrade;

import java.util.ArrayList;
import java.util.List;

import miscperipherals.core.MiscPeripherals;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraftforge.common.ForgeHooks;
import dan200.turtle.api.ITurtleAccess;
import dan200.turtle.api.TurtleSide;
import forestry.api.core.ItemInterface;

public class UpgradeScoop extends UpgradeToolConsumable {
	public static final List<ItemStack> SCOOPS = new ArrayList<ItemStack>(2);
	
	@Override
	public int getUpgradeID() {
		return 208;
	}

	@Override
	public String getAdjective() {
		return "Scoop";
	}

	@Override
	public ItemStack getCraftingItem() {
		return ItemInterface.getItem("scoop");
	}

	@Override
	public Icon getIcon(ITurtleAccess turtle, TurtleSide side) {
		return getCraftingItem().getIconIndex();
	}
	
	@Override
	protected boolean isToolEffective(ItemStack stack, int id, int meta) {
		return ForgeHooks.canToolHarvestBlock(Block.blocksList[id], meta, stack);
	}
	
	@Override
	public Iterable<ItemStack> getConsumedItems() {
		return SCOOPS;
	}
	
	public static void registerScoop(ItemStack stack) {
		if (stack != null) SCOOPS.add(stack);
		else {
			StackTraceElement ste = new Exception().getStackTrace()[1];
			MiscPeripherals.log.warning(ste.getClassName() + ":" + ste.getLineNumber() + " attempted to register a null scoop!");
		}
	}
}
