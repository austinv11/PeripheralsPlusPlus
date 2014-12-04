package com.austinv11.peripheralsplusplus.turtles;

import com.austinv11.peripheralsplusplus.reference.Config;
import com.austinv11.peripheralsplusplus.reference.Reference;
import com.austinv11.peripheralsplusplus.utils.FakeTurtlePlayer;
import com.austinv11.peripheralsplusplus.utils.TurtleUtil;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IIcon;
import net.minecraftforge.event.entity.living.LivingDropsEvent;

import java.util.HashMap;
import java.util.List;

public abstract class TurtleProjRed implements ITurtleUpgrade{

	public static HashMap<Entity, ITurtleAccess> map = new HashMap<Entity, ITurtleAccess>();

	public abstract int getID();

	public abstract ToolType getToolType();

	public abstract ToolMaterial getToolMaterial();

	public ItemStack getItem() {
		return getProjRedTool(getToolType(), getToolMaterial());
	}

	public static ItemStack getProjRedTool(ToolType toolType, ToolMaterial toolMaterial) {
		String name = "projectred.exploration."+toolType.getName()+toolMaterial.getName();
		return new ItemStack(GameRegistry.findItem("ProjRed|Exploration", name));
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
		if (Config.enableProjectRedTurtles)
			return getItem();
		return null;
	}

	@Override
	public IPeripheral createPeripheral(ITurtleAccess turtle, TurtleSide side) {
		return null;
	}

	@Override
	public TurtleCommandResult useTool(ITurtleAccess turtle, TurtleSide side, TurtleVerb verb, int direction) {
		if (!Config.enableProjectRedTurtles)
			return TurtleCommandResult.failure("Project Red turtles have been disabled");
		FakeTurtlePlayer player = new FakeTurtlePlayer(turtle);
		switch (verb) {
			case Attack:
				List<Entity> entities = TurtleUtil.getEntitiesNearTurtle(turtle, player, direction);
				Entity ent = TurtleUtil.getClosestEntity(entities, player);
				if (ent != null)
					if (ent.canAttackWithItem() && !ent.hitByEntity(player)) {
						map.put(ent, null);
						double damage = player.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue();
						damage *= ((AttributeModifier)getItem().getItem().getAttributeModifiers(getItem()).get(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName())).getAmount();
						if(damage > 0.0F && ent.attackEntityFrom(DamageSource.causePlayerDamage(player), (float)damage)) {
							return TurtleCommandResult.success();
						}
					}
				return TurtleCommandResult.failure();
			case Dig:

				break;
		}
		return TurtleCommandResult.failure("An unknown error has occurred, please tell the mod author");
	}

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

	public static class Listener {

		@SubscribeEvent
		public void onDrops(LivingDropsEvent event) {
			if (map.containsKey(event.entity)) {
				TurtleUtil.addItemListToInv(TurtleUtil.entityItemsToItemStack(event.drops), map.get(event.entity));
				map.remove(event.entity);
			}
		}
	}
}
