package com.austinv11.peripheralsplusplus.client.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Turtle - The_Real_Fx
 * Created using Tabula 4.0.2
 */
@SideOnly(Side.CLIENT)
public class ModelTurtle extends ModelBase {
	public ModelRenderer botShield1;
	public ModelRenderer midShield;
	public ModelRenderer topShield;
	public ModelRenderer botShield2;
	public ModelRenderer head;
	public ModelRenderer frontLeftLeg;
	public ModelRenderer backLeftLeg;
	public ModelRenderer frontRightLeg;
	public ModelRenderer backRightLeg;
	public ModelRenderer tail;
	
	public ModelTurtle() {
		this.textureWidth = 128;
		this.textureHeight = 64;
		this.head = new ModelRenderer(this, 20, 0);
		this.head.setRotationPoint(-3.0F, 16.0F, -17.0F);
		this.head.addBox(0.0F, 0.0F, 0.0F, 6, 4, 8);
		this.botShield2 = new ModelRenderer(this, 0, 24);
		this.botShield2.setRotationPoint(-8.0F, 16.0F, -8.0F);
		this.botShield2.addBox(0.0F, 0.0F, 0.0F, 16, 4, 16);
		this.botShield1 = new ModelRenderer(this, 56, 0);
		this.botShield1.setRotationPoint(-9.0F, 16.0F, -9.0F);
		this.botShield1.addBox(0.0F, 0.0F, 0.0F, 18, 2, 18);
		this.frontRightLeg = new ModelRenderer(this, 0, 18);
		this.frontRightLeg.setRotationPoint(-9.0F, 18.0F, -9.0F);
		this.frontRightLeg.addBox(0.0F, 0.0F, 0.0F, 4, 6, 4);
		this.backRightLeg = new ModelRenderer(this, 0, 18);
		this.backRightLeg.setRotationPoint(-9.0F, 18.0F, 5.0F);
		this.backRightLeg.addBox(0.0F, 0.0F, 0.0F, 4, 6, 4);
		this.tail = new ModelRenderer(this, 0, 10);
		this.tail.setRotationPoint(-2.0F, 18.0F, 8.0F);
		this.tail.addBox(0.0F, 0.0F, 0.0F, 4, 2, 6);
		this.frontLeftLeg = new ModelRenderer(this, 0, 0);
		this.frontLeftLeg.setRotationPoint(5.0F, 18.0F, -9.0F);
		this.frontLeftLeg.addBox(0.0F, 0.0F, 0.0F, 4, 6, 4);
		this.midShield = new ModelRenderer(this, 0, 44);
		this.midShield.setRotationPoint(-8.0F, 12.0F, -8.0F);
		this.midShield.addBox(0.0F, 0.0F, 0.0F, 16, 4, 16);
		this.backLeftLeg = new ModelRenderer(this, 0, 0);
		this.backLeftLeg.setRotationPoint(5.0F, 18.0F, 5.0F);
		this.backLeftLeg.addBox(0.0F, 0.0F, 0.0F, 4, 6, 4);
		this.topShield = new ModelRenderer(this, 80, 20);
		this.topShield.setRotationPoint(-6.0F, 10.0F, -6.0F);
		this.topShield.addBox(0.0F, 0.0F, 0.0F, 12, 2, 12);
	}
	
	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
//		setRotateAngle(head, 0, f4, 0);
		this.head.render(f5);
//		setRotateAngle(botShield2, 0, f4, 0);
		this.botShield2.render(f5);
//		setRotateAngle(botShield1, 0, f4, 0);
		this.botShield1.render(f5);
//		setRotateAngle(frontRightLeg, 0, f4, 0);
		this.frontRightLeg.render(f5);
//		setRotateAngle(backRightLeg, 0, f4, 0);
		this.backRightLeg.render(f5);
//		setRotateAngle(tail, 0, f4, 0);
		this.tail.render(f5);
//		setRotateAngle(frontLeftLeg, 0, f4, 0);
		this.frontLeftLeg.render(f5);
//		setRotateAngle(midShield, 0, f4, 0);
		this.midShield.render(f5);
//		setRotateAngle(backLeftLeg, 0, f4, 0);
		this.backLeftLeg.render(f5);
//		setRotateAngle(topShield, 0, f4, 0);
		this.topShield.render(f5);
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
