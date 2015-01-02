package miscperipherals.network;

import miscperipherals.core.MiscPeripherals;
import miscperipherals.core.Module;
import miscperipherals.item.ItemSmartHelmet;
import miscperipherals.peripheral.PeripheralBarrel;
import miscperipherals.peripheral.PeripheralNote;
import miscperipherals.peripheral.PeripheralTank;
import miscperipherals.tile.TileTeleporter;
import miscperipherals.util.Util;
import net.minecraft.entity.Entity;
import net.minecraft.network.NetServerHandler;
import net.minecraft.network.packet.NetHandler;
import net.minecraft.network.packet.Packet131MapData;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.liquids.LiquidStack;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import cpw.mods.fml.common.network.ITinyPacketHandler;
import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import dan200.turtle.api.ITurtleAccess;

public class TinyPacketHandler implements ITinyPacketHandler {	
	@Override
	public void handle(NetHandler handler, Packet131MapData mapData) {
		ByteArrayDataInput is = ByteStreams.newDataInput(mapData.itemData);
		MiscPeripherals.debug("Received packet "+mapData.uniqueID+" from "+handler+" (payload size "+mapData.itemData.length+")");
		
		switch (mapData.uniqueID) {
			case 0: { // S -> C
				if (handler instanceof NetServerHandler) return;
				
				double x = is.readDouble();
				double y = is.readDouble();
				double z = is.readDouble();
				int instrument = is.readUnsignedByte();
				int note = is.readUnsignedByte();
				
				PeripheralNote.playNote(MiscPeripherals.proxy.getClientWorld(), x, y, z, instrument, note);
				
				break;
			}
			case 1: { // S -> C
				if (handler instanceof NetServerHandler) return;
				
				int entity = is.readInt();
				
				Entity ent = MiscPeripherals.proxy.getClientEntityById(entity);
				if (ent != null) MiscPeripherals.proxy.spawnHeartParticles(ent);
				
				break;
			}
			case 2: { // S -> C
				if (handler instanceof NetServerHandler) return;
				
				int x = is.readInt();
				int y = is.readInt();
				int z = is.readInt();
				int liquidId = is.readShort();
				int liquidMeta = is.readShort();
				int amount = is.readInt();
				
				TileEntity te = MiscPeripherals.proxy.getClientWorld().getBlockTileEntity(x, y, z);
				if (te instanceof ITurtleAccess) {
					PeripheralTank tank = Util.getPeripheral((ITurtleAccess)te, PeripheralTank.class);
					if (tank != null) {
						tank.liquid = amount == Integer.MIN_VALUE ? null : new LiquidStack(liquidId, amount, liquidMeta);
					}
				}
				
				break;
			}
			case 3: { // S -> C
				if (handler instanceof NetServerHandler) return;
				
				int x = is.readInt();
				int y = is.readInt();
				int z = is.readInt();
				
				TileEntity te = MiscPeripherals.proxy.getClientWorld().getBlockTileEntity(x, y, z);
				if (te instanceof IEntityAdditionalSpawnData) {
					((IEntityAdditionalSpawnData)te).readSpawnData(is);
				}
				
				break;
			}
			case 4: { // S -> C
				if (handler instanceof NetServerHandler) return;
				
				int x = is.readInt();
				int y = is.readInt();
				int z = is.readInt();
				byte flag = is.readByte();
				
				TileEntity te = MiscPeripherals.proxy.getClientWorld().getBlockTileEntity(x, y, z);
				if (te instanceof TileTeleporter) {
					((TileTeleporter)te).onTeleport(flag);
				}
				
				break;
			}
			case 5: {
				if (handler instanceof NetServerHandler) return;
				
				int x = is.readInt();
				int y = is.readInt();
				int z = is.readInt();
				int amount = is.readInt();
				int maxSize = is.readInt();
				
				TileEntity te = MiscPeripherals.proxy.getClientWorld().getBlockTileEntity(x, y, z);
				if (te instanceof ITurtleAccess) {
					PeripheralBarrel barrel = Util.getPeripheral((ITurtleAccess)te, PeripheralBarrel.class);
					if (barrel != null) {
						barrel.clientAmount = amount;
						barrel.maxSize = maxSize;
					}
				}
				
				break;
			}
			case 6: {
				String module = is.readUTF();
				Module mod = MiscPeripherals.instance.modules.get(module);
				if (mod == null) {
					MiscPeripherals.log.warning("Received packet for unknown module "+module);
					break;
				}
				mod.handleNetworkMessage(handler, handler instanceof NetServerHandler, is);
				
				break;
			}
			case 7: {
				if (handler instanceof NetServerHandler) return;
				
				for (int i = 1; i < ItemSmartHelmet.clientLines.length; i++) {
					ItemSmartHelmet.clientLines[i - 1] = ItemSmartHelmet.clientLines[i];
				}
				
				int idx = ItemSmartHelmet.clientLines.length - 1;
				String payload = is.readUTF();
				if (payload == null) ItemSmartHelmet.clientLines[idx] = "";
				else {
					ItemSmartHelmet.clientLines[idx] = payload;
					if (ItemSmartHelmet.clientLines[idx] == null) ItemSmartHelmet.clientLines[idx] = "nil";
				}
				
				break;
			}
			case 8: {
				if (handler instanceof NetServerHandler) return;
				
				double x = is.readDouble();
				double y = is.readDouble();
				double z = is.readDouble();
				String text = is.readUTF();
				double speed = is.readDouble();
				
				MiscPeripherals.proxy.speak(text, speed, x, y, z);
				
				break;
			}
		}
	}
}
