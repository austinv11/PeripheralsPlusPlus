package miscperipherals.peripheral;

import java.util.HashMap;
import java.util.Map;

import miscperipherals.core.LuaManager;
import miscperipherals.util.SlotStack;
import miscperipherals.util.Util;
import mods.tinker.tconstruct.library.crafting.ToolBuilder;
import mods.tinker.tconstruct.library.tools.ToolCore;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import dan200.computer.api.IComputerAccess;
import dan200.computer.api.IHostedPeripheral;
import dan200.turtle.api.ITurtleAccess;

public class PeripheralToolBuilder implements IHostedPeripheral {
	public final ITurtleAccess turtle;
	
	public PeripheralToolBuilder(ITurtleAccess turtle) {
		this.turtle = turtle;
	}
	
	@Override
	public String getType() {
		return "toolStation";
	}

	@Override
	public String[] getMethodNames() {
		return new String[] {"build","buildLast","read"};
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, int method, Object[] arguments) throws Exception {
		switch (method) {
			case 0:
			case 1: {
				if (arguments.length < 1) throw new Exception("too few arguments");
				else if (arguments[0] != null && !(arguments[0] instanceof Double)) throw new Exception("bad argument #1 (expected number)");
				else if (arguments[1] != null && !(arguments[1] instanceof Double)) throw new Exception("bad argument #2 (expected number)");
				else if (arguments[2] != null && !(arguments[2] instanceof Double)) throw new Exception("bad argument #3 (expected number)");
				else if (arguments.length > 3 && !(arguments[3] instanceof String)) throw new Exception("bad argument #4 (expected string)");
				
				SlotStack[] stacks = new SlotStack[3];
				for (int i = 0; i < stacks.length; i++) {
					int slot = arguments[i] != null ? (int) Math.floor((Double) arguments[i]) - 1 : -1;
					if (slot < -1 || slot >= turtle.getInventorySize()) throw new Exception("bad slot " + (slot + 1) + " (expected 0-" + turtle.getInventorySize() + ")");
					
					if (slot != -1) stacks[i] = new SlotStack(turtle.getSlotContents(slot), slot);
				}
				
				String name = arguments.length > 3 ? (String) arguments[3] : null;
				if (name != null && name.length() > 32) name = name.substring(0, 32);
				
				ItemStack result = ToolBuilder.instance.buildTool(stacks[0] != null ? stacks[0].stack : null, stacks[1] != null ? stacks[1].stack : null, stacks[2] != null ? stacks[2].stack : null, name);
				if (result != null) {
					for (int i = 0; i < stacks.length; i++) {
						SlotStack stack = stacks[i];
						if (stack == null) continue;
						
						if (stack.stack != null) {
							if (--stack.stack.stackSize <= 0) stack.stack = null;
							turtle.setSlotContents(stack.slot, stack.stack);
						}
					}
					
					int slot = method == 0 ? stacks[0].slot : stacks[stacks.length - 1].slot;
					if (turtle.getSlotContents(slot) != null) Util.storeOrDrop(turtle, result);
					else turtle.setSlotContents(slot, result);
					
					return new Object[] {true};
				} else return new Object[] {false};
			}
			case 2: {
				if (arguments.length < 1) throw new Exception("too few arguments");
				else if (!(arguments[0] instanceof Double)) throw new Exception("bad argument #1 (expected number)");
				
				int slot = (int) Math.floor((Double) arguments[0]) - 1;
				if (slot < 0 || slot > turtle.getInventorySize()) throw new Exception("bad slot " + (slot + 1) + " (expected 1-" + turtle.getInventorySize() + ")");
				
				ItemStack stack = turtle.getSlotContents(slot);
				if (stack == null || !(stack.getItem() instanceof ToolCore) || stack.stackTagCompound == null || !stack.stackTagCompound.hasKey("InfiTool")) return new Object[] {null};
				
				NBTTagCompound data = stack.stackTagCompound.getCompoundTag("InfiTool");
				
				Map<String, Object> ret = new HashMap<String, Object>();
				ret.put("broken", data.getBoolean("Broken"));
				ret.put("attack", (float) data.getInteger("Attack") / 2.0F);
				ret.put("durabilityBase", data.getInteger("BaseDurability"));
				ret.put("durabilityBonus", data.getInteger("BonusDurability"));
				ret.put("durabilityMod", data.getFloat("ModDurability"));
				ret.put("durability", data.getInteger("TotalDurability"));
				ret.put("damage", (float) data.getInteger("Damage") / (float) data.getInteger("TotalDurability"));
				ret.put("shoddy", (int) Math.floor(data.getFloat("Shoddy")));
				ret.put("reinforced", data.getInteger("Unbreaking"));
				ret.put("speed", data.getInteger("MiningSpeed") / 100);
				return new Object[] {ret};
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
