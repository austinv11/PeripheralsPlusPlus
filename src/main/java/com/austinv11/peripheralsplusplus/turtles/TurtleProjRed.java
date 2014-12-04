package com.austinv11.peripheralsplusplus.turtles;

import com.austinv11.peripheralsplusplus.reference.Config;
import com.austinv11.peripheralsplusplus.reference.Reference;
import cpw.mods.fml.common.registry.GameRegistry;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.*;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public abstract class TurtleProjRed implements ITurtleUpgrade{

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
		World world = turtle.getWorld();
		switch (verb) {
			case Attack:

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
}
