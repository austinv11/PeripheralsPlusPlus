package com.austinv11.peripheralsplusplus.hooks;

import com.austinv11.collectiveframework.minecraft.utils.NBTHelper;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.shared.computer.core.ServerComputer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import java.util.HashMap;

public class ComputerCraftHooks {
	
	public static HashMap<Integer, IPeripheral> cachedPeripherals = new HashMap<Integer, IPeripheral>();
	
	public static void onPocketComputerCreate(ServerComputer computer, ItemStack stack, IInventory inventory) {
		if (NBTHelper.hasTag(stack, "upgrade")) {
			int upgrade = (int)NBTHelper.getShort(stack, "upgrade");
			if (upgrade != 1) { //1 is reserved for wireless modems
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
}
