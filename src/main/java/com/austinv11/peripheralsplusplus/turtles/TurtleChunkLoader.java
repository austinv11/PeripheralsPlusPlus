package com.austinv11.peripheralsplusplus.turtles;

import com.austinv11.collectiveframework.minecraft.utils.ModelManager;
import com.austinv11.peripheralsplusplus.init.ModItems;
import com.austinv11.peripheralsplusplus.reference.Reference;
import com.austinv11.peripheralsplusplus.turtles.peripherals.PeripheralChunkLoader;
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

public class TurtleChunkLoader implements ITurtleUpgrade, ModelManager.ModelRegistrar {

	@Override
	public ResourceLocation getUpgradeID() {
		return new ResourceLocation(Reference.CHUNK_LOADER_UPGRADE);
	}

	@Override
	public int getLegacyUpgradeID() {
		return Reference.CHUNK_LOADER_UPGRADE_LEGACY;
	}

	@Override
	public String getUnlocalisedAdjective() {
		return Reference.MOD_ID + ".turtle_upgrade.loader";
	}

	@Override
	public TurtleUpgradeType getType() {
		return TurtleUpgradeType.Peripheral;
	}

	@Override
	public ItemStack getCraftingItem() {
		return new ItemStack(ModItems.CHUNK_LOADER_UPGRADE);
	}

	@Override
	public IPeripheral createPeripheral(ITurtleAccess turtle, TurtleSide side) {
		return new PeripheralChunkLoader(turtle);
	}

	@Nonnull
	@Override
	public TurtleCommandResult useTool(@Nonnull ITurtleAccess iTurtleAccess, @Nonnull TurtleSide turtleSide,
									   @Nonnull TurtleVerb turtleVerb, @Nonnull EnumFacing enumFacing) {
		return TurtleCommandResult.failure();
	}

	@Nonnull
	@Override
	public Pair<IBakedModel, Matrix4f> getModel(@Nullable ITurtleAccess iTurtleAccess, @Nonnull TurtleSide turtleSide) {
		return ModelUtil.getTurtleUpgradeModel("turtle_chunk_loader", turtleSide);
	}

	@Override
	public void update(ITurtleAccess turtle, TurtleSide side) {
		IPeripheral peripheral = turtle.getPeripheral(side);
		if (peripheral instanceof PeripheralChunkLoader) {
			PeripheralChunkLoader loader = (PeripheralChunkLoader) peripheral;
			loader.update();
		}
	}

	@Override
	public void registerModels(IRegistry<ModelResourceLocation, IBakedModel> iRegistry) {
		ModelUtil.registerTurtleUpgradeModels(iRegistry, "turtle_chunk_loader");
	}
}
