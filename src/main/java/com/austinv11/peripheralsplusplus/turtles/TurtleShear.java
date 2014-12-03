package com.austinv11.peripheralsplusplus.turtles;

import com.austinv11.peripheralsplusplus.reference.Config;
import com.austinv11.peripheralsplusplus.reference.Reference;
import com.austinv11.peripheralsplusplus.utils.FakeTurtlePlayer;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.*;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Facing;
import net.minecraft.util.IIcon;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.IShearable;

import java.util.List;

public class TurtleShear implements ITurtleUpgrade {

	@Override
	public int getUpgradeID() {
		return Reference.SHEAR_UPGRADE;
	}

	@Override
	public String getUnlocalisedAdjective() {
		return "peripheralsplusplus.turtleUpgrade.shears";
	}

	@Override
	public TurtleUpgradeType getType() {
		return TurtleUpgradeType.Tool;
	}

	@Override
	public ItemStack getCraftingItem() {
		if (Config.enableShearTurtle)
			return new ItemStack(Items.shears);
		return null;
	}

	@Override
	public IPeripheral createPeripheral(ITurtleAccess turtle, TurtleSide side) {
		return null;
	}

	@Override
	public TurtleCommandResult useTool(ITurtleAccess turtle, TurtleSide side, TurtleVerb verb, int direction) {
		if (!Config.enableShearTurtle)
			return TurtleCommandResult.failure("Shearing turtles have been disabled");
		int x = turtle.getPosition().posX+Facing.offsetsXForSide[turtle.getDirection()];
		int y = turtle.getPosition().posY+Facing.offsetsYForSide[turtle.getDirection()];
		int z = turtle.getPosition().posZ+Facing.offsetsZForSide[turtle.getDirection()];
		switch (verb) {
			case Attack:
				FakeTurtlePlayer player = new FakeTurtlePlayer(turtle);
				AxisAlignedBB box = AxisAlignedBB.getBoundingBox(x, y, z, x+1.0D, y+1.0D, z+1.0D);
				List<Entity> entities = turtle.getWorld().getEntitiesWithinAABBExcludingEntity(player, box);
				Entity ent = getClosestEntity(entities, player);
				if (ent != null)
					if (((IShearable) ent).isShearable(new ItemStack(Items.shears), ent.worldObj, (int) ent.posX, (int) ent.posY, (int) ent.posZ)) {
						addItemListToInv(((IShearable) ent).onSheared(new ItemStack(Items.shears), ent.worldObj, (int) ent.posX, (int) ent.posY, (int) ent.posZ, 0), turtle, player);
						return TurtleCommandResult.success();
					}
				return TurtleCommandResult.failure();
			case Dig:
				if (!turtle.getWorld().isAirBlock(x,y,z)) {
					Block block = turtle.getWorld().getBlock(x,y,z);
					FakeTurtlePlayer fPlayer = new FakeTurtlePlayer(turtle);
					fPlayer.setCurrentItemOrArmor(0, new ItemStack(Items.shears));
					if (block.canHarvestBlock(fPlayer, block.getDamageValue(turtle.getWorld(),x,y,z))) {
						List<ItemStack> items = block.getDrops(turtle.getWorld(),x,y,z, block.getDamageValue(turtle.getWorld(),x,y,z), 0);
						addItemListToInv(items, turtle, fPlayer);
						turtle.getWorld().setBlockToAir(x,y,z);
						return TurtleCommandResult.success();
					}
				}
				return TurtleCommandResult.failure();
		}
		return TurtleCommandResult.failure();
	}

	@Override
	public IIcon getIcon(ITurtleAccess turtle, TurtleSide side) {
		return Items.shears.getIconIndex(new ItemStack(Items.shears));
	}

	@Override
	public void update(ITurtleAccess turtle, TurtleSide side) {}

	private void addItemListToInv(List<ItemStack> items, ITurtleAccess turtle, FakeTurtlePlayer player) {
		for (ItemStack item : items) {
			player.addToInv(turtle, item);
		}
	}

	private Entity getClosestEntity(List<Entity> list, Entity ent) {
		Vec3 from = Vec3.createVectorHelper(ent.posX, ent.posY, ent.posZ);
		Entity returnVal = null;
		double lastDistance = Double.MAX_VALUE;
		for (Entity entity : list)
			if (entity instanceof IShearable) {
				Vec3 to = Vec3.createVectorHelper(entity.posX, entity.posY, entity.posZ);
				if (to.distanceTo(from) < lastDistance)
					returnVal = entity;
			}
		return returnVal;
	}
}
