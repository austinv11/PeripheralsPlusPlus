package miscperipherals.peripheral;

import miscperipherals.core.LuaManager;
import miscperipherals.util.Util;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import dan200.computer.api.IComputerAccess;
import dan200.computer.api.IHostedPeripheral;
import dan200.turtle.api.ITurtleAccess;

public class PeripheralChest implements IHostedPeripheral {
	private final ITurtleAccess turtle;
	private final int pages;
	private IInventory inventory;
	private int curPage = 1;
	
	public PeripheralChest(ITurtleAccess turtle, int pages) {
		this.turtle = turtle;
		this.pages = pages;
		
		inventory = new InventoryBasic("Turtle", false, turtle.getInventorySize() * pages);
	}
	
	@Override
	public String getType() {
		return "chest";
	}

	@Override
	public String[] getMethodNames() {
		return new String[] {"swap", "swapRange", "getPages", "setPage", "getPage"};
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, int method, Object[] arguments) throws Exception {
		switch (method) {
			case 0: {
				int baseSlot = curPage * turtle.getInventorySize();
				if (arguments.length > 1) {
					int[] slots = new int[arguments.length];
					for (int i = 0; i < arguments.length; i++) {
						if (!(arguments[i] instanceof Double)) throw new Exception("bad argument #" + (i + 1) + " (expected number)");
						else {
							int slot = (int) Math.floor((Double) arguments[i]) - 1;
							if (slot < 0 || slot > turtle.getInventorySize()) throw new Exception("bad slot " + (slot + 1) + " at argument " + (i + 1) + " (expected 1-16)");
						}
					}
					
					for (int i = 0; i < slots.length; i++) {
						ItemStack stack = inventory.getStackInSlot(baseSlot + slots[i]);
						inventory.setInventorySlotContents(baseSlot + slots[i], turtle.getSlotContents(slots[i]));
						turtle.setSlotContents(slots[i], stack);
					}
				} else {
					for (int i = 0; i < turtle.getInventorySize(); i++) {
						ItemStack stack = inventory.getStackInSlot(baseSlot + i);
						inventory.setInventorySlotContents(baseSlot + i, turtle.getSlotContents(i));
						turtle.setSlotContents(i, stack);
					}
				}
				
				return new Object[0];
			}
			case 1: {
				if (arguments.length < 2) throw new Exception("too few arguments");
				else if (!(arguments[0] instanceof Double)) throw new Exception("bad argument #1 (expected number)");
				else if (!(arguments[1] instanceof Double)) throw new Exception("bad argument #2 (expected number)");
				
				int start = (int) Math.floor((Double) arguments[0]) - 1;
				if (start < 0 || start > turtle.getInventorySize()) throw new Exception("bad start slot " + (start + 1) + " (expected 1-16)");
				
				int end = (int) Math.floor((Double) arguments[1]) - 1;
				if (end < 0 || start > turtle.getInventorySize()) throw new Exception("bad end slot " + (end + 1) + " (expected 1-16)");
				
				int baseSlot = curPage * turtle.getInventorySize();
				for (int i = Math.min(start, end); i <= Math.max(start, end); i++) {
					ItemStack stack = inventory.getStackInSlot(baseSlot + i);
					inventory.setInventorySlotContents(baseSlot + i, turtle.getSlotContents(i));
					turtle.setSlotContents(i, stack);
				}
			}
			case 2: {
				return new Object[] {pages};
			}
			case 3: {
				if (arguments.length < 1) throw new Exception("too few arguments");
				else if (!(arguments[0] instanceof Double)) throw new Exception("bad argument #1 (expected number)");
				
				int page = (int) Math.floor((Double) arguments[0]);
				if (page < 1 || page > pages) throw new Exception("bad page " + page + " (expected 1-" + pages + ")");
				
				curPage = page;				
				return new Object[0];
			}
			case 4: {
				return new Object[] {curPage};
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
		
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		Util.readInventoryFromNBT(inventory, nbttagcompound.getTagList("Items"));
		curPage = nbttagcompound.getInteger("curPage");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		nbttagcompound.setTag("Items", Util.writeInventoryToNBT(inventory));
		nbttagcompound.setInteger("curPage", curPage);
	}
}
