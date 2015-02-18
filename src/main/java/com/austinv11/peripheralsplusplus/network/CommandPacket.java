package com.austinv11.peripheralsplusplus.network;

import com.austinv11.peripheralsplusplus.client.gui.GuiSmartHelmetOverlay;
import com.austinv11.peripheralsplusplus.smarthelmet.HelmetCommand;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayDeque;
import java.util.UUID;

public class CommandPacket implements IMessage {

	public HelmetCommand[] commands;
	public UUID uuid;
	public boolean doAdd = false;

	public CommandPacket() {}

	public CommandPacket(HelmetCommand[] commands, UUID uuid) {
		this.commands = commands;
		this.uuid = uuid;
	}

	public CommandPacket(HelmetCommand[] commands, UUID uuid, boolean doAdd) {
		this(commands, uuid);
		this.doAdd = doAdd;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		NBTTagCompound tagCompound = ByteBufUtils.readTag(buf);
		uuid = UUID.fromString(tagCompound.getString("uuid"));
		int num = tagCompound.getInteger("num");
		commands = new HelmetCommand[num];
		for (int i = 0; i < num; i++) {
			NBTTagCompound tag = tagCompound.getCompoundTag(i+"");
			HelmetCommand command = null;
			try {
				command = HelmetCommand.getCommandFromName(tag.getString("type"));
				command.readFromNBT(tag.getCompoundTag("command"));
				commands[i] = command;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		doAdd = tagCompound.getBoolean("doAdd");
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
		tagCompound.setBoolean("doAdd", doAdd);
		ByteBufUtils.writeTag(buf, tagCompound);
	}

	public static class CommandPacketHandler implements IMessageHandler<CommandPacket, IMessage> {

		@Override
		public IMessage onMessage(CommandPacket message, MessageContext ctx) {
			ArrayDeque<HelmetCommand> commands = new ArrayDeque<HelmetCommand>();
			if (message.doAdd) {
				if (GuiSmartHelmetOverlay.renderStack.containsKey(message.uuid))
					commands = GuiSmartHelmetOverlay.renderStack.get(message.uuid);
			}
			for (HelmetCommand command : message.commands)
				if (command != null)
					commands.offer(command);
			GuiSmartHelmetOverlay.renderStack.put(message.uuid, commands);
			return null;
		}
	}
}
