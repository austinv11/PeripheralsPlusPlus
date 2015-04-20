package com.austinv11.peripheralsplusplus.items;

import com.austinv11.peripheralsplusplus.entities.EntityNanoBotSwarm;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemNanoSwarm extends ItemPPP {
	
	public ItemNanoSwarm() {
		super();
		this.setMaxStackSize(16);
		this.setUnlocalizedName("nanoSwarm");
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		if (!world.isRemote)
			world.spawnEntityInWorld(new EntityNanoBotSwarm(world, player));
		stack.stackSize--;
		return stack;
	}
}
