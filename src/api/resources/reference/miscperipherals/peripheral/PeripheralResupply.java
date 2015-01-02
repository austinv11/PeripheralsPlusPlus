package miscperipherals.peripheral;

import miscperipherals.core.LuaManager;
import miscperipherals.tile.TileResupplyStation;
import miscperipherals.util.Util;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.Facing;
import net.minecraft.util.Vec3;
import dan200.computer.api.IComputerAccess;
import dan200.computer.api.IHostedPeripheral;
import dan200.turtle.api.ITurtleAccess;

public class PeripheralResupply implements IHostedPeripheral {
	private final ITurtleAccess turtle;
	private ChunkCoordinates link;
	private String error = "";
	
	public PeripheralResupply(ITurtleAccess turtle) {
		this.turtle = turtle;
	}
	
	@Override
	public String getType() {
		return "resupply";
	}

	@Override
	public String[] getMethodNames() {
		return new String[] {"resupply","link","getError"};
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, int method, Object[] arguments) throws Exception {
		switch (method) {
			case 0: {
				if (arguments.length != 1) throw new Exception("too few arguments");
				else if (!(arguments[0] instanceof Double)) throw new Exception("bad argument #1 (expected number)");
				
				int slot = ((Double)arguments[0]).intValue();
				if (slot < 1 || slot > turtle.getInventorySize()) throw new Exception("bad slot (expected 1-"+turtle.getInventorySize()+")");
				slot--;
				
				error = "";
				
				if (link == null) {
					error = "Not linked";
					return new Object[] {false};
				}
				
				TileEntity te = turtle.getWorld().getBlockTileEntity(link.posX, link.posY, link.posZ);
				if (!(te instanceof TileResupplyStation)) {
					error = "Station not found";
					return new Object[] {false};
				}
				
				TileResupplyStation rs = (TileResupplyStation)te;
				
				ItemStack curItem = turtle.getSlotContents(slot);
				if (curItem == null) {
					error = "Cannot identify item";
					return new Object[] {false};
				}
				
				int resupplyAmount = curItem.getMaxStackSize() - curItem.stackSize;
				if (resupplyAmount < 1) return new Object[] {true};
				
				int resupplied = rs.resupply(curItem, resupplyAmount);
				if (resupplied <= 0) {
					error = "Not enough items";
					return new Object[] {false};
				}
				
				curItem.stackSize += resupplied;
				turtle.setSlotContents(slot, curItem);
				
				return new Object[] {true};
			}
			case 1: {
				Vec3 pos = turtle.getPosition();
				for (int i = 0; i < 6; i++) {
					TileEntity te = turtle.getWorld().getBlockTileEntity((int)pos.xCoord + Facing.offsetsXForSide[i], (int)pos.yCoord + Facing.offsetsYForSide[i], (int)pos.zCoord + Facing.offsetsZForSide[i]);
					
					if (te instanceof TileResupplyStation) {
						link = new ChunkCoordinates(te.xCoord, te.yCoord, te.zCoord);
						return new Object[] {true};
					}
				}
				
				return new Object[] {false};
			}
			case 2: {
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
		if (nbttagcompound.hasKey("link")) link = Util.readChunkCoordinatesFromNBT(nbttagcompound);
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		if (link != null) {
			NBTTagCompound link = new NBTTagCompound();
			Util.writeChunkCoordinatesToNBT(this.link, link);
			nbttagcompound.setTag("link", link);
		}
	}
}
