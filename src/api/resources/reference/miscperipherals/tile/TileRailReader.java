package miscperipherals.tile;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.WeakHashMap;
import java.util.Map.Entry;
import java.util.logging.Level;

import miscperipherals.api.IDataCart;
import miscperipherals.api.IMinecartData;
import miscperipherals.core.LuaManager;
import miscperipherals.core.MiscPeripherals;
import miscperipherals.util.Util;
import net.minecraft.entity.ai.EntityMinecartMobSpawner;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityMinecartChest;
import net.minecraft.entity.item.EntityMinecartEmpty;
import net.minecraft.entity.item.EntityMinecartFurnace;
import net.minecraft.entity.item.EntityMinecartHopper;
import net.minecraft.entity.item.EntityMinecartTNT;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import cpw.mods.fml.common.FMLCommonHandler;
import dan200.computer.api.IComputerAccess;
import dan200.computer.api.IPeripheral;

public class TileRailReader extends Tile implements IPeripheral {
	private static final int TICK_RATE = 5;
	private final Map<IComputerAccess, Boolean> computers = new WeakHashMap<IComputerAccess, Boolean>();
	private final Map<EntityMinecart, Boolean> carts = Collections.synchronizedMap(new WeakHashMap<EntityMinecart, Boolean>());
	private int ticker = new Random().nextInt(TICK_RATE);
	
	@Override
	public boolean canUpdate() {
		return !FMLCommonHandler.instance().getEffectiveSide().isClient();
	}
	
	@Override
	public void updateEntity() {
		if (++ticker > TICK_RATE) {
			ticker = 0;
			updateMinecarts();
			
			for (EntityMinecart cart : carts.keySet()) {
				if (!this.carts.containsKey(cart)) {
					this.carts.put(cart, true);
					
					String cartType = "unknown";
					Map<Object, Object> cartData = new HashMap<Object, Object>();
					
					for (IMinecartData handler : IMinecartData.handlers) {
						try {
							Map<Object, Object> retData = handler.getMinecartData(cart);
							if (retData != null) {
								for (Entry<Object, Object> entry : retData.entrySet()) {
									cartData.put(entry.getKey(), entry.getValue());
								}
								
								if (retData.containsKey("__CART_TYPE__")) break;
							}
						} catch (Throwable e) {
							MiscPeripherals.log.log(Level.WARNING, "Error processing minecart data handler "+handler, e);
						}
					}
					
					if (cartData != null) {
						if (cartData.containsKey("__CART_TYPE__")) {
							cartType = (String)cartData.get("__CART_TYPE__");
							cartData.remove("__CART_TYPE__");
						}
						
						if (cart instanceof IDataCart) {
							Object[] data = ((IDataCart) cart).getData();
							cartData.put("data", data.length == 1 ? data : Util.arrayToMap(data));
						}
						if (cart instanceof IInventory) {
							cartData.put("inventory", TileInteractiveSorter.makeInventoryMap((IInventory) cart));
						}
					}
					
					for (IComputerAccess computer : computers.keySet()) {
						computer.queueEvent("minecart", new Object[] {cartType, cart.entityId, cartData});
					}
				}
			}
		}
	}

	@Override
	public String getType() {
		return "railReader";
	}

	@Override
	public String[] getMethodNames() {
		return new String[] {"setData"};
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, int method, Object[] arguments) throws Exception {
		switch (method) {
			case 0: {
				
			}
		}
		
		return new Object[0];
	}

	@Override
	public boolean canAttachToSide(int side) {
		return true;
	}

	@Override
	public void attach(IComputerAccess computer) {
		LuaManager.mount(computer);
		
		computers.put(computer, true);
	}

	@Override
	public void detach(IComputerAccess computer) {
		computers.remove(computer);
	}
	
	public void updateMinecarts() {
		double rangeSub = 0.125D;
		List<EntityMinecart> carts = (List<EntityMinecart>)worldObj.getEntitiesWithinAABB(EntityMinecart.class, AxisAlignedBB.getAABBPool().getAABB(xCoord + rangeSub, yCoord + 1, zCoord + rangeSub, xCoord + 1 - rangeSub, yCoord + 2 - rangeSub, zCoord + 1 - rangeSub));
		
		List<EntityMinecart> toRemove = new LinkedList<EntityMinecart>();
		for (EntityMinecart cart : this.carts.keySet()) {
			if (!carts.contains(cart)) toRemove.add(cart);
		}
		for (EntityMinecart cart : toRemove) this.carts.remove(cart);
	}
	
	public static class VanillaMinecartData implements IMinecartData {
		@Override
		public Map<Object, Object> getMinecartData(EntityMinecart cart) {
			Map<Object, Object> ret = new HashMap<Object, Object>();
			
			if (cart.getClass() == EntityMinecartEmpty.class) {
				ret.put("__CART_TYPE__", "basic");
				ret.put("occupied", cart.riddenByEntity != null);
				if (cart.riddenByEntity != null) ret.put("username", cart.riddenByEntity.getEntityName());
			} else if (cart.getClass() == EntityMinecartChest.class) {
				ret.put("__CART_TYPE__", "storage");
			} else if (cart.getClass() == EntityMinecartFurnace.class) {
				ret.put("__CART_TYPE__", "furnace");
				NBTTagCompound workaround = new NBTTagCompound();
				cart.writeToNBT(workaround);
				ret.put("fuel", workaround.getInteger("Fuel"));
			} else if (cart.getClass() == EntityMinecartHopper.class) {
				ret.put("__CART_TYPE__", "hopper");
			} else if (cart.getClass() == EntityMinecartTNT.class) {
				ret.put("__CART_TYPE__", "tnt");
			} else if (cart.getClass() == EntityMinecartMobSpawner.class) {
				ret.put("__CART_TYPE__", "spawner");
			}
			
			return ret.containsKey("__CART_TYPE__") ? ret : null;
		}
	}
	
	static {
		IMinecartData.handlers.add(new VanillaMinecartData());
	}
}
