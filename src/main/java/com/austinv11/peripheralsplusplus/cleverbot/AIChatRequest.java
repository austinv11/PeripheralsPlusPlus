package com.austinv11.peripheralsplusplus.cleverbot;

import com.austinv11.peripheralsplusplus.reference.Reference;
import com.austinv11.peripheralsplusplus.tiles.TileEntityAIChatBox.BotSessionLuaObject;
import dan200.computercraft.api.peripheral.IComputerAccess;

import java.util.logging.Level;
import java.util.logging.Logger;

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

				try {
					// Queue event
					// ai_response: bool success, string response, string uuid
					AIChatRequest.this.computer.queueEvent(AIChatRequest.EVENT, new Object[]{success, response, session.getUUID().toString()});
				} catch (Exception e) {
					// Main reason for this exception is
					// "You are not attached to this computer"
					// Dunno how to prevent it but this will do
					Logger.getLogger(Reference.MOD_NAME).log(Level.WARNING, "Tried queuing event to non-existing computer");
				}
			}
		});
		thread.start();
	}
}
