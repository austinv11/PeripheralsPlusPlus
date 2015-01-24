package com.austinv11.peripheralsplusplus.turtles;

import com.austinv11.peripheralsplusplus.reference.Config;
import com.austinv11.peripheralsplusplus.reference.Reference;
import com.austinv11.peripheralsplusplus.turtles.peripherals.PeripheralClick;
import com.austinv11.peripheralsplusplus.utils.FakeTurtlePlayer;
import com.austinv11.peripheralsplusplus.utils.TurtleUtil;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.*;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Facing;
import net.minecraft.util.IIcon;

public class TurtlePlayer implements ITurtleUpgrade {
	
	@Override
	public int getUpgradeID() {
		return Reference.CLICKY_UPGRADE;
	}

	@Override
	public String getUnlocalisedAdjective() {
		return "peripheralsplusplus.turtleUpgrade.clicky";
	}

	@Override
	public TurtleUpgradeType getType() {
		return Config.allowForToolUsage ? TurtleUpgradeType.Peripheral : TurtleUpgradeType.Tool;
	}

	@Override
	public ItemStack getCraftingItem() {
		if (!Config.enableClickyTurtle)
			return null;
		return new ItemStack(Items.skull, 1, 3);
	}

	@Override
	public IPeripheral createPeripheral(ITurtleAccess turtle, TurtleSide side) {
		return new PeripheralClick(turtle);
	}

	@Override
	public TurtleCommandResult useTool(ITurtleAccess turtle, TurtleSide side, TurtleVerb verb, int direction) {
		if (!Config.enableClickyTurtle)
			return TurtleCommandResult.failure("Clicky turtles have been disabled");
		FakeTurtlePlayer player = new FakeTurtlePlayer(turtle);
		ChunkCoordinates coords = turtle.getPosition();
		switch (verb) {
			case Attack:
				if (turtle.getWorld().isAirBlock(coords.posX+Facing.offsetsXForSide[direction], coords.posY+Facing.offsetsYForSide[direction], coords.posZ+Facing.offsetsZForSide[direction])) {
					Entity ent = TurtleUtil.getClosestEntity(TurtleUtil.getEntitiesNearTurtle(turtle, player, direction), player);
					if (ent != null && !ent.hitByEntity(player)) {
						ent.attackEntityFrom(DamageSource.causePlayerDamage(player), (float)player.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue());
						return TurtleCommandResult.success();
					}
				} else {
					Block block = turtle.getWorld().getBlock(coords.posX+Facing.offsetsXForSide[direction], coords.posY+Facing.offsetsYForSide[direction], coords.posZ+Facing.offsetsZForSide[direction]);
					block.onBlockClicked(turtle.getWorld(), coords.posX+Facing.offsetsXForSide[direction], coords.posY+Facing.offsetsYForSide[direction], coords.posZ+Facing.offsetsZForSide[direction], player);
					turtle.getWorld().scheduleBlockUpdate(coords.posX+Facing.offsetsXForSide[direction], coords.posY+Facing.offsetsYForSide[direction], coords.posZ+Facing.offsetsZForSide[direction], block, block.tickRate(turtle.getWorld()));
					return TurtleCommandResult.success();
				}
				return TurtleCommandResult.failure();
			case Dig:
				if (turtle.getWorld().isAirBlock(coords.posX+Facing.offsetsXForSide[direction], coords.posY+Facing.offsetsYForSide[direction], coords.posZ+Facing.offsetsZForSide[direction])) {
					Entity ent = TurtleUtil.getClosestEntity(TurtleUtil.getEntitiesNearTurtle(turtle, player, direction), player);
					if (ent != null) {
						ent.interactFirst(player);
						return TurtleCommandResult.success();
					}
				} else {
					float hitX = 0.5F + (float)Facing.offsetsXForSide[direction] * 0.5F;
					float hitY = 0.5F + (float)Facing.offsetsYForSide[direction] * 0.5F;
					float hitZ = 0.5F + (float)Facing.offsetsZForSide[direction] * 0.5F;
					if(Math.abs(hitY - 0.5F) < 0.01F)
						hitY = 0.45F;
					Block block = turtle.getWorld().getBlock(coords.posX+Facing.offsetsXForSide[direction], coords.posY+Facing.offsetsYForSide[direction], coords.posZ+Facing.offsetsZForSide[direction]);
					block.onBlockActivated(turtle.getWorld(), coords.posX+Facing.offsetsXForSide[direction], coords.posY+Facing.offsetsYForSide[direction], coords.posZ+Facing.offsetsZForSide[direction], player, turtle.getWorld().getBlockMetadata(coords.posX+Facing.offsetsXForSide[direction], coords.posY+Facing.offsetsYForSide[direction], coords.posZ+Facing.offsetsZForSide[direction]), hitX, hitY, hitZ);
					turtle.getWorld().scheduleBlockUpdate(coords.posX+Facing.offsetsXForSide[direction], coords.posY+Facing.offsetsYForSide[direction], coords.posZ+Facing.offsetsZForSide[direction], block, block.tickRate(turtle.getWorld()));
					return TurtleCommandResult.success();
				}
				return TurtleCommandResult.failure();
		}
		return TurtleCommandResult.failure("An unknown error has occurred, please tell the mod author");
	}

	@Override
	public IIcon getIcon(ITurtleAccess turtle, TurtleSide side) {
		return Items.skull.getIconFromDamage(3);
	}

	@Override
	public void update(ITurtleAccess turtle, TurtleSide side) {}
}
