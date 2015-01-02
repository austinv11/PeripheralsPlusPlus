package gregtechmod.api;

import net.minecraft.item.ItemStack;

/**
 * You are allowed to include this File in your Download, as i will not change it.
 */
public interface ICapsuleCellContainer {
	
	/**
	 * Get the amount of Tincells
	 * @param aStack the ItemStack
	 * @return the amount of Tincells per SINGLE Item in the Stack.
	 * A returnvalue for a simple Cell, like the IC2-Watercell would be 1
	 */
	public int CapsuleCellContainerCount(ItemStack aStack);
}
