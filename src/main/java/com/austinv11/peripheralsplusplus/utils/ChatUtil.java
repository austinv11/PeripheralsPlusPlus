package com.austinv11.peripheralsplusplus.utils;

import com.austinv11.peripheralsplusplus.PeripheralsPlusPlus;
import com.austinv11.peripheralsplusplus.network.ChatPacket;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import java.util.ArrayList;
import java.util.List;

public class ChatUtil {

	public static void sendMessage(TileEntity te, String text, double range, boolean unlimitedY) {
		if (range == Double.MAX_VALUE) {
		    for (WorldServer worldServer : DimensionManager.getWorlds())
		        for (EntityPlayer player : worldServer.playerEntities)
					PeripheralsPlusPlus.NETWORK.sendTo(new ChatPacket(text), (EntityPlayerMP) player);
		} else if (unlimitedY) {
			for (EntityPlayer player : te.getWorld().playerEntities) {
				Vec3d playerPos = new Vec3d(player.posX, te.getPos().getY(), player.posZ);
				if (playerPos.distanceTo(new Vec3d(te.getPos().getX(), te.getPos().getY(),
                        te.getPos().getZ())) > range)
					continue;
				PeripheralsPlusPlus.NETWORK.sendTo(new ChatPacket(text), (EntityPlayerMP) player);
			}
		} else {
			PeripheralsPlusPlus.NETWORK.sendToAllAround(new ChatPacket(text),
                    new NetworkRegistry.TargetPoint(te.getWorld().provider.getDimension(), te.getPos().getX(),
                            te.getPos().getY(), te.getPos().getZ(), range));
		}
	}

	public static boolean sendMessage(String ign, TileEntity te, String text, double range, boolean unlimitedY) {
		EntityPlayer player = getPlayer(ign, range == Double.MAX_VALUE ? null : te.getWorld());
		if (player != null) {
			Vec3d playerPos = new Vec3d(player.posX,
                    unlimitedY ? te.getPos().getY() : player.posY,
                    player.posZ);
			if (playerPos.distanceTo(new Vec3d(te.getPos().getX(),te.getPos().getY(),te.getPos().getZ())) > range)
				return false;
			PeripheralsPlusPlus.NETWORK.sendTo(new ChatPacket(text), (EntityPlayerMP) player);
			return true;
		}
		return false;
	}

	private static EntityPlayer getPlayer(String ign, World w) {
		List<EntityPlayer> players = new ArrayList<>();
		if (w == null)
		    for (WorldServer worldServer : DimensionManager.getWorlds())
				players.addAll(worldServer.playerEntities);
		else
		    players = w.playerEntities;
		for (EntityPlayer p : players) {
			if (p.getDisplayNameString().equalsIgnoreCase(ign))
				return p;
		}
		return null;
	}

	public static String getCoordsPrefix(TileEntity te) {
		return "[#" + te.getPos().getX() + "," + te.getPos().getY() + "," + te.getPos().getZ() + "] ";
	}

	public static String getCoordsPrefix(Entity ent) {
		return "[#" + ent.posX + "," + ent.posY + "," + ent.posZ + "] ";
	}
}