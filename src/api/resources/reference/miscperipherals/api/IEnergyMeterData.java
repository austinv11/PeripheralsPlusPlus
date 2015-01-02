package miscperipherals.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

/**
 * Data source interface for the Energy Meter peripheral.
 */
public interface IEnergyMeterData {
	/**
	 * Handler list - add your handlers here.
	 */
	public static final List<IEnergyMeterData> handlers = new ArrayList<IEnergyMeterData>();
	
	/**
	 * Gets whether this data source can handle a request.
	 * 
	 * @param world Target world
	 * @param x Target X
	 * @param y Target Y
	 * @param z Target Z
	 * @param side Target side
	 * @param arguments Arguments given to the function call
	 * @return Whether this data source can handle the request
	 */
	public boolean canHandle(World world, int x, int y, int z, int side, Object[] arguments);
	
	/**
	 * Set up this data source for a block.
	 * 
	 * @param world Target world
	 * @param x Target X
	 * @param y Target Y
	 * @param z Target Z
	 * @param side Target side
	 * @param data Energy meter data
	 * @param arguments Arguments given to the function call
	 */
	public void start(World world, int x, int y, int z, int side, NBTTagCompound data, Object[] arguments);
	
	/**
	 * Clean up this data source.
	 * 
	 * @param world Target world
	 * @param x Target X
	 * @param y Target Y
	 * @param z Target Z
	 * @param side Target side
	 * @param data Energy meter data
	 * @param arguments Arguments given to the function call
	 */
	public void cleanup(World world, int x, int y, int z, int side, NBTTagCompound data, Object[] arguments);
	
	/**
	 * Try to get data.
	 * 
	 * @param world Target world
	 * @param x Target X
	 * @param y Target Y
	 * @param z Target Z
	 * @param side Target side
	 * @param data Energy meter data
	 * @param arguments Arguments given to the function call
	 * @return Information
	 */
	public Map<String, Object> getData(World world, int x, int y, int z, int side, NBTTagCompound data, Object[] arguments);
}
