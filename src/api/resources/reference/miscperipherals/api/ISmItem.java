package miscperipherals.api;

import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * Smallnet entity item interface, useful for items which link entities to an UUID.
 * 
 * @author Richard
 */
public interface ISmItem {
	/**
	 * Get the UUID associated with this item.
	 * 
	 * @param player Player linking with this item
	 * @param stack Stack being linked
	 * @return UUID
	 */
	public UUID getUUID(EntityPlayer player, ItemStack stack);
}
