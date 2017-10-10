package com.austinv11.peripheralsplusplus.network;

import com.austinv11.peripheralsplusplus.tiles.TileEntityAntenna;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ScaleRequestResponsePacket implements IMessage {

	public BlockPos coords;
	public int id, width, height, dim;

	public ScaleRequestResponsePacket() {}

	public ScaleRequestResponsePacket(BlockPos coords, int id, int width, int height, int dim) {
		this.coords = coords;
		this.id = id;
		this.width = width;
		this.height = height;
		this.dim = dim;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		NBTTagCompound tag = ByteBufUtils.readTag(buf);
		coords = new BlockPos(tag.getInteger("x"), tag.getInteger("y"), tag.getInteger("z"));
		id = tag.getInteger("id");
		width = tag.getInteger("width");
		height = tag.getInteger("height");
		dim = tag.getInteger("dim");
	}

	@Override
	public void toBytes(ByteBuf buf) {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger("x", coords.getX());
		tag.setInteger("y", coords.getY());
		tag.setInteger("z", coords.getZ());
		tag.setInteger("id", id);
		tag.setInteger("width", width);
		tag.setInteger("height", height);
		tag.setInteger("dim", dim);
		ByteBufUtils.writeTag(buf, tag);
	}

	public static class ScaleRequestResponsePacketHandler implements IMessageHandler<ScaleRequestResponsePacket, IMessage> {

		@Override
		public IMessage onMessage(ScaleRequestResponsePacket message, MessageContext ctx) {
			TileEntityAntenna antenna = (TileEntityAntenna) DimensionManager.getWorld(message.dim)
					.getTileEntity(new BlockPos(message.coords.getX(), message.coords.getY(), message.coords.getZ()));
			antenna.onResponse(message.id, message.width, message.height);
			return null;
		}
	}
}
