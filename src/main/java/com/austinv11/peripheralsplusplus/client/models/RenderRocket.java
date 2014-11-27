package com.austinv11.peripheralsplusplus.client.models;

import com.austinv11.peripheralsplusplus.entities.EntityRocket;
import com.austinv11.peripheralsplusplus.reference.Reference;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderRocket extends RenderLiving {

	public RenderRocket(ModelBase par1ModelBase, float par2) {
		super(par1ModelBase, par2);
	}

	public void renderRocket(EntityRocket par1EntityRocket, double par2, double par4, double par6, float par8, float par9) {
		super.doRender(par1EntityRocket, par2, par4, par6, par8, par9);
	}

	@Override
	public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
		this.renderRocket((EntityRocket)par1Entity, par2, par4, par6, par8, par9);
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
		return new ResourceLocation(Reference.MOD_ID.toLowerCase()+":textures/models/rocketModel.png");
	}
}
