package com.austinv11.peripheralsplusplus.utils;

import dan200.computercraft.api.turtle.ITurtleAccess;
import net.minecraft.tileentity.TileEntity;

import java.lang.reflect.Method;

public class ReflectionHelper {

	public static ITurtleAccess getTurtle(TileEntity te) throws Exception{
		if (te instanceof ITurtleAccess)
			return (ITurtleAccess) te;
		Class teClass = te.getClass();
		if (teClass.getName().equals("dan200.computercraft.shared.turtle.blocks.TileTurtleExpanded") || teClass.getName().equals("dan200.computercraft.shared.turtle.blocks.TileTurtleAdvanced") || teClass.getName().equals("dan200.computercraft.shared.turtle.blocks.TileTurtle")) {
			Method getAccess = teClass.getMethod("getAccess", new Class[0]);
			ITurtleAccess turtle = (ITurtleAccess)getAccess.invoke(te, new Object[0]);
			if (turtle != null)
				return turtle;
		}
		return null;
	}

}
