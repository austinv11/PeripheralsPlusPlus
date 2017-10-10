package com.austinv11.peripheralsplusplus.client.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/*
	Created with the help of Techne Modeler.
*/

@SideOnly(Side.CLIENT)
public class ModelRidableTurtle extends ModelBase {

	public ModelRenderer box;

	public ModelRidableTurtle() {
		this.textureWidth = 64;
		this.textureHeight = 32;
		box = new ModelRenderer(this, 0, 0);
		box.addBox(0F, 0F, 0F, 16, 16, 16);
		box.setRotationPoint(0F, 0F, 0F);
		box.setTextureSize(64, 32);
		box.mirror = true;
		setRotation(box, 0F, 0F, 0F);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		box.render(f5);
	}
}
