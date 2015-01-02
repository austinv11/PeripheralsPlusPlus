package miscperipherals.peripheral;

import miscperipherals.core.LuaManager;
import miscperipherals.util.FakePlayer;
import miscperipherals.util.RandomExt;
import miscperipherals.util.Util;
import net.minecraft.block.Block;
import net.minecraft.inventory.ContainerRepair;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Facing;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import dan200.computer.api.IComputerAccess;
import dan200.computer.api.IHostedPeripheral;
import dan200.turtle.api.ITurtleAccess;
import dan200.turtle.api.TurtleSide;

public class PeripheralAnvil implements IHostedPeripheral {
	public static final float DAMAGE_CHANCE = 0.12F;
	public static final int MAX_DAMAGE = 2;
	public static final int MAX_COST = 40;
	
	private final ITurtleAccess turtle;
	private final TurtleSide side;
	private final RandomExt random = new RandomExt();
	public int damage = 0;
	
	public PeripheralAnvil(ITurtleAccess turtle, TurtleSide side) {
		this.turtle = turtle;
		this.side = side;
	}
	
	@Override
	public String getType() {
		return "anvil";
	}

	@Override
	public String[] getMethodNames() {
		return new String[] {"getRepairCost", "repair", "suicide"};
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, int method, Object[] arguments) throws Exception {
		switch (method) {
			case 0: {
				if (arguments.length < 2) throw new Exception("too few arguments");
				else if (!(arguments[0] instanceof Double)) throw new Exception("bad argument #1 (expected number)");
				else if (!(arguments[1] instanceof Double)) throw new Exception("bad argument #2 (expected number)");
				else if (arguments.length > 2 && !(arguments[2] instanceof String)) throw new Exception("bad argument #3 (expected string)");
				
				int slotA = (int)Math.floor((Double)arguments[0]);
				int slotB = (int)Math.floor((Double)arguments[1]);
				if (slotA < 1 || slotA > turtle.getInventorySize()) {
					throw new Exception("bad first slot "+slotA+" (expected 1-"+turtle.getInventorySize()+")");
				}
				slotA--;
				if (slotB < 1 || slotB > turtle.getInventorySize()) {
					throw new Exception("bad second slot "+slotB+" (expected 1-"+turtle.getInventorySize()+")");
				}
				slotB--;
				
				if (damage > MAX_DAMAGE) return new Object[] {0};
				
				AnvilData data = getAnvilData(turtle.getSlotContents(slotA), turtle.getSlotContents(slotB), arguments.length > 2 ? (String)arguments[2] : "");
				return new Object[] {(data.cost < 1 || data.cost >= MAX_COST || data.stack == null) ? 0 : data.cost};
			}
			case 1: {
				if (arguments.length < 2) throw new Exception("too few arguments");
				else if (!(arguments[0] instanceof Double)) throw new Exception("bad argument #1 (expected number)");
				else if (!(arguments[1] instanceof Double)) throw new Exception("bad argument #2 (expected number)");
				else if (arguments.length > 2 && !(arguments[2] instanceof String)) throw new Exception("bad argument #3 (expected string)");
				
				int slotA = (int)Math.floor((Double)arguments[0]);
				int slotB = (int)Math.floor((Double)arguments[1]);
				if (slotA < 1 || slotA > turtle.getInventorySize()) {
					throw new Exception("bad first slot "+slotA+" (expected 1-"+turtle.getInventorySize()+")");
				}
				slotA--;
				if (slotB < 1 || slotB > turtle.getInventorySize()) {
					throw new Exception("bad second slot "+slotB+" (expected 1-"+turtle.getInventorySize()+")");
				}
				slotB--;
				
				if (damage > MAX_DAMAGE) return new Object[] {false};
				
				PeripheralXP xp = Util.getPeripheral(turtle, PeripheralXP.class);
				if (xp == null) return new Object[] {false};
				
				AnvilData data = getAnvilData(turtle.getSlotContents(slotA), turtle.getSlotContents(slotB), (arguments.length > 2 && arguments[2] != null) ? (String)arguments[2] : "");
				if (data.cost < 1 || data.cost >= MAX_COST || data.stack == null) return new Object[] {false};
				
				xp.addLevels(-data.cost, true);
				turtle.setSlotContents(slotA, data.stack);
				turtle.setSlotContents(slotB, null);
				damage(false);
				
				return new Object[] {true};
			}
			case 2: {
				if (damage > MAX_DAMAGE) return new Object[] {false};
				
				World world = turtle.getWorld();
				int facing = turtle.getFacingDir();
				Vec3 pos = turtle.getPosition();
				int x = (int)Math.floor(pos.xCoord);
				int y = (int)Math.floor(pos.yCoord);
				int z = (int)Math.floor(pos.zCoord);
				
				boolean success = false;
				int[] directions = {0, facing, Util.OPPOSITE[facing], 1};
				for (int i = 0; i < directions.length; i++) {
					if (Util.isPassthroughBlock(world, x + Facing.offsetsXForSide[directions[i]], y + Facing.offsetsYForSide[directions[i]], z + Facing.offsetsZForSide[directions[i]]) &&
						world.setBlock(x + Facing.offsetsXForSide[directions[i]], y + Facing.offsetsYForSide[directions[i]], z + Facing.offsetsZForSide[directions[i]], Block.anvil.blockID, damage << 2, 2)) {
						Block.anvil.updateTick(world, x + Facing.offsetsXForSide[directions[i]], y + Facing.offsetsYForSide[directions[i]], z + Facing.offsetsZForSide[directions[i]], random);
						success = true;
						break;
					}
				}
				
				if (success) {
					damage(true);
				}
				
				return new Object[] {success};
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
		damage = nbttagcompound.getInteger("damage");
		random.setSeed(nbttagcompound.getLong("rndSeed"));
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		nbttagcompound.setInteger("damage", damage);
		nbttagcompound.setLong("rndSeed", random.getSeed());
	}
	
	private void damage(boolean forceBreak) {
		if ((random.nextFloat() < DAMAGE_CHANCE && ++damage > MAX_DAMAGE) || forceBreak) {
			damage = MAX_DAMAGE + 1;
			Util.setTurtleUpgrade(turtle, side, null);
		}
	}
	
	private ContainerRepair getFakeAnvil() {
		FakePlayer player = FakePlayer.get(turtle.getWorld());
		player.alignToTurtle(turtle);
		return new ContainerRepair(player.inventory, turtle.getWorld(), 0, 0, 0, player);
	}
	
	private AnvilData getAnvilData(ItemStack a, ItemStack b, String name) {
		ContainerRepair anvil = getFakeAnvil();
		IInventory input = anvil.getSlot(0).inventory;
		IInventory output = anvil.getSlot(2).inventory;
		
		input.setInventorySlotContents(0, a);
		input.setInventorySlotContents(1, b);
		anvil.updateItemName(name);
		
		return new AnvilData(anvil.maximumCost, output.getStackInSlot(0));
	}
	
	private static class AnvilData {
		public final int cost;
		public final ItemStack stack;
		
		public AnvilData(int cost, ItemStack stack) {
			this.cost = cost;
			this.stack = stack;
		}
	}
}
