package com.austinv11.peripheralsplusplus.turtles;

import com.austinv11.peripheralsplusplus.utils.TurtleUtil;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import dan200.computercraft.api.turtle.ITurtleAccess;
import net.minecraft.entity.Entity;
import net.minecraftforge.event.entity.living.LivingDropsEvent;

import java.util.HashMap;

public abstract class TurtleDropCollector {

	public static HashMap<Entity, ITurtleAccess> map = new HashMap<Entity, ITurtleAccess>();

	public void addEntity(ITurtleAccess turtle, Entity ent) {
		map.put(ent, turtle);
	}

	public Object newInstanceOfListener() {
		return new Listener();
	}

	public static class Listener {

		@SubscribeEvent
		public void onDrops(LivingDropsEvent event) {
			if (map.containsKey(event.entity)) {
				TurtleUtil.addItemListToInv(TurtleUtil.entityItemsToItemStack(event.drops), map.get(event.entity));
				event.setCanceled(true);
				map.remove(event.entity);
			}
		}
	}
}
