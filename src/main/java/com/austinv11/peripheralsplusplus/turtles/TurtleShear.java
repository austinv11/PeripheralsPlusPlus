package com.austinv11.peripheralsplusplus.turtles;

import com.austinv11.collectiveframework.minecraft.utils.ModelManager;
import com.austinv11.peripheralsplusplus.reference.Config;
import com.austinv11.peripheralsplusplus.reference.Reference;
import com.austinv11.peripheralsplusplus.utils.FakeTurtlePlayer;
import com.austinv11.peripheralsplusplus.utils.ModelUtil;
import com.austinv11.peripheralsplusplus.utils.TurtleUtil;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.*;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.IRegistry;
import net.minecraftforge.common.IShearable;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import java.util.List;

public class TurtleShear implements ITurtleUpgrade, ModelManager.ModelRegistrar {

	@Override
	public ResourceLocation getUpgradeID() {
		return new ResourceLocation(Reference.SHEAR_UPGRADE);
	}

    @Override
    public int getLegacyUpgradeID() {
        return Reference.SHEAR_UPGRADE_LEGACY;
    }

    @Override
	public String getUnlocalisedAdjective() {
		return Reference.MOD_ID + ".turtle_upgrade.shears";
	}

	@Override
	public TurtleUpgradeType getType() {
		return TurtleUpgradeType.Tool;
	}

	@Override
	public ItemStack getCraftingItem() {
		if (Config.enableShearTurtle)
			return new ItemStack(Items.SHEARS);
		return ItemStack.EMPTY;
	}

	@Override
	public IPeripheral createPeripheral(ITurtleAccess turtle, TurtleSide side) {
		return null;
	}

    @Nonnull
    @Override
    public TurtleCommandResult useTool(@Nonnull ITurtleAccess turtle, @Nonnull TurtleSide side,
									   @Nonnull TurtleVerb verb, @Nonnull EnumFacing direction) {
		if (!Config.enableShearTurtle)
			return TurtleCommandResult.failure("Shearing turtles have been disabled");
		FakeTurtlePlayer player = new FakeTurtlePlayer(turtle);
		switch (verb) {
			case Attack:
				List<Entity> entities = TurtleUtil.getEntitiesNearTurtle(turtle, player, direction);
				Entity ent = TurtleUtil.getClosestShearableEntity(entities, player);
				if (ent != null)
					if (((IShearable) ent).isShearable(new ItemStack(Items.SHEARS), ent.world, ent.getPosition())) {
						TurtleUtil.addItemListToInv(((IShearable) ent).onSheared(new ItemStack(Items.SHEARS), ent.world,
								ent.getPosition(), 0), turtle);
						return TurtleCommandResult.success();
					}
				return TurtleCommandResult.failure();
			case Dig:
				List<ItemStack> items = TurtleUtil.harvestBlock(turtle, player, direction, new ItemStack(Items.SHEARS));
				if (items != null) {
					TurtleUtil.addItemListToInv(items, turtle);
					return TurtleCommandResult.success();
				}
				return TurtleCommandResult.failure();
		}
		return TurtleCommandResult.failure("An unknown error has occurred, please tell the mod author");
    }

    @Nonnull
    @Override
    public Pair<IBakedModel, Matrix4f> getModel(@Nullable ITurtleAccess turtle, @Nonnull TurtleSide side) {
		return ModelUtil.getTurtleUpgradeModel("turtle_shear", side, true);
    }

	@Override
	public void update(ITurtleAccess turtle, TurtleSide side) {}

    @Override
    public void registerModels(IRegistry<ModelResourceLocation, IBakedModel> iRegistry) {
        ModelUtil.registerTurtleUpgradeModels(iRegistry, "turtle_shear");
    }
}
