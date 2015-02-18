package com.austinv11.peripheralsplusplus.smarthelmet;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class DrawTextureCommand extends HelmetCommand {

	public String resource;
	public int x, y, u, v, width, height;

	@SideOnly(Side.CLIENT)
	@Override
	public void call(Gui gui) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		int type = isItemLocation();
		if (u == -1)
			u = 0;
		if (v == -1)
			v = 0;
		if (type == -1) {
			GL11.glEnable(GL11.GL_BLEND);
			Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(resource));
			gui.drawTexturedModalRect(x, y, u, v, width, height);
			GL11.glDisable(GL11.GL_BLEND);
		} else {
			RenderItem render = new RenderItem();
			ItemStack toRender = type == 1 ? new ItemStack((Block)Block.blockRegistry.getObject(resource)) : new ItemStack((Item)Item.itemRegistry.getObject(resource));
			render.renderItemIntoGUI(Minecraft.getMinecraft().fontRenderer, Minecraft.getMinecraft().getTextureManager(), toRender, x, y);
		}
	}

	@Override
	public String getCommandName() {
		return "DrawTextureCommand";
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		resource = tagCompound.getString("resource");
		x = tagCompound.getInteger("x");
		y = tagCompound.getInteger("y");
		u = tagCompound.getInteger("u");
		v = tagCompound.getInteger("v");
		width = tagCompound.getInteger("width");
		height = tagCompound.getInteger("height");
	}

	@Override
	public void writeToNBT(NBTTagCompound tagCompound) {
		tagCompound.setString("resource", resource);
		tagCompound.setInteger("x", x);
		tagCompound.setInteger("y", y);
		tagCompound.setInteger("u", u);
		tagCompound.setInteger("v", v);
		tagCompound.setInteger("width", width);
		tagCompound.setInteger("height", height);
	}

	//-1 for no
	//0 for item
	//1 for block
	//Sorry for magic numbers :V
	private int isItemLocation() {
		if (Block.blockRegistry.getObject(resource) != null)
			return 1;
		if (Item.itemRegistry.getObject(resource) != null)
			return 0;
		return -1;
	}
}
