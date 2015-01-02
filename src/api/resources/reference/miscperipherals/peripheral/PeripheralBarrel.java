package miscperipherals.peripheral;

import java.util.Random;

import miscperipherals.core.LuaManager;
import miscperipherals.core.MiscPeripherals;
import miscperipherals.util.Util;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import cpw.mods.fml.common.network.PacketDispatcher;
import dan200.computer.api.IComputerAccess;
import dan200.computer.api.IHostedPeripheral;
import dan200.turtle.api.ITurtleAccess;

public class PeripheralBarrel implements IHostedPeripheral {
	public static final int TICK_RATE = 20;
	
	private final ITurtleAccess turtle;
	public ItemStack item;
	public int maxSize = 4096;
	private int ticker = new Random().nextInt();
	private int prevAmount = 0;
	private int prevMaxSize = maxSize;
	public int clientAmount = 0;
	
	public PeripheralBarrel(ITurtleAccess turtle) {
		this.turtle = turtle;
	}
	
	@Override
	public String getType() {
		return "barrel";
	}

	@Override
	public String[] getMethodNames() {
		return new String[] {"get", "put", "getItem"};
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, int method, Object[] arguments) throws Exception {
		switch (method) {
			case 0: {
				if (arguments.length > 0 && !(arguments[0] instanceof Double)) throw new Exception("bad argument #1 (expected number)");
				int amount = arguments.length > 0 ? (int)Math.floor((Double)arguments[0]) : Integer.MAX_VALUE;
				
				if (item == null) return new Object[] {0};
				ItemStack slotstack = turtle.getSlotContents(turtle.getSelectedSlot());
				if (slotstack != null && item != null && !areItemStacksEqual(slotstack, item)) return new Object[] {0};
				
				int space = slotstack == null ? item.getMaxStackSize() : slotstack.getMaxStackSize() - slotstack.stackSize;
				int take = Math.min(space, Math.min(item.stackSize, amount));
				if (slotstack == null) {
					slotstack = item.copy();
					slotstack.stackSize = take;
				} else {
					slotstack.stackSize += take;
				}
				item.stackSize -= take;
				if (item.stackSize <= 0) item = null;
				turtle.setSlotContents(turtle.getSelectedSlot(), slotstack);
				
				updateAmount();
				return new Object[] {take};
			}
			case 1: {
				if (arguments.length > 0 && !(arguments[0] instanceof Double)) throw new Exception("bad argument #1 (expected number)");
				int amount = arguments.length > 0 ? (int)Math.floor((Double)arguments[0]) : Integer.MAX_VALUE;
				
				ItemStack slotstack = turtle.getSlotContents(turtle.getSelectedSlot());
				if (slotstack == null) return new Object[] {0};
				
				if (item != null && !areItemStacksEqual(slotstack, item)) return new Object[] {0};
				
				int put = Math.min(maxSize - (item == null ? 0 : item.stackSize), Math.min(slotstack.stackSize, amount));
				if (item == null) {
					item = slotstack.copy();
				} else {
					item.stackSize += put;
				}
				slotstack.stackSize -= put;
				if (slotstack.stackSize <= 0) slotstack = null;
				turtle.setSlotContents(turtle.getSelectedSlot(), slotstack);
				
				updateAmount();
				return new Object[] {put};
			}
			case 2: {
				if (item == null) return new Object[] {null, 0};
				else return new Object[] {Util.getUUID(item), item.stackSize};
			}
		}
		
		return new Object[0];
	}

	@Override
	public boolean canAttachToSide(int side) {
		return true;
	}

	@Override
	public void attach(IComputerAccess computer) {
		LuaManager.mount(computer);
	}

	@Override
	public void detach(IComputerAccess computer) {
		
	}

	@Override
	public void update() {
		if (item != null) {
			if (item.stackSize > maxSize) item.stackSize = maxSize;
			else if (item.stackSize == 0) item = null;
		}
		
		if (turtle.getWorld() != null && !turtle.getWorld().isRemote && ++ticker >= TICK_RATE) {
			updateAmount();
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		if (nbttagcompound.hasKey("itemID") && nbttagcompound.hasKey("itemSize") && nbttagcompound.hasKey("itemMeta")) {
			item = new ItemStack(nbttagcompound.getShort("itemID"), nbttagcompound.getInteger("itemSize"), nbttagcompound.getShort("itemMeta"));
			if (nbttagcompound.hasKey("itemTag")) item.stackTagCompound = nbttagcompound.getCompoundTag("itemTag");
		} else item = null;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		if (item != null) {
			nbttagcompound.setShort("itemID", (short)item.itemID);
			nbttagcompound.setInteger("itemSize", item.stackSize);
			nbttagcompound.setShort("itemMeta", (short)item.getItemDamage());
			if (item.stackTagCompound != null) nbttagcompound.setTag("itemTag", item.stackTagCompound);
		}
	}
	
	private boolean areItemStacksEqual(ItemStack a, ItemStack b) {
		if (a == null && a == null) return true;
		else if (a == null || b == null) return false;
		else return a.itemID == b.itemID && a.getItemDamage() == b.getItemDamage() && ItemStack.areItemStackTagsEqual(a, b);
	}
	
	private void updateAmount() {
		int amount = item == null ? 0 : item.stackSize;
		if (prevAmount != amount || prevMaxSize != maxSize) {
			World world = turtle.getWorld();
			Vec3 pos = turtle.getPosition();
			int x = (int)Math.floor(pos.xCoord);
			int y = (int)Math.floor(pos.yCoord);
			int z = (int)Math.floor(pos.zCoord);
			
			ByteArrayDataOutput os = ByteStreams.newDataOutput();
			os.writeInt(x);
			os.writeInt(y);
			os.writeInt(z);
			os.writeInt(amount);
			os.writeInt(maxSize);
			PacketDispatcher.sendPacketToAllAround(pos.xCoord + 0.5D, pos.yCoord + 0.5D, pos.zCoord + 0.5D, 64.0D, world.provider.dimensionId, PacketDispatcher.getTinyPacket(MiscPeripherals.instance, (short)5, os.toByteArray()));
		}
	}
}
