package com.austinv11.peripheralsplusplus.event;

import com.austinv11.peripheralsplusplus.items.ItemBlockPeripheralContainer;
import com.austinv11.peripheralsplusplus.reference.Reference;
import com.austinv11.peripheralsplusplus.utils.NBTHelper;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.List;

public class PeripheralContainerHandler {

	@SubscribeEvent
	public void onInteract(PlayerInteractEvent event) {
		if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_AIR)
			if (event.entityPlayer.getCurrentEquippedItem().getItem() instanceof ItemBlockPeripheralContainer) {
				InventoryPlayer inv = event.entityPlayer.inventory;
				List<String> text = new ArrayList<String>();
				ItemStack stack = event.entityPlayer.getCurrentEquippedItem();
				NBTHelper.removeInfo(stack);
				text.add(Reference.Colors.RESET+Reference.Colors.UNDERLINE+"Contained Peripherals:");
				int[] ids = NBTHelper.getIntArray(stack, "ids");
				if (ids.length > 0) {
					if (ids.length > 1) {
						int[] newIds = new int[ids.length-1];
						for (int j = 0; j < ids.length-1; j++)
							newIds[j] = ids[j+1];
						NBTHelper.setIntArray(stack, "ids", newIds);
						for (int id : newIds) {
							Block peripheral = Block.getBlockById(id);
							IPeripheral iPeripheral = (IPeripheral) peripheral.createTileEntity(null, 0);
							text.add(Reference.Colors.RESET+iPeripheral.getType());
						}
						if (text.size() > 1)
							NBTHelper.setInfo(stack, text);
					} else {
						NBTHelper.removeTag(stack, "ids");
					}
					inv.addItemStackToInventory(new ItemStack(Block.getBlockById(ids[0])));
				}
			}
	}
}
