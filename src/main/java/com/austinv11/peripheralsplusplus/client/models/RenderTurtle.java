package com.austinv11.peripheralsplusplus.client.models;

import com.austinv11.peripheralsplusplus.reference.Reference;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderTurtle extends TileEntitySpecialRenderer {
	
	private ModelTurtle model = new ModelTurtle();
	
	@Override
	public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float f) {
		GL11.glPushMatrix();
		GL11.glTranslatef((float) x+.5F, (float) y+1.2F, (float) z+.5F);
		GL11.glRotatef(180F, 0, 0, 1);
		GL11.glRotatef(getRotate(tileEntity.getBlockMetadata()), 0, 1, 0);
		GL11.glScalef(0.5F, 0.5F, 0.5F);
		//GL11.glRotatef(tileEntity.getBlockMetadata() * (-90), 0.0F, 0.0F, 1.0F);
		Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(Reference.MOD_ID.toLowerCase()+":textures/models/turtle.png"));
		model.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.1F);
		GL11.glPopMatrix();
	}
	
	private float getRotate(int meta) {
		switch (ForgeDirection.getOrientation(meta)) {
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
}
