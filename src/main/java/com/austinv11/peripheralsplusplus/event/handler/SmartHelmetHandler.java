package com.austinv11.peripheralsplusplus.event.handler;

import com.austinv11.collectiveframework.minecraft.utils.NBTHelper;
import com.austinv11.peripheralsplusplus.PeripheralsPlusPlus;
import com.austinv11.peripheralsplusplus.client.gui.GuiHelmet;
import com.austinv11.peripheralsplusplus.items.ItemSmartHelmet;
import com.austinv11.peripheralsplusplus.network.InputEventPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.util.UUID;

@SideOnly(Side.CLIENT)
public class SmartHelmetHandler {

	@SubscribeEvent
	public void onClick(InputEvent.MouseInputEvent event) {
        EntityPlayer player = Minecraft.getMinecraft().player;
        Iterable<ItemStack> armor = player.getArmorInventoryList();
        for (ItemStack armorPiece : armor) {
            if (armorPiece.getItem() instanceof ItemSmartHelmet &&
                    NBTHelper.hasTag(armorPiece, "identifier")) {
                PeripheralsPlusPlus.NETWORK.sendToServer(new InputEventPacket(
                        UUID.fromString(armorPiece.getTagCompound().getString("identifier")),
                        Mouse.getEventButton(),
                        Mouse.getEventButtonState(),
                        "mouseInput",
                        player.getDisplayNameString()));
                break;
            }
        }
	}

	@SubscribeEvent
	public void onKeyPressed(InputEvent.KeyInputEvent event) {
        EntityPlayer player = Minecraft.getMinecraft().player;
        Iterable<ItemStack> armor = player.getArmorInventoryList();
        for (ItemStack armorPiece : armor) {
            if (armorPiece.getItem() instanceof ItemSmartHelmet &&
                    NBTHelper.hasTag(armorPiece, "identifier")) {
                PeripheralsPlusPlus.NETWORK.sendToServer(new InputEventPacket(
                        UUID.fromString(armorPiece.getTagCompound().getString("identifier")),
                        Keyboard.getEventKey(),
                        Keyboard.getEventKeyState(),
                        "keyInput", player.getDisplayNameString()));
                break;
            }
        }
	}

	@SubscribeEvent
	public void onButtonClick(GuiScreenEvent.ActionPerformedEvent.Post event) {
		if (event.getGui() instanceof GuiHelmet) {
            EntityPlayer player = Minecraft.getMinecraft().player;
            Iterable<ItemStack> armor = player.getArmorInventoryList();
            for (ItemStack armorPiece : armor) {
                if (armorPiece.getItem() instanceof ItemSmartHelmet &&
                        NBTHelper.hasTag(armorPiece, "identifier")) {
                    PeripheralsPlusPlus.NETWORK.sendToServer(new InputEventPacket(
                            UUID.fromString(armorPiece.getTagCompound().getString("identifier")),
                            event.getButton().id,
                            true,
                            "buttonClicked",
                            player.getDisplayNameString()));
                    break;
                }
            }
        }
	}
}
