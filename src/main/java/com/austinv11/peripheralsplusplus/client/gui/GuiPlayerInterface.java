package com.austinv11.peripheralsplusplus.client.gui;

import com.austinv11.peripheralsplusplus.reference.Reference;
import com.austinv11.peripheralsplusplus.tiles.containers.ContainerPlayerInterface;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiPlayerInterface extends GuiContainer
{
    private ResourceLocation backgroundimage = new ResourceLocation(Reference.MOD_ID.toLowerCase() + ":" + "textures/gui/guiPlayerInterface.png");
    private int sizeX, sizeY;

    public GuiPlayerInterface(EntityPlayer player, int x, int y, int z)
    {
        super(new ContainerPlayerInterface(player, (IInventory) player.worldObj.getTileEntity(x, y, z), 176, 133));
        sizeX = 176;
        sizeY = 133;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(backgroundimage);
        int x = (width - sizeX) / 2;
        int y = (height - sizeY) / 2;
        drawTexturedModalRect(x, y, 0, 0, sizeX, sizeY);
    }
}
