package com.austinv11.peripheralsplusplus.network;

import com.austinv11.peripheralsplusplus.entities.EntityRocket;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

public class RocketLaunchPacket implements IMessage {

	public int ent;
	public int world;

	public RocketLaunchPacket(){}

	public RocketLaunchPacket(Entity entity, World world) {
		this.ent = entity.getEntityId();
		this.world = world.provider.dimensionId;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		NBTTagCompound tag = ByteBufUtils.readTag(buf);
		ent = tag.getInteger("ent");
		world = tag.getInteger("world");
	}

	@Override
	public void toBytes(ByteBuf buf) {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger("ent", ent);
		tag.setInteger("world", world);
		ByteBufUtils.writeTag(buf, tag);
	}

	public static class RocketLaunchPacketHandler implements IMessageHandler<RocketLaunchPacket, IMessage> {

		@Override
		public IMessage onMessage(RocketLaunchPacket message, MessageContext ctx) {
			World world = MinecraftServer.getServer().worldServerForDimension(message.world);
			EntityRocket rocket = (EntityRocket) world.getEntityByID(message.ent);
			rocket.setIsActive(true);
			return null;
		}
	}
}
