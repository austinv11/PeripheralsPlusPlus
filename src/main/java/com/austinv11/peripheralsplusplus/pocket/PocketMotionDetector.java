package com.austinv11.peripheralsplusplus.pocket;

import com.austinv11.peripheralsplusplus.init.ModItems;
import com.austinv11.peripheralsplusplus.pocket.peripherals.PeripheralMotionDetector;
import com.austinv11.peripheralsplusplus.reference.Reference;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.pocket.IPocketAccess;
import dan200.computercraft.api.pocket.IPocketUpgrade;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PocketMotionDetector implements IPocketUpgrade {
	
	@Override
	public ResourceLocation getUpgradeID() {
		return new ResourceLocation(Reference.POCKET_MOTION_DETECTOR);
	}
	
	@Override
	public String getUnlocalisedAdjective() {
		return "peripheralsplusplus.pocket_upgrade.motion_detector";
	}
	
	@Override
	public ItemStack getCraftingItem() {
		return new ItemStack(ModItems.MOTION_DETECTOR);
	}

    @Nullable
    @Override
    public IPeripheral createPeripheral(@Nonnull IPocketAccess access) {
        return new PeripheralMotionDetector(access.getEntity());
    }

	@Override
	public void update(@Nonnull IPocketAccess access, @Nullable IPeripheral peripheral) {
		if (peripheral instanceof PeripheralMotionDetector)
			((PeripheralMotionDetector)peripheral).update(access.getEntity());
	}

	@Override
	public boolean onRightClick(@Nonnull World world, @Nonnull IPocketAccess access, @Nullable IPeripheral peripheral) {
		return false;
	}
}
