package com.austinv11.peripheralsplusplus.tiles;

import com.austinv11.peripheralsplusplus.cleverbot.AIChatRequest;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.ILuaObject;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import com.google.code.chatterbotapi.*;

import java.util.*;

public class TileEntityAIChatBox extends MountedTileEntity {

	public static String publicName = "aiChatBox";
	private String name = "tileEntityAIChatBox";
	private List<BotSessionLuaObject> sessions = new ArrayList<BotSessionLuaObject>();
	private ChatterBotFactory factory = new ChatterBotFactory();
	private List<IComputerAccess> computers = new ArrayList<IComputerAccess>();

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
		return new String[] { "newSession","getSession","getAllSessions" };
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws LuaException, InterruptedException {
		if (isInvalid()) {
			throw new LuaException("ERROR invalid tile entity");
		}

		// newSession
		if (method == 0) {
			// Run method
			return Methods.newSession(computer, context, this);
		}

		// getSession
		if (method == 1) {
			// Get arguments
			UUID uuid = ArgumentHelper.getUUID(arguments,0);

			// Run method
			return Methods.getSession(computer, context, this, uuid);
		}

		// getAllSessions
		if (method == 2) {
			// Run method
			return Methods.getAllSessions(computer, context, this);
		}

		return new Object[0];
	}

	public void sendEvent(Object[] params) {
		// Base arguments
		List<Object> arguments = new ArrayList<Object>(Arrays.asList(params));
		arguments.add(0,"SIDE");

		for (IComputerAccess computer : computers) {
			// Add the computer side
			arguments.set(0,computer.getAttachmentName());

			// Queue event
			computer.queueEvent(AIChatRequest.EVENT,arguments.toArray());
		}
	}

	@Override
	public void attach(IComputerAccess computer) {
		computers.add(computer);
		super.attach(computer);
	}

	@Override
	public void detach(IComputerAccess computer) {
		computers.remove(computer);
		super.detach(computer);
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

		// Lua arguments:
		// string uuid = newSession()
		public static Object[] newSession(IComputerAccess computer, ILuaContext context, TileEntityAIChatBox tileEntity) throws LuaException, InterruptedException {

			// Get the factory and sessions from the tile entity
			ChatterBotFactory factory = tileEntity.getFactory();
			List<BotSessionLuaObject> sessions = tileEntity.getSessions();

			try	{
				// Generate an UUID
				UUID uuid = ArgumentHelper.randomUUID(sessions);

				// Create the session
				ChatterBot bot = factory.create(ChatterBotType.CLEVERBOT);
				ChatterBotSession botSession = bot.createSession();

				BotSessionLuaObject luaSession = new BotSessionLuaObject(uuid,bot,botSession,computer,tileEntity);
				// Add the session to the tile entity's list
				sessions.add(luaSession);

				// Return corresponding UUID
				return new Object[] { luaSession };

			} catch (Exception e) {
				e.printStackTrace();
				throw new LuaException("ERROR creating session, make sure the server has internet access!");
			}
		}

		// Lua arguments:
		// BotSessionLuaObject session = getSession(string uuid)
		public static Object[] getSession(IComputerAccess computer, ILuaContext context, TileEntityAIChatBox tileEntity, UUID uuid) throws LuaException, InterruptedException {

			// Try to find a match
			for (BotSessionLuaObject session : tileEntity.getSessions()) {
				if (session.getUUID().equals(uuid))
					return new Object[] { session };
			}

			return new Object[0];
		}

		// Lua arguments:
		// table{[string uuid]=BotSessionLuaObject, [string uuid]=BotSessionLuaObject, ...} = getAllSessions()
		public static Object[] getAllSessions(IComputerAccess computer, ILuaContext context, TileEntityAIChatBox tileEntity) throws LuaException, InterruptedException {
			HashMap<String, BotSessionLuaObject> table = new HashMap<String, BotSessionLuaObject>();
			for (BotSessionLuaObject sessionLuaObject : tileEntity.getSessions()) {
				table.put(sessionLuaObject.getUUID().toString(), sessionLuaObject);
			}

			return new Object[] { table };
			//return new Object[] { tileEntity.getSessions().toArray() };
		}
	}

	public static class ArgumentHelper {

		// Get a ChatterBotType, if there's no valid BotType at that index then it will throw a LuaException.
		public static ChatterBotType getBotType(Object[] arguments, int index) throws LuaException, InterruptedException {
			return getBotType(arguments, index, null);
		}

		public static ChatterBotType getBotType(Object[] arguments, int index, ChatterBotType defaultValue) throws LuaException, InterruptedException {
			return stringToType(getString(arguments,index,defaultValue==null ? null : defaultValue.toString()));
		}

		// Get an UUID, if there's no valid uuid at that index then it will throw a LuaException.
		public static UUID getUUID(Object[] arguments, int index) throws LuaException, InterruptedException {
			return getUUID(arguments, index, null);
		}

		public static UUID getUUID(Object[] arguments, int index, UUID defaultValue) throws LuaException, InterruptedException{
			return stringToUUID(getString(arguments,index,defaultValue==null ? null : defaultValue.toString()));
		}

		// Get a string, if there's no string at that index then it will throw a LuaException.
		public static String getString(Object[] arguments, int index) throws LuaException, InterruptedException {
			return getString(arguments,index,null);
		}

		public static String getString(Object[] arguments, int index, String defaultValue) throws LuaException, InterruptedException{
			if (arguments == null || arguments.length <= index) {
				if (defaultValue == null)
					ErrorOut(index,"string","nil");
				else
					return defaultValue;
			}

			if (!(arguments[index] instanceof String)) {
				// Not string...
				if (defaultValue == null)
					ErrorOut(index,"string");
				else
					return defaultValue;
			}

			return (String) arguments[index];
		}


		// Simplifies the LuaException
		static void ErrorOut(int index, String expected) throws LuaException, InterruptedException {
			// index+1 because lua...
			throw new LuaException("Bad argument #"+(index+1)+" ("+expected+" expected)");
		}

		static void ErrorOut(int index, String expected, String got) throws LuaException, InterruptedException {
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
		private final TileEntityAIChatBox source;
		private boolean removed = false;

		public BotSessionLuaObject(final UUID uuid,final ChatterBot bot, final ChatterBotSession session,final IComputerAccess computer, TileEntityAIChatBox source) {
			this.uuid = uuid;
			this.bot = bot;
			this.session = session;
			this.computer = computer;
			this.source = source;
		}

		public String[] getMethodNames() {
			return new String[] { "think","thinkAsync","getUUID","remove" };
		}

		public Object[] callMethod(ILuaContext context, int method, Object[] arguments) throws LuaException, InterruptedException {
			if (removed) {
				throw new LuaException("ERROR accessing removed session!");
			}

			if (source.isInvalid()) {
				throw new LuaException("ERROR invalid tile entity!");
			}

			// boolean success, string response = think(string message)
			if (method == 0) {
				// Get arguments
				String message = ArgumentHelper.getString(arguments, 0);

				// Run method
				thinkAsync(message);

				// Wait for message
				Object[] event = context.pullEvent(AIChatRequest.EVENT);

				// ai_response: string side, bool success, string response, string uuid
				//   event[0]     event[1]      event[2]      event[3]       event[4]

				// Make sure you get the right message and not someone else's
				while (!uuid.toString().equals(event[4])) {
					event = context.pullEvent(AIChatRequest.EVENT);
				}

				return new Object[] { event[2],event[3] };
			}

			// nil = thinkAsync(string message)
			if (method == 1) {
				// Get arguments
				String message = ArgumentHelper.getString(arguments,0);

				// Run method
				thinkAsync(message);
			}

			// getUUID
			if (method == 2) {
				// Run method
				return new Object[] { getUUID().toString() };
			}

			// remove
			if (method == 3) {
				// Run method
				this.removed = true;
				return new Object[] { source.getSessions().remove(this) };
			}

			return new Object[0];
		}

		public UUID getUUID() {
			return uuid;
		}

		// Makes a new thread and "thinks" in that one.
		// When finished it sends an event to the designated computer.
		public void thinkAsync(String message) throws LuaException, InterruptedException {
			AIChatRequest request = new AIChatRequest(source,this,message);
		}

		// Just a normal thinking process. Haults the computer until it gets a reply (or errors out)
		// Called from AIChatRequest
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
