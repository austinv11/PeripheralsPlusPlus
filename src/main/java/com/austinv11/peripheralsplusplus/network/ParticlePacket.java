package com.austinv11.peripheralsplusplus.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ParticlePacket implements IMessage {

	private String name;
	private double x;
	private double y;
	private double z;
	private double xVel;
	private double yVel;
	private double zVel;

	public ParticlePacket() {}

	public ParticlePacket(String name, double x, double y, double z, double xVel, double yVel, double zVel) {
		this.name = name;
		this.x = x;
		this.y = y;
		this.z = z;
		this.xVel = xVel;
		this.yVel = yVel;
		this.zVel = zVel;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		NBTTagCompound tag = ByteBufUtils.readTag(buf);
		name = tag.getString("name");
		x = tag.getDouble("x");
		y = tag.getDouble("y");
		z = tag.getDouble("z");
		xVel = tag.getDouble("xVel");
		yVel = tag.getDouble("yVel");
		zVel = tag.getDouble("zVel");
	}

	@Override
	public void toBytes(ByteBuf buf) {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setString("name", name);
		tag.setDouble("x", x);
		tag.setDouble("y", y);
		tag.setDouble("z", z);
		tag.setDouble("xVel", xVel);
		tag.setDouble("yVel", yVel);
		tag.setDouble("zVel", zVel);
		ByteBufUtils.writeTag(buf, tag);
	}

	public static class ParticlePacketHandler implements IMessageHandler<ParticlePacket, IMessage> {

		@SideOnly(Side.CLIENT)
		@Override
		public IMessage onMessage(final ParticlePacket message, MessageContext ctx) {
			Runnable noteThread = () -> {
                World world = Minecraft.getMinecraft().world;
                EnumParticleTypes particle = EnumParticleTypes.getByName(message.name);
                if (particle != null)
                world.spawnParticle(particle, message.x, message.y, message.z, message.xVel, message.yVel,
                        message.zVel);
            };
			noteThread.run();
			return null;
		}

	}

}
