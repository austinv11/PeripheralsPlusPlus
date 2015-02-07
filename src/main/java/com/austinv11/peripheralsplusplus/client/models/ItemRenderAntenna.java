package com.austinv11.peripheralsplusplus.client.models;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class ItemRenderAntenna implements IItemRenderer {

	private TileEntity tileEntity;
	private TileEntitySpecialRenderer renderer;

	public ItemRenderAntenna(TileEntitySpecialRenderer renderer, TileEntity tileEntity)
	{
		this.tileEntity = tileEntity;
		this.renderer = renderer;
	}

	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		return true;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
		return true;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		if (type == IItemRenderer.ItemRenderType.ENTITY)
			GL11.glTranslatef(-0.5F, 0.0F, -0.5F);
		tileEntity.blockMetadata = 0;
		this.renderer.renderTileEntityAt(this.tileEntity, 0.0D, 0.0D, 0.0D, 0.0F);
	}
}
