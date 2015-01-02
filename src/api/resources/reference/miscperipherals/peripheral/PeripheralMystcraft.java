package miscperipherals.peripheral;


import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import miscperipherals.core.LuaManager;
import miscperipherals.core.MiscPeripherals;
import miscperipherals.core.TickHandler;
import miscperipherals.network.NetworkHelper;
import miscperipherals.util.Util;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.Vec3;
import net.minecraft.world.WorldServer;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.xcompwiz.mystcraft.api.MystObjects;
import com.xcompwiz.mystcraft.api.linking.ILinkInfo;

import cpw.mods.fml.common.network.PacketDispatcher;
import dan200.computer.api.IComputerAccess;
import dan200.computer.api.IHostedPeripheral;
import dan200.turtle.api.ITurtleAccess;

public class PeripheralMystcraft implements IHostedPeripheral {
	private final ITurtleAccess turtle;
	private int id = Integer.MIN_VALUE;
	
	public PeripheralMystcraft(ITurtleAccess turtle) {
		this.turtle = turtle;
	}
	
	@Override
	public String getType() {
		return "mystcraft";
	}

	@Override
	public String[] getMethodNames() {
		return new String[] {"useBook"};
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, int method, Object[] arguments) throws Exception {
		switch (method) {
			case 0: {
				Future<Boolean> callback = TickHandler.addTickCallback(turtle.getWorld(), new Callable<Boolean>() {
					@Override
					public Boolean call() {
						ItemStack slotstack = turtle.getSlotContents(turtle.getSelectedSlot());
						if (slotstack == null || !isValidBook(slotstack) || slotstack.stackTagCompound == null) {
							MiscPeripherals.debug("Link failed due to invalid item: "+slotstack+" "+(slotstack == null ? "N/A" : slotstack.getItem())+" "+(slotstack == null ? "N/A" : slotstack.stackTagCompound));
							return false;
						}
						
						ILinkInfo lo = new LinkInfoStack(slotstack);
						if (isLinkPermitted(lo)) {
							WorldServer world = MinecraftServer.getServer().worldServerForDimension(lo.getDimensionUID());
							if (world == null) {
								MiscPeripherals.debug("Link failed due to null world: "+lo.getDimensionUID());
								return false;
							}
							
							ChunkCoordinates dest = lo.getSpawn();
							dest.posY += 2; // whatever weird thing mystcraft does to avoid the old linking through the ground glitch
							if (Util.isPassthroughBlock(world, dest.posX, dest.posY, dest.posZ)) {
								if (!lo.getFlag("Following")) {
									for (int i = 0; i < 6; i++) {
										if (turtle.dropItemStack(slotstack, i)) break;
									}
									turtle.setSlotContents(turtle.getSelectedSlot(), null);
								}
								
								if (lo.getFlag("Disarm")) {
									for (int i = 0; i < turtle.getInventorySize(); i++) {
										ItemStack stack = turtle.getSlotContents(i);
										for (int j = 0; j < 6; j++) {
											if (turtle.dropItemStack(stack, i)) break;
										}
										turtle.setSlotContents(i, null);
									}
								}
								
								Vec3 pos = turtle.getPosition();
								String sound = "myst.sound.link";
								if (lo.getFlag("Disarm")) sound = "myst.sound.link-disarm";
								else if (lo.getProperty("Sound") != null) sound = lo.getProperty("Sound");
								else if (lo.getFlag("Following")) sound = "myst.sound.link-following";
								else if (lo.getFlag("Intra Linking")) sound = "myst.sound.link-intra";
								
								ByteArrayDataOutput os = ByteStreams.newDataOutput();
								os.writeInt((int)pos.xCoord);
								os.writeInt((int)pos.yCoord);
								os.writeInt((int)pos.zCoord);
								os.writeUTF(sound);
								PacketDispatcher.sendPacketToAllAround(pos.xCoord + 0.5D, pos.yCoord + 0.5D, pos.zCoord + 0.5D, 64.0D, turtle.getWorld().provider.dimensionId, NetworkHelper.getModulePacket(MiscPeripherals.instance.modules.get("Mystcraft"), os.toByteArray()));
								
								os = ByteStreams.newDataOutput();
								os.writeInt(dest.posX);
								os.writeInt(dest.posY);
								os.writeInt(dest.posZ);
								os.writeUTF(sound);
								PacketDispatcher.sendPacketToAllAround(dest.posX + 0.5D, dest.posY + 0.5D, dest.posZ + 0.5D, 64.0D, world.provider.dimensionId, NetworkHelper.getModulePacket(MiscPeripherals.instance.modules.get("Mystcraft"), os.toByteArray()));
								
								return Util.teleportTurtleTo(turtle, world, dest.posX, dest.posY, dest.posZ);
							}
							
							MiscPeripherals.debug("Link failed due to bad target "+world.getBlockId(dest.posX, dest.posY, dest.posZ));
							return false;
						}
						
						MiscPeripherals.debug("Link failed due to not permitted");
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
	
	private boolean isLinkPermitted(ILinkInfo lo) {
		if (!lo.getFlag("Intra Linking") && lo.getDimensionUID() != turtle.getWorld().provider.dimensionId) return false;
		
		return true;
	}
	
	private boolean isValidBook(ItemStack stack) {
		return stack.itemID == MystObjects.linkbook.itemID || stack.itemID == MystObjects.descriptive_book.itemID;
	}
	
	public class LinkInfoStack implements ILinkInfo {
		private final ItemStack stack;
		
		public LinkInfoStack(ItemStack stack) {
			this.stack = stack;
		}

		@Override
		public String getDisplayName() {
			return stack.stackTagCompound != null && stack.stackTagCompound.hasKey("agename") ? stack.stackTagCompound.getString("agename") : "???";
		}

		@Override
		public void setDisplayName(String displayname) {
			if (stack.stackTagCompound == null) stack.stackTagCompound = new NBTTagCompound();
			stack.stackTagCompound.setString("agename", displayname);
		}

		@Override
		public int getDimensionUID() {
			return stack.stackTagCompound != null ? (stack.stackTagCompound.hasKey("AgeUID") ? stack.stackTagCompound.getInteger("AgeUID") : (stack.stackTagCompound.hasKey("Dimension") ? stack.stackTagCompound.getInteger("Dimension") : 0)) : 0;
		}

		@Override
		public void setDimensionUID(int uid) {
			if (stack.stackTagCompound == null) stack.stackTagCompound = new NBTTagCompound();
			stack.stackTagCompound.setInteger("AgeUID", uid);
		}

		@Override
		public ChunkCoordinates getSpawn() {
			if (stack.stackTagCompound != null && stack.stackTagCompound.hasKey("SpawnX") && stack.stackTagCompound.hasKey("SpawnY") && stack.stackTagCompound.hasKey("SpawnZ")) {
				return new ChunkCoordinates(stack.stackTagCompound.getInteger("SpawnX"), stack.stackTagCompound.getInteger("SpawnY"), stack.stackTagCompound.getInteger("SpawnZ"));
			}
			
			return null;
		}

		@Override
		public void setSpawn(ChunkCoordinates spawn) {
			if (stack.stackTagCompound == null) stack.stackTagCompound = new NBTTagCompound();
			if (spawn != null) {
				stack.stackTagCompound.setInteger("SpawnX", spawn.posX);
				stack.stackTagCompound.setInteger("SpawnY", spawn.posY);
				stack.stackTagCompound.setInteger("SpawnZ", spawn.posZ);
			}
		}

		@Override
		public float getSpawnYaw() {
			return stack.stackTagCompound != null && stack.stackTagCompound.hasKey("SpawnYaw") ? stack.stackTagCompound.getFloat("SpawnYaw") : 0.0F;
		}

		@Override
		public void setSpawnYaw(float spawnyaw) {
			if (stack.stackTagCompound == null) stack.stackTagCompound = new NBTTagCompound();
			stack.stackTagCompound.setFloat("SpawnYaw", spawnyaw);
		}

		@Override
		public boolean getFlag(String flag) {
			return stack.stackTagCompound != null && stack.stackTagCompound.hasKey(flag) && stack.stackTagCompound.getBoolean(flag);
		}

		@Override
		public void setFlag(String flag, boolean value) {
			if (stack.stackTagCompound == null) stack.stackTagCompound = new NBTTagCompound();
			stack.stackTagCompound.setBoolean(flag, value);
		}

		@Override
		public String getProperty(String flag) {
			return stack.stackTagCompound != null && stack.stackTagCompound.hasKey(flag) ? stack.stackTagCompound.getString(flag) : null;
		}

		@Override
		public void setProperty(String flag, String value) {
			if (stack.stackTagCompound == null) stack.stackTagCompound = new NBTTagCompound();
			stack.stackTagCompound.setString(flag, value);
		}

		@Override
		public NBTTagCompound getTagCompound() {
			return stack.stackTagCompound;
		}
	}
}
