package mods.railcraft.api.core;

import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

/**
 * If you want your block to connect (or not connect) to posts,
 * implement this interface.
 *
 * The result takes priority over any other rules.
 *
 * @author CovertJaguar <http://www.railcraft.info>
 */
public interface IPostConnection
{

    /**
     * Return true if the block at this location should connect to a post.
     * @param world The World
     * @param i x-Coord
     * @param j y-Coord
     * @param k z-Coord
     * @param side Side to connect to
     * @return true if connect
     */
    public boolean connectsAt(World world, int i, int j, int k, ForgeDirection side);
}
