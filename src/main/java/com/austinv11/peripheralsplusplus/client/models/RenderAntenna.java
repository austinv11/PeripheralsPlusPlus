package com.austinv11.peripheralsplusplus.client.models;

import com.austinv11.peripheralsplusplus.init.ModItems;
import com.austinv11.peripheralsplusplus.reference.Reference;
import com.austinv11.peripheralsplusplus.tiles.TileEntityAntenna;
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
public class RenderAntenna extends TileEntitySpecialRenderer<TileEntityAntenna> {

	private ModelAntenna model = new ModelAntenna();

	@Override
	public void render(TileEntityAntenna tileEntity, double x, double y, double z, float partialTicks, int destroyStage,
                       float alpha) {
		GL11.glPushMatrix();
		GL11.glTranslatef((float) x+0.5F, (float) y+2.3F, (float) z+0.5F);
		GL11.glRotatef(180F, 0, 0, 1);
		//GL11.glRotatef(tileEntity.getBlockMetadata() * (-90), 0.0F, 0.0F, 1.0F);
		Minecraft.getMinecraft().renderEngine.bindTexture(
		        new ResourceLocation(Reference.MOD_ID + ":textures/models/antenna.png"));
		model.render(null, 0.0F, 0.0F, 0.0F, 0.0F,
				tileEntity == RenderAntennaItem.antenna ? 0 : getRotate(tileEntity.getBlockMetadata()),
				0.1F);
		GL11.glPopMatrix();
	}

    private float getRotate(int meta) {
		switch (EnumFacing.getFront(meta)) {
			case NORTH:
				return 0.0F;//0
			case SOUTH:
				return (float) Math.PI;//180
			case EAST:
				return 0.7853981633974483F*2;//90
			case WEST:
				return 0.7853981633974483F*6;//270
			default:
				return 0.7853981633974483F*4;//180
		}
	}

    public static class RenderAntennaItem extends TileEntityItemStackRenderer {
        private static TileEntityAntenna antenna = new TileEntityAntenna();
        private final TileEntityItemStackRenderer previousInstance;

        public RenderAntennaItem(TileEntityItemStackRenderer instance) {
            this.previousInstance = instance;
        }

        @Override
        public void renderByItem(ItemStack stack, float partialTicks) {
            if (!stack.getItem().equals(ModItems.ANTENNA)) {
                previousInstance.renderByItem(stack, partialTicks);
                return;
            }
            TileEntityRendererDispatcher.instance.render(antenna, 0, 0, 0, partialTicks);
        }
    }
}
