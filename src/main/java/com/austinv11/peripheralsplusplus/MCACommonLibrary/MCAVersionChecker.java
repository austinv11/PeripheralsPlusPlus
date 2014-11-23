package com.austinv11.peripheralsplusplus.MCACommonLibrary;

import com.austinv11.peripheralsplusplus.utils.Logger;

public class MCAVersionChecker {
	public static final int VersionID = 1;
	
	/** Checks for the right version of the library. Should be called by each model class. */
	public static void checkForLibraryVersion(Class modelClass, int modelVersion)
	{
		if(modelVersion > VersionID)
		{
			Logger.error("MCA WARNING: "+modelClass.getName()+" needs a newer version of the library ("+modelVersion+"). Tell the mod author immediately!!");
		}
	}
}
