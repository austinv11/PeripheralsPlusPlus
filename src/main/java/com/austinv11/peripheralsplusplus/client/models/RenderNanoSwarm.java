package com.austinv11.peripheralsplusplus.client.models;

import com.austinv11.peripheralsplusplus.reference.Reference;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderNanoSwarm extends Render {
	
	protected ModelNanoBotSwarm model = new ModelNanoBotSwarm();
	
	@Override
	public void doRender(Entity entity, double x, double y, double z, float yaw, float pitch) {
		GL11.glPushMatrix();
		this.bindEntityTexture(entity);
		GL11.glTranslated(x, y, z);
		GL11.glRotatef(entity.rotationYaw, 0, 1, 0);
		model.render(entity, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.1F);
		GL11.glPopMatrix();
	}
	
	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return new ResourceLocation(Reference.MOD_ID.toLowerCase()+":textures/models/nanoBotSwarm.png");
	}
}
