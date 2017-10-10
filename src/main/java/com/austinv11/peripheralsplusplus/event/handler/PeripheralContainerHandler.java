package com.austinv11.peripheralsplusplus.event.handler;

import com.austinv11.collectiveframework.minecraft.utils.Colors;
import com.austinv11.collectiveframework.minecraft.utils.NBTHelper;
import com.austinv11.peripheralsplusplus.items.ItemBlockPeripheralContainer;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class PeripheralContainerHandler {

	@SubscribeEvent
	public void onInteract(PlayerInteractEvent event) {
		if (event instanceof PlayerInteractEvent.RightClickEmpty)
			if (!event.getEntityPlayer().getHeldItemMainhand().isEmpty() &&
					event.getEntityPlayer().getHeldItemMainhand().getItem() instanceof ItemBlockPeripheralContainer) {
				InventoryPlayer inv = event.getEntityPlayer().inventory;
				List<String> text = new ArrayList<>();
				ItemStack stack = event.getEntityPlayer().getHeldItemMainhand();
				NBTHelper.removeInfo(stack);
				text.add(Colors.RESET.toString()+Colors.UNDERLINE+"Contained Peripherals:");
				int[] ids = NBTHelper.getIntArray(stack, "ids");
				if (ids.length > 0) {
					if (ids.length > 1) {
						int[] newIds = new int[ids.length-1];
						for (int j = 0; j < ids.length-1; j++)
							newIds[j] = ids[j+1];
						NBTHelper.setIntArray(stack, "ids", newIds);
						for (int id : newIds) {
							Block peripheral = Block.getBlockById(id);
							IPeripheral iPeripheral = (IPeripheral) peripheral.createTileEntity(event.getWorld(),
									peripheral.getDefaultState());
							text.add(Colors.RESET+iPeripheral.getType());
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
