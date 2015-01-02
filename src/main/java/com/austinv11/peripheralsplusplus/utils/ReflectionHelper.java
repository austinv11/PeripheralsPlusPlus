package com.austinv11.peripheralsplusplus.utils;

import com.gtranslate.Language;
import dan200.computercraft.api.turtle.ITurtleAccess;
import net.minecraft.tileentity.TileEntity;

import java.io.File;
import java.lang.reflect.Field;
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
}
