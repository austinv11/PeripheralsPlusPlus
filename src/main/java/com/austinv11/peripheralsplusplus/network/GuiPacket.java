package com.austinv11.peripheralsplusplus.network;

import com.austinv11.peripheralsplusplus.PeripheralsPlusPlus;
import com.austinv11.peripheralsplusplus.reference.Reference;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;

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
				Minecraft.getMinecraft().thePlayer.closeScreen();
			else
				Minecraft.getMinecraft().thePlayer.openGui(PeripheralsPlusPlus.instance, Reference.GUIs.HELMET.ordinal(), Minecraft.getMinecraft().thePlayer.worldObj, (int) Minecraft.getMinecraft().thePlayer.posX, (int) Minecraft.getMinecraft().thePlayer.posY, (int) Minecraft.getMinecraft().thePlayer.posZ);
			return null;
		}
	}
}
