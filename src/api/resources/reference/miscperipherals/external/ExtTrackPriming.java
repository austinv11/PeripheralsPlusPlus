package miscperipherals.external;

import miscperipherals.safe.Reflector;
import mods.railcraft.api.tracks.ITrackInstance;

public class ExtTrackPriming extends ExtTrackNumber {
	public ExtTrackPriming(ITrackInstance track) {
		super(track, "fuse", Short.class, Reflector.getField("mods.railcraft.common.blocks.tracks.TrackPriming", "MIN_FUSE", Integer.class), Reflector.getField("mods.railcraft.blocks.tracks.TrackPriming", "MAX_FUSE", Integer.class));
	}

	@Override
	public String getType() {
		return "trackPriming";
	}
}
