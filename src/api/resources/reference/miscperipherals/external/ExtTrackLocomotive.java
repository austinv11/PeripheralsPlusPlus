package miscperipherals.external;

import miscperipherals.module.ModuleRailcraft;
import miscperipherals.safe.Reflector;
import miscperipherals.util.Util;
import mods.railcraft.api.tracks.ITrackInstance;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import dan200.computer.api.IComputerAccess;

public class ExtTrackLocomotive extends ExtTrack {
	public static final Enum[] locoModes;
	public static final BiMap<String, Enum> modeLookup = HashBiMap.create();
	
	public ExtTrackLocomotive(ITrackInstance track) {
		super(track);
	}

	@Override
	public String getType() {
		return "trackLocomotive";
	}

	@Override
	public String[] getMethodNames() {
		return new String[] {"set", "get"};
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, int method, Object[] arguments) throws Exception {
		switch (method) {
			case 0: {
				if (arguments.length < 1) throw new Exception("too few arguments");
				else if (!(arguments[0] instanceof String)) throw new Exception("bad argument #1 (expected string)");
				
				String smode = ((String)arguments[0]).toLowerCase();
				Enum mode = modeLookup.get(smode);
				if (mode == null) throw new Exception("bad locomotive mode "+Util.camelCase((String)arguments[0])+" (expected "+Util.joinCC("/", locoModes)+")");
				
				Reflector.setField(track, "mode", (byte)mode.ordinal());
				ModuleRailcraft.updateTrack(track);
				return callMethod(computer, 1, arguments);
			}
			case 1: {
				return new Object[] {Util.camelCase(locoModes[Reflector.getField(track, "mode", Byte.class)].toString())};
			}
		}
		
		return new Object[0];
	}
	
	static {
		locoModes = Reflector.invoke("mods.railcraft.common.carts.EntityLocomotive$LocoMode", "values", Enum[].class);
		for (Enum mode : locoModes) modeLookup.put(Util.camelCase(mode.toString()).toLowerCase(), mode);
	}
}
