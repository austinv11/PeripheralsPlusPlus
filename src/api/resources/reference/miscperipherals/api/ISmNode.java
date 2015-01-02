package miscperipherals.api;

import net.minecraft.util.Vec3;
import net.minecraft.world.World;

/**
 * Base Smallnet node.
 * 
 * @author Richard
 */
public interface ISmNode {
	/**
	 * Get the node's position.
	 * 
	 * @return Position
	 * @see MiscPeripheralsAPI#getEntityPosition(net.minecraft.entity.Entity)
	 */
	public Vec3 getPosition();
	
	/**
	 * Get the node's world.
	 * 
	 * @return World
	 */
	public World getWorld();
	
	/**
	 * Get the wireless power in blocks. The final range is max(receivePower, sendPower)
	 * 
	 * @return Power
	 */
	public double getPower();
}
