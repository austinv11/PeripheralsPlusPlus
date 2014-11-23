package com.austinv11.peripheralsplusplus.MCACommonLibrary.animation;

import com.austinv11.peripheralsplusplus.MCACommonLibrary.math.Quaternion;
import com.austinv11.peripheralsplusplus.MCACommonLibrary.math.Vector3f;

import java.util.HashMap;

public class KeyFrame {
	public HashMap<String, Quaternion> modelRenderersRotations = new HashMap<String, Quaternion>();
	public HashMap<String, Vector3f> modelRenderersTranslations = new HashMap<String, Vector3f>();
	
	public boolean useBoxInRotations(String boxName)
	{
		return modelRenderersRotations.get(boxName) != null;
	}
	
	public boolean useBoxInTranslations(String boxName)
	{
		return modelRenderersTranslations.get(boxName) != null;
	}
}
