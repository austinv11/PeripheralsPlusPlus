package com.austinv11.peripheralsplusplus.client.models;

import com.austinv11.peripheralsplusplus.init.ModItems;
import com.austinv11.peripheralsplusplus.reference.Reference;
import com.austinv11.peripheralsplusplus.tiles.TileEntityTurtle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderTurtle extends TileEntitySpecialRenderer<TileEntityTurtle> {
	
	private ModelTurtle model = new ModelTurtle();

	@Override
	public void render(TileEntityTurtle te, double x, double y, double z, float partialTicks, int destroyStage,
					   float alpha) {
		GL11.glPushMatrix();
		GL11.glTranslatef((float) x+.5F, (float) y+1.2F, (float) z+.5F);
		GL11.glRotatef(180F, 0, 0, 1);
		GL11.glRotatef(getRotate(te == RenderTurtleItem.turtle ? 0 : te.getBlockMetadata()), 0, 1, 0);
		GL11.glScalef(0.5F, 0.5F, 0.5F);
		//GL11.glRotatef(tileEntity.getBlockMetadata() * (-90), 0.0F, 0.0F, 1.0F);
		Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(Reference.MOD_ID.toLowerCase()+":textures/models/turtle.png"));
		model.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.1F);
		GL11.glPopMatrix();
	}

	private float getRotate(int meta) {
		switch (EnumFacing.getFront(meta)) {
			case NORTH:
				return 0.0F;//0
			case SOUTH:
				return 180F;//180
			case EAST:
				return 90F;//90
			case WEST:
				return 270F;//270
			default:
				return 180F;//180
		}
	}

	public static class RenderTurtleItem extends TileEntityItemStackRenderer {
		private static TileEntityTurtle turtle = new TileEntityTurtle();
		private final TileEntityItemStackRenderer previousInstance;

		public RenderTurtleItem(TileEntityItemStackRenderer instance) {
			this.previousInstance = instance;
		}

		@Override
		public void renderByItem(ItemStack stack, float partialTicks) {
			if (!stack.getItem().equals(ModItems.TURTLE)) {
				previousInstance.renderByItem(stack, partialTicks);
				return;
			}
			TileEntityRendererDispatcher.instance.render(turtle, 0, 0, 0, partialTicks);
		}
	}
}
