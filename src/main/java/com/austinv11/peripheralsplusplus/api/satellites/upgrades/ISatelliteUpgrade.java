package com.austinv11.peripheralsplusplus.api.satellites.upgrades;

import com.austinv11.peripheralsplusplus.api.satellites.ISatellite;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;

public interface ISatelliteUpgrade {

	/**
	 * Returns the icon used in the physical Satellite Upgrade item
	 * @return The ResourceLocation used
	 */
	public ResourceLocation getIcon();

	/**
	 * Returns the recipe to get the Satellite Upgrade item
	 * @return The recipe
	 */
	public IRecipe getRecipe();

	/**
	 * Called to update the satellite (only when it's a MAIN upgrade)
	 * @param satellite The satellite accessing the upgrade
	 */
	public void update(ISatellite satellite);

	/**
	 * Used to determine the type of upgrade
	 * @return The SatelliteUpgradeType that fits for this upgrade
	 */
	public SatelliteUpgradeType getType();

	/**
	 * Used internally for naming the physical item, basically the same as IPeripheral.getType()
	 * @return Name of the upgrade (MUST be unlocalised)
	 */
	public String getName();

	/**
	 * Called to get the String identifier of the upgrade
	 * @return The identifier
	 */
	public String getUpgradeID();

	/**
	 * Only called when an ADDON upgrade, get's the size of the addon
	 * @return Float representing the percentage of cargo used (from 0.0F to 1.0F)
	 */
	public float getAddonWeight();

	/**
	 * Called to get the name of the physical Satellite Upgrade item name
	 * @return The unlocalised name
	 */
	public String getUnlocalisedName();

	/**
	 * Only called when a MAIN upgrade, same as IPeripheral:
	 * "Should return an array of strings that identify the methods that this
	 * peripheral exposes to Lua. This will be called once before each attachment,
	 * and should not change when called multiple times.
	 * @return 	An array of strings representing method names.
	 * @see 	#callMethod"
	 */
	public String[] getMethodNames();

	/**
	 * Only called when a MAIN upgrade, same as IPeripheral:
	 * "This is called when a lua program on an attached computercraft calls peripheral.call() with
	 * one of the methods exposed by getMethodNames().<br>
	 * <br>
	 * Be aware that this will be called from the ComputerCraft Lua thread, and must be thread-safe
	 * when interacting with minecraft objects.
	 * @param 	computer	The interface to the computercraft that is making the call. Remember that multiple
	 *						computers can be attached to a peripheral at once.
	 * @param	context		The context of the currently running lua thread. This can be used to wait for events
	 *						or otherwise yield.
	 * @param	method		An integer identifying which of the methods from getMethodNames() the computercraft
	 *						wishes to call. The integer indicates the index into the getMethodNames() table
	 *						that corresponds to the string passed into peripheral.call()
	 * @param	arguments	An array of objects, representing the arguments passed into peripheral.call().<br>
	 *						Lua values of type "string" will be represented by Object type String.<br>
	 *						Lua values of type "number" will be represented by Object type Double.<br>
	 *						Lua values of type "boolean" will be represented by Object type Boolean.<br>
	 *						Lua values of any other type will be represented by a null object.<br>
	 *						This array will be empty if no arguments are passed.
	 * @return 	An array of objects, representing values you wish to return to the lua program.<br>
	 *			Integers, Doubles, Floats, Strings, Booleans and null be converted to their corresponding lua type.<br>
	 *			All other types will be converted to nil.<br>
	 *			You may return null to indicate no values should be returned.
	 * @throws	Exception	If you throw any exception from this function, a lua error will be raised with the
	 *						same message as your exception. Use this to throw appropriate errors if the wrong
	 *						arguments are supplied to your method.
	 * @see 	#getMethodNames"
	 */
	public Object[] callMethod( IComputerAccess computer, ILuaContext context, int method, Object[] arguments ) throws LuaException, InterruptedException;

	/**
	 * Only called when a MAIN upgrade, called when a computer connects to the satellite
	 */
	public void onConnect(IComputerAccess computer);

	/**
	 * Only called when a MAIN upgrade, called when a computer diconnects from the satellite
	 */
	public void onDisconnect(IComputerAccess computer);
}
