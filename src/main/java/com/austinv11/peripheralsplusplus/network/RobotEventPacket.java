package com.austinv11.peripheralsplusplus.network;

import com.austinv11.peripheralsplusplus.event.handler.RobotHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class RobotEventPacket implements IMessage {
	
	public ActionType action;
	public PressType type;
	public Object info;
	
	public RobotEventPacket() {
		
	}
	
	public RobotEventPacket(ActionType action, PressType type, Object information) {
		this.action = action;
		this.type = type;
		this.info = information;
	}
	
	public enum ActionType {
		KEYBOARD, MOUSE_CLICK, MOUSE_MOVE
	}
	
	public enum PressType {
		PRESS, RELEASE
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		NBTTagCompound tag = ByteBufUtils.readTag(buf);
		action = ActionType.values()[tag.getInteger("action")];
		type = PressType.values()[tag.getInteger("type")];
		if (action == ActionType.KEYBOARD)
			info = tag.getString("info");
		else if (action == ActionType.MOUSE_CLICK)
			info = tag.getInteger("info");
		else
			info = tag.getIntArray("info");
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger("action", action.ordinal());
		tag.setInteger("type", type.ordinal());
		if (action == ActionType.KEYBOARD)
			tag.setString("info", (String) info);
		else if (action == ActionType.MOUSE_CLICK)
			tag.setInteger("info", (Integer)info);
		else
			tag.setIntArray("info", (int[])info);
		ByteBufUtils.writeTag(buf, tag);
	}
	
	public static class RobotEventPacketHandler implements IMessageHandler<RobotEventPacket, IMessage> {
		
		@Override
		public IMessage onMessage(RobotEventPacket message, MessageContext ctx) {
			switch (message.action) {
				case KEYBOARD:
					doKeyboardAction(message.type, (String)message.info);
					break;
				case MOUSE_CLICK:
					doMouseClickAction(message.type, (Integer)message.info);
					break;
				case MOUSE_MOVE:
					doMouseMoveAction((int[])message.info);
					break;
			}
			return null;
		}
		
		private void doKeyboardAction(PressType pressType, String key) {
			RobotHandler.KeyBoardOperation operation = new RobotHandler.KeyBoardOperation();
			operation.pressType = pressType;
			operation.key = key;
			RobotHandler.operationList.add(operation);
		}
		
		private void doMouseClickAction(PressType pressType, int button) {
			RobotHandler.MouseClickOperation operation = new RobotHandler.MouseClickOperation();
			operation.pressType = pressType;
			operation.button = button;
			RobotHandler.operationList.add(operation);
		}
		
		private void doMouseMoveAction(int[] movement) {
			RobotHandler.MouseMoveOperation operation = new RobotHandler.MouseMoveOperation();
			operation.x = movement[0];
			operation.y = movement[1];
			RobotHandler.operationList.add(operation);
		}
	}
}
