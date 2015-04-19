package com.austinv11.peripheralsplusplus.items;

import com.austinv11.collectiveframework.minecraft.utils.NBTHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;

import java.util.List;
import java.util.Random;

public class ItemBlockTurtle extends ItemBlock {
	
	private Random rng = new Random();
	
	public ItemBlockTurtle(Block block) {
		super(block);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack item, EntityPlayer player, List info, boolean isShiftHeld) {
		if (NBTHelper.hasTag(item, "desc")) {
			String description = StatCollector.translateToLocal("peripheralsplusplus.description.turtle."+NBTHelper.getInt(item, "desc"));
			info.add(description);
		} else {
			NBTHelper.setInteger(item, "desc", MathHelper.getRandomIntegerInRange(rng, 1, 5));
			addInformation(item, player, info, isShiftHeld);
		}
	}
}
