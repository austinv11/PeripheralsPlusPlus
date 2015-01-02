package miscperipherals.tile;

import miscperipherals.network.NetworkHelper;
import net.minecraft.network.packet.Packet;
import net.minecraft.tileentity.TileEntity;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;

public class TileLanCable extends TileEntity implements IEntityAdditionalSpawnData {
	private static final String MY_CLASS_NAME = TileLanCable.class.getName();
	
	private int type = 0;
	
	@Override
	public Packet getDescriptionPacket() {
		return NetworkHelper.getTileInfoPacket(this);
	}
	
	@Override
	public void writeSpawnData(ByteArrayDataOutput data) {
		data.writeByte(type);
	}

	@Override
	public void readSpawnData(ByteArrayDataInput data) {
		type = data.readByte();
		
		if (worldObj != null) worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}
	
	public void setType(int type) {
		if (this.type != type) {
			this.type = type;
			if (worldObj != null) worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		}
	}
	
	public int getType() {
		return type;
	}
	
	public boolean canInteractWith(TileEntity te) {
		if (te == null) return false;
		
		String cname = te.getClass().getName();
		return cname.equals(MY_CLASS_NAME) || cname.equals("dan200.computer.shared.TileEntityComputer") || cname.equals("dan200.computer.shared.TileEntityTurtle");
	}
	
	public float getThickness() {
		return 0.4F;
	}
}
