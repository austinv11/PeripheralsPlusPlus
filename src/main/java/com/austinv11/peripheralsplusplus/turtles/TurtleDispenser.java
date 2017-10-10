package com.austinv11.peripheralsplusplus.turtles;

import com.austinv11.collectiveframework.minecraft.utils.ModelManager;
import com.austinv11.peripheralsplusplus.reference.Reference;
import com.austinv11.peripheralsplusplus.turtles.peripherals.PeripheralDispenser;
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

public class TurtleDispenser implements ITurtleUpgrade, ModelManager.ModelRegistrar {
	
	@Override
	public ResourceLocation getUpgradeID() {
		return new ResourceLocation(Reference.DISPENSER_UPGRADE);
	}

    @Override
    public int getLegacyUpgradeID() {
        return Reference.DISPENSER_UPGRADE_LEGACY;
    }

    @Override
	public String getUnlocalisedAdjective() {
		return Reference.MOD_ID + ".turtle_upgrade.dispenser";
	}
	
	@Override
	public TurtleUpgradeType getType() {
		return TurtleUpgradeType.Peripheral;
	}
	
	@Override
	public ItemStack getCraftingItem() {
		return new ItemStack(Blocks.DISPENSER);
	}
	
	@Override
	public IPeripheral createPeripheral(ITurtleAccess turtle, TurtleSide side) {
		return new PeripheralDispenser(turtle);
	}

    @Nonnull
    @Override
    public TurtleCommandResult useTool(@Nonnull ITurtleAccess turtle, @Nonnull TurtleSide side, @Nonnull TurtleVerb verb, @Nonnull EnumFacing direction) {
        return TurtleCommandResult.failure();
    }

    @Nonnull
    @Override
    public Pair<IBakedModel, Matrix4f> getModel(@Nullable ITurtleAccess turtle, @Nonnull TurtleSide side) {
        return ModelUtil.getTurtleUpgradeModel("turtle_dispenser", side);
    }
	
	@Override
	public void update(ITurtleAccess turtle, TurtleSide side) {
		
	}

    @Override
    public void registerModels(IRegistry<ModelResourceLocation, IBakedModel> iRegistry) {
        ModelUtil.registerTurtleUpgradeModels(iRegistry, "turtle_dispenser");
    }
}
