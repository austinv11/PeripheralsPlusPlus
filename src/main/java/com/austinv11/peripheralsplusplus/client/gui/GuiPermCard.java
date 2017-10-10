package com.austinv11.peripheralsplusplus.client.gui;

import com.austinv11.collectiveframework.minecraft.utils.NBTHelper;
import com.austinv11.peripheralsplusplus.PeripheralsPlusPlus;
import com.austinv11.peripheralsplusplus.network.PermCardChangePacket;
import com.austinv11.peripheralsplusplus.reference.Reference;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import org.lwjgl.opengl.GL11;

public class GuiPermCard extends GuiScreen
{
    private ItemStack permCard;
    private ResourceLocation backgroundimage = new ResourceLocation(Reference.MOD_ID.toLowerCase() + ":" +
            "textures/gui/perm_card.png");
    private int sizeX, sizeY;
    private boolean canGetStacks, canWithdraw, canDeposit;

    public GuiPermCard(ItemStack card)
    {
        super();
        this.permCard = card;
        this.canGetStacks = NBTHelper.getBoolean(permCard, "getStacks");
        this.canWithdraw = NBTHelper.getBoolean(permCard, "withdraw");
        this.canDeposit = NBTHelper.getBoolean(permCard, "deposit");
        sizeX = 132;
        sizeY = 166;
    }

    @Override
    public void drawScreen(int par1, int par2, float par3)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(backgroundimage);
        int x = (width - sizeX) / 2;
        int y = (height - sizeY) / 2;
        drawTexturedModalRect(x, y, 0, 0, sizeX, sizeY);
        // Have to draw the buttons before the text because mojang
        drawTexturedModalRect(x + 10, y + 40, this.canGetStacks ? 0 : 9, 166, 9, 9);
        drawTexturedModalRect(x + 10, y + 60, this.canWithdraw ? 0 : 9, 166, 9, 9);
        drawTexturedModalRect(x + 10, y + 80, this.canDeposit ? 0 : 9, 166, 9, 9);
        fontRenderer.drawString(I18n.translateToLocal("peripheralsplusplus.inv.permCard"), x + 22, y + 5, 0x313131);
        fontRenderer.drawString(I18n.translateToLocal("peripheralsplusplus.inv.permCard.perms"), x + 32, y + 20, 0x313131);
        fontRenderer.drawString(I18n.translateToLocal("peripheralsplusplus.inv.permCard.get"), x + 25, y + 40, 0x313131);
        fontRenderer.drawString(I18n.translateToLocal("peripheralsplusplus.inv.permCard.withdraw"), x + 25, y + 60, 0x313131);
        fontRenderer.drawString(I18n.translateToLocal("peripheralsplusplus.inv.permCard.deposit"), x + 25, y + 80, 0x313131);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int par3)
    {
        int x = (width - sizeX) / 2;
        int y = (height - sizeY) / 2;
        if (mouseX >= x + 10 && mouseX < x + 20)
        {
            if (mouseY >= y + 40 && mouseY < y + 50)
            {
                canGetStacks = !canGetStacks;
            }

            if (mouseY >= y + 60 && mouseY < y + 70)
            {
                canWithdraw = !canWithdraw;
            }

            if (mouseY >= y + 80 && mouseY < y + 90)
            {
                canDeposit = !canDeposit;
            }
        }
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }

    @Override
    public void onGuiClosed()
    {
        PeripheralsPlusPlus.NETWORK.sendToServer(new PermCardChangePacket(canGetStacks, canWithdraw, canDeposit));
    }
}
