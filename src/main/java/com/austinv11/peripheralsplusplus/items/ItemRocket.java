package com.austinv11.peripheralsplusplus.items;

import com.austinv11.peripheralsplusplus.entities.EntityRocket;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Facing;
import net.minecraft.world.World;

import java.util.List;

public class ItemRocket extends ItemPPP {

	public ItemRocket() {
		super();
		this.setMaxStackSize(1);
		this.setUnlocalizedName("rocket");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack item, EntityPlayer player, List info, boolean isShiftHeld) {
		info.add("[WIP] This item may produce unexpected results!");
	}

	@Override
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		if (!world.isRemote) {
			world.spawnEntityInWorld(new EntityRocket(world, x+Facing.offsetsXForSide[side], y+Facing.offsetsYForSide[side], z+Facing.offsetsZForSide[side]));
			stack.stackSize -= 1;
		}
		return false;
	}

	@Override
	public boolean hasCustomEntity(ItemStack stack) {
		//return true;
		return false;
	}

	@Override
	public Entity createEntity(World world, Entity location, ItemStack itemstack) {
		return new EntityRocket(world, location.posX, location.posY, location.posZ);
	}
}
