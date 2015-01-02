package miscperipherals.peripheral;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import miscperipherals.core.LuaManager;
import miscperipherals.core.TickHandler;
import miscperipherals.tile.TilePeripheralWrapper;
import miscperipherals.util.Positionable;
import miscperipherals.util.Positionable.PositionableTurtle;
import net.minecraft.block.Block;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import buildcraft.api.gates.ActionManager;
import buildcraft.api.gates.ITrigger;
import buildcraft.api.gates.TriggerParameter;
import dan200.computer.api.IComputerAccess;
import dan200.computer.api.IHostedPeripheral;
import dan200.turtle.api.ITurtleAccess;

public class PeripheralGateReader implements IHostedPeripheral {
	public static final TriggerParameter DUMMY_PARAM = new TriggerParameter();
	private Positionable positionable;
	
	public PeripheralGateReader(ITurtleAccess turtle) {
		positionable = new Positionable.PositionableTurtle(turtle);
	}
	
	public PeripheralGateReader(TilePeripheralWrapper tile) {
		positionable = new Positionable.PositionableTile(tile);
	}
	
	@Override
	public String getType() {
		return "gateReader";
	}

	@Override
	public String[] getMethodNames() {
		return new String[] {"get"};
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, int method, Object[] arguments) throws Exception {
		switch (method) {
			case 0: {
				TriggerParameter param = DUMMY_PARAM;
				if (arguments.length > 0) {
					if (!(arguments[0] instanceof Double)) throw new Exception("bad argument #1 (expected number)");
					
					//if (!(positionable instanceof PositionableTurtle)) throw new Exception("only available in turtle version");
					
					IInventory inv = positionable.getInventory();
					int slot = (int)Math.floor((Double)arguments[0]) - 1;
					if (slot < 0 || slot >= inv.getSizeInventory()) throw new Exception("bad slot "+(slot + 1)+" (expected 1-"+inv.getSizeInventory()+")");
					
					ItemStack slotstack = inv.getStackInSlot(slot - 1);
					if (slotstack != null) {
						ItemStack stack = slotstack.copy();
						stack.stackSize = 1;
						param = new TriggerParameter();
						param.set(stack);
					}
				}
				
				final World world = positionable.getWorld();
				final TriggerParameter finalParam = param;
				Future<Map<String, Boolean>> callback = TickHandler.addTickCallback(world, new Callable<Map<String, Boolean>>() {
					@Override
					public Map<String, Boolean> call() {
						Vec3 position = positionable.getPosition();
						
						Map<String, Boolean> triggers = new HashMap<String, Boolean>();
						for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
							int x = (int)position.xCoord + direction.offsetX;
							int y = (int)position.yCoord + direction.offsetY;
							int z = (int)position.zCoord + direction.offsetZ;
							if (!world.blockExists(x, y, z)) continue;
							
							TileEntity te = world.getBlockTileEntity(x, y, z);
							for (ITrigger trigger : ActionManager.getNeighborTriggers(Block.blocksList[world.getBlockId((int)position.xCoord + direction.offsetX, (int)position.yCoord + direction.offsetY, (int)position.zCoord + direction.offsetZ)], te)) {
								triggers.put(trigger.getDescription(), trigger.isTriggerActive(direction, te, finalParam));
							}
						}
						
						return triggers;
					}
				});
				return new Object[] {callback.get()};
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
