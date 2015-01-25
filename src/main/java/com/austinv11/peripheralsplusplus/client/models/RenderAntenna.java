package com.austinv11.peripheralsplusplus.client.models;

import com.austinv11.peripheralsplusplus.reference.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;

public class RenderAntenna extends TileEntitySpecialRenderer {

	private ModelAntenna model = new ModelAntenna();

	@Override
	public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float f) {
		GL11.glPushMatrix();
		GL11.glTranslatef((float) x+0.5F, (float) y+2.3F, (float) z+0.5F);
		GL11.glRotatef(180F, 0, 0, 1);
		//GL11.glRotatef(tileEntity.getBlockMetadata() * (-90), 0.0F, 0.0F, 1.0F);
		Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(Reference.MOD_ID.toLowerCase()+":textures/models/Antenna2.png"));
		model.render(null, 0.0F, 0.0F, 0.0F, 0.0F, getRotate(tileEntity.getBlockMetadata()), 0.1F);
		GL11.glPopMatrix();
	}

	private float getRotate(int meta) {
		switch (ForgeDirection.getOrientation(meta)) {
			case NORTH:
				return 0.0F;//0
			case SOUTH:
				return 0.7853981633974483F*4;//180
			case EAST:
				return 0.7853981633974483F*2;//90
			case WEST:
				return 0.7853981633974483F*6;//270
			default:
				return 0.0F;//0
		}
	}
}
