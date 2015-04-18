package com.austinv11.peripheralsplusplus.client.models;

import com.austinv11.peripheralsplusplus.reference.Reference;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderRidableTurtle extends Render {

	private final ModelBase model;

	public RenderRidableTurtle() {
		model = new ModelRidableTurtle();
	}

	@Override
	public void doRender(Entity entity, double x, double y, double z, float yaw, float pitch) {
		GL11.glPushMatrix();
		this.bindEntityTexture(entity);
		GL11.glTranslated(x - 0.5, y, z - 0.5);
		model.render(entity, 0F, 0F, 0F, 0F, 0F, 0.0625F);
		GL11.glPopMatrix();
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return new ResourceLocation(Reference.MOD_ID.toLowerCase() + ":textures/models/ridableTurtleModel.png");
	}
}
