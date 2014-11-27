package com.austinv11.peripheralsplusplus.client.models;

import com.austinv11.peripheralsplusplus.entities.EntityRocket;
import com.austinv11.peripheralsplusplus.reference.Reference;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class RenderRocket extends Render {

	protected ModelBase model;

	public RenderRocket() {
		this.shadowSize = 2F;
		model = new ModelRocket();
	}

	public void renderRocket(EntityRocket par1EntityRocket, double par2, double par4, double par6, float par8, float par9) {
		this.bindEntityTexture(par1EntityRocket);
		model.render(par1EntityRocket, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
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
