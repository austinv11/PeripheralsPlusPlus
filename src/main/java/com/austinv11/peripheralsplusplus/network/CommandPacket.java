package com.austinv11.peripheralsplusplus.network;

import com.austinv11.peripheralsplusplus.client.gui.GuiSmartHelmetOverlay;
import com.austinv11.peripheralsplusplus.smarthelmet.ICommand;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayDeque;
import java.util.UUID;

public class CommandPacket implements IMessage {

	public ICommand[] commands;
	public UUID uuid;

	public CommandPacket() {}

	public CommandPacket(ICommand[] commands, UUID uuid) {
		this.commands = commands;
		this.uuid = uuid;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		NBTTagCompound tagCompound = ByteBufUtils.readTag(buf);
		uuid = UUID.fromString(tagCompound.getString("uuid"));
		int num = tagCompound.getInteger("num");
		commands = new ICommand[num];
		for (int i = 0; i < num; i++) {
			NBTTagCompound tag = tagCompound.getCompoundTag(i+"");
			ICommand command = null;
			try {
				command = ICommand.getCommandFromName(tag.getString("type"));
				command.readFromNBT(tag.getCompoundTag("command"));
				commands[i] = command;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		NBTTagCompound tagCompound = new NBTTagCompound();
		tagCompound.setString("uuid", uuid.toString());
		tagCompound.setInteger("num", commands.length);
		for (int i = 0; i < commands.length; i++) {
			if (commands[i] == null)
				continue;
			NBTTagCompound tag = new NBTTagCompound();
			tag.setString("type", commands[i].getCommandName());
			NBTTagCompound command = new NBTTagCompound();
			commands[i].writeToNBT(command);
			tag.setTag("command", command);
			tagCompound.setTag(i+"", tag);
		}
		ByteBufUtils.writeTag(buf, tagCompound);
	}

	public static class CommandPacketHandler implements IMessageHandler<CommandPacket, IMessage> {

		@Override
		public IMessage onMessage(CommandPacket message, MessageContext ctx) {
			ArrayDeque<ICommand> commands = new ArrayDeque<ICommand>();
			for (ICommand command : message.commands)
				if (command != null)
					commands.offer(command);
			GuiSmartHelmetOverlay.renderStack.put(message.uuid, commands);
			return null;
		}
	}
}
