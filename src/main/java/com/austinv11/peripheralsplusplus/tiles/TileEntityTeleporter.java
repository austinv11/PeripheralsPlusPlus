package com.austinv11.peripheralsplusplus.tiles;

import com.austinv11.peripheralsplusplus.PeripheralsPlusPlus;
import com.austinv11.peripheralsplusplus.blocks.BlockPppDirectional;
import com.austinv11.peripheralsplusplus.blocks.BlockTeleporter;
import com.austinv11.peripheralsplusplus.network.ParticlePacket;
import com.austinv11.peripheralsplusplus.reference.Config;
import com.austinv11.peripheralsplusplus.utils.ReflectionHelper;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.ITurtleAccess;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import java.util.HashMap;
import java.util.Stack;

public class TileEntityTeleporter extends MountedTileEntity {

	public static String publicName = "teleporter";
	private String name = "tileEntityTeleporter";
	public Stack<LinkData> links = new Stack<LinkData>();
	public String tag = null;

	public TileEntityTeleporter() {
		super();
	}

	public String getName() {
		return name;
	}

	public int getMaxLinks() {
		int tier = world.getBlockState(getPos()).getValue(BlockTeleporter.TIER);
		return tier == 0 ? 1 : 8;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		if (nbttagcompound.hasKey("tTag"))
			tag = nbttagcompound.getString("tTag");
		NBTTagList links = nbttagcompound.getTagList("links", Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < links.tagCount(); i++) {
			NBTTagCompound link = links.getCompoundTagAt(i);
			if (link.hasKey("linkX") && link.hasKey("linkY") && link.hasKey("linkZ") && link.hasKey("linkDim")) {
				this.links.add(new LinkData(link.getInteger("linkDim"),
						new BlockPos(link.getInteger("linkX"), link.getInteger("linkY"), link.getInteger("linkZ"))));
			}
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
		if (tag != null)
			nbttagcompound.setString("tTag", tag);
		NBTTagList list = new NBTTagList();
		for (int i = 0; i < links.size(); i++) {
			LinkData link = links.get(i);
			if (link != null) {
				NBTTagCompound lcompound = new NBTTagCompound();
				lcompound.setInteger("linkX", link.link.getX());
				lcompound.setInteger("linkY", link.link.getY());
				lcompound.setInteger("linkZ", link.link.getZ());
				lcompound.setInteger("linkDim", link.linkDim);
				list.appendTag(lcompound);
			}
		}
		nbttagcompound.setTag("links", list);
		return nbttagcompound;
	}

	@Override
	public String getType() {
		return publicName;
	}

	@Override
	public String[] getMethodNames() {
		return new String[] {"teleport", "tp", "getLinks", "setName"};
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws LuaException, InterruptedException {
		if (!Config.enableTurtleTeleporter)
			throw new LuaException("Turtle teleporters have been disabled");
		if (method == 0 || method == 1) {
			if (arguments.length > 0 && !(arguments[0] instanceof Double))
				throw new LuaException("Bad argument #1 (expected number)");
			int index = arguments.length > 0 ? (int)Math.floor((Double)arguments[0]) - 1 : 0;
			if (index < 0 || index >= getMaxLinks())
				throw new LuaException("Bad link "+(index+1)+" (expected 1-"+getMaxLinks()+")");
			if (index >= links.size())
				throw new LuaException("No such link");
			LinkData link = links.get(index);
			if (link == null)
				throw new LuaException("No such link");
			IBlockState blockState = world.getBlockState(getPos());
			EnumFacing direction = blockState.getValue(BlockPppDirectional.FACING);
			TileEntity te = world.getTileEntity(getPos().offset(direction));
			try {
				if (ReflectionHelper.getTurtle(te) == null)
					throw new LuaException("No turtle in front");
			}catch (Exception e) {
				throw new LuaException("No turtle in front");
			}
			World destWorld = DimensionManager.getWorld(link.linkDim);
			if (destWorld == null)
				throw new LuaException("Destination world missing");
			TileEntity dest = destWorld.getTileEntity(link.link);
			if (!(dest instanceof TileEntityTeleporter))
				throw new LuaException("Destination is not a teleporter");
			IBlockState destinationBlockState = world.getBlockState(dest.getPos());
			EnumFacing destinationDirection = destinationBlockState.getValue(BlockPppDirectional.FACING);
			BlockPos destinationPos = dest.getPos().offset(destinationDirection);
			if (!destinationBlockState.getBlock().isReplaceable(destWorld, destinationPos) ||
					destinationPos.getY() < 0 || destinationPos.getY() > 254)
				throw new LuaException("Destination obstructed");
			double distance = getPos().distanceSq(link.link);
			ITurtleAccess turtle = null;
			try {
				turtle = ReflectionHelper.getTurtle(te);
			}catch (Exception ignored) {}
			if (turtle == null)
				throw new LuaException("Could not get turtle");
			double fuelUsed = distance *
					Math.min(Math.max(Math.abs(
							world.provider.getDimension() - destWorld.provider.getDimension()),
							100), 1) *
					Config.teleporterPenalty;
			if (!turtle.consumeFuel(Math.abs((int)Math.ceil(fuelUsed))))
				throw new LuaException("Not enough fuel");
			boolean result = turtle.teleportTo(destWorld, destinationPos);
			destWorld.markAndNotifyBlock(destinationPos, destWorld.getChunkFromBlockCoords(destinationPos),
					destinationBlockState, destinationBlockState, 2);
			world.markAndNotifyBlock(pos, world.getChunkFromBlockCoords(pos), blockState, blockState, 2);
			if (result) {
				BlockPos particlePos = pos.offset(direction);
				PeripheralsPlusPlus.NETWORK.sendToAllAround(new ParticlePacket(
						"portal",
						particlePos.getX(),
						particlePos.getY(),
						particlePos.getZ(),
						world.rand.nextGaussian(),
						0,
						world.rand.nextGaussian()
				), new NetworkRegistry.TargetPoint(
						world.provider.getDimension(),
						particlePos.getX(),
						particlePos.getY(),
						particlePos.getZ(),
						16));
				world.playSound(
						particlePos.getX(),
						particlePos.getY(),
						particlePos.getZ(),
						new SoundEvent(new ResourceLocation("minecraft","mob.endermen.portal")),
						SoundCategory.BLOCKS,
						1,
						1,
						true);
			}
			return new Object[]{result};
		}else if (method == 2) {
			HashMap<Integer, Object> map1 = new HashMap<Integer,Object>();
			for (int i = 0; i < links.size(); i++) {
				HashMap<String,Object> map2 = new HashMap<String,Object>();
				map2.put("dim", links.get(i).linkDim);
				map2.put("x", links.get(i).link.getX());
				map2.put("y", links.get(i).link.getY());
				map2.put("z", links.get(i).link.getZ());
				map2.put("name", name);
				map1.put(i, map2.clone());
			}
			return new Object[]{map1};
		}else if (method == 3) {
			if (!(arguments.length >= 1) || !(arguments[0] instanceof String))
				throw new LuaException("Bad argument #1 (expected string)");
			this.name = (String) arguments[0];
			return new Object[]{name};
		}
		return new Object[0];
	}

	@Override
	public boolean equals(IPeripheral other) {
		return (other == this);
	}

	public void blockActivated(EntityPlayer player, EnumHand hand) {
		ItemStack held = player.getHeldItem(hand);
		if (!held.isEmpty() && held.isItemEqual(new ItemStack(Items.REPEATER))) {
			if (held.getTagCompound() != null) {
				if (held.getTagCompound().hasKey("p++LinkX") && held.getTagCompound().hasKey("p++LinkY") &&
						held.getTagCompound().hasKey("p++LinkZ") && held.getTagCompound().hasKey("p++LinkDim")) {
					BlockPos link = new BlockPos(held.getTagCompound().getInteger("p++LinkX"),
							held.getTagCompound().getInteger("p++LinkY"),
							held.getTagCompound().getInteger("p++LinkZ"));
					int linkDim = held.getTagCompound().getInteger("p++LinkDim");
					World srcWorld = DimensionManager.getWorld(linkDim);
					if (srcWorld == null) {
						player.sendMessage(new TextComponentString("Link failed: World is missing"));
					} else {
						TileEntity te = srcWorld.getTileEntity(link);
						if (!(te instanceof TileEntityTeleporter)) {
							player.sendMessage(new TextComponentString("Link failed: Teleporter no longer exists"));
						} else {
							TileEntityTeleporter src = (TileEntityTeleporter)te;
							if (link.equals(getPos())) {
								player.sendMessage(new TextComponentString("Link canceled"));
							} else {
								boolean unlinked = false;
								for (int i = 0; i < src.links.size(); i++) {
									LinkData rlink = src.links.get(i);
									if (rlink.link.equals(getPos()) && rlink.linkDim == world.provider.getDimension()) {
										player.sendMessage(new TextComponentString("Unlinked teleporter at " +
												rlink.linkDim + ":(" + rlink.link.getX() + "," + rlink.link.getY() + ","
												+ rlink.link.getZ() + ") (link " + (i + 1) + ") from this teleporter"));
										src.links.remove(i);
										unlinked = true;
										break;
									}
								}
								if (!unlinked) {
									src.addLink(world.provider.getDimension(), getPos());
									player.sendMessage(new TextComponentString("Linked teleporter at " + linkDim +
											":(" + link.getX() + "," + link.getY() + "," + link.getZ() + ") (link " +
											src.links.size() + ") to this teleporter"));
								}
							}
						}
					}
					held.getTagCompound().removeTag("p++LinkX");
					held.getTagCompound().removeTag("p++LinkY");
					held.getTagCompound().removeTag("p++LinkZ");
					held.getTagCompound().removeTag("p++LinkDim");
					if (held.getTagCompound().hasKey("display")) {
						NBTTagCompound display = held.getTagCompound().getCompoundTag("display");
						display.removeTag("Lore");
						if (display.hasNoTags()) {
							held.getTagCompound().removeTag("display");
						} else {
							held.getTagCompound().setTag("display", display);
						}
					}
					return;
				}
			}
			if (held.getTagCompound() == null)
				held.setTagCompound(new NBTTagCompound());
			held.getTagCompound().setInteger("p++LinkX", getPos().getX());
			held.getTagCompound().setInteger("p++LinkY", getPos().getY());
			held.getTagCompound().setInteger("p++LinkZ", getPos().getZ());
			held.getTagCompound().setInteger("p++LinkDim", world.provider.getDimension());
			NBTTagCompound display = new NBTTagCompound();
			NBTTagList lore = new NBTTagList();
			lore.appendTag(new NBTTagString("Turtle Teleporter Link"));
			lore.appendTag(new NBTTagString(world.provider.getDimension()+":("+pos.getX()+","+pos.getY()+","+
					pos.getZ()+")"));
			display.setTag("Lore", lore);
			held.getTagCompound().setTag("display", display);
			player.sendMessage(new TextComponentString("Link started"));
		}
	}

	public int addLink(int linkDim, BlockPos link) {
		links.add(new LinkData(linkDim, link));
		while (links.size() > getMaxLinks())
			links.pop();
		return links.size();
	}

	public static class LinkData {
		public int linkDim;
		public BlockPos link;

		public LinkData(int linkDim, BlockPos link) {
			this.linkDim = linkDim;
			this.link = link;
		}
	}
}
