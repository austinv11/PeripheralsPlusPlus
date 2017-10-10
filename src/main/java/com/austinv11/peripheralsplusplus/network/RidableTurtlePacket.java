package com.austinv11.peripheralsplusplus.network;

import com.austinv11.peripheralsplusplus.entities.EntityRidableTurtle;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.UUID;

public class RidableTurtlePacket implements IMessage {
	private UUID entityId;
	private EntityRidableTurtle.MovementCode movementCode;
	private int dimension;

	public RidableTurtlePacket() {}

	public RidableTurtlePacket(UUID entityId, EntityRidableTurtle.MovementCode movementCode, int dimension) {
		this.entityId = entityId;
		this.movementCode = movementCode;
		this.dimension = dimension;
	}

	@Override
	public void fromBytes(ByteBuf byteBuf) {
		NBTTagCompound tag = ByteBufUtils.readTag(byteBuf);
		this.entityId = tag.getUniqueId("entityId");
		this.movementCode = EntityRidableTurtle.MovementCode.values()[tag.getInteger("movementCode")];
		this.dimension = tag.getInteger("dimension");
	}

	@Override
	public void toBytes(ByteBuf byteBuf) {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setUniqueId("entityId", entityId);
		tag.setInteger("movementCode", movementCode.ordinal());
		tag.setInteger("dimension", dimension);
		ByteBufUtils.writeTag(byteBuf, tag);
	}

	public static class RidableTurtlePacketHandler implements IMessageHandler<RidableTurtlePacket, IMessage> {
		@Override
		public IMessage onMessage(RidableTurtlePacket ridableTurtlePacket, MessageContext messageContext) {
			EntityRidableTurtle ridableTurtle =
					(EntityRidableTurtle) DimensionManager.getWorld(ridableTurtlePacket.dimension)
							.getEntityFromUuid(ridableTurtlePacket.entityId);
			if (ridableTurtle != null)
				ridableTurtle.tryMove(ridableTurtlePacket.movementCode);
			return null;
		}
	}
}
