package com.austinv11.peripheralsplusplus.turtles;

import com.austinv11.collectiveframework.minecraft.utils.ModelManager;
import com.austinv11.peripheralsplusplus.init.ModBlocks;
import com.austinv11.peripheralsplusplus.reference.Reference;
import com.austinv11.peripheralsplusplus.tiles.TileEntityEnvironmentScanner;
import com.austinv11.peripheralsplusplus.utils.ModelUtil;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.*;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.IRegistry;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;

public class TurtleEnvironmentScanner implements ITurtleUpgrade, ModelManager.ModelRegistrar {

	@Override
	public ResourceLocation getUpgradeID() {
		return new ResourceLocation(Reference.ENVIRONMENT_UPGRADE);
	}

    @Override
    public int getLegacyUpgradeID() {
        return Reference.ENVIRONMENT_UPGRADE_LEGACY;
    }

    @Override
	public String getUnlocalisedAdjective() {
		return Reference.MOD_ID + ".turtle_upgrade.environment_scanner";
	}

	@Override
	public TurtleUpgradeType getType() {
		return TurtleUpgradeType.Peripheral;
	}

	@Override
	public ItemStack getCraftingItem() {
		return new ItemStack(ModBlocks.ENVIRONMENT_SCANNER);
	}

	@Override
	public IPeripheral createPeripheral(ITurtleAccess turtle, TurtleSide side) {
		TileEntityEnvironmentScanner te = new TileEntityEnvironmentScanner(turtle);
		te.setWorld(turtle.getWorld());
		te.setPos(turtle.getPosition());
		return te;
	}

    @Nonnull
    @Override
    public TurtleCommandResult useTool(@Nonnull ITurtleAccess turtle, @Nonnull TurtleSide side,
                                       @Nonnull TurtleVerb verb, @Nonnull EnumFacing direction) {
        return TurtleCommandResult.failure();
    }

    @Nonnull
    @Override
    public Pair<IBakedModel, Matrix4f> getModel(@Nullable ITurtleAccess turtle, @Nonnull TurtleSide side) {
        return ModelUtil.getTurtleUpgradeModel("turtle_environment_scanner", side);
    }

    @Override
	public void update(ITurtleAccess turtle, TurtleSide side) {
		IPeripheral peripheral = turtle.getPeripheral(side);
		if (peripheral instanceof TileEntityEnvironmentScanner)
			((TileEntityEnvironmentScanner) peripheral).update();
	}

    @Override
    public void registerModels(IRegistry<ModelResourceLocation, IBakedModel> iRegistry) {
        ModelUtil.registerTurtleUpgradeModels(iRegistry, "turtle_environment_scanner");
    }
}
