package com.austinv11.peripheralsplusplus.utils;

import com.mojang.authlib.GameProfile;
import dan200.computercraft.api.turtle.ITurtleAccess;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.Facing;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;

public class FakePlayer extends EntityPlayer{

	private static final int[] YAW = {0, 0, 180, 360, 90, 270};
	private static final int[] PITCH = {90, -90, 0, 0, 0, 0};

	public FakePlayer(World p_i45324_1_, GameProfile p_i45324_2_) {
		super(p_i45324_1_, p_i45324_2_);
	}

	@Override
	public void addChatMessage(IChatComponent p_145747_1_) {}

	@Override
	public boolean canCommandSenderUseCommand(int p_70003_1_, String p_70003_2_) {
		return false;
	}

	@Override
	public ChunkCoordinates getPlayerCoordinates() {
		return new ChunkCoordinates((int)Math.floor(posX), (int)Math.floor(posY), (int)Math.floor(posZ));
	}

	public void alignToTurtle(ITurtleAccess turtle) {
		if (turtle.getWorld() != worldObj)
			Logger.warn("Turtle "+turtle+" requested a fakeplayer but the world mismatches, unexpected things may happen! (t="+turtle.getWorld()+" p="+worldObj+")");
		setFlag(1, false);
		setPositionAndRotation(turtle.getPosition().posX + 0.5D + (0.48D * Facing.offsetsXForSide[turtle.getDirection()]), turtle.getPosition().posY + 0.5D + (0.48D * Facing.offsetsYForSide[turtle.getDirection()]), turtle.getPosition().posZ + 0.5D + (0.48D * Facing.offsetsZForSide[turtle.getDirection()]), YAW[turtle.getDirection()], PITCH[turtle.getDirection()]);
		this.inventory = new InventoryPlayer(this);
		inventory.mainInventory[0] = new ItemStack(Items.stick);
		for (int i = 1; i <= turtle.getInventory().getSizeInventory(); i++) {
			inventory.mainInventory[i] = turtle.getInventory().getStackInSlot(i - 1);
		}
		for (int i = turtle.getInventory().getSizeInventory() + 1; i < inventory.mainInventory.length; i++) {
			inventory.mainInventory[i] = new ItemStack(Items.stick);
		}
	}
}
