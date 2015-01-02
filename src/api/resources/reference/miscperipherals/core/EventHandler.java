package miscperipherals.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.WeakHashMap;

import miscperipherals.upgrade.UpgradeTank;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.DamageSource;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.EventPriority;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.liquids.LiquidDictionary;
import net.minecraftforge.liquids.LiquidDictionary.LiquidRegisterEvent;
import net.minecraftforge.liquids.LiquidStack;

public class EventHandler {
	public static Map<EntityLiving, DropConsumer> dropConsumers = Collections.synchronizedMap(new WeakHashMap<EntityLiving, DropConsumer>());
	public static List<ItemConsumer> itemConsumers = Collections.synchronizedList(new ArrayList<ItemConsumer>());
	
	public EventHandler() {
		MinecraftForge.EVENT_BUS.register(this);
		
		for (Entry<String, LiquidStack> entry : LiquidDictionary.getLiquids().entrySet()) {
			onLiquidRegister(new LiquidRegisterEvent(entry.getKey(), entry.getValue()));
		}
	}
	
	@ForgeSubscribe
	public void onLiquidRegister(LiquidRegisterEvent event) {
		UpgradeTank.LIQUIDS.put(new ChunkCoordIntPair(event.Liquid.itemID, event.Liquid.itemMeta), event.Name.toLowerCase());
	}
	
	@ForgeSubscribe
	public void onLivingDrops(LivingDropsEvent event) {
		DropConsumer consumer = dropConsumers.remove(event.entityLiving);
		if (consumer != null) {
			consumer.onDrops(event.source, event.drops, event.lootingLevel, event.recentlyHit, event.specialDropValue);
		}
	}
	
	@ForgeSubscribe(priority = EventPriority.LOWEST)
	public void onEntityJoinWorld(EntityJoinWorldEvent event) {
		if (event.entity instanceof EntityItem) {
			for (ItemConsumer consumer : itemConsumers) consumer.onItem((EntityItem) event.entity);
		}
	}
	
	public static interface DropConsumer {
		public void onDrops(DamageSource source, ArrayList<EntityItem> drops, int lootingLevel, boolean recentlyHit, int specialDropValue);
	}
	
	public static interface ItemConsumer {
		public void onItem(EntityItem item);
	}
}
