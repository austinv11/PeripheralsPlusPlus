package miscperipherals.tile;

import java.util.Stack;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import miscperipherals.core.LuaManager;
import miscperipherals.core.MiscPeripherals;
import miscperipherals.core.TickHandler;
import miscperipherals.util.Util;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.Facing;
import net.minecraft.world.World;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.network.PacketDispatcher;
import dan200.computer.api.IComputerAccess;
import dan200.computer.api.IPeripheral;
import dan200.turtle.api.ITurtleAccess;

public class TileTeleporter extends Tile implements IPeripheral {
	public final Stack<LinkData> links = new Stack<LinkData>();
	public final int maxLinks;
	private String error;
	
	public TileTeleporter() {
		this.maxLinks = 1;
	}
	
	public TileTeleporter(int maxLinks) {
		this.maxLinks = maxLinks;
	}
	
	@Override
	public String getType() {
		return "teleporter";
	}

	@Override
	public String[] getMethodNames() {
		return new String[] {"teleport","tp","getError"};
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, int method, Object[] arguments) throws Exception {
		switch (method) {
			case 0:
			case 1: {
				if (arguments.length > 0 && !(arguments[0] instanceof Double)) throw new Exception("bad argument #1 (expected number)");
				
				final int index = arguments.length > 0 ? (int)Math.floor((Double)arguments[0]) - 1 : 0;
				if (index < 0 || index >= maxLinks) throw new Exception("bad link "+(index+1)+" (expected 1-"+maxLinks+")");
				
				Future<Boolean> callback = TickHandler.addTickCallback(worldObj, new Callable<Boolean>() {
					@Override
					public Boolean call() {
						if (index >= links.size()) {
							error = "No such link";
							return false;
						}
						
						LinkData link = links.get(index);
						if (link == null) {
							error = "No such link";
							return false;
						}
						
						TileEntity te = worldObj.getBlockTileEntity(xCoord + Facing.offsetsXForSide[getFacing()], yCoord + Facing.offsetsYForSide[getFacing()], zCoord + Facing.offsetsZForSide[getFacing()]);
						if (!(te instanceof ITurtleAccess)) {
							error = "No turtle in front";
							return false;
						}
						
						World destWorld = MinecraftServer.getServer().worldServerForDimension(link.linkDim);
						if (destWorld == null) {
							error = "Destination world missing";
							return false;
						}
						
						TileEntity dest = destWorld.getBlockTileEntity(link.link.posX, link.link.posY, link.link.posZ);
						if (!(dest instanceof TileTeleporter)) {
							error = "Destination is not a teleporter";
							return false;
						}
						TileTeleporter teleporter = (TileTeleporter)dest;
						
						int linkToX = link.link.posX + Facing.offsetsXForSide[teleporter.getFacing()];
						int linkToY = link.link.posY + Facing.offsetsYForSide[teleporter.getFacing()];
						int linkToZ = link.link.posZ + Facing.offsetsZForSide[teleporter.getFacing()];
						if (!Util.isPassthroughBlock(destWorld, linkToX, linkToY, linkToZ)) {
							error = "Destination obstructed";
							return false;
						}
						
						int xdif = Math.abs(xCoord - link.link.posX);
						int ydif = Math.abs(yCoord - link.link.posY);
						int zdif = Math.abs(zCoord - link.link.posZ);
						ITurtleAccess turtle = (ITurtleAccess)te;
						if (!turtle.consumeFuel(Math.abs((int)Math.ceil((xdif + ydif + zdif) * (Math.abs(worldObj.provider.dimensionId - destWorld.provider.dimensionId) + 1) * MiscPeripherals.instance.teleporterPenalty)))) {
							error = "Not enough fuel";
							return false;
						}
						
						boolean result = Util.teleportTurtleTo(turtle, destWorld, linkToX, linkToY, linkToZ);
						if (result) {
							int flag = worldObj.provider.dimensionId != destWorld.provider.dimensionId ? 1 : 0;
							
							ByteArrayDataOutput os = ByteStreams.newDataOutput();
							os.writeInt(xCoord);
							os.writeInt(yCoord);
							os.writeInt(zCoord);
							os.writeByte(flag);
							PacketDispatcher.sendPacketToAllAround(xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D, 64.0D, worldObj.provider.dimensionId, PacketDispatcher.getTinyPacket(MiscPeripherals.instance, (short)4, os.toByteArray()));
							onTeleport((byte)flag);
							
							os = ByteStreams.newDataOutput();
							os.writeInt(teleporter.xCoord);
							os.writeInt(teleporter.yCoord);
							os.writeInt(teleporter.zCoord);
							os.writeByte(flag);
							PacketDispatcher.sendPacketToAllAround(teleporter.xCoord + 0.5D, teleporter.yCoord + 0.5D, teleporter.zCoord + 0.5D, 64.0D, destWorld.provider.dimensionId, PacketDispatcher.getTinyPacket(MiscPeripherals.instance, (short)4, os.toByteArray()));
							teleporter.onTeleport((byte)flag);
						}
						
						return result;
					}
				});
				return new Object[] {callback.get()};
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
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		
		NBTTagList links = compound.getTagList("links");
		for (int i = 0; i < links.tagCount(); i++) {
			NBTTagCompound link = (NBTTagCompound)links.tagAt(i);
			if (link.hasKey("linkX") && link.hasKey("linkY") && link.hasKey("linkZ") && link.hasKey("linkDim")) {
				this.links.add(new LinkData(compound.getInteger("linkDim"), new ChunkCoordinates(compound.getInteger("linkX"), compound.getInteger("linkY"), compound.getInteger("linkZ"))));
			}
		}
	}
	
	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		
		NBTTagList list = new NBTTagList();
		for (int i = 0; i < links.size(); i++) {
			LinkData link = links.get(i);
			if (link != null) {
				NBTTagCompound lcompound = new NBTTagCompound();
				lcompound.setInteger("linkX", link.link.posX);
				lcompound.setInteger("linkY", link.link.posY);
				lcompound.setInteger("linkZ", link.link.posZ);
				lcompound.setInteger("linkDim", link.linkDim);
				list.appendTag(lcompound);
			}
		}
		compound.setTag("links", list);
	}
	
	@Override
	public boolean onBlockActivated(EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		ItemStack held = player.getCurrentEquippedItem();
		if (held != null && held.itemID == Item.redstoneRepeater.itemID) {
			if (held.stackTagCompound != null) {
				if (held.stackTagCompound.hasKey("miscperipheralsLinkX") && held.stackTagCompound.hasKey("miscperipheralsLinkY") && held.stackTagCompound.hasKey("miscperipheralsLinkZ") && held.stackTagCompound.hasKey("miscperipheralsLinkDim")) {
					ChunkCoordinates link = new ChunkCoordinates(held.stackTagCompound.getInteger("miscperipheralsLinkX"), held.stackTagCompound.getInteger("miscperipheralsLinkY"), held.stackTagCompound.getInteger("miscperipheralsLinkZ"));
					int linkDim = held.stackTagCompound.getInteger("miscperipheralsLinkDim");
					
					World srcWorld = MinecraftServer.getServer().worldServerForDimension(linkDim);
					if (srcWorld == null) {
						player.sendChatToPlayer("Link failed: World is missing");
					} else {
						TileEntity te = srcWorld.getBlockTileEntity(link.posX, link.posY, link.posZ);
						if (!(te instanceof TileTeleporter)) {
							player.sendChatToPlayer("Link failed: Teleporter no longer exists");
						} else {
							TileTeleporter src = (TileTeleporter)te;
							
							if (link.posX == xCoord && link.posY == yCoord && link.posZ == zCoord) {
								player.sendChatToPlayer("Link canceled");
							} else {
								boolean unlinked = false;
								for (int i = 0; i < src.links.size(); i++) {
									LinkData rlink = src.links.get(i);
									System.out.println("comparing: "+rlink.link.posX+" "+xCoord+" "+rlink.link.posY+" "+yCoord+" "+rlink.link.posZ+" "+zCoord);
									if (rlink.link.posX == xCoord && rlink.link.posY == yCoord && rlink.link.posZ == zCoord && rlink.linkDim == worldObj.provider.dimensionId) {
										player.sendChatToPlayer("Unlinked teleporter at "+rlink.linkDim+":("+rlink.link.posX+","+rlink.link.posY+","+rlink.link.posZ+") (link "+(i+1)+") from this teleporter");
										src.links.remove(i);
										unlinked = true;
										break;
									}
								}
								
								if (!unlinked) {
									src.addLink(worldObj.provider.dimensionId, new ChunkCoordinates(xCoord, yCoord, zCoord));
									player.sendChatToPlayer("Linked teleporter at "+linkDim+":("+link.posX+","+link.posY+","+link.posZ+") (link "+src.links.size()+") to this teleporter");
								}
							}
						}
					}
					
					held.stackTagCompound.removeTag("miscperipheralsLinkX");
					held.stackTagCompound.removeTag("miscperipheralsLinkY");
					held.stackTagCompound.removeTag("miscperipheralsLinkZ");
					held.stackTagCompound.removeTag("miscperipheralsLinkDim");
					if (held.stackTagCompound.hasKey("display")) {
						NBTTagCompound display = held.stackTagCompound.getCompoundTag("display");
						display.removeTag("Lore");
						if (display.getTags().isEmpty()) {
							held.stackTagCompound.removeTag("display");
						} else {
							held.stackTagCompound.setTag("display", display);
						}
					}
					
					return true;
				}
			}
			
			if (held.stackTagCompound == null) held.stackTagCompound = new NBTTagCompound();
			held.stackTagCompound.setInteger("miscperipheralsLinkX", xCoord);
			held.stackTagCompound.setInteger("miscperipheralsLinkY", yCoord);
			held.stackTagCompound.setInteger("miscperipheralsLinkZ", zCoord);
			held.stackTagCompound.setInteger("miscperipheralsLinkDim", worldObj.provider.dimensionId);
			NBTTagCompound display = new NBTTagCompound();
			NBTTagList lore = new NBTTagList();
			lore.appendTag(new NBTTagString("", "Turtle Teleporter Link"));
			lore.appendTag(new NBTTagString("", worldObj.provider.dimensionId+":("+xCoord+","+yCoord+","+zCoord+")"));
			display.setTag("Lore", lore);
			held.stackTagCompound.setTag("display", display);
			
			player.sendChatToPlayer("Link started");
			return true;
		}
		
		return false;
	}
	
	public void onTeleport(byte flag) {
		if (!MiscPeripherals.proxy.isServer()) {
			int facing = getFacing();
			for (int j = 0; j < 32; j++) {
				worldObj.spawnParticle("portal", xCoord + 0.5D + 1.0D * Facing.offsetsXForSide[facing], yCoord + 1.0D * Facing.offsetsYForSide[facing] + worldObj.rand.nextDouble() * 2.0D, zCoord + 0.5D + 1.0D * Facing.offsetsZForSide[getFacing()], worldObj.rand.nextGaussian(), 0.0D, worldObj.rand.nextGaussian());
			}
		} else {
			String sound = "mob.endermen.portal";
			if (Loader.isModLoaded("Mystcraft")) sound = flag == 1 ? "myst.sound.link-portal" : "myst.sound.link-intra";
			worldObj.playSoundEffect(xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D, sound, 1.0F, 1.0F);
		}
	}
	
	public int addLink(int linkDim, ChunkCoordinates link) {
		links.add(new LinkData(linkDim, link));
		while (links.size() > maxLinks) links.pop();
		return links.size();
	}
	
	protected static class LinkData {
		public final int linkDim;
		public final ChunkCoordinates link;
		
		public LinkData(int linkDim, ChunkCoordinates link) {
			this.linkDim = linkDim;
			this.link = link;
		}
	}
}
