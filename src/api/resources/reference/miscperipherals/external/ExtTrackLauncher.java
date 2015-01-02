package miscperipherals.external;

import miscperipherals.safe.Reflector;
import mods.railcraft.api.tracks.ITrackInstance;

public class ExtTrackLauncher extends ExtTrackNumber {
	public ExtTrackLauncher(ITrackInstance track) {
		super(track, "launchForce", Byte.class, Reflector.getField("mods.railcraft.common.blocks.tracks.TrackLauncher", "MIN_LAUNCH_FORCE", Integer.class), Reflector.invoke("mods.railcraft.common.core.RailcraftConfig", "getLaunchRailMaxForce", Integer.class));
	}

	@Override
	public String getType() {
		return "trackLauncher";
	}
}
