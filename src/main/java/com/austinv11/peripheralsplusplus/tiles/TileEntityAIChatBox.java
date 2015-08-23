package com.austinv11.peripheralsplusplus.tiles;

import com.austinv11.peripheralsplusplus.AIChatRequest;
import com.sun.istack.internal.NotNull;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.ILuaObject;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import com.google.code.chatterbotapi.*;

import javax.annotation.Nullable;
import java.util.*;

public class TileEntityAIChatBox extends MountedTileEntity {

	public static String publicName = "aiChatBox";
	private String name = "tileEntityAIChatBox";
	private List<BotSessionLuaObject> sessions = new ArrayList<BotSessionLuaObject>();
	private ChatterBotFactory factory = new ChatterBotFactory();

	public TileEntityAIChatBox() {
		super();
	}

	public String getName() {
		return name;
	}

	@Override
	public String getType() {
		return publicName;
	}

	@Override
	public String[] getMethodNames() {
		return new String[] { "newSession","think","thinkAsync" };
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws LuaException, InterruptedException {
		// newSession
		if (method == 0) {
			// Get arguments
			ChatterBotType botType = ArgumentHelper.getBotType(arguments, 0, ChatterBotType.CLEVERBOT);

			// Run method
			return Methods.newSession(computer, context, this, botType);
		}

		return new Object[0];
	}

	@Override
	public boolean equals(IPeripheral other) {
		return this == other;
	}

	public ChatterBotFactory getFactory() {
		return factory;
	}

	public List<BotSessionLuaObject> getSessions() {
		return sessions;
	}

	// Peripheral methods
	public static class Methods {

		// string uuid = newSession([string botType="CLEVERBOT"])
		public static Object[] newSession(IComputerAccess computer, ILuaContext context, TileEntityAIChatBox tileEntity, ChatterBotType botType) throws LuaException, InterruptedException {

			// Get the factory and sessions from the tile entity
			ChatterBotFactory factory = tileEntity.getFactory();
			List<BotSessionLuaObject> sessions = tileEntity.getSessions();

			try	{
				// Generate an UUID
				UUID uuid = ArgumentHelper.randomUUID(sessions);

				// Create the session
				ChatterBot bot = factory.create(botType);
				ChatterBotSession botSession = bot.createSession();

				BotSessionLuaObject luaSession = new BotSessionLuaObject(uuid,bot,botSession,computer);
				// Add the session to the tile entity's list
				sessions.add(luaSession);

				// Return corresponding UUID
				return new Object[] { luaSession };

			} catch (Exception e) {
				e.printStackTrace();
				throw new LuaException("Error creating session, make sure the server has internet access!");
			}
		}
	}

	public static class ArgumentHelper {

		// Get a ChatterBotType, if there's no valid BotType at that index then it will throw a LuaException.
		public static ChatterBotType getBotType(Object[] arguments, int index) throws LuaException, InterruptedException {
			return getBotType(arguments, index, null);
		}

		public static ChatterBotType getBotType(Object[] arguments, int index, @Nullable ChatterBotType defaultValue) throws LuaException, InterruptedException {
			if (arguments == null || arguments.length <= index) {
				if (defaultValue == null)
					ErrorOut(index,"string","nil");
				else
					return defaultValue;
			}

			ChatterBotType botType = stringToType((String) arguments[index]);

			if (botType == null) {
				if (defaultValue == null)
					ErrorOut(index,"string");
				else
					return defaultValue;
			}

			return botType;
		}

		// Get an UUID, if there's no valid uuid at that index then it will throw a LuaException.
		public static UUID getUUID(Object[] arguments, int index) throws LuaException, InterruptedException {
			return getUUID(arguments, index, null);
		}

		public static UUID getUUID(Object[] arguments, int index, @Nullable UUID defaultValue) throws LuaException, InterruptedException{
			if (arguments == null || arguments.length <= index) {
				if (defaultValue == null)
					ErrorOut(index,"string","nil");
				else
					return defaultValue;
			}

			UUID uuid = stringToUUID((String) arguments[index]);

			if (uuid == null) {
				if (defaultValue == null)
					ErrorOut(index,"string");
				else
					return defaultValue;
			}

			return uuid;
		}

		// Get a string, if there's no string at that index then it will throw a LuaException.
		public static String getString(Object[] arguments, int index) throws LuaException, InterruptedException {
			return getString(arguments,index,null);
		}

		public static String getString(Object[] arguments, int index, @Nullable String defaultValue) throws LuaException, InterruptedException{
			if (arguments == null || arguments.length <= index) {
				if (defaultValue == null)
					ErrorOut(index,"string","nil");
				else
					return defaultValue;
			}

			String string = (String) arguments[index];

			if (string == null) {
				if (defaultValue == null)
					ErrorOut(index,"string");
				else
					return defaultValue;
			}

			return string;
		}


		// Simplifies the LuaException
		static void ErrorOut(int index, @NotNull String expected) throws LuaException, InterruptedException {
			// index+1 because lua...
			throw new LuaException("Bad argument #"+(index+1)+" ("+expected+" expected)");
		}

		static void ErrorOut(int index, @NotNull String expected, @NotNull String got) throws LuaException, InterruptedException {
			// index+1 because lua...
			throw new LuaException("Bad argument #"+(index+1)+" ("+expected+" expected, got "+got+")");
		}


		// Get a randomly generated UUID, that's guaranteed to be unique (among its comrades).
		public static UUID randomUUID(List<BotSessionLuaObject> existing) {
			// Randomize an UUID
			UUID uuid = UUID.randomUUID();

			// Make sure it's 100% unique (in this set)
			while (BotSessionLuaObject.containsUUID(existing,uuid)) {
				// Regenerate the UUID
				uuid = UUID.randomUUID();
			}
			return uuid;
		}

		// Try to convert a string variable to a ChatterBotType. If no match then return null.
		static ChatterBotType stringToType(String string) throws LuaException, InterruptedException {
			if (string != null)
				string = string.toUpperCase();

			// Loop through each type, find a match
			for (ChatterBotType type : ChatterBotType.values() ) {
				if (type.toString().equals(string))
					return type;
			}

			// No match!
			return null;
		}

		// Try to convert a string variable to an UUID. If the string is invalid then return null.
		static UUID stringToUUID(String string) throws LuaException, InterruptedException {
			if (string == null)
				return null;

			return UUID.fromString(string);
		}
	}

	public static class BotSessionLuaObject implements ILuaObject {
		private final UUID uuid;
		private final ChatterBot bot;
		private final ChatterBotSession session;
		private final IComputerAccess computer;

		public BotSessionLuaObject(final UUID uuid,final ChatterBot bot, final ChatterBotSession session,final IComputerAccess computer) {
			this.uuid = uuid;
			this.bot = bot;
			this.session = session;
			this.computer = computer;
		}

		public String[] getMethodNames() {
			return new String[] { "think","thinkAsync","getUUID" };
		}

		public Object[] callMethod(ILuaContext context, int method, Object[] arguments) throws LuaException, InterruptedException {
			// boolean success, string response = think(string message)
			if (method == 0) {
				// Get arguments
				String message = ArgumentHelper.getString(arguments, 0);

				// Run method
				thinkAsync(computer, context, uuid, session, message);

				// Wait for message
				Object[] event = context.pullEvent(AIChatRequest.EVENT);

				// Make sure you get the right message and not someone else's
				while (!uuid.toString().equals(event[3])) {
					event = context.pullEvent(AIChatRequest.EVENT);
				}

				// ai_response: bool success, string response, string uuid
				//   event[0]     event[1]       event[2]       event[3]

				return new Object[] { event[1],event[2] };
			}

			// nil = think(string message)
			if (method == 1) {
				// Get arguments
				String message = ArgumentHelper.getString(arguments,0);

				// Run method
				thinkAsync(computer,context,uuid,session,message);
			}

			// getUUID
			if (method == 2) {
				// Run method
				return new Object[] { getUUID().toString() };
			}

			return new Object[0];
		}

		public UUID getUUID() {
			return uuid;
		}

		// Makes a new thread and "thinks" in that one.
		// When finished it sends an event to the designated computer.
		public void thinkAsync(IComputerAccess computer, ILuaContext context, UUID uuid, ChatterBotSession session, String message) throws LuaException, InterruptedException {
			AIChatRequest request = new AIChatRequest(computer,this,message);
		}

		// Just a normal thinking process. Haults the computer until it gets a reply (or errors out)
		public String think(String message) throws Exception {
			return session.think(message);
		}

		// Check the entire list if there's a matching UUID inside it
		public static boolean containsUUID(List<BotSessionLuaObject> list, UUID uuid) {
			for (BotSessionLuaObject botSession : list) {
				if (botSession.getUUID() == uuid)
					return true;
			}
			return false;
		}
	}
}
