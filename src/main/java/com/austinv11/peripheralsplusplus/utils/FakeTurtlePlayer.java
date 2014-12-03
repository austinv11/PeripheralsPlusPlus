package com.austinv11.peripheralsplusplus.utils;

import com.mojang.authlib.GameProfile;
import dan200.computercraft.api.turtle.ITurtleAccess;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.Facing;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;

import java.util.UUID;

public class FakeTurtlePlayer extends FakePlayer {
	private static final GameProfile s_profile = new GameProfile(UUID.fromString("0d0c4ca0-4ff1-11e4-916c-0800200c9a66"), "ComputerCraft");

	public FakeTurtlePlayer(WorldServer world) {
		super(world, s_profile);
	}

	public FakeTurtlePlayer(ITurtleAccess turtle) {
		this((WorldServer) turtle.getWorld());
		ChunkCoordinates position = turtle.getPosition();
		setPosition(position.posX + 0.5D, position.posY + 0.5D, position.posZ + 0.5D);
	}

	public void addToInv(ITurtleAccess turtle, ItemStack stack) {
		boolean drop = false;
		IInventory inv = turtle.getInventory();
		ChunkCoordinates coords = turtle.getPosition();
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack currentStack = inv.getStackInSlot(i);
			if (currentStack == null) {
				inv.setInventorySlotContents(i, stack.copy());
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
				}
			}
		}
		if (drop) {
			int dir = turtle.getDirection();
			turtle.getWorld().spawnEntityInWorld(new EntityItem(turtle.getWorld(), coords.posX+Facing.offsetsXForSide[dir], coords.posY+Facing.offsetsYForSide[dir], coords.posZ+Facing.offsetsZForSide[dir], stack));
		}
	}

	@Override
	public float getEyeHeight() {
		return 0.0F;
	}

	@Override
	public float getDefaultEyeHeight() {
		return 0.0F;
	}

	@Override
	public void func_146100_a(TileEntity entity) {}

	@Override
	public void mountEntity(Entity entity) {}

	@Override
	public void dismountEntity(Entity entity) {}
}
