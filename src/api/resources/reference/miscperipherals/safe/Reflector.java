package miscperipherals.safe;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import miscperipherals.core.MiscPeripherals;

import com.google.common.base.Objects;

public class Reflector {
	private static Map<String, Class> classCache = new HashMap<String, Class>();
	private static Map<ClassPair, Field> fieldCache = new HashMap<ClassPair, Field>();
	private static Map<ClassPair, Method> methodCache = new HashMap<ClassPair, Method>();
	private static Map<ClassPair, Constructor> constructorCache = new HashMap<ClassPair, Constructor>();
	
	public static Class getClass(String name) {		
		try {
			if (classCache.containsKey(name)) return classCache.get(name);
			else {
				Class clazz = Class.forName(name);
				classCache.put(name, clazz);
				return clazz;
			}
		} catch (Throwable e) {
			MiscPeripherals.log.warning("Reflector: Unable to get class "+name);
			if (MiscPeripherals.DEBUG) e.printStackTrace();
			return null;
		}
	}
	
	public static void setField(Object obj, String fieldName, Object value) {		
		try {
			Class clazz = getClass(obj);
			if (clazz == null) return;
			ClassPair cp = new ClassPair(clazz, fieldName);
			Field field = fieldCache.get(cp);
			if (field == null) {
				field = getDeclaredFieldRecursive(clazz, fieldName);
				if (field == null) throw new NoSuchFieldException();
				field.setAccessible(true);
				fieldCache.put(cp, field);
			}
			field.set(getInstance(obj), value);
		} catch (Throwable e) {
			MiscPeripherals.log.warning("Reflector: Unable to set field ["+obj+"]."+fieldName);
			if (MiscPeripherals.DEBUG) e.printStackTrace();
		}
	}
	
	public static <T> T getField(Object obj, String fieldName, Class<T> type) {		
		try {
			Class clazz = getClass(obj);
			if (clazz == null) return null;
			ClassPair cp = new ClassPair(clazz, fieldName);
			Field field = fieldCache.get(cp);
			if (field == null) {
				field = getDeclaredFieldRecursive(clazz, fieldName);
				if (field == null) throw new NoSuchFieldException();
				field.setAccessible(true);
				fieldCache.put(cp, field);
			}
			return type.cast(field.get(getInstance(obj)));
		} catch (Throwable e) {
			MiscPeripherals.log.warning("Reflector: Unable to get field ["+obj+"]."+fieldName);
			if (MiscPeripherals.DEBUG) e.printStackTrace();
			return null;
		}
	}
	
	public static <T> T invoke(Object obj, String methodName, Class<T> type, Object... params) {		
		try {
			Class clazz = getClass(obj);
			if (clazz == null) return null;
			Class[] types = new Class[params.length];
			for (int i = 0; i < params.length; i++) types[i] = params[i] == null ? Object.class : params[i].getClass();
			ClassPair cp = new ClassPair(clazz, methodName, types);
			Method method = methodCache.get(cp);
			if (method == null) {
				for (Method lmethod : getRecursiveMethods(clazz)) {
					Class[] methodtypes = lmethod.getParameterTypes();
					//if (MiscPeripherals.DEBUG) MiscPeripherals.log.info("Reflector: Trying to find method ["+obj+"]."+methodName+": Trying "+lmethod.getName()+" - "+Arrays.asList(types)+" vs. "+Arrays.asList(methodtypes));
					if (!methodName.equals(lmethod.getName()) || methodtypes.length != params.length) continue;
					boolean success = true;
					for (int i = 0; i < methodtypes.length; i++) {
						if (!checkTypeWithPrimitives(types[i], methodtypes[i])) {
							//if (MiscPeripherals.DEBUG) MiscPeripherals.log.info("Reflector: "+types[i]+" does not match "+methodtypes[i]);
							success = false;
							break;
						}
					}
					if (success) {
						method = lmethod;
						break;
					}
				}
				if (method == null) throw new NoSuchMethodException();
				method.setAccessible(true);
				methodCache.put(cp, method);
			}
			return type.cast(method.invoke(getInstance(obj), params));
		} catch (Throwable e) {
			MiscPeripherals.log.warning("Reflector: Unable to invoke method ["+obj+"]."+methodName);
			if (MiscPeripherals.DEBUG) e.printStackTrace();
			return null;
		}
	}
	
	public static <T> T construct(Object target, Class<T> type, Object... params) {		
		try {
			Class[] types = new Class[params.length];
			for (int i = 0; i < params.length; i++) types[i] = params[i].getClass();
			Class clazz = getClass(target);
			ClassPair cp = new ClassPair(clazz, null, types);
			Constructor ctor = constructorCache.get(cp);
			if (ctor == null) {
				for (Constructor<T> lctor : clazz.getDeclaredConstructors()) {
					Class[] ctortypes = lctor.getParameterTypes();
					//if (MiscPeripherals.DEBUG) MiscPeripherals.log.info("Reflector: Trying to find ctor for ["+target+"]: Trying "+Arrays.asList(types)+" vs. "+Arrays.asList(ctortypes));
					if (ctortypes.length != params.length) continue;
					boolean success = true;
					for (int i = 0; i < ctortypes.length; i++) {
						if (!checkTypeWithPrimitives(types[i], ctortypes[i])) {
							//if (MiscPeripherals.DEBUG) MiscPeripherals.log.info("Reflector: "+types[i]+" does not match "+ctortypes[i]);
							success = false;
							break;
						}
					}
					if (success) ctor = lctor;
				}
				if (ctor == null) throw new NoSuchMethodException();
				ctor.setAccessible(true);
				constructorCache.put(cp, ctor);
			}
			return type.cast(ctor.newInstance(params));
		} catch (Throwable e) {
			MiscPeripherals.log.warning("Reflector: Unable to construct ["+target+"]");
			if (MiscPeripherals.DEBUG) e.printStackTrace();
			return null;
		}
	}
	
	public static Class getClass(Object instance) {
		return instance == null ? null : (instance.getClass() == String.class ? getClass((String)instance) : instance.getClass());
	}
	
	public static Object getInstance(Object instance) {
		return instance == null ? null : (instance.getClass() == String.class ? null : instance);
	}
	
	public static boolean checkTypeWithPrimitives(Class<?> a, Class<?> b) {
		if (a == Integer.class && b == int.class) return true;
		else if (a == Double.class && b == double.class) return true;
		else if (a == Boolean.class && b == boolean.class) return true;
		else if (a == Long.class && b == long.class) return true;
		else if (a == Float.class && b == float.class) return true;
		else if (a == Byte.class && b == byte.class) return true;
		else if (a == Short.class && b == short.class) return true;
		else return a.isAssignableFrom(b) || b.isAssignableFrom(a);
	}
	
	public static Iterable<Method> getRecursiveMethods(Class clazz) {
		List<Method> methods = new LinkedList<Method>();
		
		Class spr = clazz;
		do {
			methods.addAll(Arrays.asList(spr.getDeclaredMethods()));
			spr = spr.getSuperclass();
		} while (spr != null);
		
		return methods;
	}
	
	public static Field getDeclaredFieldRecursive(Class clazz, String name) throws NoSuchFieldException {
		Field f = null;
		Class spr = clazz;
		do {
			try {
				f = spr.getDeclaredField(name);
			} catch (Throwable e) {}
			if (f != null) return f;
			spr = spr.getSuperclass();
		} while (spr != null);
		
		return null;
	}
	
	private final static class ClassPair {
		public final Class clazz;
		public final Object pair;
		public Class[] array;
		
		public ClassPair(Class clazz, Object pair) {
			this.clazz = clazz;
			this.pair = pair;
			this.array = null;
		}
		
		public ClassPair(Class clazz, Object pair, Class[] array) {
			this(clazz, pair);
			this.array = array;
		}
		
		@Override
		public boolean equals(Object other) {
			if (!(other.getClass() == ClassPair.class)) return false;
			
			ClassPair ocp = (ClassPair)other;
			return ocp.clazz.equals(clazz) && (ocp.pair != null ? ocp.pair.equals(pair) : false) && arraysEqual(ocp.array, array);
		}
		
		@Override
		public int hashCode() {
			return Objects.hashCode(clazz, pair, Arrays.hashCode(array));
		}
		
		@Override
		public String toString() {
			return Objects.toStringHelper(this).add("clazz", clazz).add("pair", pair).add("array", array).toString();
		}
		
		private static <T> boolean arraysEqual(T[] a, T[] b) {
			if (a == null && b == null) return true;
			else if (a == null || b == null) return false;
			else if (a.length != b.length) return false;
			else {
				for (int i = 0; i < a.length; i++) {
					if (!Objects.equal(a[i], b[i])) return false;
				}
				
				return true;
			}
		}
	}
}
