package com.austinv11.peripheralsplusplus.turtles;

import com.austinv11.collectiveframework.minecraft.utils.ModelManager;
import com.austinv11.peripheralsplusplus.init.ModBlocks;
import com.austinv11.peripheralsplusplus.reference.Config;
import com.austinv11.peripheralsplusplus.reference.Reference;
import com.austinv11.peripheralsplusplus.tiles.TileEntityChatBox;
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

public class TurtleChatBox implements ITurtleUpgrade, ModelManager.ModelRegistrar {
    @Nonnull
    @Override
    public ResourceLocation getUpgradeID() {
        return new ResourceLocation(Reference.CHAT_BOX_UPGRADE);
    }

    @Override
    public int getLegacyUpgradeID() {
        return Reference.CHAT_BOX_UPGRADE_LEGACY;
    }

    @Override
	public String getUnlocalisedAdjective() {
		return Reference.MOD_ID + ".turtle_upgrade.chat_box";
	}

	@Override
	public TurtleUpgradeType getType() {
		return TurtleUpgradeType.Peripheral;
	}

	@Override
	public ItemStack getCraftingItem() {
    	if (Config.enableChatBox)
			return new ItemStack(ModBlocks.CHAT_BOX);
    	return ItemStack.EMPTY;
	}

	@Override
	public IPeripheral createPeripheral(ITurtleAccess turtle, TurtleSide side) {
		return new TileEntityChatBox(turtle);
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
		if (peripheral instanceof TileEntityChatBox)
			((TileEntityChatBox)peripheral).update(true);
	}

	@Nonnull
	@Override
	public Pair<IBakedModel, Matrix4f> getModel(@Nullable ITurtleAccess turtle, @Nonnull TurtleSide side) {
		return ModelUtil.getTurtleUpgradeModel("turtle_chat_box", side);
	}

	@Override
	public void registerModels(IRegistry<ModelResourceLocation, IBakedModel> registry) {
		ModelUtil.registerTurtleUpgradeModels(registry, "turtle_chat_box");
	}
}
