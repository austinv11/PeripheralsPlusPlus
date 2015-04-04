package com.austinv11.peripheralsplusplus.utils;

import com.austinv11.peripheralsplusplus.reference.Config;
import com.gtranslate.Language;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.ILuaObject;
import dan200.computercraft.api.turtle.ITurtleAccess;
import net.minecraft.tileentity.TileEntity;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.HashMap;

public class ReflectionHelper {

	private static ArrayList<Integer> usedIds = new ArrayList<Integer>();

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

	public static Object getLuaMachineFromId(int id) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
		Object iComp = getICompFromId(id);
		Class iCompClass = iComp.getClass().asSubclass(Class.forName("dan200.computercraft.shared.computer.core.ServerComputer"));
		Field compField = iCompClass.getDeclaredField("m_computer");
		compField.setAccessible(true);
		Object comp = compField.get(iComp);
		Class compClass = comp.getClass();
		Field machine = compClass.getDeclaredField("m_machine");
		machine.setAccessible(true);
		return machine.get(comp);
	}

	public static void runProgram(String path, int id) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException, NoSuchFieldException, InstantiationException {
		Object machineObject = getLuaMachineFromId(id);
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
		call.invoke(run, Class.forName("org.luaj.vm2.LuaTable").newInstance(), objectToLuaValue(path));
	}

	public static void registerAPI(final int id, final ILuaAPIProxy api) throws ClassNotFoundException, NoSuchMethodException, NoSuchFieldException, IllegalAccessException, InvocationTargetException {
		if (!Config.enableAPIs)
			return;
		if (usedIds.contains(id)) {
//			usedIds.remove(usedIds.indexOf(id));
			return;
		}
		Object o;
		if ((o = getLuaMachineFromId(id)) == null) {
			new Thread() {

				private boolean work = true;

				@Override
				public void run() {
					if (work) {
						Object serverComputerObject = null;
						try {
							synchronized (this) {
								this.wait(20);
							}
							serverComputerObject = getICompFromId(id);
							Class serverCompClass = serverComputerObject.getClass().asSubclass(Class.forName("dan200.computercraft.shared.computer.core.ServerComputer"));
							Field comp = serverCompClass.getDeclaredField("m_computer");
							comp.setAccessible(true);
							Object o = comp.get(serverComputerObject);
							Method addAPI = o.getClass().getDeclaredMethod("addAPI", Class.forName("dan200.computercraft.core.apis.ILuaAPI"));
							addAPI.invoke(o, Proxy.newProxyInstance(Class.forName("dan200.computercraft.core.apis.ILuaAPI").getClassLoader(), new Class[]{Class.forName("dan200.computercraft.core.apis.ILuaAPI")}, api));
							Method reboot = o.getClass().getDeclaredMethod("reboot");
							reboot.invoke(o);
							work = false;
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}.start();
			usedIds.add(id);
			return;
		}
		Method addAPI = o.getClass().getDeclaredMethod("addAPI", Class.forName("dan200.computercraft.core.apis.ILuaAPI"));
		addAPI.invoke(o, Proxy.newProxyInstance(Class.forName("dan200.computercraft.core.apis.ILuaAPI").getClassLoader(), new Class[]{Class.forName("dan200.computercraft.core.apis.ILuaAPI")}, api));
	}

	public static abstract class ILuaAPIProxy implements InvocationHandler, ILuaObject {

		/**
		 * Gets the names that the api can be called by in lua
		 * @return The names
		 */
		public abstract String[] getNames();

		/**
		 * Called on computer startup
		 */
		public abstract void startup();

		/**
		 * Called to tick the API
		 * @param dt (Probably) the computer date/time
		 */
		public abstract void advance(double dt);

		/**
		 * Called on computer shutdown
		 */
		public abstract void shutdown();

		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			if (method.getName().equals("getNames")) {
				return getNames();
			}else if (method.getName().equals("startup")) {
				startup();
			}else if (method.getName().equals("advance")) {
				advance((Double)args[0]);
			}else if (method.getName().equals("shutdown")) {
				shutdown();
			}else if (method.getName().equals("getMethodNames")) {
				return getMethodNames();
			}else if (method.getName().equals("callMethod")) {
				return callMethod((ILuaContext)args[0], (Integer)args[1], (Object[])args[2]);
			}
			return null;
		}
	}
}
