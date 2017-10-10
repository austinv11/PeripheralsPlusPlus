package com.austinv11.peripheralsplusplus.client.gui;

import com.austinv11.collectiveframework.minecraft.utils.NBTHelper;
import com.austinv11.peripheralsplusplus.PeripheralsPlusPlus;
import com.austinv11.peripheralsplusplus.items.ItemSmartHelmet;
import com.austinv11.peripheralsplusplus.network.InputEventPacket;
import com.austinv11.peripheralsplusplus.network.TextFieldInputEventPacket;
import com.austinv11.peripheralsplusplus.smarthelmet.AddButtonCommand;
import com.austinv11.peripheralsplusplus.smarthelmet.AddTextFieldCommand;
import com.austinv11.peripheralsplusplus.smarthelmet.HelmetCommand;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.UUID;

public class GuiHelmet extends GuiScreen {

	public static HashMap<UUID,ArrayDeque<HelmetCommand>> renderStack = new HashMap<UUID,ArrayDeque<HelmetCommand>>();
	public HashMap<Integer,GuiTextField> textFields = new HashMap<Integer,GuiTextField>();

	@Override
	public void drawScreen(int mouseX, int mouseY, float renderPartialTicks) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		EntityPlayer player = Minecraft.getMinecraft().player;
		Iterable<ItemStack> armor = player.getArmorInventoryList();
		for (ItemStack itemStack : armor) {
			if (itemStack.getItem() instanceof ItemSmartHelmet &&
					NBTHelper.hasTag(itemStack, "identifier")) {
				UUID uuid = UUID.fromString(NBTHelper.getString(itemStack, "identifier"));
				if (renderStack.containsKey(uuid)) {
					ArrayDeque<HelmetCommand> commands = new ArrayDeque<>(renderStack.get(uuid));
					while (!commands.isEmpty()) {
						HelmetCommand command = commands.poll();
						if (!(command instanceof AddTextFieldCommand) &&
								!(command instanceof AddButtonCommand))
							command.call(this);
					}
				}
			}
		}
		super.drawScreen(mouseX, mouseY, renderPartialTicks);
		for (GuiTextField text : textFields.values())
			text.drawTextBox();
	}

	@Override
	public void initGui() {
		super.initGui();
		textFields.clear();
		buttonList.clear();
		EntityPlayer player = Minecraft.getMinecraft().player;
		Iterable<ItemStack> armor = player.getArmorInventoryList();
		for (ItemStack itemStack : armor) {
			if (itemStack.getItem() instanceof ItemSmartHelmet &&
					NBTHelper.hasTag(itemStack, "identifier")) {
				UUID uuid = UUID.fromString(NBTHelper.getString(itemStack, "identifier"));
				if (renderStack.containsKey(uuid)) {
					ArrayDeque<HelmetCommand> commands = new ArrayDeque<>(renderStack.get(uuid));
					while (!commands.isEmpty()) {
						HelmetCommand command = commands.poll();
						if (command instanceof AddTextFieldCommand || command instanceof AddButtonCommand)
							command.call(this);
					}
				}
			}
		}
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	public GuiButton addButton(GuiButton button) {
		buttonList.add(button);
		return button;
	}

	public void addTextField(int id, GuiTextField field) {
		textFields.put(id, field);
	}

	@Override
	protected void mouseClicked(int x, int y, int mouseEvent) throws IOException {
		super.mouseClicked(x, y, mouseEvent);
		for (GuiTextField text : textFields.values())
			text.mouseClicked(x, y, mouseEvent);
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
						Minecraft.getMinecraft().player.getDisplayNameString()));
				break;
			}
		}
	}

	@Override
	public void updateScreen() {
		super.updateScreen();
		for (GuiTextField text : textFields.values())
			text.updateCursorCounter();
	}

	@Override
	protected void keyTyped(char eventChar, int eventKey) throws IOException {
		super.keyTyped(eventChar, eventKey);
		EntityPlayer player = Minecraft.getMinecraft().player;
		Iterable<ItemStack> armor = player.getArmorInventoryList();
		for (ItemStack armorPiece : armor) {
			if (armorPiece.getItem() instanceof ItemSmartHelmet &&
					NBTHelper.hasTag(armorPiece, "identifier")) {
				for (GuiTextField text : textFields.values())
					if (text.textboxKeyTyped(eventChar, eventKey))
						PeripheralsPlusPlus.NETWORK.sendToServer(new TextFieldInputEventPacket(
								UUID.fromString(armorPiece.getTagCompound().getString("identifier")),
								eventChar+"",
								text.getText(),
								"textboxEntry",
								player.getDisplayNameString()));
				PeripheralsPlusPlus.NETWORK.sendToServer(new InputEventPacket(
						UUID.fromString(armorPiece.getTagCompound().getString("identifier")),
						Keyboard.getEventKey(),
						Keyboard.getEventKeyState(),
						"keyInput",
						player.getDisplayNameString()));
			}
		}
	}
}
