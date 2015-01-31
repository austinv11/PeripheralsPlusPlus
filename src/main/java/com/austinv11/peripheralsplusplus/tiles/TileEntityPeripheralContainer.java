package com.austinv11.peripheralsplusplus.tiles;

import com.austinv11.peripheralsplusplus.init.ModBlocks;
import com.austinv11.peripheralsplusplus.lua.LuaObjectPeripheralWrap;
import com.austinv11.peripheralsplusplus.reference.Reference;
import com.austinv11.peripheralsplusplus.utils.NBTHelper;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TileEntityPeripheralContainer extends MountedNetworkedTileEntity {

	private List<Block> blocksContained = new ArrayList<Block>();
	private List<IPeripheral> peripheralsContained = new ArrayList<IPeripheral>();
	public static String publicName = "peripheralContainer";
	private  String name = "tileEntityPeripheralContainer";
	private boolean needsUpdate = false;

	public TileEntityPeripheralContainer() {
		super();
	}

	public String getName() {
		return name;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		int[] ids = nbttagcompound.getIntArray("ids");
		for (int id : ids)
			addPeripheral(Block.getBlockById(id));
		for (IPeripheral p : peripheralsContained) {
			NBTTagCompound tag = nbttagcompound.getCompoundTag(p.getType());
			((TileEntity) p).readFromNBT(tag);
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
		int[] ids = new int[blocksContained.size()];
		for (int i = 0; i < ids.length; i++)
			ids[i] = Block.getIdFromBlock(blocksContained.get(i));
		nbttagcompound.setIntArray("ids", ids);
		for (IPeripheral p : peripheralsContained) {
			NBTTagCompound tag = new NBTTagCompound();
			((TileEntity) p).writeToNBT(tag);
			nbttagcompound.setTag(p.getType(), tag);
		}
	}

	@Override
	public String getType() {
		return publicName;
	}

	@Override
	public String[] getMethodNames() {
		return new String[]{"getContainedPeripherals", "wrapPeripheral"};
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws LuaException, InterruptedException {
		if (method == 0) {
			HashMap<Integer, String> returnVals = new HashMap<Integer,String>();
			for (int i = 0; i < peripheralsContained.size(); i++)
				returnVals.put(i+1, peripheralsContained.get(i).getType());
			return new Object[]{returnVals};
		}else if (method == 1) {
			if (arguments.length < 1)
				throw new LuaException("Too few arguments");
			if (!(arguments[0] instanceof String))
				throw new LuaException("Bad argument #1 (expected string)");
			return new Object[]{new LuaObjectPeripheralWrap(getPeripheralByName((String)arguments[0]), computer)};
		}
		return new Object[0];
	}

	@Override
	public boolean equals(IPeripheral other) {
		return (this == other);
	}

	@Override
	public void updateEntity() {
		for (IPeripheral te : peripheralsContained) {
			((TileEntity) te).updateEntity();
			if (needsUpdate) {
				worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
				((TileEntity) te).setWorldObj(worldObj);
				((TileEntity) te).xCoord = xCoord;
				((TileEntity) te).yCoord = yCoord;
				((TileEntity) te).zCoord = zCoord;
			}
		}
	}

	public void addPeripheral(Block peripheral) {
		blocksContained.add(peripheral);
		TileEntity peripheral_ = peripheral.createTileEntity(worldObj, 0);
		peripheralsContained.add((IPeripheral)peripheral_);
		markDirty();
		if (worldObj != null) {
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
			peripheral_.setWorldObj(worldObj);
			peripheral_.xCoord = xCoord;
			peripheral_.yCoord = yCoord;
			peripheral_.zCoord = zCoord;
		} else
			needsUpdate = true;
	}

	public List<Block> getBlocksContained() {
		return blocksContained;
	}

	private IPeripheral getPeripheralByName(String peripheral) {
		for (IPeripheral p_ : peripheralsContained)
			if (p_.getType().equals(peripheral))
				return p_;
		return null;
	}

	@Override
	public void attach(IComputerAccess computer) {
		for (IPeripheral p : peripheralsContained)
			p.attach(computer);
		super.attach(computer);
	}

	@Override
	public void detach(IComputerAccess computer) {
		for (IPeripheral p : peripheralsContained)
			p.detach(computer);
		super.detach(computer);
	}

	@Override
	public void invalidate() {
		super.invalidate();
		if (!worldObj.isRemote) {
			ItemStack drop = new ItemStack(ModBlocks.peripheralContainer);
			if (peripheralsContained.size() > 0) {
				NBTTagCompound tag = new NBTTagCompound();
				this.writeToNBT(tag);
				drop.stackTagCompound = tag;
				List<String> text = new ArrayList<String>();
				text.add(Reference.Colors.RESET+Reference.Colors.UNDERLINE+"Contained Peripherals:");
				for (int id : NBTHelper.getIntArray(drop, "ids")) {
					Block peripheral = Block.getBlockById(id);
					IPeripheral iPeripheral = (IPeripheral) peripheral.createTileEntity(null, 0);
					text.add(Reference.Colors.RESET+iPeripheral.getType());
				}
				NBTHelper.setInfo(drop, text);
			}
			worldObj.spawnEntityInWorld(new EntityItem(worldObj, xCoord, yCoord+1, zCoord, drop.copy()));
		}
	}
}
