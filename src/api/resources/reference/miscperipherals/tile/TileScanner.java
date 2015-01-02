package miscperipherals.tile;

import java.util.HashMap;
import java.util.Map;

import miscperipherals.core.LuaManager;
import miscperipherals.network.GuiHandler;
import miscperipherals.safe.Reflector;
import miscperipherals.util.Util;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.world.storage.MapData;
import dan200.computer.api.IComputerAccess;
import dan200.computer.api.IPeripheral;

/**
 * RG for public source release: This went down the drain pretty early on, so disregard the usage of Thread.sleep
 */
public class TileScanner extends TileInventory implements IPeripheral {
	public TileScanner() {
		super(1);
	}
	
	@Override
	public int getGuiId() {
		return GuiHandler.SINGLE_SLOT;
	}
	
	@Override
	public String getInventoryName() {
		return "Scanner";
	}

	@Override
	public String getType() {
		return "scanner";
	}

	@Override
	public String[] getMethodNames() {
		return new String[] {"scanPrintout","scanMap"};
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, int method, Object[] arguments) throws Exception {
		switch (method) {
			case 0: {				
				ItemStack stack = getStackInSlot(0);
				if (stack == null || !stack.getItem().getClass().getName().equals("dan200.computer.shared.ItemPrintout")) return new Object[] {null, null};
				
				String title = Reflector.invoke("dan200.computer.shared.ItemPrintout", "getTitle", String.class, stack);
				String[] text = Reflector.invoke("dan200.computer.shared.ItemPrintout", "getText", String[].class, stack);
				
				if (title == null || text == null) return new Object[] {null, null};
				
				setActive(true);
				Thread.sleep(100 * text.length); // 2 ticks per line, 10 lines in 1 second
				setActive(false);
				
				Map<Integer, String> textMap = new HashMap<Integer, String>();
				for (int i = 0; i < text.length; i++) {
					textMap.put(i, text[i]);
				}
				
				return new Object[] {title, textMap};
			}
			case 1: {
				if (arguments.length != 1) throw new Exception("too few arguments");
				else if (!(arguments[0] instanceof Double)) throw new Exception("bad argument #1 (expected number)");
				
				int line = ((Double)arguments[0]).intValue();
				
				ItemStack stack = getStackInSlot(0);
				if (stack == null || !(stack.getItem() instanceof ItemMap)) return new Object[] {-1, null};
				
				MapData data = Item.map.getMapData(stack, worldObj);
				int wh = (int)Math.sqrt(data.colors.length);
				
				if (line < 1 || line > wh) throw new Exception("invalid line (expected 1-"+wh+")");
				line--;
				
				setActive(true);
				Thread.sleep(wh * 20); // total 2560 ms
				setActive(false);
				
				Map<Integer, Integer> colors = new HashMap<Integer, Integer>();
				for (int i = line * wh; i < (line * wh) + wh; i++) {
					colors.put(i, Util.MAP_COLORS[data.colors[i]].getRGB());
				}
				
				return new Object[] {stack.getItemDamage(), colors};
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
}
