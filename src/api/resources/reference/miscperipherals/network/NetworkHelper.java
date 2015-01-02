package miscperipherals.network;

import miscperipherals.core.MiscPeripherals;
import miscperipherals.core.Module;
import net.minecraft.network.packet.Packet131MapData;
import net.minecraft.tileentity.TileEntity;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;

public class NetworkHelper {
	public static Packet131MapData getTileInfoPacket(TileEntity tile) {
		if (!(tile instanceof IEntityAdditionalSpawnData)) throw new IllegalArgumentException("Tile information packets require a TileEntity implementing IEntityAdditionalSpawnData");
		IEntityAdditionalSpawnData data = (IEntityAdditionalSpawnData)tile;
		
		ByteArrayDataOutput os = ByteStreams.newDataOutput();
		os.writeInt(tile.xCoord);
		os.writeInt(tile.yCoord);
		os.writeInt(tile.zCoord);
		data.writeSpawnData(os);
		
		return PacketDispatcher.getTinyPacket(MiscPeripherals.instance, (short)3, os.toByteArray());
	}
	
	public static Packet131MapData getModulePacket(Module module, byte[] data) {
		ByteArrayDataOutput os = ByteStreams.newDataOutput();
		os.writeUTF(module.getClass().getSimpleName().substring(6));
		os.write(data);
		
		return PacketDispatcher.getTinyPacket(MiscPeripherals.instance, (short)6, os.toByteArray());
	}
}
