package gregtechmod.api;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;

/**
 * You are allowed to include this File in your Download, as i will not change it.
 */
public interface IDebugableBlock {
	
	/**
	 * Returns a Debugmessage, for a generic DebugItem
	 * @param aPlayer the Player, who rightclicked with his Debugitem
	 * @param aX Block-Coordinate
	 * @param aY Block-Coordinate
	 * @param aZ Block-Coordinate
	 * @param aLogLevel the Loglevel of the Debugitem. 0 = low; 3 = all;
	 * @return a String-Array containing the DebugInfo, every Index is a seperate line (0 = first Line)
	 */
	public ArrayList<String> getDebugInfo(EntityPlayer aPlayer, int aX, int aY, int aZ, int aLogLevel);
}