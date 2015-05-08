package com.austinv11.peripheralsplusplus.client.models;

import com.austinv11.peripheralsplusplus.entities.EntityRocket;
import com.austinv11.peripheralsplusplus.reference.Reference;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderRocket extends Render {

	protected ModelBase model;

	public RenderRocket() {
		this.shadowSize = 0.75F;
		model = new ModelRocket();
	}

	public void renderRocket(EntityRocket entityRocket, double x, double y, double z, float yaw, float pitch) {
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_CULL_FACE);
		this.bindEntityTexture(entityRocket);
		GL11.glTranslated(x, y+2.41D, z);
		if (!entityRocket.isFlipped)
			GL11.glRotatef(180F, 0F,0F,0F);
		model.render(entityRocket, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.1F);
		GL11.glPopMatrix();
	}

	@Override
	public void doRender(Entity entity, double x, double y, double z, float yaw, float pitch) {
		this.renderRocket((EntityRocket)entity, x, y, z, yaw, pitch);
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return new ResourceLocation(Reference.MOD_ID.toLowerCase()+":textures/models/rocketModel.png");
	}
}
