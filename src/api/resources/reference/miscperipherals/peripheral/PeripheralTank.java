package miscperipherals.peripheral;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import miscperipherals.core.LuaManager;
import miscperipherals.core.MiscPeripherals;
import miscperipherals.core.TickHandler;
import miscperipherals.util.Util;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.liquids.ILiquidTank;
import net.minecraftforge.liquids.ITankContainer;
import net.minecraftforge.liquids.LiquidContainerData;
import net.minecraftforge.liquids.LiquidContainerRegistry;
import net.minecraftforge.liquids.LiquidStack;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import cpw.mods.fml.common.network.PacketDispatcher;
import dan200.computer.api.IComputerAccess;
import dan200.computer.api.IHostedPeripheral;
import dan200.turtle.api.ITurtleAccess;

public class PeripheralTank implements IHostedPeripheral, ILiquidTank {
	public static final int CAPACITY = 10000;
	public static final int TICK_RATE = 20;
	
	private final ITurtleAccess turtle;
	public LiquidStack liquid;
	private LiquidStack prevLiquid;
	private int prevGauge = 0;
	private int ticker = new Random().nextInt();
	
	public PeripheralTank(ITurtleAccess turtle) {
		this.turtle = turtle;
	}
	
	@Override
	public String getType() {
		return "tank";
	}

	@Override
	public String[] getMethodNames() {
		return new String[] {"suck", "suckUp", "suckDown", "drop", "dropUp", "dropDown", "getLiquid", "pack", "unpack"};
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, final int method, Object[] arguments) throws Exception {
		switch (method) {
			case 0:
			case 1:
			case 2: {
				int amount = Integer.MAX_VALUE;
				if (arguments.length > 0) {
					if (!(arguments[0] instanceof Double)) throw new Exception("bad argument #1 (expected number)");
					amount = (int)Math.floor((Double)arguments[0]);
				}
				final int doAmount = amount;
				
				Future<Object> callback = TickHandler.addTickCallback(turtle.getWorld(), new Callable<Object>() {
					@Override
					public Object call() {
						World world = turtle.getWorld();
						MovingObjectPosition mop = Util.rayTraceBlock(turtle, method == 0 ? turtle.getFacingDir() : (method == 1 ? 1 : 0));
						TileEntity te = world.getBlockTileEntity(mop.blockX, mop.blockY, mop.blockZ);
						if (!(te instanceof ITankContainer)) {
							if (liquid != null && CAPACITY - liquid.amount < LiquidContainerRegistry.BUCKET_VOLUME) return null;
							
							int block = world.getBlockId(mop.blockX, mop.blockY, mop.blockZ);
							if (Block.blocksList[block] == null || Block.blocksList[block].isAirBlock(world, mop.blockX, mop.blockY, mop.blockZ)) return null;
							int meta = world.getBlockMetadata(mop.blockX, mop.blockY, mop.blockZ);
							
							if (liquid != null && (liquid.itemID != block || liquid.itemMeta != meta)) return 0;
							
							for (LiquidContainerData data : LiquidContainerRegistry.getRegisteredLiquidContainerData()) {
								if (data.stillLiquid.itemID == block && data.stillLiquid.itemMeta == meta) {
									world.setBlock(mop.blockX, mop.blockY, mop.blockZ, 0, 0, 2);
									world.markBlockForUpdate(mop.blockX, mop.blockY, mop.blockZ);
									
									if (liquid == null) liquid = new LiquidStack(block, LiquidContainerRegistry.BUCKET_VOLUME, meta);
									else liquid.amount += LiquidContainerRegistry.BUCKET_VOLUME;
									
									return LiquidContainerRegistry.BUCKET_VOLUME;
								}
							}
							
							return null;
						}
						ITankContainer container = (ITankContainer)te;
						
						ForgeDirection side = ForgeDirection.getOrientation(mop.sideHit);
						int adding = Math.min(doAmount, CAPACITY - (liquid == null ? 0 : liquid.amount));
						LiquidStack added = container.drain(side, adding, false);
						if (added == null || (liquid != null && (added.itemID != liquid.itemID || added.itemMeta != liquid.itemMeta))) return 0;
						added = container.drain(side, adding, true);
						
						if (liquid == null) liquid = added;
						else liquid.amount += added.amount;
						
						updateLiquid();
						return added.amount;
					};
				});

				return new Object[] {callback.get()};
			}
			case 3:
			case 4:
			case 5: {
				int amount = Integer.MAX_VALUE;
				if (arguments.length > 0) {
					if (!(arguments[0] instanceof Double)) throw new Exception("bad argument #1 (expected number)");
					amount = (int)Math.floor((Double)arguments[0]);
				}
				final int doAmount = amount;
				
				if (liquid == null) return new Object[] {0};
				
				Future<Object> callback = TickHandler.addTickCallback(turtle.getWorld(), new Callable<Object>() {
					@Override
					public Object call() {
						World world = turtle.getWorld();
						MovingObjectPosition mop = Util.rayTraceBlock(turtle, method == 3 ? turtle.getFacingDir() : (method == 4 ? 1 : 0));
						TileEntity te = world.getBlockTileEntity(mop.blockX, mop.blockY, mop.blockZ);
						if (!(te instanceof ITankContainer)) {
							if (liquid.itemID >= Block.blocksList.length || Block.blocksList[liquid.itemID] == null || Block.blocksList[liquid.itemID].isAirBlock(world, mop.blockX, mop.blockY, mop.blockZ)) return new Object[] {null};
							if (liquid.amount < LiquidContainerRegistry.BUCKET_VOLUME) return new Object[] {0};
							
							int block = world.getBlockId(mop.blockX, mop.blockY, mop.blockZ);
							if (Block.blocksList[block] != null && Block.blocksList[block].isAirBlock(world, mop.blockX, mop.blockY, mop.blockZ)) return new Object[] {null};
							
							world.setBlock(mop.blockX, mop.blockY, mop.blockZ, liquid.itemID, liquid.itemMeta, 2);
							world.markBlockForUpdate(mop.blockX, mop.blockY, mop.blockZ);
							
							liquid.amount -= LiquidContainerRegistry.BUCKET_VOLUME;
							if (liquid.amount <= 0) liquid = null;
							
							return new Object[] {LiquidContainerRegistry.BUCKET_VOLUME};
						}
						ITankContainer container = (ITankContainer)te;
						
						ForgeDirection side = ForgeDirection.getOrientation(mop.sideHit);
						LiquidStack removing = liquid.copy();
						removing.amount = Math.min(doAmount, liquid.amount);
						int removed = container.fill(side, removing, true);
						
						liquid.amount -= removed;
						if (liquid.amount <= 0) liquid = null;
						
						updateLiquid();
						return removed;
					}
				});
				
				return new Object[] {callback.get()};
			}
			case 6: {
				if (liquid == null) return new Object[] {null, 0};
				return new Object[] {Util.getUUID(liquid), liquid.amount};
			}
			case 7: {
				if (liquid == null || liquid.amount < LiquidContainerRegistry.BUCKET_VOLUME) return new Object[] {false};
				
				ItemStack slotstack = turtle.getSlotContents(turtle.getSelectedSlot());
				if (slotstack == null) return new Object[] {false};
				
				LiquidStack filling = liquid.copy();
				filling.amount = LiquidContainerRegistry.BUCKET_VOLUME;
				ItemStack filled = LiquidContainerRegistry.fillLiquidContainer(filling, slotstack);
				if (filled != null) {
					liquid.amount -= filling.amount;
					if (liquid.amount <= 0) liquid = null;
					
					slotstack.stackSize--;
					if (slotstack.stackSize <= 0) slotstack = null;
					turtle.setSlotContents(turtle.getSelectedSlot(), slotstack);
					
					Util.storeOrDrop(turtle, filled);
					
					return new Object[] {true};
				}
				
				return new Object[] {false};
			}
			case 8: {
				ItemStack slotstack = turtle.getSlotContents(turtle.getSelectedSlot());
				if (slotstack == null) return new Object[] {false};
				
				for (LiquidContainerData data : LiquidContainerRegistry.getRegisteredLiquidContainerData()) {
					if (data.filled.isItemEqual(slotstack) && (liquid == null || (liquid.isLiquidEqual(data.stillLiquid) && CAPACITY - liquid.amount >= data.stillLiquid.amount))) {
						if (liquid == null) liquid = data.stillLiquid.copy();
						else liquid.amount += data.stillLiquid.amount;
						
						if (slotstack.getItem().hasContainerItem()) {
							slotstack = slotstack.getItem().getContainerItemStack(slotstack);
						} else {
							slotstack.stackSize--;
							if (slotstack.stackSize <= 0) slotstack = null;
						}
						turtle.setSlotContents(turtle.getSelectedSlot(), slotstack);
						
						return new Object[] {true};
					}
				}
				
				return new Object[] {false};
			}
		}
		
		return new Object[0];
	}

	@Override
	public boolean canAttachToSide(int side) {
		return false;
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
		if (turtle.getWorld() != null && !turtle.getWorld().isRemote && ++ticker >= TICK_RATE) {
			ticker = 0;
			updateLiquid();
		}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		if (nbttagcompound.hasKey("liquid")) liquid = LiquidStack.loadLiquidStackFromNBT(nbttagcompound.getCompoundTag("liquid"));
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		if (liquid != null) {
			NBTTagCompound liquid = new NBTTagCompound();
			this.liquid.writeToNBT(liquid);
			nbttagcompound.setTag("liquid", liquid);
		}
	}
	
	private void updateLiquid() {
		if (!areLiquidsEqual(liquid, prevLiquid)) {
			World world = turtle.getWorld();
			Vec3 pos = turtle.getPosition();
			int x = (int)Math.floor(pos.xCoord);
			int y = (int)Math.floor(pos.yCoord);
			int z = (int)Math.floor(pos.zCoord);
			
			world.markBlockForUpdate(x, y, z); // update for lava lighting
			
			prevLiquid = liquid == null ? null : liquid.copy();
			
			ByteArrayDataOutput os = ByteStreams.newDataOutput();
			os.writeInt(x);
			os.writeInt(y);
			os.writeInt(z);
			if (liquid == null) {
				os.writeShort(Short.MIN_VALUE);
				os.writeShort(Short.MIN_VALUE);
				os.writeInt(Integer.MIN_VALUE);
			} else {
				os.writeShort(liquid.itemID);
				os.writeShort(liquid.itemMeta);
				os.writeInt(liquid.amount);
			}
			PacketDispatcher.sendPacketToAllAround(pos.xCoord + 0.5D, pos.yCoord + 0.5D, pos.zCoord + 0.5D, 64.0D, world.provider.dimensionId, PacketDispatcher.getTinyPacket(MiscPeripherals.instance, (short)2, os.toByteArray()));
		}
	}
	
	private boolean areLiquidsEqual(LiquidStack a, LiquidStack b) {
		if (a == null && b == null) return true;
		else if (a == null || b == null) return false;
		else return a.itemID == b.itemID && a.itemMeta == b.itemMeta && a.amount == b.amount;
	}

	@Override
	public int getTankPressure() {
		return 0;
	}
	
	@Override
	public LiquidStack getLiquid() {
		return liquid;
	}
	
	@Override
	public int getCapacity() {
		return CAPACITY;
	}
	
	@Override
	public int fill(LiquidStack resource, boolean doFill) {
		if (liquid != null && !resource.isLiquidEqual(liquid)) return 0;
		
		int toAdd = Math.min(CAPACITY - (liquid == null ? 0 : liquid.amount), resource.amount);
		if (toAdd < 0) toAdd = 0;
		if (doFill) {
			if (liquid == null) {
				liquid = resource.copy();
				liquid.amount = toAdd;
			}
			else liquid.amount += toAdd;
			if (toAdd != 0) updateLiquid();
		}
		return toAdd;
	}
	
	@Override
	public LiquidStack drain(int maxDrain, boolean doDrain) {
		if (liquid == null) return null;
		
		LiquidStack ret = liquid.copy();
		ret.amount = Math.min(liquid.amount, maxDrain);
		if (doDrain) {
			liquid.amount -= ret.amount;
			if (liquid.amount <= 0) liquid = null;
			if (ret.amount != 0) updateLiquid();
		}
		return ret;
	}
}
