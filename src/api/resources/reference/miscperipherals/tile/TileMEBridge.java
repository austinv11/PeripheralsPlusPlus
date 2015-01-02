package miscperipherals.tile;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import miscperipherals.core.TickHandler;
import miscperipherals.util.Util;
import net.minecraft.item.ItemStack;
import appeng.api.IAEItemStack;
import appeng.api.IItemList;
import appeng.api.exceptions.AppEngTileMissingException;
import dan200.computer.api.IComputerAccess;
import dan200.computer.api.IPeripheral;

public class TileMEBridge extends TileMEBase implements IPeripheral {
	public static float ITEM_ENERGY_FACTOR = 64.0F / 3.0F;
	
	@Override
	public String getType() {
		return "meBridge";
	}

	@Override
	public String[] getMethodNames() {
		return new String[] {"listAll", "listItems", "listCraft", "retrieve", "craft"};
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, int method, Object[] arguments) throws Exception {
		switch (method) {
			case 0: {
				if (grid == null) return new Object[0];
				
				return new Object[] {getAvailableItemMap(grid.getFullCellArray().getAvailableItems())};
			}
			case 1: {
				if (grid == null) return new Object[0];
				
				return new Object[] {getAvailableItemMap(grid.getCellArray().getAvailableItems())};
			}
			case 2: {
				if (grid == null) return new Object[0];
				
				return new Object[] {getAvailableItemMap(grid.getCraftableArray().getAvailableItems())};
			}
			case 3: {
				if (arguments.length < 3) throw new Exception("too few arguments");
				else if (!(arguments[0] instanceof Double)) throw new Exception("bad argument #1 (expected number)");
				else if (!(arguments[1] instanceof Double)) throw new Exception("bad argument #2 (expected number)");
				else if (!(arguments[2] instanceof Double)) throw new Exception("bad argument #3 (expected number)");
				
				final int uuid = (int)Math.floor((Double)arguments[0]);
				final int amount = Math.abs((int)Math.floor((Double)arguments[1]));
				
				final int outDir = (int)Math.floor((Double)arguments[2]);
				if (outDir < 0 || outDir > 5) throw new Exception("bad direction "+outDir+" (expected 0-5)");
				
				if (grid == null) return new Object[0];
				
				Future<Long> callback = TickHandler.addTickCallback(worldObj, new Callable<Long>() {
					@Override
					public Long call() {
						if (!grid.useMEEnergy(amount / ITEM_ENERGY_FACTOR, "ME Bridge Extraction")) return 0L;
						
						ItemStack ostack = Util.getUUID(uuid, amount);
						if (ostack.getItem() == null) return 0L;
						
						long ret = 0;
						while (ostack.stackSize > 0) {
							ItemStack reqStack = ostack.copy();
							reqStack.stackSize = Math.min(ostack.stackSize, ostack.getMaxStackSize());
							
							IAEItemStack request = appeng.api.Util.createItemStack(reqStack);
							IAEItemStack returned = grid.getCellArray().extractItems(request);
							
							if (returned == null) break;
							
							ItemStack output = returned.getItemStack();
							int base = output.stackSize;
							TileInteractiveSorter.outputItem(TileMEBridge.this, output, outDir);
							
							if (output.stackSize > 0) {
								IAEItemStack remainder = appeng.api.Util.createItemStack(output);
								grid.getCellArray().addItems(remainder);
							}
							
							int total = base - output.stackSize;
							ostack.stackSize -= total;
							ret += total;
						}
						
						return ret;
					}
				});
				return new Object[] {callback.get()};
			}
			case 4: {
				if (arguments.length < 2) throw new Exception("too few arguments");
				else if (!(arguments[0] instanceof Double)) throw new Exception("bad argument #1 (expected number)");
				else if (!(arguments[1] instanceof Double)) throw new Exception("bad argument #2 (expected number)");
				
				final int uuid = (int)Math.floor((Double)arguments[0]);
				final int amount = Math.abs((int)Math.floor((Double)arguments[1]));
				
				if (grid == null) return new Object[0];
				
				Future<Object> callback = TickHandler.addTickCallback(worldObj, new Callable<Object>() {
					@Override
					public Object call() {
						try {
							grid.craftingRequest(Util.getUUID(uuid, amount));
						} catch (AppEngTileMissingException e) {
							throw new RuntimeException("something went wrong on AE's side: "+e.getMessage());
						}
						
						return null;
					}
				});
				callback.get();
				return new Object[0];
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
		
	}

	@Override
	public void detach(IComputerAccess computer) {
		
	}
	
	private Map<Integer, Double> getAvailableItemMap(IItemList list) {
		// Float < Integer < Double < Long (f=i d=l is a common misconception)
		Map<Integer, Double> items = new HashMap<Integer, Double>();
		for (IAEItemStack stack : grid.getCellArray().getAvailableItems()) {
			items.put(Util.getUUID(stack.getItemStack()), (double)stack.getStackSize());
		}
		return items;
	}
}
