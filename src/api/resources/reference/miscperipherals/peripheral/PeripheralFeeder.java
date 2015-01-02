package miscperipherals.peripheral;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import miscperipherals.core.LuaManager;
import miscperipherals.core.MiscPeripherals;
import miscperipherals.core.TickHandler;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import cpw.mods.fml.common.network.PacketDispatcher;
import dan200.computer.api.IComputerAccess;
import dan200.computer.api.IHostedPeripheral;
import dan200.turtle.api.ITurtleAccess;

public class PeripheralFeeder implements IHostedPeripheral {
	private final ITurtleAccess turtle;
	
	public PeripheralFeeder(ITurtleAccess turtle) {
		this.turtle = turtle;
	}
	
	@Override
	public String getType() {
		return "feeder";
	}

	@Override
	public String[] getMethodNames() {
		return new String[] {"feed"};
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, int method, Object[] arguments) throws Exception {
		switch (method) {
			case 0: {
				final ItemStack curItem = turtle.getSlotContents(turtle.getSelectedSlot());
				if (curItem == null || curItem.stackSize <= 0) return new Object[] {false};
				
				final Vec3 pos = turtle.getPosition();
				
				Future<Boolean> callback = TickHandler.addTickCallback(turtle.getWorld(), new Callable<Boolean>() {
					@Override
					public Boolean call() {
						for (EntityAnimal animal : (List<EntityAnimal>)turtle.getWorld().getEntitiesWithinAABB(EntityAnimal.class, AxisAlignedBB.getAABBPool().getAABB(pos.xCoord - 1.5D, pos.yCoord - 1.5D, pos.zCoord - 1.5D, pos.xCoord + 1.5D, pos.yCoord + 1.5D, pos.zCoord + 1.5D))) {
							if (animal.getGrowingAge() == 0 && !animal.isInLove() && animal.isBreedingItem(curItem)) {
								animal.inLove = 600;
								animal.setTarget(null);
								
								curItem.stackSize--;
								if (curItem.stackSize <= 0) turtle.setSlotContents(turtle.getSelectedSlot(), null);
								else turtle.setSlotContents(turtle.getSelectedSlot(), curItem);
								
								ByteArrayDataOutput os = ByteStreams.newDataOutput();
								os.writeInt(animal.entityId);
								PacketDispatcher.sendPacketToAllAround(pos.xCoord, pos.yCoord, pos.zCoord, 64.0D, animal.worldObj.provider.dimensionId, PacketDispatcher.getTinyPacket(MiscPeripherals.instance, (short)1, os.toByteArray()));
								
								return true;
							}
						}
						
						return false;
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
