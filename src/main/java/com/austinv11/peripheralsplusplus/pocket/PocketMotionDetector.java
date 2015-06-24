package com.austinv11.peripheralsplusplus.pocket;

import com.austinv11.peripheralsplusplus.hooks.IPocketComputerUpgrade;
import com.austinv11.peripheralsplusplus.init.ModItems;
import com.austinv11.peripheralsplusplus.pocket.peripherals.PeripheralMotionDetector;
import com.austinv11.peripheralsplusplus.reference.Reference;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class PocketMotionDetector implements IPocketComputerUpgrade {
	
	@Override
	public int getUpgradeID() {
		return Reference.MOTION_DETECTOR;
	}
	
	@Override
	public String getUnlocalisedAdjective() {
		return "peripheralsplusplus.pocketUpgrade.motionDetector";
	}
	
	@Override
	public ItemStack getCraftingItem() {
		return new ItemStack(ModItems.motionDetector);
	}
	
	@Override
	public IPeripheral createPeripheral(Entity entity, ItemStack stack) {
		return new PeripheralMotionDetector(entity);
	}
	
	@Override
	public void update(Entity entity, ItemStack stack, IPeripheral peripheral) {
		((PeripheralMotionDetector)peripheral).update(entity);
	}
	
	@Override
	public boolean onRightClick(World world, EntityPlayer player, ItemStack stack, IPeripheral peripheral) {
		return false;
	}
}
