package miscperipherals.external;

import miscperipherals.module.ModuleRailcraft;
import miscperipherals.safe.Reflector;
import mods.railcraft.api.tracks.ITrackInstance;
import dan200.computer.api.IComputerAccess;

public class ExtTrackLimiter extends ExtTrack {
	public static int locoSpeeds = 0;
	
	public ExtTrackLimiter(ITrackInstance track) {
		super(track);
	}

	@Override
	public String getType() {
		return "trackLimiter";
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
				else if (!(arguments[0] instanceof Double)) throw new Exception("bad argument #1 (expected number)");
				
				int speed = (int)Math.floor((Double)arguments[0]);
				if (speed != -1 && (speed < 1 || speed > locoSpeeds)) throw new Exception("bad locomotive speed "+speed+" (expected -1|1-"+locoSpeeds+")");
				
				Reflector.setField(track, "mode", (byte)(speed == -1 ? locoSpeeds - 1 : speed));
				ModuleRailcraft.updateTrack(track);
				return callMethod(computer, 1, arguments);
			}
			case 1: {
				byte speed = Reflector.getField(track, "mode", Byte.class);
				return new Object[] {speed == locoSpeeds - 1 ? -1 : speed % locoSpeeds};
			}
		}
		
		return new Object[0];
	}
	
	static {		
		Object[] locoSpeeds = Reflector.invoke("mods.railcraft.common.carts.EntityLocomotive$LocoSpeed", "values", Object[].class);
		if (locoSpeeds != null) ExtTrackLimiter.locoSpeeds = locoSpeeds.length;
	}
}
