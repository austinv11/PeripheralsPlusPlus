package com.austinv11.peripheralsplusplus.turtles;

import com.austinv11.collectiveframework.minecraft.utils.ModelManager;
import com.austinv11.peripheralsplusplus.init.ModBlocks;
import com.austinv11.peripheralsplusplus.reference.Reference;
import com.austinv11.peripheralsplusplus.tiles.TileEntityPlayerSensor;
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

public class TurtlePlayerSensor implements ITurtleUpgrade, ModelManager.ModelRegistrar {

	@Override
	public ResourceLocation getUpgradeID() {
		return new ResourceLocation(Reference.PLAYER_SENSOR_UPGRADE);
	}

    @Override
    public int getLegacyUpgradeID() {
        return Reference.PLAYER_SENSOR_UPGRADE_LEGACY;
    }

    @Override
	public String getUnlocalisedAdjective() {
		return Reference.MOD_ID.toLowerCase()+".turtle_upgrade.player_sensor";
	}

	@Override
	public TurtleUpgradeType getType() {
		return TurtleUpgradeType.Peripheral;
	}

	@Override
	public ItemStack getCraftingItem() {
		return new ItemStack(ModBlocks.PLAYER_SENSOR);
	}

	@Override
	public IPeripheral createPeripheral(ITurtleAccess turtle, TurtleSide side) {
		return new TileEntityPlayerSensor(turtle);
	}

    @Nonnull
    @Override
    public TurtleCommandResult useTool(@Nonnull ITurtleAccess turtle, @Nonnull TurtleSide side,
                                       @Nonnull TurtleVerb verb, @Nonnull EnumFacing direction) {
        return null;
    }

    @Override
	public void update(ITurtleAccess turtle, TurtleSide side) {//Nothing

	}

    @Nonnull
    @Override
    public Pair<IBakedModel, Matrix4f> getModel(@Nullable ITurtleAccess turtle, @Nonnull TurtleSide side) {
        return ModelUtil.getTurtleUpgradeModel("turtle_player_sensor", side);
    }

    @Override
    public void registerModels(IRegistry<ModelResourceLocation, IBakedModel> registry) {
        ModelUtil.registerTurtleUpgradeModels(registry, "turtle_player_sensor");
    }
}
