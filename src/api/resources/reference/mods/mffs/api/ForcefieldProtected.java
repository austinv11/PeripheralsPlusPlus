/*  
    Copyright (C) 2012 Thunderdark

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
    
    Contributors:
    
    Thunderdark 
    Matchlighter
 
 */

package mods.mffs.api;

import java.lang.reflect.Method;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;



public final class ForcefieldProtected {

	
	/**
	 * Basic API for Check if Block inside a Active Forcefield
	 * 
	 *  world  = the relevant Minecraft World
	 *  x,y,z  = the Block coordinates
	 *  entityPlayer = Player.for Adv. Check or null for Basic Block Inside Ckeck.
	 * 
	 * Adv. Check: 
	 * 
	 * if entityPlayer != null System will check if the projector have a link to Securtiy Station have
	 * "entityPlayer" the right Remove Protected Block (RPB). If player have the right function return true
	 * 
	 *Return:
	 *   
	 *   True:  Block is inside a Active ForceField and if set a Playername, the player is not allowed no remove it..
	 *   
	 *   False: Block is not Protected you can remove it.
	 */

	public static boolean BlockProtected(World world,int x, int y , int z, EntityPlayer entityPlayer) {
		try {

			Method method	= Class.forName("mods.mffs.common.ForceFieldOptions").getMethod("BlockProtected", World.class, Integer.TYPE, Integer.TYPE,Integer.TYPE,EntityPlayer.class);
			return (Boolean)method.invoke(null, world,x,y,z,entityPlayer);
			
		} catch (Exception e) {

			System.err.println("[ModularForceFieldSystem] API Call Fail: ForcefieldProtected.BlockProtected()" + e.getMessage());
			return false;
		}
	}

}
