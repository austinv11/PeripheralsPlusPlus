package com.austinv11.peripheralsplusplus.utils;

import com.gtranslate.Language;
import dan200.computercraft.api.turtle.ITurtleAccess;
import net.minecraft.tileentity.TileEntity;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;

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

	public static void loadExternalLib(File lib) throws Exception {
		URLClassLoader classLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
		Method addURL = URLClassLoader.class.getDeclaredMethod("addURL", new Class[]{URL.class});
		addURL.setAccessible(true);
		addURL.invoke(classLoader, lib.toURI().toURL());
	}

	public static String getLangFromWord(String lang) throws NoSuchFieldException, IllegalAccessException {
		Field field = Language.class.getDeclaredField("hashLanguage");
		field.setAccessible(true);
		HashMap<String, String> hashLanguage = (HashMap<String, String>) field.get(Language.getInstance());
		for (String k : hashLanguage.keySet())
			if (hashLanguage.get(k).equalsIgnoreCase(lang))
				return k;
		return null;
	}

	public static Object getICompFromId(int id) throws ClassNotFoundException, NoSuchMethodException, NoSuchFieldException, IllegalAccessException, InvocationTargetException {
		Class computerRegistry = Class.forName("dan200.computercraft.shared.computer.core.ComputerRegistry");
		Method getCompFromId = computerRegistry.getMethod("lookup", int.class);
		Object registry = Class.forName("dan200.computercraft.ComputerCraft").getField("serverComputerRegistry").get(null);
		return getCompFromId.invoke(registry, id);
	}

	public static Object objectToLuaValue(Object o) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
		Class luaMachine = Class.forName("dan200.computercraft.core.lua.LuaJLuaMachine");
		Object machine = luaMachine.newInstance();
		Method toVal = luaMachine.getDeclaredMethod("toValue", Object.class);
		toVal.setAccessible(true);
		return toVal.invoke(machine, o);
	}

	public static void runProgram(String path, int id) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException, NoSuchFieldException, InstantiationException {
		Object iComp = getICompFromId(id);
		Class iCompClass = iComp.getClass().asSubclass(Class.forName("dan200.computercraft.shared.computer.core.ServerComputer"));
		Field compField = iCompClass.getDeclaredField("m_computer");
		compField.setAccessible(true);
		Object comp = compField.get(iComp);
		Class compClass = comp.getClass();
		Field machine = compClass.getDeclaredField("m_machine");
		machine.setAccessible(true);
		Object machineObject = machine.get(comp);
		Class machineClass = machineObject.getClass();
		Field globals = machineClass.getDeclaredField("m_globals");
		globals.setAccessible(true);
		Object globalsObj = globals.get(machineObject);
		Method load = globalsObj.getClass().getDeclaredMethod("get", Class.forName("org.luaj.vm2.LuaValue"));
		load.setAccessible(true);
		Object chunk = load.invoke(globalsObj, objectToLuaValue("os"));
		Method get_ = chunk.getClass().getDeclaredMethod("get", Class.forName("org.luaj.vm2.LuaValue"));
		get_.setAccessible(true);
		Object run = get_.invoke(chunk, objectToLuaValue("run"));
		Method call = run.getClass().getDeclaredMethod("call", Class.forName("org.luaj.vm2.LuaValue"), Class.forName("org.luaj.vm2.LuaValue"));
		call.invoke(run, Class.forName("org.luaj.vm2.LuaTable").newInstance(), objectToLuaValue("test"));
	}
}
