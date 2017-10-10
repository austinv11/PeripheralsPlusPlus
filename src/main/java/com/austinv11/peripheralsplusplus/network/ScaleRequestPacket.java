package com.austinv11.peripheralsplusplus.network;

import com.austinv11.peripheralsplusplus.PeripheralsPlusPlus;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ScaleRequestPacket implements IMessage {

	public BlockPos coords;
	public int id, dim;

	public ScaleRequestPacket() {}

	public ScaleRequestPacket(TileEntity te, int id, int dim) {
		this.coords = te.getPos();
		this.id = id;
		this.dim = dim;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		NBTTagCompound tag = ByteBufUtils.readTag(buf);
		coords = new BlockPos(tag.getInteger("x"), tag.getInteger("y"), tag.getInteger("z"));
		id = tag.getInteger("id");
		dim = tag.getInteger("dim");
	}

	@Override
	public void toBytes(ByteBuf buf) {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger("x", coords.getX());
		tag.setInteger("y", coords.getY());
		tag.setInteger("z", coords.getZ());
		tag.setInteger("id", id);
		tag.setInteger("dim", dim);
		ByteBufUtils.writeTag(buf, tag);
	}

	public static class ScaleRequestPacketHandler implements IMessageHandler<ScaleRequestPacket, IMessage> {

		@Override
		public IMessage onMessage(ScaleRequestPacket message, MessageContext ctx) {
			ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft());
			PeripheralsPlusPlus.NETWORK.sendToServer(new ScaleRequestResponsePacket(message.coords, message.id,
					resolution.getScaledWidth(), resolution.getScaledHeight(), message.dim));
			return null;
		}
	}
}
