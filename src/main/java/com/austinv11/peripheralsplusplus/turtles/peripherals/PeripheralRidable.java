package com.austinv11.peripheralsplusplus.turtles.peripherals;

import com.austinv11.peripheralsplusplus.entities.EntityRidableTurtle;
import com.austinv11.peripheralsplusplus.network.RidableTurtlePacket;
import com.austinv11.peripheralsplusplus.reference.Config;
import com.austinv11.peripheralsplusplus.turtles.TurtleRidable;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.ITurtleAccess;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PeripheralRidable extends MountedPeripheral {

	private final ITurtleAccess turtle;

	public PeripheralRidable(ITurtleAccess turtle) {
		this.turtle = turtle;
	}

	@Override
	public String getType() {
		return "ridable";
	}

	@Override
	public String[] getMethodNames() {
		return new String[]{"getEntity", "mountNearbyEntity", "unmount", "up"};
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws LuaException, InterruptedException {
		if (!Config.enableRidableTurtle)
			throw new LuaException("Ridable turtles have been disabled.");
		switch (method) {
			case 0:
				return getRidingEntity();
			case 1:
				return mountNearbyEntity();
			case 2:
				return unmount();
			case 3:
				return up();
		}
		return new Object[0];
	}

	private Object[] up() {
		boolean success;
		try {
			EntityRidableTurtle ridableTurtleEntity = TurtleRidable.getEntity(turtle);
			success = ridableTurtleEntity.canMoveUp();
			ridableTurtleEntity.queueAction(RidableTurtlePacket.MovementCode.ASCEND.code);
		} catch (Exception ignore) {
			return new Object[]{false};
		}
		return new Object[]{success};
	}

	private Object[] unmount() {
		try {
			EntityRidableTurtle ridableTurtleEntity = TurtleRidable.getEntity(turtle);
			ridableTurtleEntity.riddenByEntity.mountEntity(null);
		} catch (Exception e) {
			return new Object[]{false};
		}
		return new Object[]{true};
	}

	private Object[] mountNearbyEntity() {
		List entities = TurtleRidable.getNearbyEntities(turtle, 1, 2, Entity.class);
		Entity entity = null;
		for (Object ent : entities) {
			if (((Entity)ent).isEntityAlive() && !(ent instanceof EntityRidableTurtle)) {
				entity = (Entity) ent;
				break;
			}
		}
		try {
			EntityRidableTurtle ridableTurtleEntity = TurtleRidable.getEntity(turtle);
			if (entity == null || ridableTurtleEntity.riddenByEntity != null)
				return new Object[]{false};
			entity.mountEntity(ridableTurtleEntity);
		} catch (Exception e) {
			return new Object[]{false};
		}
		return new Object[]{true};
	}

	private Object[] getRidingEntity() {
		EntityRidableTurtle ridableTurtleEntity;
		try {
			ridableTurtleEntity = TurtleRidable.getEntity(turtle);
		} catch (Exception e) {
			return new Object[0];
		}
		Entity ridingEntity = ridableTurtleEntity.riddenByEntity;
		Map<String, Object> map = new HashMap<String, Object>();
		if (ridingEntity != null) {
			map.put("name", ridingEntity instanceof EntityPlayer ? ((EntityPlayer) ridingEntity).getDisplayName() :
					EntityList.getEntityString(ridingEntity));
			map.put("type", ridingEntity.getClass().getSimpleName());
			map.put("uuid", ridingEntity instanceof EntityPlayer ?
					((EntityPlayer) ridingEntity).getGameProfile().getId().toString() : ridingEntity.getUniqueID().toString());
		}
		return new Object[]{map};
	}

	@Override
	public boolean equals(IPeripheral other) {
		return other == this;
	}
}
