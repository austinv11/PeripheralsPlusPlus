package miscperipherals.api;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;

/**
 * Used by the RTG turtle upgrade to determine possible fuels.
 * 
 * @author Richard
 */
public interface IRTGFuel {
	/**
	 * Handler list - add your handlers here.
	 */
	public static final List<IRTGFuel> handlers = new ArrayList<IRTGFuel>();
	
	/**
	 * Get the RTG fuel value for the specified item.
	 * 
	 * @param stack Fuel given
	 * @return Amount of fuel, or 0 if none 
	 */
	public int get(ItemStack stack);
}
