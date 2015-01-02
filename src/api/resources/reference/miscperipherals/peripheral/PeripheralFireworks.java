package miscperipherals.peripheral;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

import miscperipherals.core.LuaManager;
import miscperipherals.core.MiscPeripherals;
import miscperipherals.core.TickHandler;
import miscperipherals.tile.TilePeripheralWrapper;
import miscperipherals.util.Positionable;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import dan200.computer.api.IComputerAccess;
import dan200.computer.api.IHostedPeripheral;
import dan200.turtle.api.ITurtleAccess;

public class PeripheralFireworks implements IHostedPeripheral {
	private final Positionable positionable;
	private String error;
	
	public PeripheralFireworks(ITurtleAccess turtle) {
		this.positionable = new Positionable.PositionableTurtle(turtle);
	}
	
	public PeripheralFireworks(TilePeripheralWrapper tile) {
		this.positionable = new Positionable.PositionableTile(tile);
	}
	
	@Override
	public String getType() {
		return "fireworks";
	}

	@Override
	public String[] getMethodNames() {
		return new String[] {"launch", "getError"};
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, int method, Object[] arguments) throws Exception {
		switch (method) {
			case 0: {
				if (arguments.length == 0) throw new Exception("too few arguments");
				
				List<Integer> slots = new ArrayList<Integer>();
				for (int i = 0; i < arguments.length; i++) {
					if (!(arguments[i] instanceof Double)) throw new Exception("bad argument #"+(i+1)+" (expected number)");
					slots.add((int)Math.floor((Double)arguments[i]));
				}
				
				IInventory inv = positionable.getInventory();
				if (inv == null) {
					error = "No inventory";
					return new Object[] {false};
				}
				
				int[] slotReferencesLeft = new int[inv.getSizeInventory()];
				Arrays.fill(slotReferencesLeft, Integer.MAX_VALUE);
				List<Integer> star = new ArrayList<Integer>();
				List<Integer> starFade = new ArrayList<Integer>();
				List<Integer> rocket = new ArrayList<Integer>();
				boolean gunpowderFound = false;
				for (int i = 0; i < slots.size(); i++) {
					int slot = slots.get(i) - 1;
					if (slot < 0 || slot >= inv.getSizeInventory()) {
						error = "Slot out of bounds: "+(slot+1)+" (argument "+(i+1)+")";
						return new Object[] {false};
					}
					ItemStack stack = inv.getStackInSlot(slot);
					if (stack == null) continue;
					
					if (slotReferencesLeft[slot] == Integer.MAX_VALUE) slotReferencesLeft[slot] = stack.stackSize;
					if (--slotReferencesLeft[slot] < 0) continue;
					
					if (stack.itemID == Item.dyePowder.itemID) {
						if (gunpowderFound) star.add(slot);
						else starFade.add(slot);
					} else if (stack.itemID == Item.gunpowder.itemID) {
						gunpowderFound = true;
						rocket.add(slot);
					} else {
						star.add(slot);
					}
				}
				
				if (MiscPeripherals.DEBUG) MiscPeripherals.log.info("star="+star+" starFade="+starFade+" rocket="+rocket);
				Container container = new Container() {
					@Override
					public boolean canInteractWith(EntityPlayer var1) {
						return true;
					}
				};
				final World world = positionable.getWorld();
				CraftingManager manager = CraftingManager.getInstance();
				InventoryCrafting craft;
				ItemStack starRes = null;
				if (star.size() > 0) {
					craft = new InventoryCrafting(container, star.size() + 1, 1);
					int i = 0;
					craft.setInventorySlotContents(i++, new ItemStack(Item.gunpowder));
					for (int j = 0; j < star.size(); j++) {
						craft.setInventorySlotContents(i++, inv.getStackInSlot(star.get(j)));
					}
					
					starRes = manager.findMatchingRecipe(craft, world);
					if (starRes == null) {
						error = "No star match";
						return new Object[] {false};				
					}
					
					if (starFade.size() > 0) {
						craft = new InventoryCrafting(container, starFade.size() + 1, 1);
						i = 0;
						craft.setInventorySlotContents(i++, starRes);
						for (int j = 0; j < starFade.size(); j++) {
							craft.setInventorySlotContents(i++, inv.getStackInSlot(starFade.get(j)));
						}
						
						starRes = manager.findMatchingRecipe(craft, world);
						if (starRes == null) {
							error = "No star fade match";
							return new Object[] {false};
						}
					}
				}
				
				craft = new InventoryCrafting(container, rocket.size() + 2, 1);
				int i = 0;
				craft.setInventorySlotContents(i++, new ItemStack(Item.paper));
				craft.setInventorySlotContents(i++, starRes);
				for (int j = 0; j < rocket.size(); j++) {
					craft.setInventorySlotContents(i++, inv.getStackInSlot(rocket.get(j)));
				}
				ItemStack firework = manager.findMatchingRecipe(craft, world);
				if (firework == null) {
					error = "No rocket match";
					return new Object[] {false};
				}
				
				for (i = 0; i < slots.size(); i++) {
					int slot = slots.get(i);
					ItemStack stack = inv.getStackInSlot(--slot);
					if (stack == null) continue;
					
					if (--stack.stackSize <= 0) stack = null;
					inv.setInventorySlotContents(slot, stack);
				}
				
				Vec3 pos = positionable.getPosition();
				final EntityFireworkRocket rocketEnt = new EntityFireworkRocket(world, pos.xCoord + 0.5D, pos.yCoord + 1.1D, pos.zCoord + 0.5D, firework);
				
				TickHandler.addTickCallback(world, new Callable<Object>() {
					@Override
					public Object call() throws Exception {
						world.spawnEntityInWorld(rocketEnt);				
						return null;
					}
				});
				
				return new Object[] {true};
			}
			case 1: {
				return new Object[] {error};
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
		
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		
	}
}
