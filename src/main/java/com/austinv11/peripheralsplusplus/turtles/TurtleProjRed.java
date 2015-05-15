package com.austinv11.peripheralsplusplus.turtles;

import com.austinv11.collectiveframework.minecraft.reference.ModIds;
import com.austinv11.peripheralsplusplus.reference.Config;
import com.austinv11.peripheralsplusplus.reference.Reference;
import com.austinv11.peripheralsplusplus.utils.FakeTurtlePlayer;
import com.austinv11.peripheralsplusplus.utils.TurtleUtil;
import com.austinv11.peripheralsplusplus.utils.Util;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Facing;
import net.minecraft.util.IIcon;

import java.util.List;

public abstract class TurtleProjRed extends TurtleDropCollector implements ITurtleUpgrade {

	public abstract int getID();

	public abstract ToolType getToolType();

	public abstract ToolMaterial getToolMaterial();

	public ItemStack getItem() {
		return getProjRedTool(getToolType(), getToolMaterial());
	}

	public static ItemStack getProjRedTool(ToolType toolType, ToolMaterial toolMaterial) {
		String name = "projectred.exploration."+toolType.getName()+toolMaterial.getName();
		return new ItemStack(GameRegistry.findItem(ModIds.ProjectRed_Exploration, name));
	}

	@Override
	public int getUpgradeID() {
		return Reference.BASE_PROJ_RED_UPGRADE + getID();
	}

	@Override
	public String getUnlocalisedAdjective() {
		return getToolType().getAdj();
	}

	@Override
	public TurtleUpgradeType getType() {
		return TurtleUpgradeType.Tool;
	}

	@Override
	public ItemStack getCraftingItem() {
		if (Config.enableRedPowerLikeTurtles)
			return getItem();
		return null;
	}

	@Override
	public IPeripheral createPeripheral(ITurtleAccess turtle, TurtleSide side) {
		return null;
	}

	@Override
	public TurtleCommandResult useTool(ITurtleAccess turtle, TurtleSide side, TurtleVerb verb, int direction) {
		if (!Config.enableRedPowerLikeTurtles)
			return TurtleCommandResult.failure("RedPower-like turtles have been disabled");
		FakeTurtlePlayer player = new FakeTurtlePlayer(turtle);
		switch (verb) {
			case Attack:
				List<Entity> entities = TurtleUtil.getEntitiesNearTurtle(turtle, player, direction);
				Entity ent = TurtleUtil.getClosestEntity(entities, player);
				if (ent != null)
					if (ent.canAttackWithItem() && !ent.hitByEntity(player)) {
						addEntity(turtle, ent);
						double damage = player.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue();
						damage *= Util.getDamageAttribute(getItem());
						if(damage > 0.0F && ent.attackEntityFrom(DamageSource.causePlayerDamage(player), (float)damage)) {
							return TurtleCommandResult.success();
						}
					}
				return TurtleCommandResult.failure();
			case Dig:
				if (getToolType() == ToolType.HOE) {
					int x = turtle.getPosition().posX+Facing.offsetsXForSide[direction];
					int y = turtle.getPosition().posY+Facing.offsetsYForSide[direction];
					int z = turtle.getPosition().posZ+Facing.offsetsZForSide[direction];
					float hitX = 0.5F + (float)Facing.offsetsXForSide[direction] * 0.5F;
					float hitY = 0.5F + (float)Facing.offsetsYForSide[direction] * 0.5F;
					float hitZ = 0.5F + (float)Facing.offsetsZForSide[direction] * 0.5F;
					if(Math.abs(hitY - 0.5F) < 0.01F)
						hitY = 0.45F;
					if (getItem().getItem().onItemUse(getItem(), player, turtle.getWorld(), x, y, z, Facing.oppositeSide[direction], hitX, hitY, hitZ))
						return TurtleCommandResult.success();
				}else {
					List<ItemStack> items = TurtleUtil.harvestBlock(turtle, player, direction, getItem());
					if (items != null) {
						TurtleUtil.addItemListToInv(items, turtle);
						return TurtleCommandResult.success();
					}
				}
				return TurtleCommandResult.failure();
		}
		return TurtleCommandResult.failure("An unknown error has occurred, please tell the mod author");
	}

	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIcon(ITurtleAccess turtle, TurtleSide side) {
		return getItem().getItem().getIconIndex(getItem());
	}

	@Override
	public void update(ITurtleAccess turtle, TurtleSide side) {}


	public static enum ToolType {
		SWORD("upgrade.minecraft:diamond_sword.adjective", "sword"),AXE("upgrade.minecraft:diamond_axe.adjective", "axe"),SHOVEL("upgrade.minecraft:diamond_shovel.adjective", "shovel"),PICKAXE("upgrade.minecraft:diamond_pickaxe.adjective", "pickaxe"),HOE("upgrade.minecraft:diamond_hoe.adjective", "hoe"),UNKNOWN("ERROR", "ERROR");
		private String adj;
		private String name;
		public String getAdj(){
			return adj;
		}
		public String getName() {
			return name;
		}
		private ToolType(String s, String s2) {
			adj = s;
			name = s2;
		}
	}

	public static enum ToolMaterial {
		PERIDOT("peridot"),RUBY("ruby"),SAPPHIRE("sapphire"), UNKNOWN("ERROR");
		private String name;
		public String getName(){
			return name;
		}
		private ToolMaterial(String s) {
			name = s;
		}
	}
}
