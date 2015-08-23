package com.austinv11.peripheralsplusplus;

import com.austinv11.peripheralsplusplus.tiles.TileEntityAIChatBox;
import com.google.code.chatterbotapi.*;
import dan200.computercraft.api.peripheral.IComputerAccess;

import java.util.UUID;

public class AIChatRequest {
	public static final String EVENT = "ai_response";

	private final IComputerAccess computer;
	private final UUID uuid;
	private final ChatterBotSession session;
	private final String message;

	//private boolean complete = false;
	private boolean success = false;
	private String response = null;

	public AIChatRequest(IComputerAccess computer, final UUID uuid, final ChatterBotSession session, final String message) {
		this.computer = computer;
		this.uuid = uuid;
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
				// ai_response: string uuid, bool success, string response
				AIChatRequest.this.computer.queueEvent(AIChatRequest.EVENT, new Object[]{ uuid.toString(), success, response });
			}
		});
		thread.start();
	}
}
