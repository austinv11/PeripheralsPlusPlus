package miscperipherals.api;

import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3;
import net.minecraftforge.liquids.LiquidStack;
import dan200.turtle.api.ITurtleUpgrade;

/**
 * General API methods.
 * 
 * @author Richard
 */
public abstract class MiscPeripheralsAPI {
	/**
	 * API instance. Will be null if MiscPeripherals is not loaded yet!
	 */
	public static MiscPeripheralsAPI instance;
	
	/**
	 * Returns whether the sideSensitive config option is on.
	 * 
	 * @return Whether sideSensitive is on
	 */
	public abstract boolean isSideSensitive();
	
	/**
	 * Returns whether the descriptive config option is on.
	 * 
	 * @return Whether descriptive is on
	 */
	public abstract boolean isDescriptive();
	
	/**
	 * Returns the fuel-EU conversion rate.
	 * 
	 * @return Fuel-EU conversion rate
	 */
	public abstract int getFuelEU();
	
	/**
	 * Returns the fuel-MJ conversion rate.
	 * 
	 * @return Fuel-MJ conversion rate
	 */
	public abstract int getFuelMJ();
	
	/**
	 * Add a furnace class to the XP turtle upgrade for XP gathering.
	 * 
	 * @param clazz Furnace tile class
	 * @param slots Output slots
	 */
	public abstract void addFurnaceXP(Class<? extends TileEntity> clazz, int... slots);
	
	/**
	 * Add a liquid stack to the XP turtle upgrade for XP gathering.
	 * 
	 * I recommend using the minimum liquid possible for obtaining 1 XP point.
	 * 
	 * @param liquid Liquid
	 * @param xp XP points given by that liquid stack
	 */
	public abstract void addLiquidXP(LiquidStack liquid, int xp);
	
	/**
	 * Encode an item stack into an item UUID.
	 * 
	 * @param stack Stack to encode
	 * @return UUID
	 */
	public abstract int getUUID(ItemStack stack);
	
	/**
	 * Encode a liquid stack into an item UUID.
	 * 
	 * @param stack Stack to encode
	 * @return UUID
	 */
	public abstract int getUUID(LiquidStack stack);
	
	/**
	 * Register an upgrade implementing {@link IUpgradeIcons} or {@link IUpgradeToolIcons} to receive icons.
	 * 
	 * @param icons Upgrade to be registered
	 */
	public abstract void registerUpgradeIcons(ITurtleUpgrade icons);
	
	/**
	 * Get an entity's position as a Vec3.
	 * 
	 * @param ent Entity
	 * @return Position
	 */
	public abstract Vec3 getEntityPosition(Entity ent);
	
	/**
	 * Get the Smallnet UUID for an entity.
	 * 
	 * @param ent Entity
	 * @return uuid UUID
	 */
	public abstract UUID getEntityUUID(Entity ent);
	
	/**
	 * Set the Smallnet UUID for an entity.
	 * 
	 * @param ent Entity
	 * @param uuid UUID
	 */
	public abstract void setEntityUUID(Entity ent, UUID uuid);
	
	/**
	 * Get a random Smallnet UUID. The resulting UUID <b>is</b> added to the list of used UUIDs.
	 * 
	 * @return Random UUID
	 */
	public abstract UUID getRandomUUID();
	
	/**
	 * Bind a {@link ISmEntityFactory} to a specified entity class (and all subclasses)
	 * @param <T>
	 * 
	 * @param clazz Entity class
	 * @param factory Factory instance
	 */
	public abstract <T> void bindSmEntity(Class<T> clazz, ISmEntityFactory<T> factory);
	
	/**
	 * Send a message to a Smallnet entity.
	 * 
	 * @param from Sender
	 * @param uuid UUID to send to
	 * @param type Message type
	 * @param payload Message payload
	 */
	public abstract void sendSm(ISmSender from, UUID uuid, String type, String payload);
}
