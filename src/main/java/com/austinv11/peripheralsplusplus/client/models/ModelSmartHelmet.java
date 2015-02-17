package com.austinv11.peripheralsplusplus.client.models;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class ModelSmartHelmet extends ModelBiped {

	public ModelSmartHelmet() {
		super(1.0F);
	}

	@Override //Taken from ModelBiped, only enabled blend, disabled rendering anything but the helmet and added sneak detection
	public void render(Entity p_78088_1_, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float p_78088_7_)
	{
		this.isSneak = p_78088_1_.isSneaking();
		this.setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_, p_78088_1_);
		if (this.isChild)
		{
			float f6 = 2.0F;
			GL11.glPushMatrix();
			GL11.glScalef(1.5F/f6, 1.5F/f6, 1.5F/f6);
			GL11.glTranslatef(0.0F, 16.0F*p_78088_7_, 0.0F);
			this.bipedHead.render(p_78088_7_);
			GL11.glPopMatrix();
			GL11.glPushMatrix();
			GL11.glScalef(1.0F / f6, 1.0F / f6, 1.0F / f6);
			GL11.glTranslatef(0.0F, 24.0F * p_78088_7_, 0.0F);
			this.bipedBody.render(p_78088_7_);
			this.bipedRightArm.render(p_78088_7_);
			this.bipedLeftArm.render(p_78088_7_);
			this.bipedRightLeg.render(p_78088_7_);
			this.bipedLeftLeg.render(p_78088_7_);
			this.bipedHeadwear.render(p_78088_7_);
			GL11.glPopMatrix();
		}
		else
		{
			GL11.glEnable(GL11.GL_BLEND);
			this.bipedHead.render(p_78088_7_);
//			this.bipedBody.render(p_78088_7_);
//			this.bipedRightArm.render(p_78088_7_);
//			this.bipedLeftArm.render(p_78088_7_);
//			this.bipedRightLeg.render(p_78088_7_);
//			this.bipedLeftLeg.render(p_78088_7_);
//			this.bipedHeadwear.render(p_78088_7_);
			GL11.glDisable(GL11.GL_BLEND);
		}
	}
}
