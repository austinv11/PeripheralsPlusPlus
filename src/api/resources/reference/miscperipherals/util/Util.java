package miscperipherals.util;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import miscperipherals.core.MiscPeripherals;
import miscperipherals.safe.Reflector;
import miscperipherals.tile.TileInventory;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFluid;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.Facing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.liquids.LiquidStack;
import net.minecraftforge.oredict.OreDictionary;
import dan200.computer.api.IHostedPeripheral;
import dan200.turtle.api.ITurtleAccess;
import dan200.turtle.api.ITurtleUpgrade;
import dan200.turtle.api.TurtleSide;

public class Util {
	public static final int[] YAW = {0, 0, 180, 360, 90, 270};
	public static final int[] PITCH = {90, -90, 0, 0, 0, 0};
	public static final int[] SIDE_TO_FACE = {-1, -1, 2, 0, 1, 3};
	public static final int[] FACE_TO_SIDE = {3, 4, 2, 5};
	public static final int[] OPPOSITE = {1, 0, 3, 2, 5, 4};
	public static final Color[] MAP_COLORS = {
		new Color(0,0,0),
		new Color(0,0,0),
		new Color(0,0,0),
		new Color(0,0,0),
		new Color(89,125,39),
		new Color(109,153,48),
		new Color(127,178,56),
		new Color(109,153,48),
		new Color(174,164,115),
		new Color(213,201,140),
		new Color(247,233,163),
		new Color(213,201,140),
		new Color(117,117,117),
		new Color(144,144,144),
		new Color(167,167,167),
		new Color(144,144,144),
		new Color(180,0,0),
		new Color(220,0,0),
		new Color(255,0,0),
		new Color(220,0,0),
		new Color(112,112,180),
		new Color(138,138,220),
		new Color(160,160,255),
		new Color(138,138,220),
		new Color(117,117,117),
		new Color(144,144,144),
		new Color(167,167,167),
		new Color(144,144,144),
		new Color(0,87,0),
		new Color(0,106,0),
		new Color(0,124,0),
		new Color(0,106,0),
		new Color(180,180,180),
		new Color(220,220,220),
		new Color(255,255,255),
		new Color(220,220,220),
		new Color(115,118,129),
		new Color(141,144,158),
		new Color(164,168,184),
		new Color(141,144,158),
		new Color(129,74,33),
		new Color(157,91,40),
		new Color(183,106,47),
		new Color(157,91,40),
		new Color(79,79,79),
		new Color(96,96,96),
		new Color(112,112,112),
		new Color(96,96,96),
		new Color(45,45,180),
		new Color(55,55,220),
		new Color(64,64,255),
		new Color(55,55,220),
		new Color(73,58,35),
		new Color(89,71,43),
		new Color(104,83,50),
		new Color(89,71,43)
	};
	public static final TurtleSide[] TURTLE_SIDES = TurtleSide.values();
	private static NBTTagCompound e = null;
	
	public static Map<Integer, Integer> arrayToMap(int[] array) {
		Map<Integer, Integer> ret = new HashMap<Integer, Integer>();
		for (int i = 0; i < array.length; i++) ret.put(i + 1, array[i]);
		return ret;
	}
	
	public static <T> Map<Integer, T> arrayToMap(T[] array) {
		Map<Integer, T> ret = new HashMap<Integer, T>();
		for (int i = 0; i < array.length; i++) ret.put(i + 1, array[i]);
		return ret;
	}
	
	public static <T> Map<Integer, T> listToMap(List<T> list) {
		Map<Integer, T> ret = new HashMap<Integer, T>();
		for (int i = 0; i < list.size(); i++) ret.put(i + 1, list.get(i));
		return ret;
	}

	public static <T> Map<Integer, T> iterableToMap(Iterable<? extends T> iterable) {
		Map<Integer, T> ret = new HashMap<Integer, T>();
		int i = 1;
		for (T item : iterable) ret.put(i++, item);
		return ret;
	}
	
	public static <T> T getFirstElement(List<? extends T> list) {
		for (int i = 0; i < list.size(); i++) {
			T obj = list.get(i);
			if (obj != null) return obj;
		}
		
		return null;
	}
	
	public static <T> T getFirstElement(T[] array) {
		for (int i = 0; i < array.length; i++) {
			if (array[i] != null) return array[i];
		}
		
		return null;
	}
	
	public static <T> List<T> merge(Collection<T>... collections) {
		int size = 0;
		for (Collection collection : collections) {
			size += collection.size();
		}
		
		ArrayList<T> ret = new ArrayList<T>(size);
		for (Collection collection : collections) {
			ret.addAll(collection);
		}
		return ret;
	}
	
	public static int[] getInventorySlots(IInventory inv, int side) {
		return inv instanceof ISidedInventory ? ((ISidedInventory)inv).getAccessibleSlotsFromSide(side) : (inv instanceof net.minecraftforge.common.ISidedInventory ? makeLegacySlotArray((net.minecraftforge.common.ISidedInventory)inv, side) : makeSlotArray(inv));
	}
	
	public static List<Integer> buildInventorySlotWhitelist(IInventory inv) {
		List<Integer> ret = new ArrayList<Integer>(inv.getSizeInventory());

		if (inv instanceof ISidedInventory) {
			ISidedInventory sidedinv = (ISidedInventory)inv;

			for (int i = 0; i < 6; i++) {
				int[] slots = sidedinv.getAccessibleSlotsFromSide(i);
				for (int j = 0; j < slots.length; j++) {
					ret.add(j);
				}
			}
			
			if (inv instanceof TileEntityFurnace) { // special case for furnace exploit
				ret.remove(2);
			}
		} else {
			for (int i = 0; i < inv.getSizeInventory(); i++) {
				ret.add(i);
			}
		}

		return ret;
	}

	public static AxisAlignedBB getAABB(ITurtleAccess turtle, int direction) {
		Vec3 pos = turtle.getPosition();
		return AxisAlignedBB.getAABBPool().getAABB(
				pos.xCoord - 0.5D,
				pos.yCoord - 0.5D,
				pos.zCoord - 0.5D,
				pos.xCoord + 0.5D + (1.0D * Facing.offsetsXForSide[direction]),
				pos.yCoord + 0.5D + (1.0D * Facing.offsetsYForSide[direction]),
				pos.zCoord + 0.5D + (1.0D * Facing.offsetsZForSide[direction])
				);
	}

	public static void storeOrDrop(ITurtleAccess turtle, ItemStack stack) {
		if (!turtle.storeItemStack(stack)) {
			if (!turtle.dropItemStack(stack, OPPOSITE[turtle.getFacingDir()])) {
				turtle.dropItemStack(stack, turtle.getFacingDir());
			}
		}
	}
	
	public static void storeOrDrop(TileInventory inv, ItemStack stack) {
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack slotstack = inv.getStackInSlot(i);
			if (slotstack != null && !Util.areStacksEqual(slotstack, stack)) continue;
			
			int add = Math.min(stack.stackSize, slotstack == null ? Math.min(stack.getMaxStackSize(), inv.getInventoryStackLimit()) : slotstack.getMaxStackSize() - slotstack.stackSize);
			
			if (slotstack == null) {
				slotstack = stack.copy();
			} else {
				slotstack.stackSize += add;
			}
			stack.stackSize -= add;
			inv.setInventorySlotContents(i, slotstack);
			
			if (stack.stackSize <= 0) return;
		}
		
		int direction = 0;
		float xoff = inv.worldObj.rand.nextFloat() * 0.8F + 0.1F + (0.5F * Facing.offsetsXForSide[direction]);
		float yoff = inv.worldObj.rand.nextFloat() * 0.8F + 0.1F + (0.5F * Facing.offsetsYForSide[direction]);
		float zoff = inv.worldObj.rand.nextFloat() * 0.8F + 0.1F + (0.5F * Facing.offsetsZForSide[direction]);
		
		EntityItem item = new EntityItem(inv.worldObj, inv.xCoord + xoff, inv.yCoord + yoff, inv.zCoord + zoff, stack);
		item.delayBeforeCanPickup = 10;
		item.motionX = (float)inv.worldObj.rand.nextGaussian() * 0.05F + Facing.offsetsXForSide[direction];
		item.motionY = (float)inv.worldObj.rand.nextGaussian() * 0.05F + Facing.offsetsYForSide[direction];
		item.motionZ = (float)inv.worldObj.rand.nextGaussian() * 0.05F + Facing.offsetsZForSide[direction];
		
		inv.worldObj.spawnEntityInWorld(item);
	}

	public static boolean areStacksEqual(ItemStack stack1, ItemStack stack2) {
		if (stack1 == null && stack2 == null) return true;
		else if (stack1 == null || stack2 == null) return false;
		else return stack1.itemID == stack2.itemID && (stack1.getItemDamage() == OreDictionary.WILDCARD_VALUE || stack2.getItemDamage() == OreDictionary.WILDCARD_VALUE || (stack1.isItemStackDamageable() && stack2.isItemStackDamageable()) || stack1.getItemDamage() == stack2.getItemDamage()) && ItemStack.areItemStackTagsEqual(stack1, stack2);
	}
	
	public static String camelCase(String in) {
		String s = "";
		String[] split = in.replace('_', ' ').split(" ");
		for (int i = 0; i < split.length; i++) {
			if (i == 0) s += split[i].toLowerCase();
			else s += split[i].substring(0, 1).toUpperCase() + split[i].substring(1).toLowerCase();
		}
		return s;
	}
	
	public static String sanitize(String s, boolean allowColorCodes) {
		// well-known client crash in FontRenderer
		while (s.endsWith("\u00a7")) s.substring(0, s.length() - 1);
		
		// strip minecraft and IRC codes
		if (!allowColorCodes) s = s.replace('\u00a7', ' ').replace((char)0x03, ' ').replace((char)0x02, ' ').replace((char)0x1D, ' ').replace((char)0x1F, ' ').replace((char)0x16, ' ');
		
		// nope
		s = s.replace('\n', ' ').replace('\r', ' ').replace('\b', ' ').replace('\f', ' ').replace("\t", "    ");
		
		return s;
	}
	
	public static int getXPFromLevel(int level) {
		return (int)(level <= 15 ? 17 * level : (level <= 30 ? 1.5D * (level * level) - 29.5D * level + 360 : 3.5D * (level - level) - 151.5D * level + 2220));
	}
	
	/**
	 * EntityPlayer.xpBarCap
	 */
	public static int getXPToAdvance(int level) {
		return level >= 30 ? 62 + (level - 30) * 7 : (level >= 15 ? 17 + (level - 15) * 3 : 17);
	}
	
	public static int getLevelFromXP(int xp) {
		int level = 0;
		while (xp >= 0) {
			xp -= getXPToAdvance(level);
			if (xp >= 0) level++;
		}
		return level;
	}
	
	public static <T> T getPeripheral(ITurtleAccess turtle, Class<T> clazz) {
		for (TurtleSide side : TURTLE_SIDES) {
			IHostedPeripheral peripheral = turtle.getPeripheral(side);
			if (peripheral != null && clazz.isAssignableFrom(peripheral.getClass())) return (T)peripheral;
		}
		
		return null;
	}
	
	public static TurtleSide getPeripheralSide(ITurtleAccess turtle, Class clazz) {
		for (TurtleSide side : TURTLE_SIDES) {
			IHostedPeripheral peripheral = turtle.getPeripheral(side);
			if (peripheral != null && clazz.isAssignableFrom(peripheral.getClass())) return side;
		}
		
		return null;
	}
	
	public static int addFuel(ITurtleAccess turtle, int amount) {
		if (Integer.MAX_VALUE - amount <= turtle.getFuelLevel()) {
			int added = Integer.MAX_VALUE - turtle.getFuelLevel();
			turtle.consumeFuel(-added);
			return added;
		} else {
			turtle.consumeFuel(-amount);
			return amount;
		}
	}
	
	public static boolean teleportTurtleTo(ITurtleAccess turtle, World world, int x, int y, int z) {
		Vec3 pos = turtle.getPosition();
		World prevWorld = turtle.getWorld();
		int id = prevWorld.getBlockId((int)pos.xCoord, (int)pos.yCoord, (int)pos.zCoord);
		int meta = prevWorld.getBlockMetadata((int)pos.xCoord, (int)pos.yCoord, (int)pos.zCoord);
		
		world.setBlock(x, y, z, id, meta, 2);
		TileEntity te = world.getBlockTileEntity(x, y, z);
		if (!(te instanceof ITurtleAccess)) return false;
		
		Reflector.setField(turtle, "m_moved", true);
		prevWorld.setBlock((int)pos.xCoord, (int)pos.yCoord, (int)pos.zCoord, 0, 0, 2);
		Reflector.invoke(te, "transferStateFrom", Object.class, turtle);
		
		prevWorld.markBlockForUpdate((int)pos.xCoord, (int)pos.yCoord, (int)pos.zCoord);
		world.markBlockForUpdate(x, y, z);
		
		world.notifyBlockChange(x, y, z, id);
		prevWorld.notifyBlockChange((int)pos.xCoord, (int)pos.yCoord, (int)pos.zCoord, 0);
		
		return true;
	}
	
	public static boolean isPassthroughBlock(World world, int x, int y, int z) {
		if (y < 0 || y > 254) return false;
		
		int id = world.getBlockId(x, y, z);
		return Block.blocksList[id] == null || Block.blocksList[id].isAirBlock(world, x, y, z) || Block.blocksList[id] instanceof BlockFluid || Block.blocksList[id] instanceof BlockSnow || Block.blocksList[id] instanceof BlockTallGrass;
	}
	
	public static void writeChunkCoordinatesToNBT(ChunkCoordinates coordinates, NBTTagCompound compound) {
		compound.setInteger("x", coordinates.posX);
		compound.setInteger("y", coordinates.posY);
		compound.setInteger("z", coordinates.posZ);
	}
	
	public static ChunkCoordinates readChunkCoordinatesFromNBT(NBTTagCompound compound) {
		return new ChunkCoordinates(compound.getInteger("x"), compound.getInteger("y"), compound.getInteger("z"));
	}
	
	public static NBTTagList writeInventoryToNBT(IInventory inv) {
		NBTTagList list = new NBTTagList();
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			
			if (stack != null) {
				NBTTagCompound itemTag = new NBTTagCompound();
				itemTag.setByte("Slot", (byte)i);
				stack.writeToNBT(itemTag);
				list.appendTag(itemTag);
			}
		}
		return list;
	}
	
	public static void readInventoryFromNBT(IInventory inv, NBTTagList list) {
		for (int i = 0; i < list.tagCount(); i++) {
			NBTTagCompound itemTag = (NBTTagCompound)list.tagAt(i);
			
			int slot = itemTag.getByte("Slot") & 255;
			if (slot >= 0 && slot < inv.getSizeInventory()) {
				inv.setInventorySlotContents(slot, ItemStack.loadItemStackFromNBT(itemTag));
			}
		}
	}
	
	public static ForgeDirection getDirectionFromTurtleSide(TurtleSide side, int facing) {
		if (side == null) return null;
		
		switch (side) {
			case Left: return ForgeDirection.getOrientation(facing).getRotation(ForgeDirection.WEST);
			case Right: return ForgeDirection.getOrientation(facing).getRotation(ForgeDirection.EAST);
			default: return null;
		}
	}
	
	public static <T> String join(String separator, T[] array) {
		String ret = "";
		for (int i = 0; i < array.length; i++) {
			ret += separator+array[i].toString();
		}
		return ret.isEmpty() ? "" : ret.substring(separator.length());
	}
	
	public static <T> String join(String separator, Iterable<T> iterable) {
		String ret = "";
		for (T s : iterable) {
			ret += separator+s.toString();
		}
		return ret.isEmpty() ? "" : ret.substring(separator.length());
	}
	
	public static <T> String joinCC(String separator, T[] array) {
		String ret = "";
		for (int i = 0; i < array.length; i++) {
			ret += separator+camelCase(array[i].toString());
		}
		return ret.isEmpty() ? "" : ret.substring(separator.length());
	}
	
	public static <T> String joinCC(String separator, Iterable<T> iterable) {
		String ret = "";
		for (T s : iterable) {
			ret += separator+camelCase(s.toString());
		}
		return ret.isEmpty() ? "" : ret.substring(separator.length());
	}
	
	public static void setTurtleUpgrade(ITurtleAccess turtle, TurtleSide side, ITurtleUpgrade upgrade) {
		Reflector.invoke(turtle, "set"+side.name()+"Upgrade", Object.class, upgrade);
	}
	
	public static int[] makeSlotArray(IInventory inv) {
		int[] slots = new int[inv.getSizeInventory()];
		for (int i = 0; i < slots.length; i++) slots[i] = i;
		return slots;
	}
	
	public static int[] makeLegacySlotArray(net.minecraftforge.common.ISidedInventory inv, int side) {
		ForgeDirection dir = ForgeDirection.getOrientation(side);
		int[] slots = new int[inv.getSizeInventorySide(dir)];
		int start = inv.getStartInventorySide(dir);
		for (int i = 0; i < slots.length; i++) slots[i] = start + i;
		return slots;
	}
	
	public static int getFuelValue(ItemStack stack) {
		return TileEntityFurnace.getItemBurnTime(stack);
	}

	public static ItemStack createItemStackNC(Object obj, int size, int meta) {
		if (obj != null) {
			if (obj instanceof Block) return new ItemStack((Block) obj, size, meta);
			else if (obj instanceof Item) return new ItemStack((Item) obj, size, meta);
			else if (obj instanceof Integer) return new ItemStack((Integer) obj, size, meta);
		}
		
		return null;
	}
	
	public static MovingObjectPosition rayTraceBlock(ITurtleAccess turtle, int direction) {
		Vec3 pos = turtle.getPosition();
		int x = (int)pos.xCoord + Facing.offsetsXForSide[direction];
		int y = (int)pos.yCoord + Facing.offsetsYForSide[direction];
		int z = (int)pos.zCoord + Facing.offsetsZForSide[direction];
		return new MovingObjectPosition(x, y, z, OPPOSITE[direction], Vec3.createVectorHelper(x, y, z));
	}
	
	public static MovingObjectPosition rayTraceBlock(ITurtleAccess turtle) {
		return rayTraceBlock(turtle, turtle.getFacingDir());
	}
	
	public static int getUUID(ItemStack stack) {
		return ((stack.getItemDamage() * 32768) + stack.itemID) ^ MiscPeripherals.instance.itemIDSeed;
	}
	
	public static ItemStack getUUID(int uuid) {
		return getUUID(uuid, 1);
	}
	
	public static ItemStack getUUID(int uuid, int stackSize) {
		uuid ^= MiscPeripherals.instance.itemIDSeed;
		int meta = (int)Math.floor(uuid / 32768);
		int id = uuid % 32768;
		return new ItemStack(id, stackSize, meta);
	}
	
	public static int getUUID(LiquidStack stack) {
		return getUUID(new ItemStack(stack.itemID, stack.amount, stack.itemMeta));
	}
	
	// RG for public source release: Code below from iChun, used with permission.

	public static MovingObjectPosition rayTrace(EntityLiving ent, double d) {
		if (ent == null) {
			return null;
		}
        double d1 = d;
        float f = 1.0F;
        MovingObjectPosition mop = rayTrace(ent, d, f);
        Vec3 vec3d = getPosition(ent, f);
        if (mop != null) d1 = mop.hitVec.distanceTo(vec3d);
        double dd2 = d;
        if (d1 > dd2) d1 = dd2;
        d = d1;
        Vec3 vec3d1 = ent.getLook(f);
        Vec3 vec3d2 = vec3d.addVector(vec3d1.xCoord * d, vec3d1.yCoord * d, vec3d1.zCoord * d);
        Entity entity1 = null;
        float f1 = 1.0F;
        List list = ent.worldObj.getEntitiesWithinAABBExcludingEntity(ent, ent.boundingBox.addCoord(vec3d1.xCoord * d, vec3d1.yCoord * d, vec3d1.zCoord * d).expand(f1, f1, f1));
        double d2 = 0.0D;
        for (int i = 0; i < list.size(); i++) {
            Entity entity = (Entity)list.get(i);
            if (!entity.canBeCollidedWith()) continue;
            float f2 = entity.getCollisionBorderSize();
            AxisAlignedBB axisalignedbb = entity.boundingBox.expand(f2, f2, f2);
            MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(vec3d, vec3d2);
            if (axisalignedbb.isVecInside(vec3d)) {
                if (0.0D < d2 || d2 == 0.0D) {
                    entity1 = entity;
                    d2 = 0.0D;
                }
                continue;
            }
            if (movingobjectposition == null) continue;
            double d3 = vec3d.distanceTo(movingobjectposition.hitVec);
            if (d3 < d2 || d2 == 0.0D) {
                entity1 = entity;
                d2 = d3;
            }
        }
        if (entity1 != null) mop = new MovingObjectPosition(entity1);
        return mop;
	}

    public static MovingObjectPosition rayTrace(EntityLiving ent, double par1, float par3) {
        Vec3 var4 = getPosition(ent, par3);
        Vec3 var5 = ent.getLook(par3);
        Vec3 var6 = var4.addVector(var5.xCoord * par1, var5.yCoord * par1, var5.zCoord * par1);
        return ent.worldObj.rayTraceBlocks(var4, var6);
    }
    
    public static Vec3 getPosition(Entity ent, float par1) {
        if (par1 == 1.0F) return ent.worldObj.getWorldVec3Pool().getVecFromPool(ent.posX, ent.posY + ent.getEyeHeight(), ent.posZ);
        else {
            double var2 = ent.prevPosX + (ent.posX - ent.prevPosX) * (double)par1;
            double var4 = ent.prevPosY + ent.getEyeHeight() + (ent.posY - ent.prevPosY) * (double)par1;
            double var6 = ent.prevPosZ + (ent.posZ - ent.prevPosZ) * (double)par1;
            return ent.worldObj.getWorldVec3Pool().getVecFromPool(var2, var4, var6);
        }
    }
}
