package com.austinv11.peripheralsplusplus.client.models;

import com.austinv11.peripheralsplusplus.entities.EntityRidableTurtle;
import com.austinv11.peripheralsplusplus.reference.Reference;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;

@SideOnly(Side.CLIENT)
public class RenderRidableTurtle extends Render<EntityRidableTurtle> {

	private final ModelBase model;
	private float scale;

	public RenderRidableTurtle(RenderManager renderManager) {
		super(renderManager);
		model = new ModelRidableTurtle();
		scale = 1f / 16f;
	}

	@Override
	public void doRender(EntityRidableTurtle entity, double x, double y, double z, float entityYaw,
						 float partialTicks) {
		GL11.glPushMatrix();
		this.bindEntityTexture(entity);
		GL11.glTranslated(x - 0.5, y, z - 0.5);
		model.render(entity, 0, 0, 0, 0, 0, scale);
		GL11.glPopMatrix();
	}

	@Nullable
	@Override
	protected ResourceLocation getEntityTexture(EntityRidableTurtle entity) {
		return new ResourceLocation(Reference.MOD_ID.toLowerCase() +
				":textures/models/ridable_turtle.png");
	}
}
