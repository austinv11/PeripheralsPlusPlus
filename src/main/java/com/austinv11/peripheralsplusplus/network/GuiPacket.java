package com.austinv11.peripheralsplusplus.network;

import com.austinv11.peripheralsplusplus.PeripheralsPlusPlus;
import com.austinv11.peripheralsplusplus.reference.Reference;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class GuiPacket implements IMessage {

	public boolean close;

	public GuiPacket() {}

	public GuiPacket(boolean close) {
		this.close = close;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		NBTTagCompound tag = ByteBufUtils.readTag(buf);
		close = tag.getBoolean("close");
	}

	@Override
	public void toBytes(ByteBuf buf) {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setBoolean("close", close);
		ByteBufUtils.writeTag(buf, tag);
	}

	public static class GuiPacketHandler implements IMessageHandler<GuiPacket, IMessage> {

		@Override
		public IMessage onMessage(GuiPacket message, MessageContext ctx) {
			if (message.close)
				Minecraft.getMinecraft().player.closeScreen();
			else
				Minecraft.getMinecraft().player.openGui(PeripheralsPlusPlus.instance, Reference.GUIs.HELMET.ordinal(),
						Minecraft.getMinecraft().player.world, (int) Minecraft.getMinecraft().player.posX,
						(int) Minecraft.getMinecraft().player.posY, (int) Minecraft.getMinecraft().player.posZ);
			return null;
		}
	}
}
