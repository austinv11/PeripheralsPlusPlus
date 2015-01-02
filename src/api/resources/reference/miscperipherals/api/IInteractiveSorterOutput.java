package miscperipherals.api;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Interactive Sorter output handler.
 * 
 * @author Richard
 */
public interface IInteractiveSorterOutput {
	/**
	 * Handler list - add your handlers here.
	 */
	public static final List<IInteractiveSorterOutput> handlers = new ArrayList<IInteractiveSorterOutput>();
	
	/**
	 * Output an item.
	 * 
	 * Do not null the passed stack, keep it at stack size 0 instead.
	 * 
	 * @param stack Item to output
	 * @param world World the interactive sorter is in
	 * @param position Position of the interactive sorter
	 * @param direction Direction the item is being OUTPUT from
	 */
	public void output(ItemStack stack, World world, int x, int y, int z, int direction);
}
