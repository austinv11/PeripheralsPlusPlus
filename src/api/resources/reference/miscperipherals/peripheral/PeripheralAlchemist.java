package miscperipherals.peripheral;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import miscperipherals.core.LuaManager;
import miscperipherals.core.TickHandler;
import miscperipherals.safe.ReflectionStore;
import miscperipherals.safe.Reflector;
import miscperipherals.util.FakePlayer;
import miscperipherals.util.Util;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Facing;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.Event;
import net.minecraftforge.event.Event.Result;
import dan200.computer.api.IComputerAccess;
import dan200.computer.api.IHostedPeripheral;
import dan200.turtle.api.ITurtleAccess;

public class PeripheralAlchemist implements IHostedPeripheral {
	public final ITurtleAccess turtle;
	public long miniumCharge = 0;
	private String error;
	
	public PeripheralAlchemist(ITurtleAccess turtle) {
		this.turtle = turtle;
	}
	
	@Override
	public String getType() {
		return "alchemist";
	}

	@Override
	public String[] getMethodNames() {
		return new String[] {"transmuteItem","transmute","transmuteUp","transmuteDown","rechargeMinium","getMiniumCharge","getError"};
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, int method, Object[] arguments) throws Exception {
		switch (method) {
			case 0: {
				ItemStack slot = turtle.getSlotContents(turtle.getSelectedSlot());
				int amount = Integer.MIN_VALUE;
				if (arguments.length > 0) {
					if (!(arguments[0] instanceof Double)) throw new Exception("bad argument #1 (expected number)");
					amount = (int)Math.floor((Double)arguments[0]);
				}
				
				if (slot == null) {
					error = "No item to transmute";
					return new Object[] {false};
				}
				if (amount == Integer.MIN_VALUE) amount = slot.stackSize;
				if (amount < 1) {
					error = "Zero items to transmute";
					return new Object[] {false};
				}
				if (amount > 8) amount = 8;
				
				InventoryCrafting inv = new InventoryCrafting(new Container() {
					@Override
					public boolean canInteractWith(EntityPlayer var1) {
						return true;
					}
				}, 3, 3);
				
				inv.setInventorySlotContents(0, new ItemStack(ReflectionStore.miniumStone));
				
				ItemStack single = slot.copy();
				single.stackSize = 1;
				for (int i = 0; i < amount; i++) {
					inv.setInventorySlotContents(i + 1, single);
				}
				
				ItemStack match = CraftingManager.getInstance().findMatchingRecipe(inv, turtle.getWorld());
				if (match == null) {
					error = "No transmutation recipe";
					return new Object[] {false};
				}
				
				if (!useMinium(1, true)) {
					error = "Not enough minium charge";
					return new Object[] {false};
				}
				
				slot.stackSize -= amount;
				if (slot.stackSize <= 0) slot = null;
				turtle.setSlotContents(turtle.getSelectedSlot(), slot);
				Util.storeOrDrop(turtle, match);
				
				return new Object[] {true};
			}
			case 1:
			case 2:
			case 3: {
				boolean sneak = false;
				if (arguments.length > 0) {
					if (!(arguments[0] instanceof Boolean)) throw new Exception("bad argument #1 (expected boolean)");
					sneak = (Boolean)arguments[0];
				}
				final boolean doSneak = sneak;
				
				final int direction = method == 1 ? turtle.getFacingDir() : (method == 2 ? 1 : 0);
				final World world = turtle.getWorld();
				Vec3 pos = turtle.getPosition();
				final int x = (int)Math.floor(pos.xCoord) + Facing.offsetsXForSide[direction];
				final int y = (int)Math.floor(pos.yCoord) + Facing.offsetsYForSide[direction];
				final int z = (int)Math.floor(pos.zCoord) + Facing.offsetsZForSide[direction];
				
				Future<Boolean> callback = TickHandler.addTickCallback(world, new Callable<Boolean>() {
					@Override
					public Boolean call() {
						try {
							if (!world.blockExists(x, y, z)) {
								error = "Block unloaded";
								return false;
							}
							
							if (!useMinium(1, false)) {
								error = "Not enough minium charge";
								return false;
							}
							
							int id = world.getBlockId(x, y, z);
							if (Block.blocksList[id] == null || Block.blocksList[id].isAirBlock(world, x, y, z)) {
								error = "No block to transmute";
								return false;
							}
							int meta = world.getBlockMetadata(x, y, z);
							
							ItemStack next = new ItemStack(id, 1, meta);
							do {
								next = Reflector.invoke("com.pahimar.ee3.core.helper.TransmutationHelper", "getNextBlock", ItemStack.class, next.itemID, next.getItemDamage());
								System.out.println(next);
							} while (!doSneak && next != null && Block.blocksList[next.itemID] instanceof BlockSand);
							
							if (next == null) {
								error = "Nothing to transmute to";
								return false;
							}
							
							ItemStack stone = new ItemStack(ReflectionStore.miniumStone);
							FakePlayer player = FakePlayer.get(turtle);
							player.alignToTurtle(turtle, direction);
							player.setHeldItem(stone);
							player.setFlag(1, doSneak);
							
							// FIXME formatTargetBlockInfo(ItemStack) uses the client-global target stack instead of the one passed
							Event event = Reflector.construct("com.pahimar.ee3.event.WorldTransmutationEvent", Event.class, Reflector.getField("com.pahimar.ee3.lib.ActionTypes", "TRANSMUTATION", Object.class), stone, player, world, x, y, z, false, next.itemID+":"+next.getItemDamage());
							
							if (event != null) {
								Event req = Reflector.construct("com.pahimar.ee3.event.ActionRequestEvent", Event.class, player, event, x, y, z, direction);
								MinecraftForge.EVENT_BUS.post(req);
								
								if (Reflector.getField(req, "allowEvent", Result.class) != Result.DENY) {
									MinecraftForge.EVENT_BUS.post(event);
								}
								
								if (Reflector.getField(event, "actionResult", Object.class) == ReflectionStore.actionResultSuccess) {
									useMinium(1, true);
									world.playSoundEffect(x + 0.5D, y + 0.5D, z + 0.5D, Reflector.getField("com.pahimar.ee3.lib.Sounds", "TRANSMUTE", String.class), 0.5F, 1.0F);
									
									return true;
								} else error = "Transmutation denied";
							} else error = "Internal error";
							
							return false;
						} catch (Throwable e) {
							e.printStackTrace();
							return false;
						}
					}
				});
				return new Object[] {callback.get()};
			}
			case 4: {
				ItemStack slot = turtle.getSlotContents(turtle.getSelectedSlot());
				int amount = Integer.MIN_VALUE;
				if (arguments.length > 0) {
					if (!(arguments[0] instanceof Double)) throw new Exception("bad argument #1 (expected number)");
					amount = (int)Math.floor((Double)arguments[0]);
				}
				
				if (slot == null) {
					error = "No item to recharge from";
					return new Object[] {false};
				}
				
				long recharge = 0;
				if (slot.itemID == ReflectionStore.miniumStone.itemID) {
					recharge = Math.min(amount == Integer.MIN_VALUE ? Integer.MAX_VALUE : amount, (ReflectionStore.miniumStone.getMaxDamage() - 1) - slot.getItemDamage());
					amount = 1;
				} else if (slot.itemID == ReflectionStore.philStone.itemID) {
					recharge = Long.MAX_VALUE;
					amount = 1;
				}
				
				if (recharge == Long.MAX_VALUE) miniumCharge = Long.MAX_VALUE;
				else miniumCharge += recharge * amount;
				
				if (recharge > 0) {
					slot.stackSize -= amount;
					if (slot.stackSize <= 0) slot = null;
					turtle.setSlotContents(turtle.getSelectedSlot(), slot);
				} else error = "Not a minium charge source";
				
				return new Object[] {recharge};
			}
			case 5: {
				return new Object[] {miniumCharge};
			}
			case 6: {
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
		miniumCharge = nbttagcompound.getLong("miniumCharge");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		nbttagcompound.setLong("miniumCharge", miniumCharge);
	}
	
	private boolean useMinium(int amount, boolean doUse) {
		if (miniumCharge == Long.MAX_VALUE || amount < 1) return true;
		if (amount > miniumCharge) return false;
		if (doUse) miniumCharge -= amount;
		return true;
	}
}
