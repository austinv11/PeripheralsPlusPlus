package com.austinv11.peripheralsplusplus.turtles;

import com.austinv11.peripheralsplusplus.reference.Config;
import com.austinv11.peripheralsplusplus.reference.Reference;
import com.austinv11.peripheralsplusplus.utils.FakePlayer;
import com.mojang.authlib.GameProfile;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.*;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

import java.util.ArrayList;

public class TurtleWrench implements ITurtleUpgrade {

	private static ArrayList<ItemStack> stack = new ArrayList<ItemStack>();
	private static ArrayList<ModPlugin> plugins = new ArrayList<ModPlugin>();
	private static final int[] OPPOSITE = {1, 0, 3, 2, 5, 4};

	public TurtleWrench() {//Prioritizes:TE,BC,IC2,RA
		if (Loader.isModLoaded("ThermalExpansion")) {
			plugins.add(ModPlugin.TE);
			stack.add(GameRegistry.findItemStack("ThermalExpansion", "wrench", 1));
		}
		if (Loader.isModLoaded("Buildcraft|Core")) {
			plugins.add(ModPlugin.BC);
			stack.add(GameRegistry.findItemStack("Buildcraft|Core", "wrenchItem", 1));
		}
		if (Loader.isModLoaded("IC2")) {
			plugins.add(ModPlugin.IC2);
			stack.add(GameRegistry.findItemStack("IC2", "itemToolWrench", 1));
		}
		if (Loader.isModLoaded("RedstoneArsenal")){
			plugins.add(ModPlugin.RA);
			stack.add(GameRegistry.findItemStack("RedstoneArsenal", "tool.wrenchFlux", 1));
		}
	}

	public static boolean isUsable() {
		return (Loader.isModLoaded("ThermalExpansion") || Loader.isModLoaded("Buildcraft|Core") || Loader.isModLoaded("IC2") || Loader.isModLoaded("RedstoneArsenal"));
	}

	@Override
	public int getUpgradeID() {
		return Reference.WRENCH_UPGRADE;
	}

	@Override
	public String getUnlocalisedAdjective() {
		return "peripheralsplusplus.turtleUpgrade.wrench";
	}

	@Override
	public TurtleUpgradeType getType() {
		return TurtleUpgradeType.Tool;
	}

	@Override
	public ItemStack getCraftingItem() {
		if (Config.enableWrenchTurtle)
			return stack.get(0);
		return null;
	}

	@Override
	public IPeripheral createPeripheral(ITurtleAccess turtle, TurtleSide side) {
		return null;
	}

	@Override
	public TurtleCommandResult useTool(ITurtleAccess turtle, TurtleSide side, TurtleVerb verb, int direction) {
		if (!Config.enableWrenchTurtle)
			return TurtleCommandResult.failure("Wrench turtles have been disabled");
		switch (verb) {
			case Attack:
				FakePlayer player = new FakePlayer(turtle.getWorld(), new GameProfile(null, "ComputerCraft"));
				player.alignToTurtle(turtle);
				MovingObjectPosition mop = player.rayTrace(1.5D, 1F);
				if (mop == null || mop.typeOfHit != MovingObjectPosition.MovingObjectType.ENTITY)
					return TurtleCommandResult.failure();
				for (int i = 0; i < stack.size(); i++) {
					player.setCurrentItemOrArmor(0, stack.get(i));
					if (mop.entityHit.interactFirst(player))
						return TurtleCommandResult.success();;
				}

				return TurtleCommandResult.failure();
			case Dig:
				FakePlayer fPlayer = new FakePlayer(turtle.getWorld(), new GameProfile(null, "ComputerCraft"));
				fPlayer.alignToTurtle(turtle);
				MovingObjectPosition MOP = new MovingObjectPosition(turtle.getPosition().posX, turtle.getPosition().posY, turtle.getPosition().posZ, OPPOSITE[direction], Vec3.createVectorHelper(turtle.getPosition().posX, turtle.getPosition().posY, turtle.getPosition().posZ));
				Block block = turtle.getWorld().getBlock(MOP.blockX, MOP.blockY, MOP.blockZ);
				for (int i = 0; i < stack.size(); i++) {
					fPlayer.setCurrentItemOrArmor(0, stack.get(i));
					if (block != null && block.onBlockActivated(turtle.getWorld(), MOP.blockX, MOP.blockY, MOP.blockZ, fPlayer, MOP.sideHit, 0.0F, 0.0F, 0.0F))
						return TurtleCommandResult.success();
					if (stack.get(i).getItem().onItemUse(stack.get(i), fPlayer, turtle.getWorld(), MOP.blockX, MOP.blockY, MOP.blockZ, MOP.sideHit, 0.0F, 0.0F, 0.0F))
						return TurtleCommandResult.success();
				}
				return TurtleCommandResult.failure();
		}
		return TurtleCommandResult.failure();
	}

	@Override
	public IIcon getIcon(ITurtleAccess turtle, TurtleSide side) {
		return stack.get(0).getIconIndex();
	}

	@Override
	public void update(ITurtleAccess turtle, TurtleSide side) {}

	private enum ModPlugin {
		IC2,TE,RA,BC
	}
}
