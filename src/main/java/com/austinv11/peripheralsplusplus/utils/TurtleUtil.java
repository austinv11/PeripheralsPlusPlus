package com.austinv11.peripheralsplusplus.utils;

import com.austinv11.collectiveframework.minecraft.reference.ModIds;
import com.austinv11.collectiveframework.minecraft.utils.NBTHelper;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.pocket.IPocketAccess;
import dan200.computercraft.api.pocket.IPocketUpgrade;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.ITurtleUpgrade;
import dan200.computercraft.api.turtle.TurtleSide;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class TurtleUtil {

	public static List<ItemStack> harvestBlock(ITurtleAccess turtle, FakeTurtlePlayer player, EnumFacing dir,
											   ItemStack itemToUse) {
		int x = turtle.getPosition().getX()+dir.getFrontOffsetX();
		int y = turtle.getPosition().getY()+dir.getFrontOffsetY();
		int z = turtle.getPosition().getZ()+dir.getFrontOffsetZ();
		if (!turtle.getWorld().isAirBlock(new BlockPos(x,y,z))) {
			IBlockState blockState = turtle.getWorld().getBlockState(new BlockPos(x,y,z));
			player.setHeldItem(EnumHand.MAIN_HAND, itemToUse);
			if (blockState.getBlockHardness(turtle.getWorld(), new BlockPos(x,y,z)) >= 0 &&
					blockState.getBlock().canHarvestBlock(turtle.getWorld(), new BlockPos(x,y,z), player)) {
				NonNullList<ItemStack> items = NonNullList.create();
				blockState.getBlock().getDrops(items, turtle.getWorld(), new BlockPos(x,y,z), blockState, 0);;
				turtle.getWorld().setBlockToAir(new BlockPos(x,y,z));
				return items;
			}
		}
		return null;
	}

	public static List<Entity> getEntitiesNearTurtle(ITurtleAccess turtle, FakeTurtlePlayer player, EnumFacing dir) {
		int x = turtle.getPosition().getX()+dir.getFrontOffsetX();
		int y = turtle.getPosition().getY()+dir.getFrontOffsetY();
		int z = turtle.getPosition().getZ()+dir.getFrontOffsetZ();
		AxisAlignedBB box = new AxisAlignedBB(x, y, z, x+1.0D, y+1.0D, z+1.0D);
		return turtle.getWorld().getEntitiesWithinAABBExcludingEntity(player, box);
	}

	public static Entity getClosestShearableEntity(List<Entity> list, Entity ent) {
		Vec3d from = new Vec3d(ent.posX, ent.posY, ent.posZ);
		Entity returnVal = null;
		double lastDistance = Double.MAX_VALUE;
		for (Entity entity : list)
			if (entity instanceof IShearable) {
				Vec3d to = new Vec3d(entity.posX, entity.posY, entity.posZ);
				if (to.distanceTo(from) < lastDistance)
					returnVal = entity;
			}
		return returnVal;
	}

	public static Entity getClosestEntity(List<Entity> list, Entity ent) {
		Vec3d from = new Vec3d(ent.posX, ent.posY, ent.posZ);
		Entity returnVal = null;
		double lastDistance = Double.MAX_VALUE;
		for (Entity entity : list) {
			Vec3d to = new Vec3d(entity.posX, entity.posY, entity.posZ);
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
			stacks.add(e.getItem());
		}
		return stacks;
	}

	public static void addToInv(ITurtleAccess turtle, ItemStack stack) {
		boolean drop = true;
		IInventory inv = turtle.getInventory();
		BlockPos coords = turtle.getPosition();
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack currentStack = inv.getStackInSlot(i);
			if (currentStack.isEmpty()) {
				inv.setInventorySlotContents(i, stack);
				drop = false;
				break;
			}
			if (currentStack.isStackable() && currentStack.isItemEqual(stack)) {
				int space = currentStack.getMaxStackSize() - currentStack.getCount();
				if (stack.getCount() > space) {
					currentStack.setCount(currentStack.getMaxStackSize());
					stack.setCount(stack.getCount() - space);
					drop = true;
				} else {
					currentStack.setCount(currentStack.getCount() + stack.getCount());
					stack.setCount(0);
					drop = false;
					break;
				}
			}
		}
		if (drop) {
			EnumFacing dir = turtle.getDirection();
			turtle.getWorld().spawnEntity(new EntityItem(turtle.getWorld(), coords.getX()+dir.getFrontOffsetX(),
					coords.getY()+dir.getFrontOffsetY()+1, coords.getZ()+dir.getFrontOffsetZ(), stack.copy()));
		}
	}
	
	public static ItemStack getTurtle(boolean isAdvanced, ITurtleUpgrade upgradeLeft, ITurtleUpgrade upgradeRight) {
		if (upgradeLeft == null && upgradeRight == null) {
			return GameRegistry.makeItemStack(String.format("%s:%s",
					"computercraft", isAdvanced ? "turtle_advanced" : "turtle"), 0, 1, "");
		} else {
			ItemStack turtle = GameRegistry.makeItemStack(String.format("%s:%s",
					"computercraft", isAdvanced ? "turtle_advanced" : "turtle_expanded"), 0, 1, "");
			if (upgradeLeft != null)
				NBTHelper.setString(turtle, "leftUpgrade", upgradeLeft.getUpgradeID().toString());
			if (upgradeRight != null)
				NBTHelper.setString(turtle, "rightUpgrade", upgradeRight.getUpgradeID().toString());
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

	public static ItemStack getPocket(boolean advanced) {
		return getPocket(advanced, null);
	}

	public static ItemStack getPocketServerItemStack(IPocketAccess access) {
		try {
			Class pocketServer = Class.forName("dan200.computercraft.shared.pocket.core.PocketServerComputer");
			if (!pocketServer.isInstance(access))
				return ItemStack.EMPTY;
			Field stack = pocketServer.getDeclaredField("m_stack");
			stack.setAccessible(true);
			ItemStack itemStack = (ItemStack) stack.get(access);
			if (itemStack == null)
				return ItemStack.EMPTY;
			return itemStack;
		} catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {
			return ItemStack.EMPTY;
		}
	}

	public static ItemStack getPocket(boolean advanced, IPocketUpgrade pocketUpgrade) {
		ItemStack pocket = GameRegistry.makeItemStack(ModIds.COMPUTERCRAFT + ":pocket_computer", advanced ? 1 : 0,
				1, "");
		if (pocketUpgrade != null) {
			NBTTagCompound tag = new NBTTagCompound();
			tag.setString("upgrade", pocketUpgrade.getUpgradeID().toString());
			pocket.setTagCompound(tag);
		}
		return pocket;
	}
}
