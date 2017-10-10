package com.austinv11.peripheralsplusplus.creativetab;

import com.austinv11.peripheralsplusplus.init.ModBlocks;
import com.austinv11.peripheralsplusplus.init.ModPeripherals;
import com.austinv11.peripheralsplusplus.reference.Reference;
import com.austinv11.peripheralsplusplus.utils.TurtleUtil;
import dan200.computercraft.api.pocket.IPocketUpgrade;
import dan200.computercraft.api.turtle.ITurtleUpgrade;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CreativeTabPPP {

	public static final CreativeTabs PPP_TAB = new CreativeTabs(Reference.MOD_ID.toLowerCase()) {

		@SideOnly(Side.CLIENT)
		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(Item.getItemFromBlock(ModBlocks.CHAT_BOX));
		}

		@SideOnly(Side.CLIENT)
		@Override
		public void displayAllRelevantItems(NonNullList<ItemStack> list) {
			super.displayAllRelevantItems(list);
			for (ITurtleUpgrade upgrade : ModPeripherals.TURTLE_UPGRADES) {
				list.add(TurtleUtil.getTurtle(false, upgrade));
				list.add(TurtleUtil.getTurtle(true, upgrade));
			}
			for (IPocketUpgrade pocketUpgrade : ModPeripherals.POCKET_UPGRADES) {
				list.add(TurtleUtil.getPocket(false, pocketUpgrade));
				list.add(TurtleUtil.getPocket(true, pocketUpgrade));
			}
		}
	};
}
