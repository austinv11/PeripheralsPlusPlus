package com.austinv11.peripheralsplusplus.network;

import com.austinv11.peripheralsplusplus.entities.EntityRidableTurtle;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;

public class RidableTurtlePacket implements IMessage {

	private int entityId;
	private int movementCode;
	private int dimension;

	public RidableTurtlePacket() {}

	public RidableTurtlePacket(int entityId, int movementCode, int dimension) {
		this.entityId = entityId;
		this.movementCode = movementCode;
		this.dimension = dimension;
	}

	@Override
	public void fromBytes(ByteBuf byteBuf) {
		NBTTagCompound tag = ByteBufUtils.readTag(byteBuf);
		this.entityId = tag.getInteger("entityId");
		this.movementCode = tag.getInteger("movementCode");
		this.dimension = tag.getInteger("dimension");
	}

	@Override
	public void toBytes(ByteBuf byteBuf) {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger("entityId", entityId);
		tag.setInteger("movementCode", movementCode);
		tag.setInteger("dimension", dimension);
		ByteBufUtils.writeTag(byteBuf, tag);
	}

	public enum MovementCode {
		FORWARD(0),
		TURN_LEFT(1),
		TURN_RIGHT(2),
		ASCEND(3),
		DESCEND(4);
		public int code;
		MovementCode(int code) {this.code = code;}
	}

	public static class RidableTurtlePacketHandler implements IMessageHandler<RidableTurtlePacket, IMessage> {
		@Override
		public IMessage onMessage(RidableTurtlePacket ridableTurtlePacket, MessageContext messageContext) {
			EntityRidableTurtle ridableTurtle = (EntityRidableTurtle)MinecraftServer.getServer().worldServerForDimension(ridableTurtlePacket.dimension).
					getEntityByID(ridableTurtlePacket.entityId);
			ridableTurtle.queueAction(ridableTurtlePacket.movementCode);
			return null;
		}
	}
}
