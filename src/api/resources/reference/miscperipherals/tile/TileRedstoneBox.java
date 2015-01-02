package miscperipherals.tile;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import miscperipherals.core.LuaManager;
import miscperipherals.core.MiscPeripherals;
import miscperipherals.core.TickHandler;
import miscperipherals.util.Util;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Direction;
import net.minecraft.util.Facing;
import net.minecraftforge.common.ForgeDirection;
import dan200.computer.api.IComputerAccess;
import dan200.computer.api.IPeripheral;

public class TileRedstoneBox extends Tile implements IPeripheral {	
	private int[] redstone = new int[6];
	private int[] comparator = new int[6];
	
	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		
		for (int i = 0; i < 6; i++) {
			redstone[i] = tag.getByte("redstone[" + i + "]") & 15;
			comparator[i] = tag.getByte("comparator[" + i + "]") & 15;
		}
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		
		for (int i = 0; i < 6; i++) {
			tag.setByte("redstone[" + i + "]", (byte) (redstone[i] & 15));
			tag.setByte("comparator[" + i + "]", (byte) (comparator[i] & 15));
		}
	}
	
	@Override
	public int getRedstone(int side) {
		return redstone[side];
	}
	
	@Override
	public int getComparator(int side) {
		return comparator[Util.FACE_TO_SIDE[side]];
	}

	@Override
	public String getType() {
		return "redstoneBox";
	}

	@Override
	public String[] getMethodNames() {
		return new String[] {"getInput","setOutput","setComparatorOutput","getOutput","getComparatorOutput","reset"};
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, final int method, Object[] arguments) throws Exception {
		switch (method) {
			case 0: {
				if (arguments.length < 1) throw new Exception("too few arguments");
				else if (!(arguments[0] instanceof String)) throw new Exception("bad argument #1 (expected string)");
				
				int side = resolveSide((String) arguments[0]);
				if (side == -1) throw new Exception("bad side " + (String) arguments[0]);
				
				return new Object[] {getRedstoneInput(side)};
			}
			case 1:
			case 2: {
				if (arguments.length < 2) throw new Exception("too few arguments");
				else if (!(arguments[0] instanceof String)) throw new Exception("bad argument #1 (expected string)");
				else if (!(arguments[1] instanceof Double)) throw new Exception("bad argument #2 (expected number)");
				
				final int side = resolveSide((String) arguments[0]);
				if (side == -1) throw new Exception("bad side " + (String) arguments[0]);
				
				int strength = (int) Math.floor((Double) arguments[1]);
				
				if (method == 1) redstone[side] = strength & 15;
				else comparator[side] = strength & 15;
				
				TickHandler.addTickCallback(worldObj, new Callable<Object>() {
					@Override
					public Object call() throws Exception {
						int id = MiscPeripherals.instance.blockAlpha.blockID;
						worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, id);
						worldObj.markBlockForUpdate(xCoord + Facing.offsetsXForSide[side], yCoord + Facing.offsetsYForSide[side], zCoord + Facing.offsetsZForSide[side]);
						
						if (method != 1) worldObj.func_96440_m(xCoord, yCoord, zCoord, id);
						
						return null;
					}
				});
				
				return new Object[] {strength & 15};
			}
			case 3:
			case 4: {
				if (arguments.length < 1) throw new Exception("too few arguments");
				else if (!(arguments[0] instanceof String)) throw new Exception("bad argument #1 (expected string)");
				
				int side = resolveSide((String) arguments[0]);
				if (side == -1) throw new Exception("bad side " + (String) arguments[0]);
				
				return new Object[] {method == 3 ? redstone[side] : comparator[side]};
			}
			case 5: {
				for (int i = 0; i < 6; i++) {
					redstone[i] = 0;
					comparator[i] = 0;
				}
				
				TickHandler.addTickCallback(worldObj, new Callable<Object>() {
					@Override
					public Object call() throws Exception {
						int id = MiscPeripherals.instance.blockAlpha.blockID;
						
						worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, id);
						
						for (int side = 0; side < 6; side++) {
							worldObj.markBlockForUpdate(xCoord + Facing.offsetsXForSide[side], yCoord + Facing.offsetsYForSide[side], zCoord + Facing.offsetsZForSide[side]);
						}
						
						worldObj.func_96440_m(xCoord, yCoord, zCoord, id);
						
						return null;
					}
				});
				
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
		LuaManager.mount(computer);
	}

	@Override
	public void detach(IComputerAccess computer) {
		
	}
	
	private int resolveSide(String side) {
		if (side == null) return -1;
		else if (side.equals("bottom")) return 0;
		else if (side.equals("top")) return 1;
		else if (side.equals("front")) return getFacing();
		else if (side.equals("back")) return Util.OPPOSITE[getFacing()];
		else if (side.equals("left")) {
			int facing = getFacing();
			if (facing < 2) return -1;
			return Util.FACE_TO_SIDE[Direction.rotateLeft[Util.SIDE_TO_FACE[facing]]];
		} else if (side.equals("right")) {
			int facing = getFacing();
			if (facing < 2) return -1;
			return Util.FACE_TO_SIDE[Direction.rotateRight[Util.SIDE_TO_FACE[facing]]];
		} else return -1;
	}
}
