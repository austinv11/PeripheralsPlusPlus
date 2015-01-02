package miscperipherals.tile;

import miscperipherals.core.MiscPeripherals;
import miscperipherals.network.NetworkHelper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.util.Facing;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;

public class Tile extends TileEntity implements IEntityAdditionalSpawnData {
	protected boolean loaded = false;
	private boolean active = false;
	private int facing = 4;
	public String displayName = null;
	
	public Tile() {
		super();
	}
	
	@Override
	public void validate() {
		super.validate();
		
		if (!loaded) {
			if (!isInvalid() && worldObj != null) {
				onLoaded();
			} else {
				MiscPeripherals.log.warning(this+" ("+xCoord+","+yCoord+","+zCoord+") was not initialized due to a null world! This should never happen!");
			}
		}
	}
	
	@Override
	public void invalidate() {
		super.invalidate();
		
		if (loaded) onUnloaded();
	}
	
	@Override
	public void onChunkUnload() {
		super.onChunkUnload();
		
		if (loaded) onUnloaded();
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		
		facing = tag.getByte("facing");
		active = tag.getBoolean("active");
		if (tag.hasKey("displayName")) displayName = tag.getString("displayName");
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		
		tag.setByte("facing", (byte)facing);
		tag.setBoolean("active", active);
		if (displayName != null) tag.setString("displayName", displayName);
	}
	
	@Override
	public Packet getDescriptionPacket() {
		return NetworkHelper.getTileInfoPacket(this);
	}
	
	@Override
	public void writeSpawnData(ByteArrayDataOutput data) {
		data.writeByte(facing);
		
		data.writeByte(
			(active              ? 1 : 0) << 0 |
			(displayName != null ? 1 : 0) << 1
		);
		
		if (displayName != null) {
			data.writeUTF(displayName);
		}
	}

	@Override
	public void readSpawnData(ByteArrayDataInput data) {
		setFacing(data.readByte());
		
		byte flags = data.readByte();
		setActive((flags & (1 << 0)) != 0);
		
		if ((flags & (1 << 1)) != 0) {
			displayName = data.readUTF();
		}
	}
	
	public void onLoaded() {
		loaded = true;
	}
	
	public void onUnloaded() {
		loaded = false;
	}
	
	public void setActive(boolean active) {
		if (this.active != active) {
			this.active = active;
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		}
	}
	
	public boolean isActive() {
		return active;
	}
	
	public void setFacing(int facing) {
		if (this.facing != facing) {
			this.facing = facing;
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		}
	}
	
	public short getFacing() {
		return (short)facing;
	}
	
	public int getGuiId() {
		return -1;
	}
	
	public boolean onBlockActivated(EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		int id = getGuiId();
		if (id < 0) return false;
		player.openGui(MiscPeripherals.instance, id, worldObj, xCoord, yCoord, zCoord);
		
		return true;
	}

	public void onPlaced(World world, int x, int y, int z, EntityLiving entity, ItemStack stack) {
		if (stack.hasDisplayName()) {
			displayName = stack.getDisplayName();
		}
	}
	
	public int getRedstoneInput() {
		return worldObj == null ? 0 : worldObj.getBlockPowerInput(xCoord, yCoord, zCoord);
	}
	
	public int getRedstoneInput(int side) {
		return worldObj == null ? 0 : worldObj.isBlockProvidingPowerTo(xCoord + Facing.offsetsXForSide[side], yCoord + Facing.offsetsYForSide[side], zCoord + Facing.offsetsZForSide[side], side);
	}
	
	public int getRedstone(int side) {
		return 0;
	}

	public int getComparator(int side) {
		return 0;
	}
	
	public String getInventoryName() {
		return "Unnamed Inventory";
	}
}
