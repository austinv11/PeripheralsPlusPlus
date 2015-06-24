package com.austinv11.peripheralsplusplus.pocket.peripherals;

import com.austinv11.collectiveframework.minecraft.utils.Location;
import com.austinv11.collectiveframework.minecraft.utils.NBTHelper;
import com.austinv11.peripheralsplusplus.reference.Config;
import com.austinv11.peripheralsplusplus.reference.Reference;
import com.austinv11.peripheralsplusplus.turtles.peripherals.MountedPeripheral;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.shared.pocket.items.ItemPocketComputer;
import net.minecraft.entity.Entity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class PeripheralMotionDetector extends MountedPeripheral {
	
	private Location oldLocation = null;
	private IComputerAccess computer;
	private float pitch, yaw;
	
	public PeripheralMotionDetector(Entity entity) {
		if (entity != null) {
			oldLocation = new Location(entity);
			setPitchAndYaw(entity);
		}
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	private void setPitchAndYaw(Entity entity) {
		pitch = entity.rotationPitch;
		yaw = entity.rotationYaw;
	}
	
	@Override
	public String getType() {
		return "motionDetector";
	}
	
	@Override
	public String[] getMethodNames() {
		return new String[0];
	}
	
	@Override
	public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws LuaException, InterruptedException {
		return new Object[0];
	}
	
	@Override
	public boolean equals(IPeripheral other) {
		return other == this;
	}
	
	public void update(Entity entity) {
		if (!Config.enableMotionDetector)
			return;
		if (computer != null) {
			Location newLocation = new Location(entity);
			if (oldLocation == null)
				oldLocation = new Location(newLocation);
			if (!newLocation.equals(oldLocation)) {
				if (!newLocation.getWorld().equals(oldLocation.getWorld()))
					computer.queueEvent("worldChanged", new Object[]{oldLocation.getWorld().provider.dimensionId,
							newLocation.getWorld().provider.dimensionId});
				computer.queueEvent("locationChanged", new Object[]{newLocation.getX()-oldLocation.getX(),
						newLocation.getY()-oldLocation.getY(), newLocation.getZ()-oldLocation.getZ()});
				oldLocation = newLocation;
			}
			if (entity.rotationPitch != pitch || entity.rotationYaw != yaw) {
				computer.queueEvent("rotationChanged", new Object[]{entity.rotationYaw, entity.rotationPitch});
				setPitchAndYaw(entity);
			}
		}
	}
	
	@Override
	public void attach(IComputerAccess computer) {
		this.computer = computer;
		super.attach(computer);
	}
	
	@Override
	public void detach(IComputerAccess computer) {
		this.computer = null;
		super.detach(computer);
	}
	
	@SubscribeEvent
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (!Config.enableMotionDetector)
			return;
		if (computer == null)
			return;
		if (!event.isCanceled())
			if (event.entityPlayer.getCurrentEquippedItem() != null)
				if (event.entityPlayer.getCurrentEquippedItem().getItem() instanceof ItemPocketComputer)
					if (NBTHelper.hasTag(event.entityPlayer.getCurrentEquippedItem(), "upgrade")) {
						int upgrade = (int)NBTHelper.getShort(event.entityPlayer.getCurrentEquippedItem(), "upgrade");
						if (upgrade == Reference.MOTION_DETECTOR) {
							if (event.action == PlayerInteractEvent.Action.LEFT_CLICK_BLOCK)
								computer.queueEvent("blockHit", new Object[0]);
							else
								computer.queueEvent("rightClick", new Object[0]);
						}
					}
	}
}
