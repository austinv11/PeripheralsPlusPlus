package miscperipherals.tile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import miscperipherals.api.IInteractiveSorterOutput;
import miscperipherals.core.LuaManager;
import miscperipherals.core.TickHandler;
import miscperipherals.network.GuiHandler;
import miscperipherals.util.Util;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Facing;
import net.minecraft.world.World;
import cpw.mods.fml.common.FMLCommonHandler;
import dan200.computer.api.IComputerAccess;
import dan200.computer.api.IPeripheral;

public class TileInteractiveSorter extends TileInventory implements IPeripheral {
	private Map<IComputerAccess, Boolean> computers = new WeakHashMap<IComputerAccess, Boolean>();
	private ItemStack lastStack;
	private int direction = -1;
	private int amount = 0;
	private int activeTicks = 0;
	private boolean tcDone = false;
	
	public TileInteractiveSorter() {
		super(1);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		
		if (compound.hasKey("lastStack")) {
			lastStack = ItemStack.loadItemStackFromNBT(compound.getCompoundTag("lastStack"));
		}
	}
	
	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		
		if (lastStack != null) {
			NBTTagCompound lastStackCompound = new NBTTagCompound();
			lastStack.writeToNBT(lastStackCompound);
			compound.setCompoundTag("lastStack", lastStackCompound);
		}
	}
	
	@Override
	public void onInventoryChanged() {
		ItemStack stack = getStackInSlot(0);
		
		if (stack != null && stack != lastStack) {
			lastStack = stack;
			for (IComputerAccess computer : computers.keySet()) {
				computer.queueEvent("isort_item", new Object[] {Util.getUUID(stack), stack.stackSize});
			}
		}
	}

	@Override
	public String getType() {
		return "interactiveSorter";
	}

	@Override
	public String[] getMethodNames() {
		return new String[] {"sort", "list", "extract", "getItem"};
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, int method, Object[] arguments) throws Exception {
		switch (method) {
			case 0: {
				if (arguments.length < 1) throw new Exception("too few arguments");
				else if (!(arguments[0] instanceof Double)) throw new Exception("bad argument #1 (expected number)");
				else if (arguments.length > 1 && !(arguments[1] instanceof Double)) throw new Exception("bad argument #2 (expected number)");
				
				int direction = (int)Math.floor((Double)arguments[0]);
				if (direction < 0 || direction > 5) throw new Exception("bad direction "+direction+" (expected 0-5)");
				
				while (this.direction != -1) Thread.sleep(10L); // wait until it's not busy
				
				ItemStack stack = getStackInSlot(0);
				if (stack == null) return new Object[] {false};
				
				if (arguments.length > 1) amount = (int)Math.floor((Double)arguments[1]);
				else amount = stack.stackSize;
				if (amount < 1 || amount > stack.stackSize) return new Object[] {false};
				
				this.direction = direction;
				
				return new Object[] {true};
			}
			case 1: {
				if (arguments.length < 1) throw new Exception("too few arguments");
				else if (!(arguments[0] instanceof Double)) throw new Exception("bad argument #1 (expected number)");
				
				final int direction = (int)Math.floor((Double)arguments[0]);
				if (direction < 0 || direction > 5) throw new Exception("bad direction "+direction+" (expected 0-5)");
				
				Future<Map<Long, Integer>> callback = TickHandler.addTickCallback(worldObj, new Callable<Map<Long, Integer>>() {
					@Override
					public Map<Long, Integer> call() {
						TileEntity te = worldObj.getBlockTileEntity(xCoord + Facing.offsetsXForSide[direction], yCoord + Facing.offsetsYForSide[direction], zCoord + Facing.offsetsZForSide[direction]);
						if (!(te instanceof IInventory)) return null;
						
						return makeInventoryMap((IInventory) te);
					}
				});
				
				return new Object[] {callback.get()};
			}
			case 2: {
				if (arguments.length < 3) throw new Exception("too few arguments");
				else if (!(arguments[0] instanceof Double)) throw new Exception("bad argument #1 (expected number)");
				else if (!(arguments[1] instanceof Double)) throw new Exception("bad argument #2 (expected number)");
				else if (!(arguments[2] instanceof Double)) throw new Exception("bad argument #3 (expected number)");
				
				final int direction = (int)Math.floor((Double)arguments[0]);
				if (direction < 0 || direction > 5) throw new Exception("bad direction "+direction+" (expected 0-5)");
				
				final int uuid = (int)Math.floor((Double)arguments[1]);
				
				final int outDir = (int)Math.floor((Double)arguments[2]);
				if (outDir < 0 || outDir > 5) throw new Exception("bad output direction "+outDir+" (expected 0-5)");
				
				int tamount = Integer.MIN_VALUE;
				if (arguments.length > 3) {
					if (!(arguments[3] instanceof Double)) throw new Exception("bad argument #4 (expected number)");
					tamount = Math.abs((int)Math.floor((Double)arguments[3]));
				}
				final int amount = tamount;
				
				Future<Integer> callback = TickHandler.addTickCallback(worldObj, new Callable<Integer>() {
					@Override
					public Integer call() {
						TileEntity te = worldObj.getBlockTileEntity(xCoord + Facing.offsetsXForSide[direction], yCoord + Facing.offsetsYForSide[direction], zCoord + Facing.offsetsZForSide[direction]);
						if (!(te instanceof IInventory)) return 0;
						
						List<ItemStack> stacks = new ArrayList<ItemStack>();
						int remain = amount;
						IInventory inv = (IInventory)te;
						for (int i = 0; i < inv.getSizeInventory(); i++) {
							ItemStack slotstack = inv.getStackInSlot(i);
							if (slotstack != null && Util.getUUID(slotstack) == uuid) {
								int adding = Math.min(slotstack.stackSize, remain);
								remain -= adding;
								
								ItemStack output = slotstack.copy();
								output.stackSize = adding;
								stacks.add(output);
								
								slotstack.stackSize -= adding;
								if (slotstack.stackSize <= 0) slotstack = null;
								inv.setInventorySlotContents(i, slotstack);
								
								if (remain <= 0) break;
							}
						}
						
						for (int i = 0; i < stacks.size(); i++) {
							outputItem(TileInteractiveSorter.this, stacks.get(i), outDir);
						}
						
						return amount - remain;
					}
				});
				
				return new Object[] {callback.get()};
			}
			case 5: {
				ItemStack stack = getStackInSlot(0);
				if (stack == null) return new Object[] {null, 0};
				else return new Object[] {Util.getUUID(stack), stack.stackSize};
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
		
		computers.put(computer, true);
	}

	@Override
	public void detach(IComputerAccess computer) {
		computers.remove(computer);
	}
	
	@Override
	public boolean canUpdate() {
		return !FMLCommonHandler.instance().getEffectiveSide().isClient();
	}
	
	@Override
	public void updateEntity() {
		super.updateEntity();
		
		if (activeTicks > 0) {
			if (--activeTicks <= 0) setActive(false);
			else setActive(true);
		}
		
		if (direction > -1) {
			ItemStack stack = getStackInSlot(0);
			int oldSize = stack.stackSize;
			stack.stackSize = amount;
			
			outputItem(this, stack, direction);
			
			stack.stackSize = oldSize - amount;
			if (stack.stackSize <= 0) stack = null;
			setInventorySlotContents(0, stack);
			activeTicks = 10;
			direction = -1;
		}
	}
	
	@Override
	public int getGuiId() {
		return GuiHandler.SINGLE_SLOT;
	}
	
	@Override
	public String getInventoryName() {
		return "Interactive Sorter";
	}
	
	public static void outputItem(TileEntity tile, ItemStack stack, int direction) {
		for (IInteractiveSorterOutput output : IInteractiveSorterOutput.handlers) {
			output.output(stack, tile.worldObj, tile.xCoord,tile. yCoord, tile.zCoord, direction);
			if (stack.stackSize <= 0) break;
		}
		
		if (stack.stackSize > 0) { // drop
			float xoff = tile.worldObj.rand.nextFloat() * 0.8F + 0.1F + (0.5F * Facing.offsetsXForSide[direction]);
			float yoff = tile.worldObj.rand.nextFloat() * 0.8F + 0.1F + (0.5F * Facing.offsetsYForSide[direction]);
			float zoff = tile.worldObj.rand.nextFloat() * 0.8F + 0.1F + (0.5F * Facing.offsetsZForSide[direction]);
			
			EntityItem item = new EntityItem(tile.worldObj, tile.xCoord + xoff, tile.yCoord + yoff, tile.zCoord + zoff, stack.copy());
			item.delayBeforeCanPickup = 10;
			item.motionX = (float)tile.worldObj.rand.nextGaussian() * 0.05F + Facing.offsetsXForSide[direction];
			item.motionY = (float)tile.worldObj.rand.nextGaussian() * 0.05F + Facing.offsetsYForSide[direction];
			item.motionZ = (float)tile.worldObj.rand.nextGaussian() * 0.05F + Facing.offsetsZForSide[direction];
			
			tile.worldObj.spawnEntityInWorld(item);
			stack.stackSize = 0;
		}
	}
	
	public static Map<Long, Integer> makeInventoryMap(IInventory inv) {
		Map<Long, Integer> items = new HashMap<Long, Integer>();
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack slotstack = inv.getStackInSlot(i);
			if (slotstack == null) continue;
			long uuid = Util.getUUID(slotstack);
			if (items.containsKey(uuid)) items.put(uuid, items.get(uuid) + slotstack.stackSize);
			else items.put(uuid, slotstack.stackSize);
		}
		
		return items;
	}
	
	public static class InventoryInteractiveSorterOutput implements IInteractiveSorterOutput {
		@Override
		public void output(ItemStack stack, World world, int posX, int posY, int posZ, int direction) {
			TileEntity te = world.getBlockTileEntity(posX + Facing.offsetsXForSide[direction], posY + Facing.offsetsYForSide[direction], posZ + Facing.offsetsZForSide[direction]);
			if (stack != null && te instanceof IInventory) { // try inventory
				IInventory inv = (IInventory)te;
				int[] slots = Util.getInventorySlots(inv, Util.OPPOSITE[direction]);
				
				for (int i = 0; i < slots.length; i++) {
					int slot = slots[i];
					ItemStack slotStack = inv.getStackInSlot(slot);
					
					if (slotStack == null) {
						ItemStack put = stack.copy();
						put.stackSize = Math.min(stack.stackSize, inv.getInventoryStackLimit());
						inv.setInventorySlotContents(slot, put);
						
						stack.stackSize -= put.stackSize;
						if (stack.stackSize <= 0) break;
					} else if (Util.areStacksEqual(slotStack, stack)) {
						ItemStack put = slotStack.copy();
						int toPut = Math.min(stack.stackSize, Math.min(slotStack.getMaxStackSize() - slotStack.stackSize, inv.getInventoryStackLimit() - slotStack.stackSize));
						put.stackSize += toPut;
						inv.setInventorySlotContents(slot, put);
						
						stack.stackSize -= toPut;
						if (stack.stackSize <= 0) break;
					}
				}
			}	
		}
	}
	
	static {
		IInteractiveSorterOutput.handlers.add(new InventoryInteractiveSorterOutput());
	}
}
