package miscperipherals.util;

import miscperipherals.safe.Reflector;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.common.registry.GameRegistry;

/**
 * Wrapper class for the fact CC doesn't register stacks yet
 */
public class CCItems {
	public static ItemStack getComputer() {
		ItemStack stack = GameRegistry.findItemStack("ComputerCraft", "CC-Computer", 1);
		if (stack != null) stack.setItemDamage(OreDictionary.WILDCARD_VALUE);
		else stack = new ItemStack(Block.blockIron);
		return stack;
	}
	
	public static ItemStack getModem() {
		ItemStack stack = GameRegistry.findItemStack("ComputerCraft", "CC-Peripheral", 1);
		if (stack != null) stack.setItemDamage(1);
		else stack = new ItemStack(Item.enderPearl);
		return stack;
	}
	
	public static ItemStack getMonitor() {
		ItemStack stack = GameRegistry.findItemStack("ComputerCraft", "CC-Peripheral", 1);
		if (stack != null) stack.setItemDamage(2);
		else stack = new ItemStack(Block.glowStone);
		return stack;
	}
	
	public static ItemStack[] getDisks() {
		ItemStack[] ret = new ItemStack[2];
		Item disk = Reflector.getField("dan200.ComputerCraft$Items", "disk", Item.class);
		if (disk != null) ret[0] = new ItemStack(disk, 1, OreDictionary.WILDCARD_VALUE);
		Item diskExpanded = Reflector.getField("dan200.ComputerCraft$Items", "diskExpanded", Item.class);
		if (diskExpanded != null) ret[1] = new ItemStack(diskExpanded, 1, -1);
		return ret;
	}
}
