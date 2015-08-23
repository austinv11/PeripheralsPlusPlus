package com.austinv11.peripheralsplusplus.tiles;

import com.austinv11.peripheralsplusplus.AIChatRequest;
import com.sun.istack.internal.NotNull;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import com.google.code.chatterbotapi.*;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

public class TileEntityAIChatBox extends MountedTileEntity {

	public static String publicName = "aiChatBox";
	private String name = "tileEntityAIChatBox";
	private HashMap<UUID,ChatterBotSession> sessions = new HashMap<UUID, ChatterBotSession>();
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
			return Methods.newSession(computer,context,this,botType);
		}

		// think
		if (method == 1) {
			// Get arguments
			UUID uuid = ArgumentHelper.getSessionUUID(arguments, 0, sessions.keySet());
			ChatterBotSession session = sessions.get(uuid);
			String message = ArgumentHelper.getString(arguments, 1);

			// Run method
			return Methods.think(computer, context, uuid, session, message);
		}

		// thinkAsync
		if (method == 1) {
			// Get arguments
			UUID uuid = ArgumentHelper.getSessionUUID(arguments, 0, sessions.keySet());
			ChatterBotSession session = sessions.get(uuid);
			String message = ArgumentHelper.getString(arguments,1);

			// Run method
			return Methods.thinkAsync(computer, context, uuid, session, message);
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

	public HashMap<UUID, ChatterBotSession> getSessions() {
		return sessions;
	}

	// Peripheral methods
	public static class Methods {

		// string uuid = newSession([string botType="CLEVERBOT"])
		public static Object[] newSession(IComputerAccess computer, ILuaContext context, TileEntityAIChatBox tileEntity, ChatterBotType botType) throws LuaException, InterruptedException {

			// Get the factory and sessions from the tile entity
			ChatterBotFactory factory = tileEntity.getFactory();
			HashMap<UUID, ChatterBotSession> sessions = tileEntity.getSessions();

			// Generate an UUID
			UUID uuid = ArgumentHelper.randomUUID(sessions.keySet());

			try	{
				// Create the session
				ChatterBot bot = factory.create(botType);
				ChatterBotSession botSession = bot.createSession();

				// Add the session to the tile entity's list
				sessions.put(uuid, botSession);

				// Return corresponding UUID
				return new Object[] { uuid.toString() };

			} catch (Exception e) {
				e.printStackTrace();
				throw new LuaException("Error creating session, make sure the server has internet access!");
			}
		}

		// bool success, string response = think(string uuid, string msg)
		public static Object[] think(IComputerAccess computer, ILuaContext context, UUID uuid, ChatterBotSession session, String message) throws LuaException, InterruptedException {

			thinkAsync(computer, context, uuid, session, message);
			Object[] event = context.pullEvent(AIChatRequest.EVENT);

			// ai_response: string uuid, bool success, string response
			//   event[0]     event[1]     event[2]       event[3]

			return new Object[] { event[2],event[3] };
		}

		// nil = thinkAsync(string uuid, string msg)
		public static Object[] thinkAsync(IComputerAccess computer, ILuaContext context, UUID uuid, ChatterBotSession session, String message) throws LuaException, InterruptedException {

			AIChatRequest request = new AIChatRequest(computer,uuid,session,message);

			return new Object[0];
		}
	}

	public static class ArgumentHelper {

		// Get a ChatterBotType, if there's no valid BotType at that index then it will throw a LuaException.
		public static ChatterBotType getBotType(Object[] arguments, int index) throws LuaException, InterruptedException {
			return getBotType(arguments,index,null);
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


		// Get an UUID that has a corresponding session.
		public static UUID getSessionUUID(Object[] arguments, int index, Set<UUID> existing) throws LuaException, InterruptedException {
			return getSessionUUID(arguments,index,existing,null);
		}

		public static UUID getSessionUUID(Object[] arguments, int index, Set<UUID> existing, @Nullable UUID defaultValue) throws LuaException, InterruptedException {
			UUID uuid = getUUID(arguments,index,defaultValue);

			if (!existing.contains(uuid)) {
				throw new LuaException("Bad argument #"+(index+1)+" (no session with that uuid)");
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
		public static UUID randomUUID(Set<UUID> existing) {
			// Randomize an UUID
			UUID uuid = UUID.randomUUID();

			// Make sure it's 100% unique (in this set)
			while (existing.contains(uuid)) {
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
}
