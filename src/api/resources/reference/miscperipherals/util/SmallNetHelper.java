package miscperipherals.util;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.WeakHashMap;

import miscperipherals.api.ISmEntity;
import miscperipherals.api.ISmEntityFactory;
import miscperipherals.api.ISmNode;
import miscperipherals.api.ISmSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import cpw.mods.fml.common.IPlayerTracker;
import cpw.mods.fml.common.registry.GameRegistry;

public class SmallNetHelper implements IPlayerTracker {
	private static final List<UUID> usedUUIDs = new LinkedList<UUID>();
	private static final Multimap<Class<? extends Entity>, ISmEntityFactory> binds = HashMultimap.create();
	private static final Map<UUID, Entity> uuidCache = new WeakHashMap<UUID, Entity>();
	
	public static UUID randomUUID() {
		UUID ret;
		do {
			ret = UUID.randomUUID();
		} while (usedUUIDs.contains(ret));
		usedUUIDs.add(ret);
		return ret;
	}
	
	public static void setUUID(Entity entity, UUID uuid) {
		NBTTagCompound data = entity.getEntityData();
		
		NBTTagCompound persisted;
		if (data.hasKey(EntityPlayer.PERSISTED_NBT_TAG)) persisted = data.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
		else data.setTag(EntityPlayer.PERSISTED_NBT_TAG, persisted = new NBTTagCompound());
		
		NBTTagCompound ourData;
		if (data.hasKey("MiscPeripherals")) ourData = data.getCompoundTag("MiscPeripherals");
		else persisted.setTag("MiscPeripherals", ourData = new NBTTagCompound());
		
		if (ourData.hasKey("smNetLSB") && ourData.hasKey("smNetMSB")) {
			UUID oldUUID = new UUID(data.getLong("smNetMSB"), data.getLong("smNetLSB"));
			usedUUIDs.remove(oldUUID);
			uuidCache.remove(oldUUID);
		}
		
		ourData.setLong("smNetLSB", uuid.getLeastSignificantBits());
		ourData.setLong("smNetMSB", uuid.getMostSignificantBits());
		usedUUIDs.add(uuid);
		uuidCache.put(uuid, entity);
	}
	
	public static UUID getUUID(Entity entity) {
		UUID uuid = getSavedUUID(entity);
		if (uuid == null) {
			uuid = randomUUID();
			setUUID(entity, uuid);
		}
		return uuid;
	}
	
	public static UUID getSavedUUID(Entity entity) {
		NBTTagCompound data = entity.getEntityData();
		if (data.hasKey(EntityPlayer.PERSISTED_NBT_TAG)) {
			data = data.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
			if (data.hasKey("MiscPeripherals")) {
				data = data.getCompoundTag("MiscPeripherals");
				if (data.hasKey("smNetLSB") && data.hasKey("smNetMSB")) {
					UUID uuid = new UUID(data.getLong("smNetMSB"), data.getLong("smNetLSB"));
					uuidCache.put(uuid, entity);
					usedUUIDs.add(uuid);
					return uuid;
				}
			}
		}
		
		return null;
	}
	
	public static void send(ISmSender from, UUID uuid, String type, String payload) {
		if (uuid == null) throw new NullPointerException();
		
		Entity ent = null;
		for (Entry<UUID, Entity> entry : uuidCache.entrySet()) {
			if (uuid.equals(entry.getKey())) {
				ent = entry.getValue();
				break;
			}
		}
		if (ent == null) return;
		
		if (ent instanceof ISmEntity && isInRange(from, (ISmEntity) ent)) {
			((ISmEntity) ent).receive(from, type, payload);
		}
		
		for (Entry<Class<? extends Entity>, ISmEntityFactory> entry : binds.entries()) {
			if (entry.getKey().isAssignableFrom(ent.getClass())) {
				ISmEntity sment = entry.getValue().getEntity(ent);
				if (isInRange(from, sment)) {
					sment.receive(from, type, payload);
				}
			}
		}
	}
	
	public static boolean isInRange(ISmNode a, ISmNode b) {
		return a.getWorld() == b.getWorld() && a.getPosition().distanceTo(b.getPosition()) < Math.max(a.getPower(), b.getPower());
	}

	public static void bindEntity(Class<? extends Entity> clazz, ISmEntityFactory factory) {
		binds.put(clazz, factory);
	}
	
	public static void init() {
		new SmallNetHelper();
	}
	
	// ========================================================================== //
	
	public SmallNetHelper() {
		MinecraftForge.EVENT_BUS.register(this);
		GameRegistry.registerPlayerTracker(this);
	}
	
	@ForgeSubscribe
	public void onEntityJoinWorld(EntityJoinWorldEvent event) {
		getSavedUUID(event.entity);
	}

	@Override
	public void onPlayerLogin(EntityPlayer player) {
		getSavedUUID(player);
	}

	@Override
	public void onPlayerLogout(EntityPlayer player) {
		uuidCache.remove(player);
	}

	@Override
	public void onPlayerChangedDimension(EntityPlayer player) {
		
	}

	@Override
	public void onPlayerRespawn(EntityPlayer player) {
		
	}
}
