package com.austinv11.peripheralsplusplus.client.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

/**
 * NanoBots - austinv11
 * Created using Tabula 4.1.1
 */
public class ModelNanoBotSwarm extends ModelBase {
    public ModelRenderer Bot0;
    public ModelRenderer Bot1;
    public ModelRenderer Bot2;
    public ModelRenderer Bot3;
    public ModelRenderer Bot4;
    public ModelRenderer Bot5;
    public ModelRenderer Bot6;
    public ModelRenderer Bot7;

    public ModelNanoBotSwarm() {
        this.textureWidth = 16;
        this.textureHeight = 16;
        this.Bot3 = new ModelRenderer(this, 0, 0);
        this.Bot3.setRotationPoint(-2.1F, 4.6F, -3.6F);
        this.Bot3.addBox(0.0F, 0.0F, 0.0F, 1, 1, 2, 0.0F);
        this.Bot0 = new ModelRenderer(this, 0, 0);
        this.Bot0.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.Bot0.addBox(0.0F, 0.0F, 0.0F, 1, 1, 2, 0.0F);
        this.Bot5 = new ModelRenderer(this, 0, 0);
        this.Bot5.setRotationPoint(3.5F, -2.8F, -2.8F);
        this.Bot5.addBox(0.0F, 0.0F, 0.0F, 1, 1, 2, 0.0F);
        this.Bot6 = new ModelRenderer(this, 0, 0);
        this.Bot6.setRotationPoint(-3.4F, 1.9F, -6.1F);
        this.Bot6.addBox(0.0F, 0.0F, 0.0F, 1, 1, 2, 0.0F);
        this.Bot4 = new ModelRenderer(this, 0, 0);
        this.Bot4.setRotationPoint(-4.3F, -2.8F, 3.3F);
        this.Bot4.addBox(0.0F, 0.0F, 0.0F, 1, 1, 2, 0.0F);
        this.Bot2 = new ModelRenderer(this, 0, 0);
        this.Bot2.setRotationPoint(-3.1F, 2.8F, 4.6F);
        this.Bot2.addBox(0.0F, 0.0F, 0.0F, 1, 1, 2, 0.0F);
        this.Bot7 = new ModelRenderer(this, 0, 0);
        this.Bot7.setRotationPoint(4.3F, 2.1F, 1.9F);
        this.Bot7.addBox(0.0F, 0.0F, 0.0F, 1, 1, 2, 0.0F);
        this.Bot1 = new ModelRenderer(this, 0, 0);
        this.Bot1.setRotationPoint(3.2F, 5.6F, 2.2F);
        this.Bot1.addBox(0.0F, 0.0F, 0.0F, 1, 1, 2, 0.0F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        GL11.glPushMatrix();
        GL11.glTranslatef(this.Bot3.offsetX, this.Bot3.offsetY, this.Bot3.offsetZ);
        GL11.glTranslatef(this.Bot3.rotationPointX * f5, this.Bot3.rotationPointY * f5, this.Bot3.rotationPointZ * f5);
        GL11.glScaled(1.0D, 0.5D, 0.8D);
        GL11.glTranslatef(-this.Bot3.offsetX, -this.Bot3.offsetY, -this.Bot3.offsetZ);
        GL11.glTranslatef(-this.Bot3.rotationPointX * f5, -this.Bot3.rotationPointY * f5, -this.Bot3.rotationPointZ * f5);
        this.Bot3.render(f5);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glTranslatef(this.Bot0.offsetX, this.Bot0.offsetY, this.Bot0.offsetZ);
        GL11.glTranslatef(this.Bot0.rotationPointX * f5, this.Bot0.rotationPointY * f5, this.Bot0.rotationPointZ * f5);
        GL11.glScaled(1.0D, 0.5D, 0.8D);
        GL11.glTranslatef(-this.Bot0.offsetX, -this.Bot0.offsetY, -this.Bot0.offsetZ);
        GL11.glTranslatef(-this.Bot0.rotationPointX * f5, -this.Bot0.rotationPointY * f5, -this.Bot0.rotationPointZ * f5);
        this.Bot0.render(f5);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glTranslatef(this.Bot5.offsetX, this.Bot5.offsetY, this.Bot5.offsetZ);
        GL11.glTranslatef(this.Bot5.rotationPointX * f5, this.Bot5.rotationPointY * f5, this.Bot5.rotationPointZ * f5);
        GL11.glScaled(1.0D, 0.5D, 0.8D);
        GL11.glTranslatef(-this.Bot5.offsetX, -this.Bot5.offsetY, -this.Bot5.offsetZ);
        GL11.glTranslatef(-this.Bot5.rotationPointX * f5, -this.Bot5.rotationPointY * f5, -this.Bot5.rotationPointZ * f5);
        this.Bot5.render(f5);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glTranslatef(this.Bot6.offsetX, this.Bot6.offsetY, this.Bot6.offsetZ);
        GL11.glTranslatef(this.Bot6.rotationPointX * f5, this.Bot6.rotationPointY * f5, this.Bot6.rotationPointZ * f5);
        GL11.glScaled(1.0D, 0.5D, 0.8D);
        GL11.glTranslatef(-this.Bot6.offsetX, -this.Bot6.offsetY, -this.Bot6.offsetZ);
        GL11.glTranslatef(-this.Bot6.rotationPointX * f5, -this.Bot6.rotationPointY * f5, -this.Bot6.rotationPointZ * f5);
        this.Bot6.render(f5);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glTranslatef(this.Bot4.offsetX, this.Bot4.offsetY, this.Bot4.offsetZ);
        GL11.glTranslatef(this.Bot4.rotationPointX * f5, this.Bot4.rotationPointY * f5, this.Bot4.rotationPointZ * f5);
        GL11.glScaled(1.0D, 0.5D, 0.8D);
        GL11.glTranslatef(-this.Bot4.offsetX, -this.Bot4.offsetY, -this.Bot4.offsetZ);
        GL11.glTranslatef(-this.Bot4.rotationPointX * f5, -this.Bot4.rotationPointY * f5, -this.Bot4.rotationPointZ * f5);
        this.Bot4.render(f5);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glTranslatef(this.Bot2.offsetX, this.Bot2.offsetY, this.Bot2.offsetZ);
        GL11.glTranslatef(this.Bot2.rotationPointX * f5, this.Bot2.rotationPointY * f5, this.Bot2.rotationPointZ * f5);
        GL11.glScaled(1.0D, 0.5D, 0.8D);
        GL11.glTranslatef(-this.Bot2.offsetX, -this.Bot2.offsetY, -this.Bot2.offsetZ);
        GL11.glTranslatef(-this.Bot2.rotationPointX * f5, -this.Bot2.rotationPointY * f5, -this.Bot2.rotationPointZ * f5);
        this.Bot2.render(f5);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glTranslatef(this.Bot7.offsetX, this.Bot7.offsetY, this.Bot7.offsetZ);
        GL11.glTranslatef(this.Bot7.rotationPointX * f5, this.Bot7.rotationPointY * f5, this.Bot7.rotationPointZ * f5);
        GL11.glScaled(1.0D, 0.5D, 0.8D);
        GL11.glTranslatef(-this.Bot7.offsetX, -this.Bot7.offsetY, -this.Bot7.offsetZ);
        GL11.glTranslatef(-this.Bot7.rotationPointX * f5, -this.Bot7.rotationPointY * f5, -this.Bot7.rotationPointZ * f5);
        this.Bot7.render(f5);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glTranslatef(this.Bot1.offsetX, this.Bot1.offsetY, this.Bot1.offsetZ);
        GL11.glTranslatef(this.Bot1.rotationPointX * f5, this.Bot1.rotationPointY * f5, this.Bot1.rotationPointZ * f5);
        GL11.glScaled(1.0D, 0.5D, 0.8D);
        GL11.glTranslatef(-this.Bot1.offsetX, -this.Bot1.offsetY, -this.Bot1.offsetZ);
        GL11.glTranslatef(-this.Bot1.rotationPointX * f5, -this.Bot1.rotationPointY * f5, -this.Bot1.rotationPointZ * f5);
        this.Bot1.render(f5);
        GL11.glPopMatrix();
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
