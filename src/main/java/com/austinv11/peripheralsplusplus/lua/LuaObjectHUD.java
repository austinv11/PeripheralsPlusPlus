package com.austinv11.peripheralsplusplus.lua;

import com.austinv11.peripheralsplusplus.utils.Util;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.ILuaObject;
import dan200.computercraft.api.lua.LuaException;
import net.minecraft.entity.player.EntityPlayer;

import java.util.PriorityQueue;

public class LuaObjectHUD implements ILuaObject{

	public PriorityQueue<Object> renderStack = new PriorityQueue<Object>();
	private EntityPlayer player;

	public LuaObjectHUD(String player) {
		this.player = Util.getPlayer(player);
	}

	@Override
	public String[] getMethodNames() {
		return new String[]{"getScaledWidth","getScaledHeight", "sendMessage", "drawString", "drawTexture", "sync"};
	}

	@Override
	public Object[] callMethod(ILuaContext context, int method, Object[] arguments) throws LuaException, InterruptedException {
		switch (method) {
			case 0:

				return new Object[0];
			case 1:

				return new Object[0];
			case 2:

			case 3:

			case 4:

			case 5:

		}
		return new Object[0];
	}
}
