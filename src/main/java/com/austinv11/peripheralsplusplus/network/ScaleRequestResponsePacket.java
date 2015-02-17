package com.austinv11.peripheralsplusplus.network;

import com.austinv11.peripheralsplusplus.tiles.TileEntityAntenna;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChunkCoordinates;

public class ScaleRequestResponsePacket implements IMessage {

	public ChunkCoordinates coords;
	public int id, width, height, dim;

	public ScaleRequestResponsePacket() {}

	public ScaleRequestResponsePacket(ChunkCoordinates coords, int id, int width, int height, int dim) {
		this.coords = coords;
		this.id = id;
		this.width = width;
		this.height = height;
		this.dim = dim;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		NBTTagCompound tag = ByteBufUtils.readTag(buf);
		coords = new ChunkCoordinates(tag.getInteger("x"), tag.getInteger("y"), tag.getInteger("z"));
		id = tag.getInteger("id");
		width = tag.getInteger("width");
		height = tag.getInteger("height");
		dim = tag.getInteger("dim");
	}

	@Override
	public void toBytes(ByteBuf buf) {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger("x", coords.posX);
		tag.setInteger("y", coords.posY);
		tag.setInteger("z", coords.posZ);
		tag.setInteger("id", id);
		tag.setInteger("width", width);
		tag.setInteger("height", height);
		tag.setInteger("dim", dim);
		ByteBufUtils.writeTag(buf, tag);
	}

	public static class ScaleRequestResponsePacketHandler implements IMessageHandler<ScaleRequestResponsePacket, IMessage> {

		@Override
		public IMessage onMessage(ScaleRequestResponsePacket message, MessageContext ctx) {
			TileEntityAntenna antenna = (TileEntityAntenna) MinecraftServer.getServer().worldServerForDimension(message.dim).getTileEntity(message.coords.posX, message.coords.posY, message.coords.posZ);
			antenna.onResponse(message.id, message.width, message.height);
			return null;
		}
	}
}
