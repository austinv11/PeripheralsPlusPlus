package miscperipherals.core;

import java.util.LinkedList;
import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;

import dan200.turtle.api.ITurtleUpgrade;
import dan200.turtle.api.TurtleUpgradeType;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class CreativeTabMiscPeripherals extends CreativeTabs {
	private static final List<ITurtleUpgrade> upgrades = new LinkedList<ITurtleUpgrade>();
	
	private ItemStack icon;
	
	public CreativeTabMiscPeripherals() {
		super("miscperipherals");
	}
	
	@Override
	public ItemStack getIconItemStack() {
		if (icon == null) {
			if (MiscPeripherals.instance.itemSmartHelmet != null) icon = new ItemStack(MiscPeripherals.instance.itemSmartHelmet);
			else {
				icon = new ItemStack(MiscPeripherals.instance.blockAlpha, 1, MiscPeripherals.instance.blockAlpha.findFirstUsedMeta());
				if (icon.getItemDamage() == -1) icon = new ItemStack(MiscPeripherals.instance.blockBeta, 1, MiscPeripherals.instance.blockBeta.findFirstUsedMeta());
				if (icon.getItemDamage() == -1) icon = new ItemStack(MiscPeripherals.instance.itemAlpha, 1, MiscPeripherals.instance.itemAlpha.findFirstUsedMeta());
				if (icon.getItemDamage() == -1) icon = new ItemStack(Item.redstone);
			}
		}
		
		return icon;
	}
	
	@Override
	public void displayAllReleventItems(List list) {
		super.displayAllReleventItems(list);
		
		ItemStack base = GameRegistry.findItemStack("CCTurtle", "CC-TurtleExpanded", 1);
		if (base == null) return;
		
		for (ITurtleUpgrade upgrade : upgrades) {
			boolean isTool = upgrade.getType() == TurtleUpgradeType.Tool;
			
			ItemStack upg1 = base.copy();
			upg1.stackTagCompound = new NBTTagCompound();
			upg1.stackTagCompound.setShort(isTool ? "leftUpgrade" : "rightUpgrade", (short) upgrade.getUpgradeID());
			
			for (int id : new int[] {1,2,3,4,5,6,7}) {
				if (isTool && id > 2) continue;
				
				ItemStack upg2 = upg1.copy();
				upg2.stackTagCompound.setShort(isTool ? "rightUpgrade" : "leftUpgrade", (short) id);
				list.add(upg2);
			}
		}
	}
	
	public static void registerUpgrade(ITurtleUpgrade upgrade) {
		upgrades.add(upgrade);
	}
}
