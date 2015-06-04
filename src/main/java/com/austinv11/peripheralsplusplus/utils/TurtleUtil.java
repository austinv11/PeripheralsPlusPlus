package com.austinv11.peripheralsplusplus.utils;

import com.austinv11.collectiveframework.minecraft.utils.NBTHelper;
import cpw.mods.fml.common.registry.GameRegistry;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.ITurtleUpgrade;
import dan200.computercraft.api.turtle.TurtleSide;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.Facing;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.IShearable;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class TurtleUtil {

	public static List<ItemStack> harvestBlock(ITurtleAccess turtle, FakeTurtlePlayer player, int dir, ItemStack itemToUse) {
		int x = turtle.getPosition().posX+Facing.offsetsXForSide[dir];
		int y = turtle.getPosition().posY+Facing.offsetsYForSide[dir];
		int z = turtle.getPosition().posZ+Facing.offsetsZForSide[dir];
		if (!turtle.getWorld().isAirBlock(x,y,z)) {
			Block block = turtle.getWorld().getBlock(x,y,z);
			player.setCurrentItemOrArmor(0, itemToUse);
			if (block.getBlockHardness(turtle.getWorld(), x,y,z) >= 0 && block.canHarvestBlock(player, block.getDamageValue(turtle.getWorld(),x,y,z))) {
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

	public static Entity getClosestShearableEntity(List<Entity> list, Entity ent) {
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

	public static Entity getClosestEntity(List<Entity> list, Entity ent) {
		Vec3 from = Vec3.createVectorHelper(ent.posX, ent.posY, ent.posZ);
		Entity returnVal = null;
		double lastDistance = Double.MAX_VALUE;
		for (Entity entity : list) {
			Vec3 to = Vec3.createVectorHelper(entity.posX, entity.posY, entity.posZ);
			if (to.distanceTo(from) < lastDistance)
				returnVal = entity;
		}
		return returnVal;
	}

	public static void addItemListToInv(List<ItemStack> items, ITurtleAccess turtle) {
		for (ItemStack item : items) {
			addToInv(turtle, item);
		}
	}

	public static ArrayList<ItemStack> entityItemsToItemStack(ArrayList<EntityItem> entities) {
		ArrayList<ItemStack> stacks = new ArrayList<ItemStack>();
		for (EntityItem e : entities) {
			stacks.add(e.getEntityItem());
		}
		return stacks;
	}

	public static void addToInv(ITurtleAccess turtle, ItemStack stack) {
		boolean drop = true;
		IInventory inv = turtle.getInventory();
		ChunkCoordinates coords = turtle.getPosition();
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack currentStack = inv.getStackInSlot(i);
			if (currentStack == null) {
				inv.setInventorySlotContents(i, stack);
				drop = false;
				break;
			}
			if (currentStack.isStackable() && currentStack.isItemEqual(stack)) {
				int space = currentStack.getMaxStackSize() - currentStack.stackSize;
				if (stack.stackSize > space) {
					currentStack.stackSize = currentStack.getMaxStackSize();
					stack.stackSize -= space;
					drop = true;
				} else {
					currentStack.stackSize += stack.stackSize;
					stack.stackSize = 0;
					drop = false;
					break;
				}
			}
		}
		if (drop) {
			int dir = turtle.getDirection();
			turtle.getWorld().spawnEntityInWorld(new EntityItem(turtle.getWorld(), coords.posX+Facing.offsetsXForSide[dir], coords.posY+Facing.offsetsYForSide[dir]+1, coords.posZ+Facing.offsetsZForSide[dir], stack.copy()));
		}
	}
	
	public static ItemStack getTurtle(boolean isAdvanced, ITurtleUpgrade upgradeLeft, ITurtleUpgrade upgradeRight) {
		if (upgradeLeft == null && upgradeRight == null) {
			return new ItemStack(GameRegistry.findBlock("ComputerCraft", isAdvanced ? "CC-TurtleAdvanced" : "CC-Turtle"));
		} else {
			ItemStack turtle = new ItemStack(GameRegistry.findBlock("ComputerCraft", isAdvanced ? "CC-TurtleAdvanced" : "CC-TurtleExpanded"));
			if (upgradeLeft != null)
				NBTHelper.setShort(turtle, "leftUpgrade", (short)upgradeLeft.getUpgradeID());
			if (upgradeRight != null)
				NBTHelper.setShort(turtle, "rightUpgrade", (short)upgradeRight.getUpgradeID());
			return turtle;
		}
	}
	
	public static ItemStack getTurtle(boolean isAdvanced, ITurtleUpgrade upgrade) {
		return getTurtle(isAdvanced, upgrade, null);
	}
	
	public static ItemStack getTurtle(boolean isAdvanced) {
		return getTurtle(isAdvanced, null);
	}
	
	public static <T> T getPeripheral(ITurtleAccess turtle, Class<T> clazz) {
		for (TurtleSide side : EnumSet.allOf(TurtleSide.class)) {
			IPeripheral peripheral = turtle.getPeripheral(side);
			if (peripheral != null && clazz.isAssignableFrom(peripheral.getClass())) 
				return (T)peripheral;
		}
		
		return null;
	}
	
	public static TurtleSide getPeripheralSide(ITurtleAccess turtle, Class clazz) {
		for (TurtleSide side : EnumSet.allOf(TurtleSide.class)) {
			IPeripheral peripheral = turtle.getPeripheral(side);
			if (peripheral != null && clazz.isAssignableFrom(peripheral.getClass())) 
				return side;
		}
		
		return null;
	}
}
