package com.austinv11.peripheralsplusplus.cleverbot;

import com.austinv11.peripheralsplusplus.reference.Reference;
import com.austinv11.peripheralsplusplus.tiles.TileEntityAIChatBox;
import com.austinv11.peripheralsplusplus.tiles.TileEntityAIChatBox.BotSessionLuaObject;
import dan200.computercraft.api.peripheral.IComputerAccess;

import java.util.logging.Level;
import java.util.logging.Logger;

public class AIChatRequest {
	public static final String EVENT = "ai_response";

	private final TileEntityAIChatBox tileEntity;
	private final BotSessionLuaObject session;
	private final String message;

	//private boolean complete = false;
	private boolean success = false;
	private String response = null;

	public AIChatRequest(final TileEntityAIChatBox tileEntity, final BotSessionLuaObject session, final String message) {
		this.tileEntity = tileEntity;
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

				// Queue event
				// ai_response: string side, bool success, string response, string uuid
				tileEntity.sendEvent(new Object[]{ success, response, session.getUUID().toString() });
			}
		});
		thread.start();
	}
}
