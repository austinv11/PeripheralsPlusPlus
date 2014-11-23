package com.austinv11.peripheralsplusplus.client.models;

import com.austinv11.peripheralsplusplus.MCACommonLibrary.MCAVersionChecker;
import com.austinv11.peripheralsplusplus.MCACommonLibrary.math.Matrix4f;
import com.austinv11.peripheralsplusplus.MCACommonLibrary.math.Quaternion;
import com.austinv11.peripheralsplusplus.client.MCAClientLibrary.MCAModelRenderer;
import com.austinv11.peripheralsplusplus.entities.EntityRocket;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;

import java.util.HashMap;

public class ModelRocket extends ModelBase {
public final int MCA_MIN_REQUESTED_VERSION = 1;
public HashMap<String, MCAModelRenderer> parts = new HashMap<String, MCAModelRenderer>();

MCAModelRenderer wall1;
MCAModelRenderer engineBase;
MCAModelRenderer coneBase;
MCAModelRenderer wings;
MCAModelRenderer wall2;
MCAModelRenderer engine1;
MCAModelRenderer cone1;
MCAModelRenderer wing1;
MCAModelRenderer wing2;
MCAModelRenderer wing3;
MCAModelRenderer wall3;
MCAModelRenderer engine2;
MCAModelRenderer cone2;
MCAModelRenderer wing1p1;
MCAModelRenderer wing2p1;
MCAModelRenderer wing3p1;
MCAModelRenderer wing4;
MCAModelRenderer wall4;
MCAModelRenderer engine3;
MCAModelRenderer antenna;
MCAModelRenderer wing1p2;
MCAModelRenderer wing2p2;
MCAModelRenderer wing3p2;
MCAModelRenderer wing4p1;
MCAModelRenderer engine4;
MCAModelRenderer wing1p3;
MCAModelRenderer wing2p3;
MCAModelRenderer wing3p3;
MCAModelRenderer wing4p2;
MCAModelRenderer wing1p4;
MCAModelRenderer wing2p4;
MCAModelRenderer wing3p4;
MCAModelRenderer wing4p3;
MCAModelRenderer wing2p5;
MCAModelRenderer wing3p5;
MCAModelRenderer wing4p4;
MCAModelRenderer wing4p5;
public ModelRocket()
{
MCAVersionChecker.checkForLibraryVersion(getClass(), MCA_MIN_REQUESTED_VERSION);

textureWidth = 128;
textureHeight = 128;

wall1 = new MCAModelRenderer(this, "Wall1", 112, 93);
wall1.mirror = false;
wall1.addBox(0.0F, 0.0F, 0.0F, 1, 28, 7);
wall1.setInitialRotationPoint(-13.0F, -20.0F, 0.0F);
wall1.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
wall1.setTextureSize(128, 128);
parts.put(wall1.boxName, wall1);

engineBase = new MCAModelRenderer(this, "EngineBase", 100, 44);
engineBase.mirror = false;
engineBase.addBox(0.0F, 0.0F, 0.0F, 6, 1, 6);
engineBase.setInitialRotationPoint(-12.0F, -20.0F, 0.0F);
engineBase.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
engineBase.setTextureSize(128, 128);
parts.put(engineBase.boxName, engineBase);

coneBase = new MCAModelRenderer(this, "ConeBase", 12, 87);
coneBase.mirror = false;
coneBase.addBox(0.0F, 0.0F, 0.0F, 6, 2, 6);
coneBase.setInitialRotationPoint(-12.0F, 8.0F, 0.0F);
coneBase.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
coneBase.setTextureSize(128, 128);
parts.put(coneBase.boxName, coneBase);

wings = new MCAModelRenderer(this, "Wings", 35, 12);
wings.mirror = false;
wings.addBox(0.0F, 0.0F, 0.0F, 1, 9, 1);
wings.setInitialRotationPoint(-5.0F, -19.0F, 6.25F);
wings.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, -0.38268346F, 0.0F, 0.9238795F)).transpose());
wings.setTextureSize(128, 128);
parts.put(wings.boxName, wings);

wall2 = new MCAModelRenderer(this, "Wall2", 96, 99);
wall2.mirror = false;
wall2.addBox(0.0F, 0.0F, 0.0F, 7, 28, 1);
wall2.setInitialRotationPoint(0.0F, 0.0F, -1.0F);
wall2.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
wall2.setTextureSize(128, 128);
parts.put(wall2.boxName, wall2);
wall1.addChild(wall2);

engine1 = new MCAModelRenderer(this, "Engine1", 108, 36);
engine1.mirror = false;
engine1.addBox(0.0F, 0.0F, 0.0F, 2, 1, 2);
engine1.setInitialRotationPoint(2.0F, -1.0F, 2.0F);
engine1.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
engine1.setTextureSize(128, 128);
parts.put(engine1.boxName, engine1);
engineBase.addChild(engine1);

cone1 = new MCAModelRenderer(this, "Cone1", 15, 81);
cone1.mirror = false;
cone1.addBox(0.0F, 0.0F, 0.0F, 4, 2, 4);
cone1.setInitialRotationPoint(1.0F, 2.0F, 1.0F);
cone1.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
cone1.setTextureSize(128, 128);
parts.put(cone1.boxName, cone1);
coneBase.addChild(cone1);

wing1 = new MCAModelRenderer(this, "Wing1", 39, 13);
wing1.mirror = false;
wing1.addBox(0.0F, 0.0F, 0.0F, 1, 8, 1);
wing1.setInitialRotationPoint(1.0F, 0.0F, 0.0F);
wing1.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
wing1.setTextureSize(128, 128);
parts.put(wing1.boxName, wing1);
wings.addChild(wing1);

wing2 = new MCAModelRenderer(this, "Wing2", 35, 12);
wing2.mirror = true;
wing2.addBox(0.0F, 0.0F, 0.0F, 1, 9, 1);
wing2.setInitialRotationPoint(-11.0F, 0.0F, 0.0F);
wing2.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
wing2.setTextureSize(128, 128);
parts.put(wing2.boxName, wing2);
wings.addChild(wing2);

wing3 = new MCAModelRenderer(this, "Wing3", 35, 12);
wing3.mirror = false;
wing3.addBox(0.0F, 0.0F, 0.0F, 1, 9, 1);
wing3.setInitialRotationPoint(-5.65F, 0.0F, -4.5F);
wing3.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.70710677F, 0.0F, 0.70710677F)).transpose());
wing3.setTextureSize(128, 128);
parts.put(wing3.boxName, wing3);
wings.addChild(wing3);

wall3 = new MCAModelRenderer(this, "Wall3", 112, 93);
wall3.mirror = false;
wall3.addBox(0.0F, 0.0F, 0.0F, 1, 28, 7);
wall3.setInitialRotationPoint(7.0F, 0.0F, 0.0F);
wall3.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
wall3.setTextureSize(128, 128);
parts.put(wall3.boxName, wall3);
wall2.addChild(wall3);

engine2 = new MCAModelRenderer(this, "Engine2", 104, 39);
engine2.mirror = false;
engine2.addBox(0.0F, 0.0F, 0.0F, 4, 1, 4);
engine2.setInitialRotationPoint(-1.0F, -1.0F, -1.0F);
engine2.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
engine2.setTextureSize(128, 128);
parts.put(engine2.boxName, engine2);
engine1.addChild(engine2);

cone2 = new MCAModelRenderer(this, "Cone2", 19, 75);
cone2.mirror = false;
cone2.addBox(0.0F, 0.0F, 0.0F, 2, 4, 2);
cone2.setInitialRotationPoint(1.0F, 2.0F, 1.0F);
cone2.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
cone2.setTextureSize(128, 128);
parts.put(cone2.boxName, cone2);
cone1.addChild(cone2);

wing1p1 = new MCAModelRenderer(this, "Wing1p1", 43, 14);
wing1p1.mirror = false;
wing1p1.addBox(0.0F, 0.0F, 0.0F, 1, 6, 1);
wing1p1.setInitialRotationPoint(1.0F, 1.0F, 0.0F);
wing1p1.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
wing1p1.setTextureSize(128, 128);
parts.put(wing1p1.boxName, wing1p1);
wing1.addChild(wing1p1);

wing2p1 = new MCAModelRenderer(this, "Wing2p1", 39, 13);
wing2p1.mirror = true;
wing2p1.addBox(0.0F, 0.0F, 0.0F, 1, 8, 1);
wing2p1.setInitialRotationPoint(-1.0F, 0.0F, 0.0F);
wing2p1.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
wing2p1.setTextureSize(128, 128);
parts.put(wing2p1.boxName, wing2p1);
wing2.addChild(wing2p1);

wing3p1 = new MCAModelRenderer(this, "Wing3p1", 39, 13);
wing3p1.mirror = false;
wing3p1.addBox(0.0F, 0.0F, 0.0F, 1, 8, 1);
wing3p1.setInitialRotationPoint(1.0F, 0.0F, 0.0F);
wing3p1.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
wing3p1.setTextureSize(128, 128);
parts.put(wing3p1.boxName, wing3p1);
wing3.addChild(wing3p1);

wing4 = new MCAModelRenderer(this, "Wing4", 35, 12);
wing4.mirror = true;
wing4.addBox(0.0F, 0.0F, 0.0F, 1, 9, 1);
wing4.setInitialRotationPoint(-11.0F, 0.0F, 0.0F);
wing4.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
wing4.setTextureSize(128, 128);
parts.put(wing4.boxName, wing4);
wing3.addChild(wing4);

wall4 = new MCAModelRenderer(this, "Wall4", 96, 99);
wall4.mirror = false;
wall4.addBox(0.0F, 0.0F, 0.0F, 7, 28, 1);
wall4.setInitialRotationPoint(-6.0F, 0.0F, 7.0F);
wall4.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
wall4.setTextureSize(128, 128);
parts.put(wall4.boxName, wall4);
wall3.addChild(wall4);

engine3 = new MCAModelRenderer(this, "Engine3", 76, 44);
engine3.mirror = false;
engine3.addBox(0.0F, 0.0F, 0.0F, 6, 2, 6);
engine3.setInitialRotationPoint(-1.0F, -2.0F, -1.0F);
engine3.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
engine3.setTextureSize(128, 128);
parts.put(engine3.boxName, engine3);
engine2.addChild(engine3);

antenna = new MCAModelRenderer(this, "Antenna", 21, 67);
antenna.mirror = false;
antenna.addBox(0.0F, 0.0F, 0.0F, 1, 7, 1);
antenna.setInitialRotationPoint(0.5F, 4.0F, 0.5F);
antenna.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
antenna.setTextureSize(128, 128);
parts.put(antenna.boxName, antenna);
cone2.addChild(antenna);

wing1p2 = new MCAModelRenderer(this, "Wing1p2", 47, 15);
wing1p2.mirror = false;
wing1p2.addBox(0.0F, 0.0F, 0.0F, 1, 5, 1);
wing1p2.setInitialRotationPoint(1.0F, 0.0F, 0.0F);
wing1p2.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
wing1p2.setTextureSize(128, 128);
parts.put(wing1p2.boxName, wing1p2);
wing1p1.addChild(wing1p2);

wing2p2 = new MCAModelRenderer(this, "Wing2p2", 43, 14);
wing2p2.mirror = true;
wing2p2.addBox(0.0F, 0.0F, 0.0F, 1, 6, 1);
wing2p2.setInitialRotationPoint(-1.0F, 1.0F, 0.0F);
wing2p2.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
wing2p2.setTextureSize(128, 128);
parts.put(wing2p2.boxName, wing2p2);
wing2p1.addChild(wing2p2);

wing3p2 = new MCAModelRenderer(this, "Wing3p2", 43, 14);
wing3p2.mirror = false;
wing3p2.addBox(0.0F, 0.0F, 0.0F, 1, 6, 1);
wing3p2.setInitialRotationPoint(1.0F, 1.0F, 0.0F);
wing3p2.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
wing3p2.setTextureSize(128, 128);
parts.put(wing3p2.boxName, wing3p2);
wing3p1.addChild(wing3p2);

wing4p1 = new MCAModelRenderer(this, "Wing4p1", 39, 13);
wing4p1.mirror = true;
wing4p1.addBox(0.0F, 0.0F, 0.0F, 1, 8, 1);
wing4p1.setInitialRotationPoint(-1.0F, 0.0F, 0.0F);
wing4p1.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
wing4p1.setTextureSize(128, 128);
parts.put(wing4p1.boxName, wing4p1);
wing4.addChild(wing4p1);

engine4 = new MCAModelRenderer(this, "Engine4", 96, 52);
engine4.mirror = false;
engine4.addBox(0.0F, 0.0F, 0.0F, 8, 3, 8);
engine4.setInitialRotationPoint(-1.0F, -3.0F, -1.0F);
engine4.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
engine4.setTextureSize(128, 128);
parts.put(engine4.boxName, engine4);
engine3.addChild(engine4);

wing1p3 = new MCAModelRenderer(this, "Wing1p3", 51, 16);
wing1p3.mirror = false;
wing1p3.addBox(0.0F, 0.0F, 0.0F, 1, 3, 1);
wing1p3.setInitialRotationPoint(1.0F, 1.0F, 0.0F);
wing1p3.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
wing1p3.setTextureSize(128, 128);
parts.put(wing1p3.boxName, wing1p3);
wing1p2.addChild(wing1p3);

wing2p3 = new MCAModelRenderer(this, "Wing2p3", 47, 15);
wing2p3.mirror = true;
wing2p3.addBox(0.0F, 0.0F, 0.0F, 1, 5, 1);
wing2p3.setInitialRotationPoint(-1.0F, 0.0F, 0.0F);
wing2p3.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
wing2p3.setTextureSize(128, 128);
parts.put(wing2p3.boxName, wing2p3);
wing2p2.addChild(wing2p3);

wing3p3 = new MCAModelRenderer(this, "Wing3p3", 47, 15);
wing3p3.mirror = false;
wing3p3.addBox(0.0F, 0.0F, 0.0F, 1, 5, 1);
wing3p3.setInitialRotationPoint(1.0F, 0.0F, 0.0F);
wing3p3.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
wing3p3.setTextureSize(128, 128);
parts.put(wing3p3.boxName, wing3p3);
wing3p2.addChild(wing3p3);

wing4p2 = new MCAModelRenderer(this, "Wing4p2", 43, 14);
wing4p2.mirror = true;
wing4p2.addBox(0.0F, 0.0F, 0.0F, 1, 6, 1);
wing4p2.setInitialRotationPoint(-1.0F, 1.0F, 0.0F);
wing4p2.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
wing4p2.setTextureSize(128, 128);
parts.put(wing4p2.boxName, wing4p2);
wing4p1.addChild(wing4p2);

wing1p4 = new MCAModelRenderer(this, "Wing1p4", 55, 17);
wing1p4.mirror = false;
wing1p4.addBox(0.0F, 0.0F, 0.0F, 1, 1, 1);
wing1p4.setInitialRotationPoint(1.0F, 1.0F, 0.0F);
wing1p4.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
wing1p4.setTextureSize(128, 128);
parts.put(wing1p4.boxName, wing1p4);
wing1p3.addChild(wing1p4);

wing2p4 = new MCAModelRenderer(this, "Wing2p4", 51, 16);
wing2p4.mirror = true;
wing2p4.addBox(0.0F, 0.0F, 0.0F, 1, 3, 1);
wing2p4.setInitialRotationPoint(-1.0F, 1.0F, 0.0F);
wing2p4.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
wing2p4.setTextureSize(128, 128);
parts.put(wing2p4.boxName, wing2p4);
wing2p3.addChild(wing2p4);

wing3p4 = new MCAModelRenderer(this, "Wing3p4", 51, 16);
wing3p4.mirror = false;
wing3p4.addBox(0.0F, 0.0F, 0.0F, 1, 3, 1);
wing3p4.setInitialRotationPoint(1.0F, 1.0F, 0.0F);
wing3p4.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
wing3p4.setTextureSize(128, 128);
parts.put(wing3p4.boxName, wing3p4);
wing3p3.addChild(wing3p4);

wing4p3 = new MCAModelRenderer(this, "Wing4p3", 47, 15);
wing4p3.mirror = true;
wing4p3.addBox(0.0F, 0.0F, 0.0F, 1, 5, 1);
wing4p3.setInitialRotationPoint(-1.0F, 0.0F, 0.0F);
wing4p3.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
wing4p3.setTextureSize(128, 128);
parts.put(wing4p3.boxName, wing4p3);
wing4p2.addChild(wing4p3);

wing2p5 = new MCAModelRenderer(this, "Wing2p5", 55, 17);
wing2p5.mirror = true;
wing2p5.addBox(0.0F, 0.0F, 0.0F, 1, 1, 1);
wing2p5.setInitialRotationPoint(-1.0F, 1.0F, 0.0F);
wing2p5.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
wing2p5.setTextureSize(128, 128);
parts.put(wing2p5.boxName, wing2p5);
wing2p4.addChild(wing2p5);

wing3p5 = new MCAModelRenderer(this, "Wing3p5", 55, 17);
wing3p5.mirror = false;
wing3p5.addBox(0.0F, 0.0F, 0.0F, 1, 1, 1);
wing3p5.setInitialRotationPoint(1.0F, 1.0F, 0.0F);
wing3p5.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
wing3p5.setTextureSize(128, 128);
parts.put(wing3p5.boxName, wing3p5);
wing3p4.addChild(wing3p5);

wing4p4 = new MCAModelRenderer(this, "Wing4p4", 51, 16);
wing4p4.mirror = true;
wing4p4.addBox(0.0F, 0.0F, 0.0F, 1, 3, 1);
wing4p4.setInitialRotationPoint(-1.0F, 1.0F, 0.0F);
wing4p4.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
wing4p4.setTextureSize(128, 128);
parts.put(wing4p4.boxName, wing4p4);
wing4p3.addChild(wing4p4);

wing4p5 = new MCAModelRenderer(this, "Wing4p5", 55, 17);
wing4p5.mirror = false;
wing4p5.addBox(0.0F, 0.0F, 0.0F, 1, 1, 1);
wing4p5.setInitialRotationPoint(-1.0F, 1.0F, 0.0F);
wing4p5.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
wing4p5.setTextureSize(128, 128);
parts.put(wing4p5.boxName, wing4p5);
wing4p4.addChild(wing4p5);

}

@Override
public void render(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7) 
{
EntityRocket entity = (EntityRocket)par1Entity;

//Render every non-child part
wall1.render(par7);
engineBase.render(par7);
coneBase.render(par7);
wings.render(par7);

//AnimationHandler.performAnimationInModel(parts, entity);
}
@Override
public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity par7Entity) {}

public MCAModelRenderer getModelRendererFromName(String name)
{
return parts.get(name) != null ? parts.get(name) : null;
}
}
