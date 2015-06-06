package com.austinv11.peripheralsplusplus.turtles;

import com.austinv11.peripheralsplusplus.entities.EntityRidableTurtle;
import com.austinv11.peripheralsplusplus.init.ModBlocks;
import com.austinv11.peripheralsplusplus.reference.Config;
import com.austinv11.peripheralsplusplus.reference.Reference;
import com.austinv11.peripheralsplusplus.turtles.peripherals.PeripheralRidable;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.ITurtleUpgrade;
import dan200.computercraft.api.turtle.TurtleCommandResult;
import dan200.computercraft.api.turtle.TurtleSide;
import dan200.computercraft.api.turtle.TurtleUpgradeType;
import dan200.computercraft.api.turtle.TurtleVerb;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.List;

public class TurtleRidable implements ITurtleUpgrade {

	@Override
	public int getUpgradeID() {
		return Reference.RIDABLE_UPGRADE;
	}

	@Override
	public String getUnlocalisedAdjective() {
		return Reference.MOD_ID.toLowerCase() + ".turtleUpgrade.ridable";
	}

	@Override
	public TurtleUpgradeType getType() {
		return TurtleUpgradeType.Peripheral;
	}

	@Override
	public ItemStack getCraftingItem() {
		if (Config.enableRidableTurtle) {
			return new ItemStack(Items.saddle);
		}
		return null;
	}

	@Override
	public IPeripheral createPeripheral(ITurtleAccess turtle, TurtleSide side) {
		return new PeripheralRidable(turtle);
	}

	@Override
	public TurtleCommandResult useTool(ITurtleAccess turtle, TurtleSide side, TurtleVerb verb, int direction) {
		return null;
	}

	@Override
	public IIcon getIcon(ITurtleAccess turtle, TurtleSide side) {
		return ModBlocks.dummyBlock.getIcon(0, 4);
	}

	@Override
	public void update(ITurtleAccess turtle, TurtleSide side) {
		if (!Config.enableRidableTurtle) {
			return;
		}
		World world = turtle.getWorld();
		if (!world.isRemote) {
			try {
				getEntity(turtle);
			} catch (Exception e) {
				EntityRidableTurtle ridableTurtle = new EntityRidableTurtle(world);
				ridableTurtle.setPosition(turtle.getPosition().posX + 0.5, turtle.getPosition().posY,
						turtle.getPosition().posZ + 0.5);
				ridableTurtle.setTurtle(turtle);
				world.spawnEntityInWorld(ridableTurtle);
			}
		}
	}

	public static EntityRidableTurtle getEntity(ITurtleAccess turtle) throws Exception {
		List entities = getNearbyEntities(turtle, 0, 1, EntityRidableTurtle.class);
		if (entities.size() < 1)
			throw new Exception("No entity bound to turtle.");
		return (EntityRidableTurtle) entities.get(0);
	}

	public static List getNearbyEntities(ITurtleAccess turtle, int radiusStart, int radiusEnd, Class entityType) {
		World world = turtle.getWorld();
		AxisAlignedBB bb = AxisAlignedBB.getBoundingBox(
				turtle.getPosition().posX - radiusStart, turtle.getPosition().posY - radiusStart,
				turtle.getPosition().posZ - radiusStart, turtle.getPosition().posX + radiusEnd,
				turtle.getPosition().posY + radiusEnd, turtle.getPosition().posZ + radiusEnd);
		return world.getEntitiesWithinAABB(entityType, bb);
	}
}
