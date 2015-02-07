package com.austinv11.peripheralsplusplus.turtles;

import com.austinv11.peripheralsplusplus.reference.Config;
import com.austinv11.peripheralsplusplus.reference.Reference;
import com.austinv11.peripheralsplusplus.utils.FakeTurtlePlayer;
import com.austinv11.peripheralsplusplus.utils.TurtleUtil;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.*;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
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
		FakeTurtlePlayer player = new FakeTurtlePlayer(turtle);
		switch (verb) {
			case Attack:
				List<Entity> entities = TurtleUtil.getEntitiesNearTurtle(turtle, player, direction);
				Entity ent = TurtleUtil.getClosestShearableEntity(entities, player);
				if (ent != null)
					if (((IShearable) ent).isShearable(new ItemStack(Items.shears), ent.worldObj, (int) ent.posX, (int) ent.posY, (int) ent.posZ)) {
						TurtleUtil.addItemListToInv(((IShearable) ent).onSheared(new ItemStack(Items.shears), ent.worldObj, (int) ent.posX, (int) ent.posY, (int) ent.posZ, 0), turtle);
						return TurtleCommandResult.success();
					}
				return TurtleCommandResult.failure();
			case Dig:
				List<ItemStack> items = TurtleUtil.harvestBlock(turtle, player, direction, new ItemStack(Items.shears));
				if (items != null) {
					TurtleUtil.addItemListToInv(items, turtle);
					return TurtleCommandResult.success();
				}
				return TurtleCommandResult.failure();
		}
		return TurtleCommandResult.failure("An unknown error has occurred, please tell the mod author");
	}

	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIcon(ITurtleAccess turtle, TurtleSide side) {
		return Items.shears.getIconIndex(new ItemStack(Items.shears));
	}

	@Override
	public void update(ITurtleAccess turtle, TurtleSide side) {}
}
