package com.austinv11.peripheralsplusplus.event.handler;

import com.austinv11.peripheralsplusplus.network.RobotEventPacket;
import com.austinv11.peripheralsplusplus.reference.Config;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.lang.reflect.Field;
import java.util.concurrent.CopyOnWriteArrayList;

public class RobotHandler {
	
	private Robot robot;
	public static CopyOnWriteArrayList<RobotOperation> operationList = new CopyOnWriteArrayList<RobotOperation>();
	
	public RobotHandler() {
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}
	
	@SubscribeEvent
	public void onClientTick(TickEvent.ClientTickEvent event) {
		doOperations();
	}
	
	private void doOperations() {
		for (RobotOperation operation : operationList) {
			if (operation.isFinished()) {
				operation.onFinish(robot);
				operationList.remove(operation);
			} else
				operation.tick(robot);
		}
	}
	
	public static abstract class RobotOperation {
		
		private int ticker;
		private int maxTick;
		
		public RobotOperation() {
			maxTick = ticker = Math.round((float)(Config.secondsBeforeReversal * 20));
		}
		
		public boolean isFinished() {
			return ticker == 0;
		}
		
		public void tick(Robot robot) {
			if (maxTick == ticker)
				operate(robot);
			if (ticker != 0)
				ticker--;
		}
		
		public abstract void onFinish(Robot robot);
		
		public abstract void operate(Robot robot);
	}
	
	public static class KeyBoardOperation extends RobotOperation {
		
		public RobotEventPacket.PressType pressType;
		public String key;
		
		public KeyBoardOperation() {
			super();
		}
		
		@Override
		public void onFinish(Robot robot) {
			if (pressType == RobotEventPacket.PressType.PRESS)
				try {
					robot.keyRelease(getKeyField().getInt(null));
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
		}
		
		@Override
		public void operate(Robot robot) {
			try {
				robot.keyPress(getKeyField().getInt(null));
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		
		public Field getKeyField() {
			try {
				return KeyEvent.class.getField("VK_"+key.toUpperCase());
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			}
			return null; //This shouldn't be reached
		}
	}
	
	public static class MouseClickOperation extends RobotOperation {
		
		public RobotEventPacket.PressType pressType;
		public int button;
		
		public MouseClickOperation() {
			super();
		}
		
		@Override
		public void onFinish(Robot robot) {
			if (pressType == RobotEventPacket.PressType.PRESS)
				robot.mouseRelease(button);
		}
		
		@Override
		public void operate(Robot robot) {
			robot.mousePress(button);
		}
	}
	
	public static class MouseMoveOperation extends RobotOperation {
		
		public int x,y;
		
		public MouseMoveOperation() {
			super();
		}
		
		@Override
		public void onFinish(Robot robot) {
			
		}
		
		@Override
		public void operate(Robot robot) {
			robot.mouseMove(x, y);
		}
	}
}
