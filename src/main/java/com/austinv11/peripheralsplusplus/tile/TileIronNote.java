package com.austinv11.peripheralsplusplus.tile;

import com.austinv11.peripheralsplusplus.PeripheralsPlusPlus;
import com.austinv11.peripheralsplusplus.network.ParticlePacket;
import com.austinv11.peripheralsplusplus.util.CCMethod;
import dan200.computercraft.api.lua.LuaException;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class TileIronNote extends TilePeripheral {
	public static final String name = "tileIronNote";

	private final String[] instruments = {"harp", "bd", "snare", "hat", "basstattack"};

	@CCMethod
	public void playNote(Object[] arguments) throws LuaException {
		if (arguments.length < 2)
			throw new LuaException("Wrong number of arguments. 2 expected.");
		if (!(arguments[0] instanceof Double))
			throw new LuaException("Bad argument #1 (expected number)");
		if (!(arguments[1] instanceof Double))
			throw new LuaException("Bad argument #2 (expected number)");

		String instrument = "note." + instruments[(int) (double) (Double) arguments[0]];
		float note = (float) Math.pow(2D, ((int) (double) (Double) arguments[1] - 12) / 12D);

		getWorld().playSoundEffect(getPos().getX() + 0.5D, getPos().getY() + 0.5D, getPos().getZ() + 0.5D, instrument, 3F, note);
		PeripheralsPlusPlus.NETWORK.sendToAllAround(new ParticlePacket("note", getPos().getX() + 0.5,
						getPos().getY() + 1.2, getPos().getZ() + 0.5, note / 24D, 0, 0),
				new NetworkRegistry.TargetPoint(getWorld().provider.getDimensionId(), getPos().getX(), getPos().getY(),
						getPos().getZ(), 16));
	}

	@CCMethod
	public void playSound(Object[] arguments) throws LuaException {
		if (arguments.length < 3)
			throw new LuaException("Wrong number of arguments. 3 expected.");
		if (!(arguments[0] instanceof String))
			throw new LuaException("Bad argument #1 (expected string)");
		if (!(arguments[1] instanceof Double))
			throw new LuaException("Bad argument #2 (expected number)");
		if (!(arguments[2] instanceof Double))
			throw new LuaException("Bad argument #3 (expected number)");

		String sound = (String) arguments[0];
		float volume = (float) (double) (Double) arguments[1];
		float pitch = (float) (double) (Double) arguments[2];

		getWorld().playSoundEffect(getPos().getX() + 0.5D, getPos().getY() + 0.5D, getPos().getZ() + 0.5D, sound, volume, pitch);
	}

	@Override
	public String getType() {
		return "ironNote";
	}
}
