package com.austinv11.peripheralsplusplus.proxy;

import com.austinv11.peripheralsplusplus.entities.EntityRocket;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import java.util.List;

public class RocketGUIPacket implements IMessage {

	public EntityPlayer player;

	public RocketGUIPacket() {}

	public RocketGUIPacket(EntityPlayer player) {
		this.player = player;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		List<EntityPlayer> players = MinecraftServer.getServer().getConfigurationManager().playerEntityList;
		for (EntityPlayer p : players)
			if (p.getCommandSenderName().equals(ByteBufUtils.readUTF8String(buf))) {
				player = p;
				break;
			}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, player.getCommandSenderName());
	}

	public static class RocketGUIPacketHandler implements IMessageHandler<RocketGUIPacket, IMessage> {

		@Override
		public IMessage onMessage(RocketGUIPacket message, MessageContext ctx) {
			EntityPlayer player = message.player;
			MovingObjectPosition mop = player.rayTrace(4.0D, 1.0F);
			if (mop.entityHit != null && mop.entityHit instanceof EntityRocket)
				return new RocketGUIPacketResponse(mop.entityHit);
			return null;
		}
	}

	public static class RocketGUIPacketResponse implements IMessage {

		public Entity entity;
		private World world;

		public RocketGUIPacketResponse() {}

		public RocketGUIPacketResponse(Entity entity) {
			this.entity = entity;
			this.world = entity.worldObj;
		}

		@Override
		public void fromBytes(ByteBuf buf) {
			NBTTagCompound tag = ByteBufUtils.readTag(buf);
			world = MinecraftServer.getServer().worldServerForDimension(tag.getInteger("dim"));
			entity = world.getEntityByID(tag.getInteger("id"));
		}

		@Override
		public void toBytes(ByteBuf buf) {
			NBTTagCompound tag = new NBTTagCompound();
			tag.setInteger("dim", world.provider.dimensionId);
			tag.setInteger("id", entity.getEntityId());
			ByteBufUtils.writeTag(buf, tag);
		}
	}
}
