package com.austinv11.peripheralsplusplus.lua;

import com.austinv11.peripheralsplusplus.PeripheralsPlusPlus;
import com.austinv11.peripheralsplusplus.network.CommandPacket;
import com.austinv11.peripheralsplusplus.smarthelmet.*;
import com.austinv11.peripheralsplusplus.utils.Util;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.ILuaObject;
import dan200.computercraft.api.lua.LuaException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

import java.awt.*;
import java.util.ArrayDeque;
import java.util.UUID;

public class LuaObjectHUD implements ILuaObject{

	public ArrayDeque<HelmetCommand> renderStack = new ArrayDeque<HelmetCommand>();
	private EntityPlayer player;
	public int width = -1;
	public int height = -1;
	private UUID uuid;

	public LuaObjectHUD(String player, UUID uuid) {
		this.player = Util.getPlayer(player);
		this.uuid = uuid;
		renderStack.add(new MessageCommand());
	}

	@Override
	public String[] getMethodNames() {
		return new String[]{"getResolution", "sendMessage", "drawString", "drawTexture", "drawRectangle", "drawHorizontalLine", "drawVerticalLine", "sync", "clear", "add", "getColorFromRGB"};
	}

	@Override
	public Object[] callMethod(ILuaContext context, int method, Object[] arguments) throws LuaException, InterruptedException {
//		try {
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
				if (arguments.length < 3)
					throw new LuaException("Not enough arguments");
				if (!(arguments[0] instanceof String))
					throw new LuaException("Bad argument #1 (expected string)");
				if (!(arguments[1] instanceof Double))
					throw new LuaException("Bad argument #2 (expected number)");
				if (!(arguments[2] instanceof Double))
					throw new LuaException("Bad argument #3 (expected number)");
				if (arguments.length > 3 && !(arguments[3] instanceof Double))
					throw new LuaException("Bad argument #4 (expected number)");
				if (arguments.length > 4 && !(arguments[4] instanceof Boolean))
					throw new LuaException("Bad argument #5 (expected boolean)");
				int color = Color.WHITE.getRGB();
				if (arguments.length > 3)
					color = (int)(double)(Double)arguments[3];
				boolean shadow = true;
				if (arguments.length > 4)
					shadow = (Boolean)arguments[4];
				DrawStringCommand drawString = new DrawStringCommand();
				drawString.message = (String)arguments[0];
				drawString.x = (int)(double)(Double)arguments[1];
				drawString.y = (int)(double)(Double)arguments[2];
				drawString.color = color;
				drawString.shadow = shadow;
				renderStack.add(drawString);
				break;
			case 3:
				if (arguments.length < 3)
					throw new LuaException("Not enough arguments");
				if (!(arguments[0] instanceof String))
					throw new LuaException("Bad argument #1 (expected string)");
				if (!(arguments[1] instanceof Double))
					throw new LuaException("Bad argument #2 (expected number)");
				if (!(arguments[2] instanceof Double))
					throw new LuaException("Bad argument #3 (expected number)");
				if (arguments.length > 3 && !(arguments[3] instanceof Double))
					throw new LuaException("Bad argument #4 (expected number)");
				if (arguments.length > 4 && !(arguments[4] instanceof Double))
					throw new LuaException("Bad argument #5 (expected number)");
				if (arguments.length > 5 && !(arguments[5] instanceof Double))
					throw new LuaException("Bad argument #6 (expected number)");
				if (arguments.length > 6 && !(arguments[6] instanceof Double))
					throw new LuaException("Bad argument #7 (expected number)");
				int u = -1;
				int v = -1;
				int width = 256;
				int height = 256;
				if (arguments.length > 3)
					width = (int)(double)(Double)arguments[3];
				if (arguments.length > 4)
					height = (int)(double)(Double)arguments[4];
				if (arguments.length > 5)
					u = (int)(double)(Double)arguments[5];
				if (arguments.length > 36)
					v = (int)(double)(Double)arguments[6];
				DrawTextureCommand command = new DrawTextureCommand();
				command.resource = (String)arguments[0];
				command.x = (int)(double)(Double)arguments[1];
				command.y = (int)(double)(Double)arguments[2];
				command.u = u;
				command.v = v;
				command.width = width;
				command.height = height;
				renderStack.add(command);
				break;
			case 4:
				if (arguments.length < 5)
					throw new LuaException("Not enough arguments");
				if (!(arguments[0] instanceof Double))
					throw new LuaException("Bad argument #1 (expected number)");
				if (!(arguments[1] instanceof Double))
					throw new LuaException("Bad argument #2 (expected number)");
				if (!(arguments[2] instanceof Double))
					throw new LuaException("Bad argument #3 (expected number)");
				if (!(arguments[3] instanceof Double))
					throw new LuaException("Bad argument #4 (expected number)");
				if (!(arguments[4] instanceof Double))
					throw new LuaException("Bad argument #5 (expected number)");
				if (arguments.length > 5 && !(arguments[5] instanceof Double))
					throw new LuaException("Bad argument #6 (expected number)");
				DrawRectangleCommand c = new DrawRectangleCommand();
				c.x1 = (int)(double)(Double)arguments[0];
				c.y1 = (int)(double)(Double)arguments[1];
				c.x2 = (int)(double)(Double)arguments[2];
				c.y2 = (int)(double)(Double)arguments[3];
				c.color = new Color((int)(double)(Double)arguments[4]);
				if (arguments.length > 5)
					c.color2 = new Color((int)(double)(Double)arguments[5]);
				renderStack.add(c);
				break;
			case 5:
				if (arguments.length < 4)
					throw new LuaException("Not enough arguments");
				if (!(arguments[0] instanceof Double))
					throw new LuaException("Bad argument #1 (expected number)");
				if (!(arguments[1] instanceof Double))
					throw new LuaException("Bad argument #2 (expected number)");
				if (!(arguments[2] instanceof Double))
					throw new LuaException("Bad argument #3 (expected number)");
				if (!(arguments[3] instanceof Double))
					throw new LuaException("Bad argument #4 (expected number)");
				DrawRectangleCommand c_ = new DrawRectangleCommand();
				c_.x1 = (int)(double)(Double)arguments[0];
				c_.y1 = (int)(double)(Double)arguments[1];
				c_.x2 = (int)(double)(Double)arguments[2] + 1;
				c_.y2 = c_.y1+1;
				c_.color = new Color((int)(double)(Double)arguments[3]);
				renderStack.add(c_);
				break;
			case 6:
				if (arguments.length < 4)
					throw new LuaException("Not enough arguments");
				if (!(arguments[0] instanceof Double))
					throw new LuaException("Bad argument #1 (expected number)");
				if (!(arguments[1] instanceof Double))
					throw new LuaException("Bad argument #2 (expected number)");
				if (!(arguments[2] instanceof Double))
					throw new LuaException("Bad argument #3 (expected number)");
				if (!(arguments[3] instanceof Double))
					throw new LuaException("Bad argument #4 (expected number)");
				DrawRectangleCommand c1 = new DrawRectangleCommand();
				c1.x1 = (int)(double)(Double)arguments[0];
				c1.y1 = (int)(double)(Double)arguments[1] + 1;
				c1.x2 = c1.x1 + 1;
				c1.y2 = (int)(double)(Double)arguments[2];
				c1.color = new Color((int)(double)(Double)arguments[3]);
				renderStack.add(c1);
				break;
			case 7:
				PeripheralsPlusPlus.NETWORK.sendTo(new CommandPacket(stackToArray(), uuid), (EntityPlayerMP) player);
				MessageCommand.messageStack.clear();
				renderStack.clear();
				renderStack.add(new MessageCommand());
				break;
			case 8:
				PeripheralsPlusPlus.NETWORK.sendTo(new CommandPacket(new HelmetCommand[0], uuid), (EntityPlayerMP) player);
				MessageCommand.messageStack.clear();
				renderStack.clear();
				renderStack.add(new MessageCommand());
				break;
			case 9:
				PeripheralsPlusPlus.NETWORK.sendTo(new CommandPacket(stackToArray(), uuid, true), (EntityPlayerMP) player);
				MessageCommand.messageStack.clear();
				renderStack.clear();
				renderStack.add(new MessageCommand());
				break;
			case 10:
				if (arguments.length < 3)
					throw new LuaException("Not enough arguments");
				if (!(arguments[0] instanceof Double))
					throw new LuaException("Bad argument #1 (expected number)");
				if (!(arguments[1] instanceof Double))
					throw new LuaException("Bad argument #2 (expected number)");
				if (!(arguments[2] instanceof Double))
					throw new LuaException("Bad argument #3 (expected number)");
				if (arguments.length > 3 && !(arguments[3] instanceof Double))
					throw new LuaException("Bad argument #4 (expected number)");
				if (arguments.length > 3)
					return new Object[]{new Color((int)(double)(Double)arguments[0], (int)(double)(Double)arguments[1], (int)(double)(Double)arguments[2], (int)(double)(Double)arguments[3]).getRGB()};
				return new Object[]{new Color((int)(double)(Double)arguments[0], (int)(double)(Double)arguments[1], (int)(double)(Double)arguments[2]).getRGB()};
		}
//		}catch (Exception e) {
//			e.printStackTrace();
//		}
		return new Object[0];
	}

	private HelmetCommand[] stackToArray() {
		HelmetCommand[] array = new HelmetCommand[renderStack.size()];
		int i = 0;
		while (!renderStack.isEmpty()) {
			array[i] = renderStack.poll();
			i++;
		}
		return array;
	}
}
