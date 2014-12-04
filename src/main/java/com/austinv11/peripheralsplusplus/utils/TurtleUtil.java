package com.austinv11.peripheralsplusplus.utils;

import dan200.computercraft.api.turtle.ITurtleAccess;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Facing;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.IShearable;

import java.util.List;

public class TurtleUtil {

	public static List<ItemStack> harvestBlock(ITurtleAccess turtle, FakeTurtlePlayer player, int dir, ItemStack itemToUse) {
		int x = turtle.getPosition().posX+Facing.offsetsXForSide[dir];
		int y = turtle.getPosition().posY+Facing.offsetsYForSide[dir];
		int z = turtle.getPosition().posZ+Facing.offsetsZForSide[dir];
		if (!turtle.getWorld().isAirBlock(x,y,z)) {
			Block block = turtle.getWorld().getBlock(x,y,z);
			player.setCurrentItemOrArmor(0, itemToUse);
			if (block.canHarvestBlock(player, block.getDamageValue(turtle.getWorld(),x,y,z))) {
				List<ItemStack> items = block.getDrops(turtle.getWorld(),x,y,z, block.getDamageValue(turtle.getWorld(),x,y,z), 0);
				turtle.getWorld().setBlockToAir(x,y,z);
				return items;
			}
		}
		return null;
	}

	public static List<Entity> getEntitiesNearTurtle(ITurtleAccess turtle, FakeTurtlePlayer player, int dir) {
		int x = turtle.getPosition().posX+Facing.offsetsXForSide[dir];
		int y = turtle.getPosition().posY+Facing.offsetsYForSide[dir];
		int z = turtle.getPosition().posZ+Facing.offsetsZForSide[dir];
		AxisAlignedBB box = AxisAlignedBB.getBoundingBox(x, y, z, x+1.0D, y+1.0D, z+1.0D);
		return turtle.getWorld().getEntitiesWithinAABBExcludingEntity(player, box);
	}

	public static Entity getClosestEntity(List<Entity> list, Entity ent) {
		Vec3 from = Vec3.createVectorHelper(ent.posX, ent.posY, ent.posZ);
		Entity returnVal = null;
		double lastDistance = Double.MAX_VALUE;
		for (Entity entity : list)
			if (entity instanceof IShearable) {
				Vec3 to = Vec3.createVectorHelper(entity.posX, entity.posY, entity.posZ);
				if (to.distanceTo(from) < lastDistance)
					returnVal = entity;
			}
		return returnVal;
	}

	public static void addItemListToInv(List<ItemStack> items, ITurtleAccess turtle, FakeTurtlePlayer player) {
		for (ItemStack item : items) {
			player.addToInv(turtle, item);
		}
	}
}
