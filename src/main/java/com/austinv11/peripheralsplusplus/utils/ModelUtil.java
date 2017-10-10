package com.austinv11.peripheralsplusplus.utils;

import com.austinv11.peripheralsplusplus.reference.Reference;
import dan200.computercraft.api.turtle.TurtleSide;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelManager;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.IRegistry;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import org.apache.commons.lang3.tuple.Pair;

import javax.vecmath.Matrix4f;
import java.util.Arrays;
import java.util.Locale;

public class ModelUtil {
    /**
     * Loads, bakes, and registers turtle upgrade models
     * @param registry model registry
     * @param name model name - a left/right postfix will be added
     */
    public static void registerTurtleUpgradeModels(IRegistry<ModelResourceLocation, IBakedModel> registry,
                                                   String name) {
        String[] sides = new String[]{"left", "right"};
        for (String side : sides)
            registerModel(registry, name + "_" + side, ModelType.BLOCK, "inventory");
    }

    /**
     * Loads, bakes, and registers models
     * @param registry model registry
     * @param name name of model
     * @param modelType block/item type
     * @param variants variant types to register this model as
     */
    public static void registerModel(IRegistry<ModelResourceLocation, IBakedModel> registry, String name,
                                      ModelType modelType, String... variants) {
        IModel model = ModelLoaderRegistry.getModelOrLogError(new ResourceLocation(Reference.MOD_ID,
                modelType.name().toLowerCase(Locale.US) + "/" + name),
                String.format("Failed to load model: %s/%s with variants #%s",
                        modelType.name().toLowerCase(Locale.US),
                        name, Arrays.toString(variants)));
        IBakedModel bakedModel = model.bake(
                model.getDefaultState(),
                DefaultVertexFormats.ITEM,
                resourceLocation -> Minecraft.getMinecraft().getTextureMapBlocks()
                        .getAtlasSprite(resourceLocation.toString()));
        for (String variant : variants)
            registry.putObject(new ModelResourceLocation(Reference.MOD_ID + ":" + name, variant),
                    bakedModel);
    }

    /**
     * @see ModelUtil#getTurtleUpgradeModel(String, TurtleSide, boolean)
     */
    public static Pair<IBakedModel, Matrix4f> getTurtleUpgradeModel(String modelName, TurtleSide side) {
        return getTurtleUpgradeModel(modelName, side, false);
    }

    /**
     * Gets a model from the model manager for a turtle side
     * @param modelName name of model
     * @param side side of model to get
     * @param toolTransform transform model to side of turtle
     * @return baked turtle upgrade model
     */
    public static Pair<IBakedModel, Matrix4f> getTurtleUpgradeModel(String modelName, TurtleSide side,
                                                                    boolean toolTransform) {
        ModelManager modelManager = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getModelManager();
        ModelResourceLocation modelResource = new ModelResourceLocation(Reference.MOD_ID + ":" +
                modelName + "_" + side.name().toLowerCase(Locale.US), "inventory");
        IBakedModel model = modelManager.getModel(modelResource);
        Matrix4f transform = null;
        if (toolTransform) {
            float xOffset = side.equals(TurtleSide.Left) ? -0.40625f : 0.40625f;
            transform = new Matrix4f(
                    0, 0, -1, 1 + xOffset,
                    1, 0, 0, 0,
                    0, -1, 0, 1,
                    0, 0, 0, 1
            );
        }
        return Pair.of(model,transform);
    }

    public enum ModelType {BLOCK, ITEM}
}
