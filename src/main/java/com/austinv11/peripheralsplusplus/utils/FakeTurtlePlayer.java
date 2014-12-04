package com.austinv11.peripheralsplusplus.utils;

import com.mojang.authlib.GameProfile;
import dan200.computercraft.api.turtle.ITurtleAccess;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
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
}
