package com.austinv11.peripheralsplusplus.turtles;

import com.austinv11.peripheralsplusplus.reference.Reference;
import com.austinv11.peripheralsplusplus.utils.IconManager;
import com.mojang.authlib.GameProfile;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.*;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;

import java.util.List;

public class TurtleProjRed implements ITurtleUpgrade, IconManager.IIconNeeded{

	private int i = 0;
	private ToolType type = ToolType.UNKNOWN;
	private ItemStack item = null;
	private IIcon icon;

	public void setI(int i) {
		this.i = i;
	}

	public void setType(ToolType type){
		this.type = type;
	}

	public void setItem(ItemStack stack) {
		this.item = stack;
	}

	@Override
	public int getUpgradeID() {
		return 0;//Reference.BASE_PROJ_RED_UPGRADE + i;
	}

	@Override
	public String getUnlocalisedAdjective() {
		return type.getName();
	}

	@Override
	public TurtleUpgradeType getType() {
		return TurtleUpgradeType.Tool;
	}

	@Override
	public ItemStack getCraftingItem() {
		return item;
	}

	@Override
	public IPeripheral createPeripheral(ITurtleAccess turtle, TurtleSide side) {
		return null;
	}

	@Override
	public TurtleCommandResult useTool(ITurtleAccess turtle, TurtleSide side, TurtleVerb verb, int direction) {
		World world = turtle.getWorld();
		switch (verb) {
			case Attack:
				FakePlayer player = new FakePlayer((WorldServer) world, new GameProfile(null, "ComputerCraft"));
				player.setPositionAndRotation(turtle.getPosition().posX, turtle.getPosition().posY, turtle.getPosition().posZ, turtle.getVisualYaw(1f), 1f);
				player.setCurrentItemOrArmor(0, this.getCraftingItem());
				MovingObjectPosition mop = player.rayTrace(1D, 1F);
				if (mop == null || mop.typeOfHit != MovingObjectPosition.MovingObjectType.ENTITY)
					return TurtleCommandResult.failure();
				return attack(turtle, mop.entityHit);
			case Dig:

				break;
		}
		return TurtleCommandResult.failure("An unknown error has occurred, please tell the mod author");
	}

	public TurtleCommandResult attack(ITurtleAccess turtle, Entity ent) {
		if (!(ent instanceof EntityLiving));
		return null;
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

	public enum ToolType {
		SWORD("upgrade.minecraft:diamond_sword.adjective"),AXE("upgrade.minecraft:diamond_axe.adjective"),SHOVEL("upgrade.minecraft:diamond_shovel.adjective"),PICKAXE("upgrade.minecraft:diamond_pickaxe.adjective"),HOE("upgrade.minecraft:diamond_hoe.adjective"),UNKNOWN("ERROR");
		private String name;
		public String getName(){
			return name;
		}
		private ToolType(String s) {
			name = s;
		}
	}
}
