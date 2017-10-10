package com.austinv11.peripheralsplusplus.smarthelmet;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

public class DrawTextureCommand extends HelmetCommand {

	public ResourceLocation resource;
	public int x, y, u, v, width, height;

	@SideOnly(Side.CLIENT)
	@Override
	public void call(Gui gui) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		ItemType type = isItemLocation();
		if (u == -1)
			u = 0;
		if (v == -1)
			v = 0;
		if (type.equals(ItemType.NONE)) {
			GL11.glEnable(GL11.GL_BLEND);
			Minecraft.getMinecraft().getTextureManager().bindTexture(resource);
			gui.drawTexturedModalRect(x, y, u, v, width, height);
			GL11.glDisable(GL11.GL_BLEND);
		} else {
			ItemStack toRender = null;
			switch (type) {
                case ITEM:
                    Item item = ForgeRegistries.ITEMS.getValue(resource);
                    if (item == null)
                        return;
                    toRender = new ItemStack(item);
                    break;
                case BLOCK:
                    Block block = ForgeRegistries.BLOCKS.getValue(resource);
                    if (block == null)
                        return;
                    toRender = new ItemStack(block);
                    break;
            }
            if (toRender != null)
			    Minecraft.getMinecraft().getRenderItem().renderItemIntoGUI(toRender, x, y);
		}
	}

	@Override
	public String getCommandName() {
		return "DrawTextureCommand";
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		resource = new ResourceLocation(tagCompound.getString("resource"));
		x = tagCompound.getInteger("x");
		y = tagCompound.getInteger("y");
		u = tagCompound.getInteger("u");
		v = tagCompound.getInteger("v");
		width = tagCompound.getInteger("width");
		height = tagCompound.getInteger("height");
	}

	@Override
	public void writeToNBT(NBTTagCompound tagCompound) {
		tagCompound.setString("resource", resource.toString());
		tagCompound.setInteger("x", x);
		tagCompound.setInteger("y", y);
		tagCompound.setInteger("u", u);
		tagCompound.setInteger("v", v);
		tagCompound.setInteger("width", width);
		tagCompound.setInteger("height", height);
	}

	private ItemType isItemLocation() {
		if (ForgeRegistries.BLOCKS.getValue(resource) != Blocks.AIR)
			return ItemType.BLOCK;
		if (ForgeRegistries.ITEMS.getValue(resource) != Items.AIR)
			return ItemType.ITEM;
		return ItemType.NONE;
	}

    private enum ItemType {ITEM, BLOCK, NONE}
}
