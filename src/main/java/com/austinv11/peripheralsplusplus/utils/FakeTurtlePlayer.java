package com.austinv11.peripheralsplusplus.utils;

import com.mojang.authlib.GameProfile;
import dan200.computercraft.api.turtle.ITurtleAccess;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.item.EntityMinecartHopper;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.*;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.IChatComponent;
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

	@Override
	public void addChatMessage(IChatComponent chatmessagecomponent){}

	@Override
	public void displayGUIWorkbench(int p_71058_1_, int p_71058_2_, int p_71058_3_){}

	@Override
	public void displayGUIEnchantment(int p_71002_1_, int p_71002_2_, int p_71002_3_, String p_71002_4_){}

	@Override
	public void displayGUIAnvil(int p_82244_1_, int p_82244_2_, int p_82244_3_){}

	@Override
	public void displayGUIChest(IInventory p_71007_1_){}

	@Override
	public void func_146093_a(TileEntityHopper p_146093_1_){}

	@Override
	public void displayGUIHopperMinecart(EntityMinecartHopper p_96125_1_){}

	@Override
	public void func_146101_a(TileEntityFurnace p_146101_1_){}

	@Override
	public void func_146102_a(TileEntityDispenser p_146102_1_){}

	@Override
	public void func_146098_a(TileEntityBrewingStand p_146098_1_){}

	@Override
	public void func_146104_a(TileEntityBeacon p_146104_1_) {}

	@Override
	public void displayGUIMerchant(IMerchant p_71030_1_, String p_71030_2_) {}

	@Override
	public void displayGUIHorse(EntityHorse p_110298_1_, IInventory p_110298_2_) {}
}
