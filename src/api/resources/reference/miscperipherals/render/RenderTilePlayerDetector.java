package miscperipherals.render;

import miscperipherals.tile.TilePlayerDetector;
import miscperipherals.util.Util;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderTilePlayerDetector extends TileEntitySpecialRenderer {
	@Override
	public void renderTileEntityAt(TileEntity te, double x, double y, double z, float tick) {
		if (!(te instanceof TilePlayerDetector)) return;
		TilePlayerDetector tile = (TilePlayerDetector)te;
		
		//bindTextureByURL("http://s3.amazonaws.com/MinecraftSkins/"+tile.player+".png", "/mob/char.png");
		bindTextureByURL("http://s3.amazonaws.com/MinecraftSkins/Notch.png", "/mob/char.png");
		//bindTextureByName("/terrain.png");
		
		Tessellator tes = Tessellator.instance;
		
		for (int i = 0; i < 6; i++) {
			GL11.glPushMatrix();
			GL11.glTranslated(x + 0.5D, y + 0.5D, z + 0.5D);
			GL11.glRotatef(Util.YAW[i], 0, 1, 0);
			GL11.glRotatef(Util.PITCH[i], 1, 0, 0);
			
			tes.startDrawingQuads();
			tes.addVertexWithUV(0.51D, -0.25D, -0.25D, 0.125D, 0.25D);
			tes.addVertexWithUV(0.51D, 0.25D, -0.25D, 0.25D, 0.25D);
			tes.addVertexWithUV(0.51D, 0.25D, 0.25D, 0.25D, 0.5D);
			tes.addVertexWithUV(0.51D, -0.25D, 0.25D, 0.125D, 0.5D);
			tes.draw();
			GL11.glPopMatrix();
		}
	}
}
