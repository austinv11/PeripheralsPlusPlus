package com.austinv11.peripheralsplusplus.turtles;

import com.austinv11.collectiveframework.minecraft.utils.ModelManager;
import com.austinv11.collectiveframework.minecraft.utils.TextureManager;
import com.austinv11.peripheralsplusplus.entities.EntityRidableTurtle;
import com.austinv11.peripheralsplusplus.reference.Config;
import com.austinv11.peripheralsplusplus.reference.Reference;
import com.austinv11.peripheralsplusplus.utils.ModelUtil;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.*;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.IRegistry;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;

public class TurtleRidable implements ITurtleUpgrade, TextureManager.TextureRegistrar, ModelManager.ModelRegistrar {
	@Nonnull
	@Override
	public ResourceLocation getUpgradeID() {
		return new ResourceLocation(Reference.RIDABLE_UPGRADE);
	}

	@Override
	public int getLegacyUpgradeID() {
		return Reference.RIDABLE_UPGRADE_LEGACY;
	}

	@Override
	public String getUnlocalisedAdjective() {
		return Reference.MOD_ID + ".turtle_upgrade.ridable";
	}

	@Override
	public TurtleUpgradeType getType() {
		return TurtleUpgradeType.Peripheral;
	}

	@Override
	public ItemStack getCraftingItem() {
		if (Config.enableRidableTurtle)
			return new ItemStack(Items.SADDLE);
		return ItemStack.EMPTY;
	}

	@Override
	public IPeripheral createPeripheral(ITurtleAccess turtle, TurtleSide side) {
		EntityRidableTurtle entity = new EntityRidableTurtle(turtle.getWorld());
		entity.setPosition(turtle.getPosition().getX(), turtle.getPosition().getY(), turtle.getPosition().getZ());
		entity.setTurtle(turtle);
		turtle.getWorld().spawnEntity(entity);
		return entity;
	}

	@Nonnull
	@Override
	public TurtleCommandResult useTool(@Nonnull ITurtleAccess turtle, @Nonnull TurtleSide side,
									   @Nonnull TurtleVerb verb, @Nonnull EnumFacing direction) {
		return TurtleCommandResult.failure();
	}

	@Override
	public void update(ITurtleAccess turtle, TurtleSide side) {
		IPeripheral peripheral = turtle.getPeripheral(side);
		if (peripheral instanceof EntityRidableTurtle)
			((EntityRidableTurtle)peripheral).update(turtle);
	}

	@Nonnull
	@Override
	public Pair<IBakedModel, Matrix4f> getModel(@Nullable ITurtleAccess turtle, @Nonnull TurtleSide side) {
		return ModelUtil.getTurtleUpgradeModel("turtle_ridable", side);
	}

	@Override
	public void registerModels(IRegistry<ModelResourceLocation, IBakedModel> registry) {
		ModelUtil.registerTurtleUpgradeModels(registry, "turtle_ridable");
	}

	@Override
	public void registerTextures(TextureMap textureMap) {
		textureMap.registerSprite(new ResourceLocation(Reference.MOD_ID, "blocks/ridable_turtle_upgrade"));
	}
}
