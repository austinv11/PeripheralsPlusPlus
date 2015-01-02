package miscperipherals.peripheral;

import miscperipherals.core.LuaManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.Facing;
import net.minecraft.util.Vec3;
import dan200.computer.api.IComputerAccess;
import dan200.computer.api.IHostedPeripheral;
import dan200.turtle.api.ITurtleAccess;

public class PeripheralSignReader implements IHostedPeripheral {
	private final ITurtleAccess turtle;
	
	public PeripheralSignReader(ITurtleAccess turtle) {
		this.turtle = turtle;
	}
	
	@Override
	public String getType() {
		return "signReader";
	}

	@Override
	public String[] getMethodNames() {
		return new String[] {"read","readUp","readDown"};
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, int method, Object[] arguments) throws Exception {
		switch (method) {
			case 0:
			case 1:
			case 2: {
				int direction = method == 0 ? turtle.getFacingDir() : (method == 1 ? 1 : 0);
				Vec3 pos = turtle.getPosition();
				int x = (int)Math.floor(pos.xCoord) + Facing.offsetsXForSide[direction];
				int y = (int)Math.floor(pos.yCoord) + Facing.offsetsYForSide[direction];
				int z = (int)Math.floor(pos.zCoord) + Facing.offsetsZForSide[direction];
				
				TileEntity te = turtle.getWorld().getBlockTileEntity(x, y, z);
				if (te instanceof TileEntitySign) {
					return ((TileEntitySign)te).signText.clone();
				}
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
