package com.austinv11.peripheralsplusplus.turtles;

import com.austinv11.collectiveframework.minecraft.utils.ModelManager;
import com.austinv11.peripheralsplusplus.reference.Config;
import com.austinv11.peripheralsplusplus.reference.Reference;
import com.austinv11.peripheralsplusplus.turtles.peripherals.PeripheralXP;
import com.austinv11.peripheralsplusplus.utils.ModelUtil;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.*;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.IRegistry;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;

public class TurtleXP implements ITurtleUpgrade, ModelManager.ModelRegistrar {

	@Override
	public ResourceLocation getUpgradeID() {
		return new ResourceLocation(Reference.XP_UPGRADE);
	}

	@Override
	public int getLegacyUpgradeID() {
		return Reference.XP_UPGRADE_LEGACY;
	}

	@Override
	public String getUnlocalisedAdjective() {
		return "peripheralsplusplus.turtle_upgrade.xp";
	}

	@Override
	public TurtleUpgradeType getType() {
		return TurtleUpgradeType.Peripheral;
	}

	@Override
	public ItemStack getCraftingItem() {
		if (Config.enableXPTurtle)
			return new ItemStack(Blocks.ENCHANTING_TABLE);
		return ItemStack.EMPTY;
	}

	@Override
	public IPeripheral createPeripheral(ITurtleAccess turtle, TurtleSide side) {
		return new PeripheralXP(turtle, side);
	}

	@Nonnull
	@Override
	public TurtleCommandResult useTool(@Nonnull ITurtleAccess turtle, @Nonnull TurtleSide side, @Nonnull TurtleVerb verb, @Nonnull EnumFacing direction) {
		return TurtleCommandResult.failure();
	}

	@Nonnull
	@Override
	public Pair<IBakedModel, Matrix4f> getModel(@Nullable ITurtleAccess turtle, @Nonnull TurtleSide side) {
		return ModelUtil.getTurtleUpgradeModel("turtle_xp", side);
	}

	@Override
	public void update(ITurtleAccess turtle, TurtleSide side) {
		IPeripheral peripheral = turtle.getPeripheral(side);
		if (peripheral instanceof PeripheralXP)
			((PeripheralXP)peripheral).update();
	}

	@Override
	public void registerModels(IRegistry<ModelResourceLocation, IBakedModel> iRegistry) {
		ModelUtil.registerTurtleUpgradeModels(iRegistry, "turtle_xp");
	}
}
