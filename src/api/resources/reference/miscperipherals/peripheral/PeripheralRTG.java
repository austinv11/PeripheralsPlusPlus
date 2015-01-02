package miscperipherals.peripheral;

import miscperipherals.api.IRTGFuel;
import miscperipherals.core.LuaManager;
import miscperipherals.core.MiscPeripherals;
import miscperipherals.util.Util;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;
import dan200.computer.api.IComputerAccess;
import dan200.computer.api.IHostedPeripheral;
import dan200.turtle.api.ITurtleAccess;

public class PeripheralRTG implements IHostedPeripheral {
	public static int OUTPUT = 1;
	public static int URANIUM_TOTAL = 50000 * 20;
	public static float THORIUM_MOD = 1F;
	public static float PLUTONIUM_MOD = 4F;
	
	private final ITurtleAccess turtle;
	private float euBuffer = 0;
	private int fuel = 0;
	
	public PeripheralRTG(ITurtleAccess turtle) {
		this.turtle = turtle;
	}
	
	@Override
	public String getType() {
		return "rtg";
	}

	@Override
	public String[] getMethodNames() {
		return new String[] {"refuel"};
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, int method, Object[] arguments) throws Exception {
		switch (method) {
			case 0: {
				ItemStack selstack = turtle.getSlotContents(turtle.getSelectedSlot());
				
				int amount = Integer.MIN_VALUE;
				if (arguments.length > 0) {
					if (!(arguments[0] instanceof Double)) throw new Exception("bad argument #1 (expected number)");
					amount = (int)Math.floor((Double)arguments[0]);
				}
				
				if (selstack == null) return new Object[] {0};
				
				int refuel = 0;
				for (IRTGFuel fuelhandler : IRTGFuel.handlers) {
					int fuel = fuelhandler.get(selstack);
					if (fuel > 0) {
						refuel = fuel;
						break;
					}
				}
				
				if (amount < 1 || amount > selstack.stackSize) amount = selstack.stackSize;
				refuel *= amount;
				fuel += refuel;
				
				selstack.stackSize -= amount;
				if (selstack.stackSize <= 0) selstack = null;
				turtle.setSlotContents(turtle.getSelectedSlot(), selstack);
				
				return new Object[] {refuel};
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
		if (!MiscPeripherals.proxy.isServer()) return;
		
		if (fuel >= OUTPUT) fuel -= OUTPUT;
		else return;
		
		euBuffer += OUTPUT;
		while (euBuffer >= MiscPeripherals.instance.fuelEU) {
			euBuffer -= MiscPeripherals.instance.fuelEU * Util.addFuel(turtle, 1);
		}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		fuel = nbttagcompound.getInteger("rtgFuel");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		nbttagcompound.setInteger("rtgFuel", fuel);
	}
	
	public static class DefaultRTGFuel implements IRTGFuel {
		@Override
		public int get(ItemStack stack) {
			for (ItemStack uranium : Util.merge(OreDictionary.getOres("dropUranium"), OreDictionary.getOres("ingotUranium"))) {
				if (Util.areStacksEqual(stack, uranium)) return URANIUM_TOTAL;
			}
			for (ItemStack thorium : Util.merge(OreDictionary.getOres("dustThorium"), OreDictionary.getOres("ingotThorium"))) {
				if (Util.areStacksEqual(stack, thorium)) return (int)(URANIUM_TOTAL * THORIUM_MOD);
			}
			for (ItemStack plutonium : Util.merge(OreDictionary.getOres("dustPlutonium"), OreDictionary.getOres("ingotPlutonium"))) {
				if (Util.areStacksEqual(stack, plutonium)) return (int)(URANIUM_TOTAL * PLUTONIUM_MOD);
			}
			
			return 0;
		}
	}
	
	static {
		IRTGFuel.handlers.add(new DefaultRTGFuel());
	}
}
