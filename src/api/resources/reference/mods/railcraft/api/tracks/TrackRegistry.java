package mods.railcraft.api.tracks;

import cpw.mods.fml.common.FMLCommonHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import mods.railcraft.api.core.ITextureLoader;

/**
 * The TrackRegistry is part of a system that allows 3rd party addons to simply,
 * quickly, and easily define new Tracks with unique behaviors without requiring
 * that any additional block ids be used.
 *
 * All the tracks in RailcraftProxy are implemented using this system 100%
 * (except for Gated Tracks and Switch Tracks which have some custom render
 * code).
 *
 * To define a new track, you need to define a TrackSpec and create a
 * ITrackInstance.
 *
 * The TrackSpec contains basic constant information about the Track, while the
 * TrackInstace controls how an individual Track block interact with the world.
 *
 * @author CovertJaguar <http://www.railcraft.info>
 * @see TrackSpec
 * @see ITrackInstance
 * @see TrackInstanceBase
 */
public class TrackRegistry {

    private static Map<Short, TrackSpec> trackSpecs = new HashMap<Short, TrackSpec>();
    private static List<ITextureLoader> iconLoaders = new ArrayList<ITextureLoader>();

    /**
     * Provides a means to hook into the texture loader of my Track block.
     *
     * You should load your track textures in the ITextureLoader and put them
     * someplace to be fed to the TrackSpec/TrackInstance later.
     *
     * This should be called before the Post-Init Phase, during the Init or
     * Pre-Init Phase.
     *
     * @param iconLoader
     */
    public static void registerIconLoader(ITextureLoader iconLoader) {
        iconLoaders.add(iconLoader);
    }

    public static List<ITextureLoader> getIconLoaders() {
        return iconLoaders;
    }

    /**
     * Registers a new TrackSpec. This should be called before the Post-Init
     * Phase, during the Init or Pre-Init Phase.
     *
     * @param trackSpec
     */
    public static void registerTrackSpec(TrackSpec trackSpec) {
        if (trackSpecs.put(trackSpec.getTrackId(), trackSpec) != null) {
            throw new RuntimeException("TrackId conflict detected, please adjust your config or contact the author of the " + trackSpec.getTrackTag());
        }
    }

    /**
     * Returns a cached copy of a TrackSpec object.
     *
     * @param trackId
     * @return
     */
    public static TrackSpec getTrackSpec(int trackId) {
        TrackSpec spec = trackSpecs.get((short) trackId);
        if (spec == null) {
            FMLCommonHandler.instance().getFMLLogger().log(Level.WARNING, "[Railcraft] Unknown Track Spec ID(" + trackId + "), reverting to normal track");
            spec = trackSpecs.get(-1);
        }
        return spec;
    }

    /**
     * Returns all Registered TrackSpecs.
     *
     * @return list of TrackSpecs
     */
    public static Map<Short, TrackSpec> getTrackSpecs() {
        return trackSpecs;
    }

}
