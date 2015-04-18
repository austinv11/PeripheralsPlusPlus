package com.austinv11.peripheralsplusplus.turtles;

import com.austinv11.peripheralsplusplus.entities.EntityRidableTurtle;
import com.austinv11.peripheralsplusplus.init.ModBlocks;
import com.austinv11.peripheralsplusplus.reference.Config;
import com.austinv11.peripheralsplusplus.reference.Reference;
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

public class TurtleRideable implements ITurtleUpgrade {

	private EntityRidableTurtle ridableTurtle;

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
		if (Config.enableRidableTurtle)
			return new ItemStack(Items.saddle);
		return null;
	}

	@Override
	public IPeripheral createPeripheral(ITurtleAccess turtle, TurtleSide side) {
		return null;
	}

	@Override
	public TurtleCommandResult useTool(ITurtleAccess turtle, TurtleSide side, TurtleVerb verb, int direction) {
		return null;
	}

	@Override
	public IIcon getIcon(ITurtleAccess turtle, TurtleSide side) {
		return ModBlocks.dummyBlock.getIcon(0, 0); // TODO add icon to dummy block
	}

	@Override
	public void update(ITurtleAccess turtle, TurtleSide side) {
		World world = turtle.getWorld();
		if (!world.isRemote) {
			AxisAlignedBB bb = AxisAlignedBB.getBoundingBox(
					turtle.getPosition().posX, turtle.getPosition().posY,
					turtle.getPosition().posZ, turtle.getPosition().posX + 1,
					turtle.getPosition().posY + 1, turtle.getPosition().posZ + 1);
			List entities = world.getEntitiesWithinAABB(EntityRidableTurtle.class, bb);
			if (entities.size() < 1) {
				ridableTurtle = new EntityRidableTurtle(world);
				ridableTurtle.setPosition(turtle.getPosition().posX + 0.5, turtle.getPosition().posY,
						turtle.getPosition().posZ + 0.5);
				ridableTurtle.setTurtle(turtle);
				world.spawnEntityInWorld(ridableTurtle);
			}
		}
	}
}
