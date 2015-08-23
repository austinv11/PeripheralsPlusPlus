package com.austinv11.peripheralsplusplus;

import com.austinv11.peripheralsplusplus.tiles.TileEntityAIChatBox;
import com.austinv11.peripheralsplusplus.tiles.TileEntityAIChatBox.BotSessionLuaObject;
import com.google.code.chatterbotapi.*;
import dan200.computercraft.api.peripheral.IComputerAccess;

import java.util.UUID;

public class AIChatRequest {
	public static final String EVENT = "ai_response";

	private final IComputerAccess computer;
	private final BotSessionLuaObject session;
	private final String message;

	//private boolean complete = false;
	private boolean success = false;
	private String response = null;

	public AIChatRequest(IComputerAccess computer, final BotSessionLuaObject session, final String message) {
		this.computer = computer;
		this.session = session;
		this.message = message;

		Thread thread = new Thread(new Runnable() {
			public void run() {
				try {
					response = session.think(message);
					success = true;
				} catch (Exception e) {
					response = e.toString();
					success = false;
				}

				if (AIChatRequest.this.computer == null) {
					System.out.println("WHAT? COMPUTER IS NULL NOOOOOOOOOOOO");
				}

				// Queue event
				// ai_response: bool success, string response, string uuid
				AIChatRequest.this.computer.queueEvent(AIChatRequest.EVENT, new Object[]{ success, response, session.getUUID().toString() });
			}
		});
		thread.start();
	}
}
