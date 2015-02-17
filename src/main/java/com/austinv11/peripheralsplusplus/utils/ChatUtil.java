package com.austinv11.peripheralsplusplus.utils;

import com.austinv11.peripheralsplusplus.PeripheralsPlusPlus;
import com.austinv11.peripheralsplusplus.network.ChatPacket;
import cpw.mods.fml.common.network.NetworkRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import java.util.List;

public class ChatUtil {

	public static void sendMessage(TileEntity te, String text, double range, boolean unlimitedY) {
		if (unlimitedY) {
			for (EntityPlayer player : (Iterable<EntityPlayer>) te.getWorldObj().playerEntities) {
				Vec3 playerPos = player.getPosition(1f);
					playerPos.yCoord = te.yCoord;
			}
		}else {
			PeripheralsPlusPlus.NETWORK.sendToAllAround(new ChatPacket(text), new NetworkRegistry.TargetPoint(te.getWorldObj().provider.dimensionId, te.xCoord, te.yCoord, te.zCoord, range));
		}
	}

	public static void sendMessage(Entity ent, ChatComponentText text, double range, boolean unlimitedY) {
		for (EntityPlayer player : (Iterable<EntityPlayer>) ent.worldObj.playerEntities) {
			Vec3 playerPos = player.getPosition(1f);
			if (unlimitedY)
				playerPos.yCoord = ent.posY;
			if (playerPos.distanceTo(Vec3.createVectorHelper(ent.posX,ent.posY,ent.posZ)) > range)
				continue;
			player.addChatComponentMessage(text);
		}
	}

	public static boolean sendMessage(String ign, TileEntity te, String text, double range, boolean unlimitedY) {
		EntityPlayer player = getPlayer(ign, te.getWorldObj());
		if (player != null) {
			Vec3 playerPos = player.getPosition(1f);
			if (unlimitedY)
				playerPos.yCoord = te.yCoord;
			if (playerPos.distanceTo(Vec3.createVectorHelper(te.xCoord,te.yCoord,te.zCoord)) > range)
				return false;
			PeripheralsPlusPlus.NETWORK.sendTo(new ChatPacket(text), (EntityPlayerMP) player);
			return true;
		}
		return false;
	}

	private static EntityPlayer getPlayer(String ign, World w) {
		List<EntityPlayer> players = w.playerEntities;
		for (EntityPlayer p : players) {
			if (p.getDisplayName().equalsIgnoreCase(ign))
				return p;
		}
		return null;
	}

	public static String getCoordsPrefix(TileEntity te) {
		return "[#" + te.xCoord + "," + te.yCoord + "," + te.zCoord + "] ";
	}

	public static String getCoordsPrefix(Entity ent) {
		return "[#" + ent.posX + "," + ent.posY + "," + ent.posZ + "] ";
	}
}
