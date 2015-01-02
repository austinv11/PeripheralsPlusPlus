package miscperipherals.external;

import miscperipherals.safe.Reflector;
import mods.railcraft.api.tracks.ITrackInstance;
import dan200.computer.api.IComputerAccess;

public abstract class ExtTrackNumber extends ExtTrack {
	private final String field;
	private final Class fieldType;
	private final int min;
	private final int max;
	
	public ExtTrackNumber(ITrackInstance track, String field, Class fieldType, int min, int max) {
		super(track);
		this.field = field;
		this.fieldType = fieldType;
		this.min = min;
		this.max = max;
	}
	
	@Override
	public abstract String getType();

	@Override
	public String[] getMethodNames() {
		return new String[] {"get", "set"};
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, int method, Object[] arguments) throws Exception {
		switch (method) {
			case 0: {
				if (arguments.length < 1) throw new Exception("too few arguments");
				else if (!(arguments[0] instanceof Double)) throw new Exception("bad argument #1 (expected number)");
				
				int num = (int)Math.floor((Double)arguments[0]);
				if (num < min || num > max) throw new Exception("invalid value "+num+" (expected "+min+"-"+max+")");
				Reflector.setField(track, field, Reflector.construct(fieldType.getName(), fieldType, Integer.toString(num)));				
				return callMethod(computer, 1, arguments);
			}
			case 1: {
				return new Object[] {Reflector.getField(track, field, fieldType)};
			}
		}
		
		return null;
	}
}
