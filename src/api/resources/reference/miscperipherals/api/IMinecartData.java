package miscperipherals.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.minecraft.entity.item.EntityMinecart;

/**
 * Minecart extra data handler.
 * 
 * @author Richard
 */
public interface IMinecartData {
	/**
	 * Handler list - add your handlers here.
	 */
	public static final List<IMinecartData> handlers = new ArrayList<IMinecartData>();
	
	/**
	 * Get data for a minecart.
	 * 
	 * @param cart Minecart entity
	 * @return Minecart data (translated to Lua types as ComputerCraft does), or null if the minecart is unknown
	 */
	public Map<Object, Object> getMinecartData(EntityMinecart cart);
}
