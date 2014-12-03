package com.austinv11.peripheralsplusplus.turtles;

import com.austinv11.peripheralsplusplus.reference.Config;
import com.austinv11.peripheralsplusplus.reference.Reference;
import com.austinv11.peripheralsplusplus.utils.IconManager;
import cpw.mods.fml.common.registry.GameRegistry;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.*;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class TurtleProjRed implements ITurtleUpgrade, IconManager.IIconNeeded{

	private int id = 0;
	private ToolType type = ToolType.UNKNOWN;
	private ToolMaterial material = ToolMaterial.UNKNOWN;
	private ItemStack item = null;
	private IIcon icon;

	public TurtleProjRed(ToolMaterial material, ToolType type, int id) {
		setID(id);
		setType(type);
		setMaterial(material);
		setItem(getProjRedTool(type, material));
	}

	public void setID(int id) {
		this.id = id;
	}

	public void setType(ToolType type){
		this.type = type;
	}

	public void setItem(ItemStack stack) {
		this.item = stack;
	}

	public void setMaterial(ToolMaterial material) {
		this.material = material;
	}

	public static ItemStack getProjRedTool(ToolType toolType, ToolMaterial toolMaterial) {
		String name = "projectred.exploration."+toolType.getName()+toolMaterial.getName();
		return new ItemStack(GameRegistry.findItem("ProjRed|Exploration", name));
	}

	@Override
	public int getUpgradeID() {
		return Reference.BASE_PROJ_RED_UPGRADE + id;
	}

	@Override
	public String getUnlocalisedAdjective() {
		return type.getAdj();
	}

	@Override
	public TurtleUpgradeType getType() {
		return TurtleUpgradeType.Tool;
	}

	@Override
	public ItemStack getCraftingItem() {
		if (Config.enableProjectRedTurtles)
			return item;
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
		return icon;
	}

	@Override
	public void update(ITurtleAccess turtle, TurtleSide side) {}

	@Override
	public void registerIcon(IIconRegister register) {
		icon = register.registerIcon("ProjRed|Exploration:"+getUnwrappedUnlocalizedName(item.getUnlocalizedName()));
	}

	protected String getUnwrappedUnlocalizedName(String unlocalizedName){//Removes the "item." from the item name
		return unlocalizedName.substring(unlocalizedName.indexOf(".")+1);
	}

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
