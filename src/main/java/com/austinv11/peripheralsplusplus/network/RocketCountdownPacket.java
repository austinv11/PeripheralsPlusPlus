package com.austinv11.peripheralsplusplus.network;

import com.austinv11.peripheralsplusplus.entities.EntityRocket;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;

public class RocketCountdownPacket implements IMessage {

	public int rocketID;
	public int countdownNum;

	public RocketCountdownPacket(){}

	public RocketCountdownPacket(Entity rocket, int countdownNum) {
		rocketID = rocket.getEntityId();
		this.countdownNum = countdownNum;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		NBTTagCompound tag = ByteBufUtils.readTag(buf);
		this.rocketID = tag.getInteger("rocketID");
		this.countdownNum = tag.getInteger("countdownNum");
	}

	@Override
	public void toBytes(ByteBuf buf) {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger("rocketID", rocketID);
		tag.setInteger("countdownNum", countdownNum);
		ByteBufUtils.writeTag(buf, tag);
	}

	public static class RocketCountdownPacketHandler implements IMessageHandler<RocketCountdownPacket, IMessage> {

		@Override
		public IMessage onMessage(RocketCountdownPacket message, MessageContext ctx) {
			EntityRocket rocket = (EntityRocket)Minecraft.getMinecraft().theWorld.getEntityByID(message.rocketID);
			rocket.onCount(message.countdownNum);
			return null;
		}
	}
}
