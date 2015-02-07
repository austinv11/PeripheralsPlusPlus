package com.austinv11.peripheralsplusplus.client.models;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * Antenna2 - austinv11
 * Created using Tabula 4.1.1
 */
@SideOnly(Side.CLIENT)
public class ModelAntenna extends ModelBase {
    public ModelRenderer Base;
    public ModelRenderer Base1;
    public ModelRenderer DishBase;
    public ModelRenderer BaseTransition;
    public ModelRenderer Dish;
    public ModelRenderer Dish1;
    public ModelRenderer Transmitter;
    public ModelRenderer Dish2;
    public ModelRenderer Dish3;
    public ModelRenderer Transmitter1;

    public ModelAntenna() {
        this.textureWidth = 32;
        this.textureHeight = 32;
        this.Dish3 = new ModelRenderer(this, 6, 13);
        this.Dish3.setRotationPoint(0.2F, -1.2F, 0.0F);
        this.Dish3.addBox(-0.52F, -0.03F, 0.0F, 2, 1, 4, 0.0F);
        this.setRotateAngle(Dish3, 0.0F, 0.0F, -0.39269908169872414F);
        this.Dish = new ModelRenderer(this, 2, 8);
        this.Dish.setRotationPoint(0.42F, -0.01F, -1.0F);
        this.Dish.addBox(-3.0F, 0.1F, 0.0F, 3, 1, 4, 0.0F);
        this.setRotateAngle(Dish, 0.0F, 0.0F, 0.39269908169872414F);
        this.Base = new ModelRenderer(this, 12, 0);
        this.Base.setRotationPoint(0.0F, 22.5F, 0.0F);
        this.Base.addBox(-2.5F, -0.5F, -2.5F, 5, 1, 5, 0.0F);
        this.Transmitter1 = new ModelRenderer(this, 26, 18);
        this.Transmitter1.setRotationPoint(-0.5F, -1.3F, 1.55F);
        this.Transmitter1.addBox(0.0F, 0.0F, -0.05F, 2, 2, 1, 0.0F);
        this.setRotateAngle(Transmitter1, -0.8726646259971648F, 0.0F, 0.0F);
        this.Dish2 = new ModelRenderer(this, 0, 0);
        this.Dish2.setRotationPoint(-4.2F, -0.5F, 0.0F);
        this.Dish2.addBox(-0.28F, 0.03F, 0.0F, 2, 1, 4, 0.0F);
        this.setRotateAngle(Dish2, 0.0F, 0.0F, 0.39269908169872414F);
        this.DishBase = new ModelRenderer(this, 16, 8);
        this.DishBase.setRotationPoint(-0.5F, -2.5F, -0.7F);
        this.DishBase.addBox(0.0F, 0.0F, -1.0F, 2, 1, 4, 0.0F);
        this.setRotateAngle(DishBase, 1.5707963267948966F, 0.0F, 0.0F);
        this.Dish1 = new ModelRenderer(this, 18, 13);
        this.Dish1.setRotationPoint(4.72F, -0.01F, -1.0F);
        this.Dish1.addBox(-2.9F, -1.11F, 0.0F, 3, 1, 4, 0.0F);
        this.setRotateAngle(Dish1, 0.0F, 0.0F, -0.39269908169872414F);
        this.Transmitter = new ModelRenderer(this, 2, 14);
        this.Transmitter.setRotationPoint(0.5F, -2.0F, -2.0F);
        this.Transmitter.addBox(0.0F, 0.0F, 0.0F, 1, 3, 1, 0.0F);
        this.setRotateAngle(Transmitter, 0.5235987755982988F, 0.0F, 0.0F);
        this.Base1 = new ModelRenderer(this, 28, 6);
        this.Base1.setRotationPoint(-0.5F, -2.8F, -0.5F);
        this.Base1.addBox(0.0F, -3.09F, 0.0F, 1, 6, 1, 0.0F);
        this.setRotateAngle(Base1, -0.6283185307179586F, 0.0F, 0.0F);
        this.BaseTransition = new ModelRenderer(this, 22, 18);
        this.BaseTransition.setRotationPoint(0.0F, -3.8F, 0.28F);
        this.BaseTransition.addBox(0.0F, 0.0F, 0.0F, 1, 1, 1, 0.0F);
        this.setRotateAngle(BaseTransition, -0.7853981633974483F, 0.0F, 0.0F);
        this.Dish1.addChild(this.Dish3);
        this.DishBase.addChild(this.Dish);
        this.Transmitter.addChild(this.Transmitter1);
        this.Dish.addChild(this.Dish2);
        this.Base1.addChild(this.DishBase);
        this.DishBase.addChild(this.Dish1);
        this.DishBase.addChild(this.Transmitter);
        this.Base.addChild(this.Base1);
        this.Base1.addChild(this.BaseTransition);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        setRotateAngle(Base, 0, f4, 0);
        this.Base.render(f5);
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
