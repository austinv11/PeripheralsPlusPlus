package com.austinv11.peripheralsplusplus.hooks;

import com.austinv11.collectiveframework.minecraft.utils.NBTHelper;
import com.austinv11.peripheralsplusplus.PeripheralsPlusPlus;
import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.shared.computer.core.ServerComputer;
import dan200.computercraft.shared.pocket.items.ItemPocketComputer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import java.util.HashMap;

public class ComputerCraftHooks {
	
	public static HashMap<Integer, IPeripheral> cachedPeripherals = new HashMap<Integer, IPeripheral>();
	
	public static void onPocketComputerCreate(ServerComputer computer, ItemStack stack, IInventory inventory) {
		if (NBTHelper.hasTag(stack, "upgrade")) {
			int upgrade = (int)NBTHelper.getShort(stack, "upgrade");
			if (upgrade != 1) { //1 is reserved for wireless modems
				if (!ComputerCraftRegistry.pocketUpgrades.containsKey(upgrade)) {
					PeripheralsPlusPlus.LOGGER.warn("A pocket computer upgrade with an ID of "+upgrade+" cannot be found! Removing it...");
					NBTHelper.removeTag(stack, "upgrade");
					return;
				}
				IPeripheral peripheral = cachedPeripherals.containsKey(computer.getID()) ?
						cachedPeripherals.get(computer.getID()) : 
						ComputerCraftRegistry.pocketUpgrades.get(upgrade).createPeripheral(inventory == null ?
						null : ((InventoryPlayer)inventory).player, stack);
				cachedPeripherals.put(computer.getID(), peripheral);
				computer.setPeripheral(2, peripheral);
			}
		}
	}
	
	public static String getName(String baseName, ItemStack stack) {
		if (NBTHelper.hasTag(stack, "upgrade")) {
			baseName = StatCollector.translateToLocal(baseName+".upgraded.name");
			int upgrade = (int) NBTHelper.getShort(stack, "upgrade");
			if (upgrade == 1) {
				baseName = baseName.replace("%s", StatCollector.translateToLocal("upgrade.computercraft:wireless_modem.adjective"));
			} else {
				baseName = baseName.replace("%s", StatCollector.translateToLocal(ComputerCraftRegistry.pocketUpgrades.get(upgrade).getUnlocalisedAdjective()));
			}
		} else {
			baseName = StatCollector.translateToLocal(baseName+".name");
		}
		return baseName;
	}
	
	public static void update(Entity entity, ItemStack stack, ServerComputer computer) {
		if (NBTHelper.hasTag(stack, "upgrade")) {
			int upgrade = (int)NBTHelper.getShort(stack, "upgrade");
			if (upgrade != 1) { //1 is reserved for wireless modems
				ComputerCraftRegistry.pocketUpgrades.get(upgrade).update(entity, stack, cachedPeripherals.get(upgrade));
			}
		}
	}
	
	@SubscribeEvent
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (!event.isCanceled())
			if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_AIR || event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK)
				if (event.useItem != Event.Result.DENY)
					if (event.entityPlayer.getCurrentEquippedItem() != null)
						if (event.entityPlayer.getCurrentEquippedItem().getItem() instanceof ItemPocketComputer)
							event.setCanceled(rightClick(event.world, event.entityPlayer, event.entityPlayer.getCurrentEquippedItem()));
	}
	
	public static boolean rightClick(World world, EntityPlayer player, ItemStack stack) {
		if (NBTHelper.hasTag(stack, "upgrade")) {
			int upgrade = (int)NBTHelper.getShort(stack, "upgrade");
			if (upgrade != 1) { //1 is reserved for wireless modems
				return ComputerCraftRegistry.pocketUpgrades.get(upgrade).onRightClick(world, player, stack, cachedPeripherals.get(upgrade));
			}
		}
		return false;
	}
}
