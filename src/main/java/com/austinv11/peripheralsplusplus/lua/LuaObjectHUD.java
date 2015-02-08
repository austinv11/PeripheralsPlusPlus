package com.austinv11.peripheralsplusplus.lua;

import com.austinv11.peripheralsplusplus.PeripheralsPlusPlus;
import com.austinv11.peripheralsplusplus.network.CommandPacket;
import com.austinv11.peripheralsplusplus.smarthelmet.ICommand;
import com.austinv11.peripheralsplusplus.smarthelmet.MessageCommand;
import com.austinv11.peripheralsplusplus.utils.Util;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.ILuaObject;
import dan200.computercraft.api.lua.LuaException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.PriorityQueue;
import java.util.UUID;

public class LuaObjectHUD implements ILuaObject{

	public PriorityQueue<ICommand> renderStack = new PriorityQueue<ICommand>();
	private EntityPlayer player;
	public int width = -1;
	public int height = -1;
	private UUID uuid;

	public LuaObjectHUD(String player, UUID uuid) {
		this.player = Util.getPlayer(player);
		this.uuid = uuid;
	}

	@Override
	public String[] getMethodNames() {
		return new String[]{"getResolution", "sendMessage", "drawString", "drawTexture", "sync", "clear"};
	}

	@Override
	public Object[] callMethod(ILuaContext context, int method, Object[] arguments) throws LuaException, InterruptedException {
		try {
		switch (method) {
			case 0:
				return new Object[]{width, height};
			case 1:
				if (arguments.length < 1)
					throw new LuaException("Not enough arguments");
				if (!(arguments[0] instanceof String))
					throw new LuaException("Bad argument #1 (expected string)");
				MessageCommand.messageStack.push((String) arguments[0]);
				break;
			case 2:

			case 3:

			case 4:
				PeripheralsPlusPlus.NETWORK.sendTo(new CommandPacket(stackToArray(), uuid), (EntityPlayerMP) player);
				renderStack.clear();
				renderStack.add(new MessageCommand());
				break;
			case 5:
				PeripheralsPlusPlus.NETWORK.sendTo(new CommandPacket(new ICommand[0], uuid), (EntityPlayerMP) player);
				renderStack.clear();
				renderStack.add(new MessageCommand());
		}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return new Object[0];
	}

	private ICommand[] stackToArray() {
		ICommand[] array = new ICommand[renderStack.size()];
		int i = 0;
		while (!renderStack.isEmpty()) {
			array[i] = renderStack.poll();
			i++;
		}
		return array;
	}
}
