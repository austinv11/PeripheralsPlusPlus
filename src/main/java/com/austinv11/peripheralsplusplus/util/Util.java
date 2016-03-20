package com.austinv11.peripheralsplusplus.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Util {
	/**
	 * Converts the passed list to an indexed map.
	 * NOTE: The returned Map is ONE-indexed, rather than zero for easier interaction with Lua.
	 */
	public static Map<Integer, Object> listToIndexedMap(List<?> list) {
		Map<Integer, Object> indexedMap = new HashMap<Integer, Object>();
		for (Object obj : list) {
			indexedMap.put(list.indexOf(obj) + 1, obj);
		}
		return indexedMap;
	}
}
