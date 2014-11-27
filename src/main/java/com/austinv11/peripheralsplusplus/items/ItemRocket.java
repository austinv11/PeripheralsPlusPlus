package com.austinv11.peripheralsplusplus.items;

import com.austinv11.peripheralsplusplus.entities.EntityRocket;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Facing;
import net.minecraft.world.World;

public class ItemRocket extends PPPItem {

	public ItemRocket() {
		super();
		this.setMaxStackSize(1);
		this.setUnlocalizedName("rocket");
	}

	@Override
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		if (!world.isRemote) {
			world.spawnEntityInWorld(new EntityRocket(world, (int)Math.ceil(x+Facing.offsetsXForSide[side]), (int)Math.ceil(y+Facing.offsetsYForSide[side]), (int)Math.ceil(z+Facing.offsetsZForSide[side])));
			stack.stackSize -= 1;
		}
		return true;
	}

	@Override
	public boolean hasCustomEntity(ItemStack stack) {
		return true;
	}

	@Override
	public Entity createEntity(World world, Entity location, ItemStack itemstack) {
		return new EntityRocket(world, (int)Math.ceil(location.posX), (int)Math.ceil(location.posY), (int)Math.ceil(location.posZ));
	}
}
