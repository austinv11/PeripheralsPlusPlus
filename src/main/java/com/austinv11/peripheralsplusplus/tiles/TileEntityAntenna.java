package com.austinv11.peripheralsplusplus.tiles;

import com.austinv11.collectiveframework.minecraft.utils.NBTHelper;
import com.austinv11.peripheralsplusplus.PeripheralsPlusPlus;
import com.austinv11.peripheralsplusplus.items.ItemSmartHelmet;
import com.austinv11.peripheralsplusplus.lua.LuaObjectEntityControl;
import com.austinv11.peripheralsplusplus.lua.LuaObjectHUD;
import com.austinv11.peripheralsplusplus.network.ScaleRequestPacket;
import com.austinv11.peripheralsplusplus.reference.Config;
import com.austinv11.peripheralsplusplus.utils.Util;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class TileEntityAntenna extends MountedTileEntity implements ITickable {

	public static String publicName = "antenna";
	private  String name = "tileEntityAntenna";
	private int dimension = 0;
	public HashMap<IComputerAccess, Boolean> computers = new HashMap<IComputerAccess,Boolean>();
	private HashMap<Integer, LuaObjectHUD> huds = new HashMap<Integer,LuaObjectHUD>();
	public static final HashMap<UUID, TileEntityAntenna> ANTENNA_REGISTRY = new HashMap<>();
	public UUID identifier;
	public String label;
	private volatile List<Entity> associatedEntities = new ArrayList<Entity>();

	public TileEntityAntenna() {
		super();
		identifier = UUID.randomUUID();
		while (ANTENNA_REGISTRY.containsKey(identifier)) {
			if (ANTENNA_REGISTRY.get(identifier).equals(this))
				break;
			identifier = UUID.randomUUID();
		}
	}

	public String getName() {
		return name;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		if (nbttagcompound.hasKey("identifier"))
			identifier = UUID.fromString(nbttagcompound.getString("identifier"));
		if (nbttagcompound.hasKey("label"))
			label = nbttagcompound.getString("label");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
		nbttagcompound.setString("identifier", identifier.toString());
		if (label != null)
			nbttagcompound.setString("label", label);
		return nbttagcompound;
	}

	@Override
	public String getType() {
		return publicName;
	}

	@Override
	public String[] getMethodNames() {
		return new String[]{"getPlayers",/*Lists players wearing smart helmets linked to this antenna*/
				"getHUD",/*Returns a hud handle for the given player*/
				"setLabel",/*Sets the label of the antenna*/
				"getLabel",/*Gets the current label of the antenna*/
				//==Smart Helmet APIs end===
				"getInfectedEntities", /*Lists the entity ids for all entities currently infected by a nanobot swarm*/
				"getInfectedEntity"/*Gets the handle for a specified entity*/};
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws LuaException, InterruptedException {
		if (!Config.enableSmartHelmet && method < 6)
			throw new LuaException("Smart Helmets have been disabled");
		else if (!Config.enableNanoBots)
			throw new LuaException("Nano bots have been disabled");
//		try {
		switch (method) {
			case 0://getPlayers
				synchronized (this) {
					List<String> playerNames = new ArrayList<>();
					List<EntityPlayer> players = getPlayersWearingSmartHelmets();
					for (EntityPlayer player : players) {
						Iterable<ItemStack> armor = player.getArmorInventoryList();
						for (ItemStack itemStack : armor) {
							if (itemStack.getItem() instanceof ItemSmartHelmet &&
									NBTHelper.hasTag(itemStack, "identifier") &&
									identifier.equals(UUID.fromString(NBTHelper.getString(itemStack,
											"identifier"))))
								playerNames.add(player.getDisplayNameString());
						}
					}
					return new Object[]{Util.arrayToMap(playerNames.toArray())};
				}
			case 1:
				synchronized (this) {
					if (arguments.length < 1)
						throw new LuaException("Not enough arguments");
					if (!(arguments[0] instanceof String))
						throw new LuaException("Bad argument #1 (expected string)");
					if (Util.getPlayer((String) arguments[0]) == null)
						return new Object[]{null};
					LuaObjectHUD obj = new LuaObjectHUD((String) arguments[0], identifier);
					huds.put(computer.getID(), obj);
					PeripheralsPlusPlus.NETWORK.sendTo(new ScaleRequestPacket(this, computer.getID(), dimension),
							(EntityPlayerMP) Util.getPlayer((String)arguments[0]));
					context.pullEvent("resolution");
					return new Object[]{obj};
				}
			case 2:
				synchronized (this){
					if (arguments.length != 1)
						throw new LuaException("Incorrect Arguments!");
					this.setLabel(arguments[0].toString());
				}
			case 3:
				synchronized (this){
					return new Object[]{this.getLabel()};
				}
			case 4:
				HashMap<Integer, Integer> entities = new HashMap<Integer, Integer>();
				for (int i = 0; i < associatedEntities.size(); i++) {
					entities.put(i + 1, associatedEntities.get(i).getEntityId());
				}
				return new Object[]{entities};
			case 5:
				if (arguments.length < 1)
					throw new LuaException("Too few arguments");
				if (!(arguments[0] instanceof Double))
					throw new LuaException("Bad argument #1 (expected number)");
				Entity ent = entityFromId((int)(double)(Double)arguments[0]);
				if (ent != null)
					return new Object[]{new LuaObjectEntityControl(identifier, ent)};
				else
					throw new LuaException("Entity with id "+arguments[0]+" not found");
		}
//		}catch (Exception e) {
//			e.printStackTrace();
//		}
		return new Object[0];
	}

	public static List<EntityPlayer> getPlayersWearingSmartHelmets() {
		List<EntityPlayer> players = new ArrayList<>();
		for (WorldServer worldServer : DimensionManager.getWorlds()) {
			if (worldServer.getMinecraftServer() != null) {
				for (EntityPlayer player : worldServer.playerEntities) {
					Iterable<ItemStack> armor = player.getArmorInventoryList();
					for (ItemStack itemStack : armor) {
						if (itemStack.getItem() instanceof ItemSmartHelmet)
							players.add(player);
					}
				}
			}
		}
		return players;
	}
	
	private Entity entityFromId(int id) {
		for (Entity entity : associatedEntities)
			if (entity.getEntityId() == id)
				return entity;
		return null;
	}

	@Override
	public void update() {
		if (world != null) {
			dimension = world.provider.getDimension();
			if (!ANTENNA_REGISTRY.containsKey(identifier))
				ANTENNA_REGISTRY.put(identifier, this);
		}
	}
	
	@Override
	public void invalidate() {
		super.invalidate();
		ANTENNA_REGISTRY.remove(identifier);
	}
	
	@Override
	public void validate() {
		super.validate();
		ANTENNA_REGISTRY.put(identifier, this);
	}

	@Override
	public void attach(IComputerAccess computer) {
		computers.put(computer, true);
		super.attach(computer);
	}

	@Override
	public void detach(IComputerAccess computer) {
		computers.remove(computer);
		super.detach(computer);
	}

	@Override
	public boolean equals(IPeripheral other) {
		return (this == other);
	}

	public void onResponse(int id, int width, int height) {
		if (huds.containsKey(id)) {
			huds.get(id).height = height;
			huds.get(id).width = width;
			for (IComputerAccess comp : computers.keySet())
				if (comp.getID() == id)
					comp.queueEvent("resolution", new Object[]{height, width});
		}
	}

	public void setLabel(String newLabel)
	{
		this.label = newLabel;
	}

	public String getLabel()
	{
		return this.label;
	}

	/**
	 * Register an entity with this antenna only if it is not already registered
	 * @param entity entity to register
	 */
	public void registerEntity(Entity entity) {
		if (!associatedEntities.contains(entity))
			associatedEntities.add(entity);
	}

	/**
	 * Check if an entity is registered with this antenna
	 * @param entity entity to check for
	 * @return is registered
	 */
	public boolean isEntityRegistered(Entity entity) {
		return associatedEntities.contains(entity);
	}

	/**
	 * Remove a registered entity
	 * @param entity entity to remove
	 */
	public void removeEntity(Entity entity) {
		associatedEntities.remove(entity);
	}
}
