package com.austinv11.peripheralsplusplus.tile;

import com.austinv11.peripheralsplusplus.reference.Config;
import com.austinv11.peripheralsplusplus.util.CCMethod;
import com.austinv11.peripheralsplusplus.util.Util;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EntityDamageSource;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TileChatBox extends TilePeripheral {
	public static final String name = "tileChatBox";
	protected static ArrayList<TileChatBox> chatBoxes = new ArrayList<TileChatBox>();

	public TileChatBox() {
		chatBoxes.add(this);
	}

	@CCMethod
	public boolean say(Object[] arguments) throws LuaException {
		if (arguments.length < 2)
			throw new LuaException("Wrong number of arguments. 2 expected.");
		if (!(arguments[0] instanceof String))
			throw new LuaException("Bad argument #1. Expected string.");
		if (!(arguments[1] instanceof Double))
			throw new LuaException("Bad argument #2. Expected number.");

		String message = (String) arguments[0];
		int range = (int) (double) (Double) arguments[1];
		String label = String.format("[%d,%d,%d]", getPos().getX(), getPos().getY(), getPos().getZ());

		if (arguments.length > 2) {
			if (!(arguments[2] instanceof String)) {
				throw new LuaException("Bad argument #3. Expected string.");
			}
			label = (String) arguments[2];
		}
		sendChatMessageInRange(message, range > Config.chatBoxMaxRange ? Config.chatBoxMaxRange : range, label);
		return true;
	}

	@CCMethod
	public boolean tell(Object[] arguments) throws LuaException {
		if (arguments.length < 2)
			throw new LuaException("Wrong number of arguments. 2 expected");
		if (!(arguments[0] instanceof String))
			throw new LuaException("Bad argument #1. Expected string.");
		if (!(arguments[1] instanceof String))
			throw new LuaException("Bad argument #2. Expected string.");

		String recipientName = (String) arguments[0];
		String message = (String) arguments[1];
		String label = String.format("[%d,%d,%d]", getPos().getX(), getPos().getY(), getPos().getZ());

		if (arguments.length > 2) {
			if (!(arguments[2] instanceof String)) {
				throw new LuaException("Bad argument #3. Expected string.");
			}
			label = (String) arguments[2];
		}

		String messageWithLabel = label + " " + message;
		EntityPlayer recipient = MinecraftServer.getServer().getConfigurationManager().getPlayerByUsername(recipientName);
		if (recipient != null) {
			recipient.addChatMessage(new ChatComponentText(messageWithLabel));
			return true;
		}
		return false;
	}

	private void sendChatMessageInRange(String message, int range, String label) {
		String messageWithLabel = label + " " + message;
		List<? extends EntityPlayer> players;

		if (range >= 0) {
			AxisAlignedBB bb = AxisAlignedBB.fromBounds(pos.getX() - range, pos.getY() - range, pos.getZ() - range, pos.getX() + range, pos.getY() + range, pos.getZ() + range);
			players = worldObj.getEntitiesWithinAABB(EntityPlayer.class, bb);
		} else {
			players = MinecraftServer.getServer().getConfigurationManager().playerEntityList;
		}

		for (EntityPlayer player : players) {
			player.addChatMessage(new ChatComponentText(messageWithLabel));
		}
	}

	@Override
	public String getType() {
		return "chatBox";
	}

	public static class ChatListener {
		@SubscribeEvent
		public void onChat(ServerChatEvent event) {
			if (event.message.startsWith(Config.commandDiscriminator)) { // TODO: Configurable
				String commandName = event.message.substring(1);
				if (commandName.contains(" ")) {
					commandName = commandName.substring(0, commandName.indexOf(" "));
				}
				String[] args = new String[] {};
				if (event.message.contains(" ")) {
					args = event.message.substring(event.message.indexOf(" ") + 1).split("\\s+");
				}

				for (TileChatBox chatBox : TileChatBox.chatBoxes) {
					chatBox.queueEvent("command", new Object[] {event.username, commandName, Util.listToIndexedMap(Arrays.asList(args))});
				}
				event.setCanceled(true);
			} else {
				for (TileChatBox chatBox : TileChatBox.chatBoxes) {
					chatBox.queueEvent("chat", new Object[] {event.username, event.message});
				}
			}
		}

		@SubscribeEvent
		public void onDeath(LivingDeathEvent event) {
			if (event.entity instanceof EntityPlayer) {
				String killerName = null;
				if (event.source instanceof EntityDamageSource) {
					killerName = event.source.getEntity().getName();
				}

				for (TileChatBox chatBox : TileChatBox.chatBoxes) {
					chatBox.queueEvent("death", new Object[] {event.entity.getName(), killerName, event.source.damageType});
				}
			}
		}
	}
}
