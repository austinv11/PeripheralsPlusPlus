package miscperipherals.peripheral;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import miscperipherals.core.LuaManager;
import miscperipherals.core.TickHandler;
import miscperipherals.safe.ReflectionStore;
import miscperipherals.safe.Reflector;
import miscperipherals.util.Util;
import mods.mffs.api.IForceEnergyItems;
import mods.mffs.api.IForceEnergyStorageBlock;
import mods.mffs.api.ISwitchabel;
import mods.mffs.api.PointXYZ;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.Facing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import dan200.computer.api.IComputerAccess;
import dan200.computer.api.IHostedPeripheral;
import dan200.turtle.api.ITurtleAccess;

public class PeripheralMFFS implements IHostedPeripheral {
	private final ITurtleAccess turtle;
	private int id = Integer.MIN_VALUE;
	private String error;
	
	public PeripheralMFFS(ITurtleAccess turtle) {
		this.turtle = turtle;
	}
	
	@Override
	public String getType() {
		return "mffs";
	}

	@Override
	public String[] getMethodNames() {
		return new String[] {"switch", "createCard", "teleport", "teleportUp", "teleportDown", "switchUp", "switchDown", "getError"};
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, int method, Object[] arguments) throws Exception {
		switch (method) {
			case 0:
			case 5: 
			case 6: {
				final World world = turtle.getWorld();
				final int direction = method == 0 ? turtle.getFacingDir() : (method == 8 ? 1 : 0);
				Future<Boolean> callback = TickHandler.addTickCallback(world, new Callable<Boolean>() {
					@Override
					public Boolean call() {
						MovingObjectPosition mop = Util.rayTraceBlock(turtle, direction);
						
						TileEntity te = world.getBlockTileEntity(mop.blockX, mop.blockY, mop.blockZ);
						if (te instanceof ISwitchabel) {
							Boolean ag = Reflector.invoke("mods.mffs.common.SecurityHelper", "isAccessGranted", Boolean.class, te, getDummyPlayer(), world, ReflectionStore.eb, false);
							if (ag != null && ag) {
								ISwitchabel switchable = (ISwitchabel)te;
								if (switchable.isSwitchabel() && consumeFP(1000)) {
									switchable.toggelSwitchValue();
									return true;
								}
							}
						}
						
						return false;
					}
				});
				return new Object[] {callback.get()};
			}
			case 1: {
				final World world = turtle.getWorld();
				Future<Boolean> callback = TickHandler.addTickCallback(world, new Callable<Boolean>() {
					@Override
					public Boolean call() {
						if (ReflectionStore.MFFSitemcardempty == null || ReflectionStore.MFFSItemIDCard == null) return false;
						
						ItemStack slotstack = turtle.getSlotContents(turtle.getSelectedSlot());
						if (slotstack != null && slotstack.itemID == ReflectionStore.MFFSitemcardempty.itemID && consumeFP(1000)) {
							ItemStack card = new ItemStack(ReflectionStore.MFFSItemIDCard);
							Reflector.invoke("mods.mffs.common.item.ItemCardPersonalID", "setOwner", Object.class, card, getTurtleUsername());
							
							if (--slotstack.stackSize <= 0) {
								turtle.setSlotContents(turtle.getSelectedSlot(), card);
							} else {
								turtle.setSlotContents(turtle.getSelectedSlot(), slotstack);
								Util.storeOrDrop(turtle, card);
							}
							
							return true;
						}
						
						return false;
					}
				});
				return new Object[] {callback.get()};
			}
			case 2:
			case 3:
			case 4: {
				final World world = turtle.getWorld();
				final int direction = method == 2 ? turtle.getFacingDir() : (method == 3 ? 1 : 0);
				Future<Boolean> callback = TickHandler.addTickCallback(world, new Callable<Boolean>() {
					@Override
					public Boolean call() {
						if (ReflectionStore.forcefieldtransportcost == null) return false;
						
						MovingObjectPosition mop = Util.rayTraceBlock(turtle, direction);
						if (!world.blockExists(mop.blockX, mop.blockY, mop.blockZ)) {
							error = "Forcefield not loaded";
							return false;
						}
						
						Object ffw = Reflector.invoke("mods.mffs.common.WorldMap", "getForceFieldWorld", Object.class, world);
						if (ffw == null) {
							error = "World forcefield map missing";
							return false;
						}
						Object ffbs = Reflector.invoke(ffw, "getForceFieldStackMap", Object.class, new PointXYZ(mop.blockX, mop.blockY, mop.blockZ, world));
						if (ffbs != null) {
							int ffb = -1;
							Integer genid = Reflector.invoke(ffbs, "getGenratorID", Integer.class);
							Integer projid = Reflector.invoke(ffbs, "getProjectorID", Integer.class);
							if (genid == null || projid == null) {
								error = "No capacitor or projector ID associated with this forcefield";
								return false;
							}
							
							Object net = Reflector.invoke("mods.mffs.common.Linkgrid", "getWorldMap", Object.class, world);
							Map capacitorMap = Reflector.invoke(net, "getCapacitor", Map.class);
							Map projectorMap = Reflector.invoke(net, "getProjektor", Map.class);
							if (capacitorMap == null || projectorMap == null) {
								error = "No capacitor or projector associated with this forcefield";
								return false;
							}
							Object capacitor = capacitorMap.get(genid);
							Object projector = projectorMap.get(projid);
							
							Boolean active = Reflector.invoke(projector, "isActive", Boolean.class);
							if (active != null && active) {
								boolean pass = false;
								
								Integer accessType = Reflector.invoke(projector, "getaccesstyp", Integer.class);
								if (accessType == null) return false;
								switch (accessType) {
									case 1: {
										pass = true;
										break;
									}
									case 2: {
										Boolean lpass = Reflector.invoke("mods.mffs.common.SecurityHelper", "isAccessGranted", Boolean.class, capacitor, getDummyPlayer(), world, ReflectionStore.ffb, false);
										pass = lpass != null && lpass;
										break;
									}
									case 3: {
										Boolean lpass = Reflector.invoke("mods.mffs.common.SecurityHelper", "isAccessGranted", Boolean.class, projector, getDummyPlayer(), world, ReflectionStore.ffb, false);
										pass = lpass != null && lpass;
										break;
									}
								}
								
								if (pass) {
									int i = 0;
									while (ffb != 0) {
										Integer lffb = Reflector.invoke(ffw, "isExistForceFieldStackMap", Integer.class, mop.blockX, mop.blockY, mop.blockZ, i, direction);
										if (lffb == null) return false;
										ffb = lffb;
										if (ffb != 0) i++;
									}
									
									Integer lgenid = Reflector.invoke(ffw, "isExistForceFieldStackMap", Integer.class, mop.blockX, mop.blockY, mop.blockZ, i - 1, turtle.getFacingDir());
									if (lgenid != null && genid == lgenid && i >= 0 && i <= 5) {
										int toX = mop.blockX + Facing.offsetsXForSide[direction] * i;
										int toY = mop.blockY + Facing.offsetsYForSide[direction] * i;
										int toZ = mop.blockZ + Facing.offsetsZForSide[direction] * i;
										
										if (Util.isPassthroughBlock(world, toX, toY, toZ)) {
											if (consumeFP(ReflectionStore.forcefieldtransportcost) && Util.teleportTurtleTo(turtle, world, toX, toY, toZ)) {
												return true;
											} else error = "Not enough force power or teleport failed";
										} else error = "Destination obstructed or not enough force power";
									} else error = "Field too thick";
								} else error = "Access denied";
							} else error = "Projector not active";
						} else error = "Not in front of a forcefield";
						
						return false;
					}
				});
				return new Object[] {callback.get()};
			}
			case 7: {
				return new Object[] {error};
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
		
		id = computer.getID();
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
	
	private boolean consumeFP(int amount) {
		int haveFP = 0;
		
		World world = turtle.getWorld();
		for (int i = 0; i < 6; i++) {
			if (haveFP >= amount) break;
			
			MovingObjectPosition mop = Util.rayTraceBlock(turtle, i);
			TileEntity te = world.getBlockTileEntity(mop.blockX, mop.blockY, mop.blockZ);
			if (te instanceof IForceEnergyStorageBlock) {
				haveFP += ((IForceEnergyStorageBlock)te).getStorageAvailablePower();
			}
		}
		
		for (int i = 0; i < turtle.getInventorySize(); i++) {
			if (haveFP >= amount) break;
			
			ItemStack stack = turtle.getSlotContents(i);
			if (stack != null && stack.getItem() instanceof IForceEnergyItems) {
				haveFP += ((IForceEnergyItems)stack.getItem()).getAvailablePower(stack);
			}
		}
		
		if (haveFP < amount) return false;
		int remain = amount;
		
		for (int i = 0; i < 6; i++) {
			if (remain <= 0) break;
			
			MovingObjectPosition mop = Util.rayTraceBlock(turtle, i);
			TileEntity te = world.getBlockTileEntity(mop.blockX, mop.blockY, mop.blockZ);
			if (te instanceof IForceEnergyStorageBlock) {
				IForceEnergyStorageBlock fesb = (IForceEnergyStorageBlock)te;
				int drain = Math.min(fesb.getStorageAvailablePower(), remain);
				fesb.consumePowerfromStorage(drain, false);
				remain -= drain;
			}
		}
		
		for (int i = 0; i < turtle.getInventorySize(); i++) {
			if (remain <= 0) break;
			
			ItemStack stack = turtle.getSlotContents(i);
			if (stack != null && stack.getItem() instanceof IForceEnergyItems) {
				IForceEnergyItems feitem = (IForceEnergyItems)stack.getItem();
				int drain = Math.min(feitem.getAvailablePower(stack), remain);
				feitem.consumePower(stack, drain, false);
				turtle.setSlotContents(i, stack);
				remain -= drain;
			}
		}
		
		return true;
	}
	
	private EntityPlayer getDummyPlayer() {
		EntityPlayer player = new EntityPlayer(turtle.getWorld()) {
			@Override
			public void sendChatToPlayer(String var1) {
				
			}
			
			@Override
			public ChunkCoordinates getPlayerCoordinates() {
				return new ChunkCoordinates(0, 0, 0);
			}
			
			@Override
			public boolean canCommandSenderUseCommand(int var1, String var2) {
				return false;
			}
		};
		player.username = getTurtleUsername();
		return player;
	}
	
	private String getTurtleUsername() {
		return "Turtle "+(id == Integer.MIN_VALUE ? "???" : "#"+id);
	}
}
