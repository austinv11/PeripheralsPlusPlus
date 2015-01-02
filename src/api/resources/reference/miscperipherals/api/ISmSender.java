package miscperipherals.api;

import java.util.UUID;

/**
 * Smallnet sender, may be anything (not just an entity or tile entity)
 * 
 * @author Richard
 */
public interface ISmSender extends ISmNode {
	/**
	 * Get the entity UUIDs linked to this sender.
	 * 
	 * @return Entity UUIDs linked
	 * @see MiscPeripheralsAPI#getEntityUUID(net.minecraft.entity.Entity)
	 */
	public Iterable<UUID> getLinks();
	
	/**
	 * Add a link to this sender.
	 * 
	 * @param uuid UUID to add
	 * @throws UnsupportedOperationException If the operation is not supported
	 */
	public void addLink(UUID uuid) throws UnsupportedOperationException;
	
	/**
	 * Remove a link from this sender.
	 * 
	 * @param uuid UUID to remove
	 * @throws UnsupportedOperationException If the operation is not supported
	 */
	public void removeLink(UUID uuid) throws UnsupportedOperationException;
}
