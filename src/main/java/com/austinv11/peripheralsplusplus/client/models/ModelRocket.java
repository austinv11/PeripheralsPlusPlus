package com.austinv11.peripheralsplusplus.client.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

/**
 * Rocket - austinv11
 * Created using Tabula 4.0.1
 */
public class ModelRocket extends ModelBase {
    public ModelRenderer Wall1;
    public ModelRenderer Base;
    public ModelRenderer Cone1;
    public ModelRenderer Wing1;
    public ModelRenderer Wing3;
    public ModelRenderer Wall2;
    public ModelRenderer Wall3;
    public ModelRenderer Wall4;
    public ModelRenderer Engine1;
    public ModelRenderer Engine2;
    public ModelRenderer Engine3;
    public ModelRenderer Engine4;
    public ModelRenderer Cone2;
    public ModelRenderer Cone3;
    public ModelRenderer Antenna;
    public ModelRenderer Wing2;
    public ModelRenderer Wing1p1;
    public ModelRenderer Wing2p1;
    public ModelRenderer Wing2p2;
    public ModelRenderer Wing2p3;
    public ModelRenderer Wing2p4;
    public ModelRenderer Wing2p5;
    public ModelRenderer Wing1p2;
    public ModelRenderer Wing1p3;
    public ModelRenderer Wing1p4;
    public ModelRenderer Wing1p5;
    public ModelRenderer Wing4;
    public ModelRenderer Wing3p1;
    public ModelRenderer Wing4p1;
    public ModelRenderer Wing4p2;
    public ModelRenderer Wing4p3;
    public ModelRenderer Wing4p4;
    public ModelRenderer Wing4p5;
    public ModelRenderer Wing3p2;
    public ModelRenderer Wing3p3;
    public ModelRenderer Wing3p4;
    public ModelRenderer Wing3p5;

    public ModelRocket() {
        this.textureWidth = 128;
        this.textureHeight = 128;
        this.Wing1 = new ModelRenderer(this, 69, 45);
        this.Wing1.setRotationPoint(-4.0F, 9.0F, 4.0F);
        this.Wing1.addBox(-0.5F, -0.5F, -0.5F, 1, 9, 1);
        this.setRotateAngle(Wing1, 0.0F, 0.7853981633974483F, 0.0F);
        this.Wing1p1 = new ModelRenderer(this, 73, 46);
        this.Wing1p1.setRotationPoint(-1.5F, 0.5F, -0.5F);
        this.Wing1p1.addBox(0.0F, 0.0F, 0.0F, 1, 8, 1);
        this.Wing1.addChild(this.Wing1p1);
        this.Wing1p2 = new ModelRenderer(this, 77, 47);
        this.Wing1p2.setRotationPoint(-1.0F, 1.0F, 0.0F);
        this.Wing1p2.addBox(0.0F, 0.0F, 0.0F, 1, 6, 1);
        this.Wing1p1.addChild(this.Wing1p2);
        this.Wing1p3 = new ModelRenderer(this, 81, 48);
        this.Wing1p3.setRotationPoint(-1.0F, 1.0F, 0.0F);
        this.Wing1p3.addBox(0.0F, 0.0F, 0.0F, 1, 5, 1);
        this.Wing1p2.addChild(this.Wing1p3);
        this.Wing1p4 = new ModelRenderer(this, 85, 49);
        this.Wing1p4.setRotationPoint(-1.0F, 1.0F, 0.0F);
        this.Wing1p4.addBox(0.0F, 0.0F, 0.0F, 1, 3, 1);
        this.Wing1p3.addChild(this.Wing1p4);
        this.Wing1p5 = new ModelRenderer(this, 89, 50);
        this.Wing1p5.setRotationPoint(-1.0F, 1.0F, 0.0F);
        this.Wing1p5.addBox(0.0F, 0.0F, 0.0F, 1, 1, 1);
        this.Wing1p4.addChild(this.Wing1p5);
        this.Wing2 = new ModelRenderer(this, 69, 56);
        this.Wing2.setRotationPoint(11.3F, 0.0F, 0.0F);
        this.Wing2.addBox(-0.5F, -0.5F, -0.5F, 1, 9, 1);
        this.Wing1.addChild(this.Wing2);
        this.Wing2p1 = new ModelRenderer(this, 73, 57);
        this.Wing2p1.setRotationPoint(0.5F, 0.5F, -0.5F);
        this.Wing2p1.addBox(0.0F, 0.0F, 0.0F, 1, 8, 1);
        this.Wing2.addChild(this.Wing2p1);
        this.Wing2p2 = new ModelRenderer(this, 77, 58);
        this.Wing2p2.setRotationPoint(1.0F, 1.0F, 0.0F);
        this.Wing2p2.addBox(0.0F, 0.0F, 0.0F, 1, 6, 1);
        this.Wing2p1.addChild(this.Wing2p2);
        this.Wing2p3 = new ModelRenderer(this, 81, 59);
        this.Wing2p3.setRotationPoint(1.0F, 1.0F, 0.0F);
        this.Wing2p3.addBox(0.0F, 0.0F, 0.0F, 1, 5, 1);
        this.Wing2p2.addChild(this.Wing2p3);
        this.Wing2p4 = new ModelRenderer(this, 85, 60);
        this.Wing2p4.setRotationPoint(1.0F, 1.0F, 0.0F);
        this.Wing2p4.addBox(0.0F, 0.0F, 0.0F, 1, 3, 1);
        this.Wing2p3.addChild(this.Wing2p4);
        this.Wing2p5 = new ModelRenderer(this, 89, 61);
        this.Wing2p5.setRotationPoint(1.0F, 1.0F, 0.0F);
        this.Wing2p5.addBox(0.0F, 0.0F, 0.0F, 1, 1, 1);
        this.Wing2p4.addChild(this.Wing2p5);
        this.Wing3 = new ModelRenderer(this, 69, 67);
        this.Wing3.setRotationPoint(4.0F, 9.0F, 4.0F);
        this.Wing3.addBox(-0.5F, -0.5F, -0.5F, 1, 9, 1);
        this.setRotateAngle(Wing3, 0.0F, 2.356194490192345F, 0.0F);
        this.Wing3p1 = new ModelRenderer(this, 73, 68);
        this.Wing3p1.setRotationPoint(-1.5F, 0.5F, -0.5F);
        this.Wing3p1.addBox(0.0F, 0.0F, 0.0F, 1, 8, 1);
        this.Wing3.addChild(this.Wing3p1);
        this.Wing3p2 = new ModelRenderer(this, 77, 69);
        this.Wing3p2.setRotationPoint(-1.0F, 1.0F, 0.0F);
        this.Wing3p2.addBox(0.0F, 0.0F, 0.0F, 1, 6, 1);
        this.Wing3p1.addChild(this.Wing3p2);
        this.Wing3p3 = new ModelRenderer(this, 81, 70);
        this.Wing3p3.setRotationPoint(-1.0F, 1.0F, 0.0F);
        this.Wing3p3.addBox(0.0F, 0.0F, 0.0F, 1, 5, 1);
        this.Wing3p2.addChild(this.Wing3p3);
        this.Wing3p4 = new ModelRenderer(this, 85, 71);
        this.Wing3p4.setRotationPoint(-1.0F, 1.0F, 0.0F);
        this.Wing3p4.addBox(0.0F, 0.0F, 0.0F, 1, 3, 1);
        this.Wing3p3.addChild(this.Wing3p4);
        this.Wing3p5 = new ModelRenderer(this, 89, 72);
        this.Wing3p5.setRotationPoint(-1.0F, 1.0F, 0.0F);
        this.Wing3p5.addBox(0.0F, 0.0F, 0.0F, 1, 1, 1);
        this.Wing3p4.addChild(this.Wing3p5);
        this.Wing4 = new ModelRenderer(this, 69, 78);
        this.Wing4.setRotationPoint(11.3F, 0.0F, 0.0F);
        this.Wing4.addBox(-0.5F, -0.5F, -0.5F, 1, 9, 1);
        this.Wing3.addChild(this.Wing4);
        this.Wing4p1 = new ModelRenderer(this, 73, 79);
        this.Wing4p1.setRotationPoint(0.5F, 0.5F, -0.5F);
        this.Wing4p1.addBox(0.0F, 0.0F, 0.0F, 1, 8, 1);
        this.Wing4.addChild(this.Wing4p1);
        this.Wing4p2 = new ModelRenderer(this, 77, 80);
        this.Wing4p2.setRotationPoint(1.0F, 1.0F, 0.0F);
        this.Wing4p2.addBox(0.0F, 0.0F, 0.0F, 1, 6, 1);
        this.Wing4p1.addChild(this.Wing4p2);
        this.Wing4p3 = new ModelRenderer(this, 81, 81);
        this.Wing4p3.setRotationPoint(1.0F, 1.0F, 0.0F);
        this.Wing4p3.addBox(0.0F, 0.0F, 0.0F, 1, 5, 1);
        this.Wing4p2.addChild(this.Wing4p3);
        this.Wing4p4 = new ModelRenderer(this, 85, 82);
        this.Wing4p4.setRotationPoint(1.0F, 1.0F, 0.0F);
        this.Wing4p4.addBox(0.0F, 0.0F, 0.0F, 1, 3, 1);
        this.Wing4p3.addChild(this.Wing4p4);
        this.Wing4p5 = new ModelRenderer(this, 89, 83);
        this.Wing4p5.setRotationPoint(1.0F, 1.0F, 0.0F);
        this.Wing4p5.addBox(0.0F, 0.0F, 0.0F, 1, 1, 1);
        this.Wing4p4.addChild(this.Wing4p5);
        this.Cone1 = new ModelRenderer(this, 8, 48);
        this.Cone1.setRotationPoint(-3.0F, -11.0F, -3.0F);
        this.Cone1.addBox(0.0F, 0.0F, 0.0F, 6, 2, 6);
        this.Cone2 = new ModelRenderer(this, 12, 42);
        this.Cone2.setRotationPoint(1.0F, -2.0F, 1.0F);
        this.Cone2.addBox(0.0F, 0.0F, 0.0F, 4, 2, 4);
        this.Cone1.addChild(this.Cone2);
        this.Cone3 = new ModelRenderer(this, 16, 36);
        this.Cone3.setRotationPoint(1.0F, -4.0F, 1.0F);
        this.Cone3.addBox(0.0F, 0.0F, 0.0F, 2, 4, 2);
        this.Cone2.addChild(this.Cone3);
        this.Antenna = new ModelRenderer(this, 18, 27);
        this.Antenna.setRotationPoint(0.5F, -8.0F, 0.5F);
        this.Antenna.addBox(0.0F, 0.0F, 0.0F, 1, 8, 1);
        this.Cone3.addChild(this.Antenna);
        this.Base = new ModelRenderer(this, 10, 66);
        this.Base.setRotationPoint(-3.0F, 17.0F, -3.0F);
        this.Base.addBox(0.0F, 0.0F, 0.0F, 6, 1, 6);
        this.Engine1 = new ModelRenderer(this, 18, 73);
        this.Engine1.setRotationPoint(2.0F, 1.0F, 2.0F);
        this.Engine1.addBox(0.0F, 0.0F, 0.0F, 2, 1, 2);
        this.Base.addChild(this.Engine1);
        this.Engine2 = new ModelRenderer(this, 14, 76);
        this.Engine2.setRotationPoint(-1.0F, 1.0F, -1.0F);
        this.Engine2.addBox(0.0F, 0.0F, 0.0F, 4, 1, 4);
        this.Engine1.addChild(this.Engine2);
        this.Engine3 = new ModelRenderer(this, 10, 81);
        this.Engine3.setRotationPoint(-1.0F, 1.0F, -1.0F);
        this.Engine3.addBox(0.0F, 0.0F, 0.0F, 6, 2, 6);
        this.Engine2.addChild(this.Engine3);
        this.Engine4 = new ModelRenderer(this, 6, 89);
        this.Engine4.setRotationPoint(-1.0F, 2.0F, -1.0F);
        this.Engine4.addBox(0.0F, 0.0F, 0.0F, 8, 2, 8);
        this.Engine3.addChild(this.Engine4);
        this.Wall1 = new ModelRenderer(this, 68, 6);
        this.Wall1.setRotationPoint(-3.0F, -9.0F, -4.0F);
        this.Wall1.addBox(0.0F, 0.0F, 0.0F, 7, 27, 1);
        this.Wall2 = new ModelRenderer(this, 52, 0);
        this.Wall2.setRotationPoint(-1.0F, 0.0F, 0.0F);
        this.Wall2.addBox(0.0F, 0.0F, 0.0F, 1, 27, 7);
        this.Wall1.addChild(this.Wall2);
        this.Wall3 = new ModelRenderer(this, 100, 6);
        this.Wall3.setRotationPoint(0.0F, 0.0F, 7.0F);
        this.Wall3.addBox(0.0F, 0.0F, 0.0F, 7, 27, 1);
        this.Wall2.addChild(this.Wall3);
        this.Wall4 = new ModelRenderer(this, 84, 0);
        this.Wall4.setRotationPoint(7.0F, 0.0F, -6.0F);
        this.Wall4.addBox(0.0F, 0.0F, 0.0F, 1, 27, 7);
        this.Wall3.addChild(this.Wall4);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        GL11.glRotatef(180F, 0F, 0F, 0F);
        this.Wing1.render(f5);
        this.Wall1.render(f5);
        this.Base.render(f5);
        this.Cone1.render(f5);
        this.Wing3.render(f5);
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
