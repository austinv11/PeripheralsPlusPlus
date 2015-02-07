package com.austinv11.peripheralsplusplus.network;

import com.austinv11.peripheralsplusplus.utils.Util;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public class JoinPacket implements IMessage {

	public String player;
	public int height, width;

	public JoinPacket() {}

	public JoinPacket(EntityPlayer player, int height, int width) {
		this.player = player.getCommandSenderName();
		this.height = height;
		this.width = width;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		NBTTagCompound tag = ByteBufUtils.readTag(buf);
		player = tag.getString("player");
		height = tag.getInteger("height");
		width = tag.getInteger("width");
	}

	@Override
	public void toBytes(ByteBuf buf) {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setString("player", player);
		tag.setInteger("height", height);
		tag.setInteger("width", width);
		ByteBufUtils.writeTag(buf, tag);
	}

	public static class JoinPacketHandler implements IMessageHandler<JoinPacket, IMessage> {

		@Override
		public IMessage onMessage(JoinPacket message, MessageContext ctx) {
			EntityPlayer player = Util.getPlayer(message.player);
			if (player.getExtendedProperties("resolution") != null)
				player.registerExtendedProperties("resolution", new ResolutionProperties(message.height, message.width));
			return null;
		}
	}

	public static class ResolutionProperties implements IExtendedEntityProperties {

		public int height,width;

		public ResolutionProperties(int height, int width) {
			this.height = height;
			this.width = width;
		}

		@Override
		public void saveNBTData(NBTTagCompound compound) {
			compound.setInteger("height", height);
			compound.setInteger("width", width);
		}

		@Override
		public void loadNBTData(NBTTagCompound compound) {
			height = compound.getInteger("height");
			width = compound.getInteger("width");
		}

		@Override
		public void init(Entity entity, World world) {}
	}
}
