package com.austinv11.peripheralsplusplus.tiles;

import com.austinv11.collectiveframework.minecraft.utils.Location;
import com.austinv11.collectiveframework.minecraft.utils.WorldUtils;
import com.austinv11.peripheralsplusplus.reference.Config;
import com.austinv11.peripheralsplusplus.utils.Util;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.block.Block;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TileEntityInteractiveSorter extends MountedTileEntityInventory {
	
	public static String publicName = "interactiveSorter";
	private String name = "tileEntityInteractiveSorter";
	private List<IComputerAccess> computers = new ArrayList<IComputerAccess>();
	
	public TileEntityInteractiveSorter() {
		super();
	}
	
	@Override
	public int getSize() {
		return 1;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public String getType() {
		return publicName;
	}
	
	@Override
	public String[] getMethodNames() {
		return new String[]{"analyze", "push", "pull", "isInventoryPresent"};
	}
	
	@Override
	public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws LuaException, InterruptedException {
		if (!Config.enableInteractiveSorter)
			throw new LuaException("Interactive Sorters have been disabled");
		if (method == 0) {
			return new Object[]{getItemInfo(getStackInSlot(0))};
			
		} else if (method == 1) {
			if (getStackInSlot(0).isEmpty())
				return new Object[]{false};
			EnumFacing dir;
			if (arguments.length < 1)
				throw new LuaException("Too few arguments");
			if (!(arguments[0] instanceof String) && !(arguments[0] instanceof Double))
				throw new LuaException("Bad argument #1 (expected string or number)");
			if (arguments.length > 1 && !(arguments[1] instanceof Double))
				throw new LuaException("Bad argument #2 (expected number)");
			if (arguments[0] instanceof String)
				dir = EnumFacing.valueOf(((String) arguments[0]).toUpperCase());
			else
				dir = EnumFacing.getFront((int) (double) (Double) arguments[0]);
			int amount = arguments.length > 1 ? MathHelper.clamp((int) (double) (Double) arguments[1], 0,
					getStackInSlot(0).getCount()) : getStackInSlot(0).getCount();
			IInventory inventory = TileEntityMEBridge.getInventoryForSide(world, getPos(), dir);
			if (inventory == null) {
				BlockPos pos = getPos().offset(dir);
				WorldUtils.spawnItemInWorld(new Location(pos.getX(), pos.getY(), pos.getZ(), world),
						getStackInSlot(0).splitStack(amount));
				markDirty();
				return new Object[]{true};
			}

			int oldSize = getStackInSlot(0).getCount();
			int[] slots = inventory instanceof ISidedInventory ? 
					((ISidedInventory) inventory).getSlotsForFace(dir.getOpposite()) : getDefaultSlots(inventory);
			int currentSlot = 0;
			while (!getStackInSlot(0).isEmpty() && getStackInSlot(0).getCount() > oldSize-amount && currentSlot < slots.length) {
				if (inventory.getStackInSlot(slots[currentSlot]).isEmpty()) {
					inventory.setInventorySlotContents(slots[currentSlot], getStackInSlot(0));
					setInventorySlotContents(0, ItemStack.EMPTY);
				} else {
					if (!inventory.getStackInSlot(slots[currentSlot]).isItemEqual(getStackInSlot(0))) {
						currentSlot++;
						continue;
					}
					int transferred = MathHelper.clamp(inventory.getStackInSlot(slots[currentSlot]).getCount()+amount,
							getStackInSlot(0).getCount(), getStackInSlot(0).getMaxStackSize());
					
					getStackInSlot(0).setCount(getStackInSlot(0).getCount() - transferred);
					inventory.getStackInSlot(0).setCount(inventory.getStackInSlot(0).getCount() + transferred);
				}
				inventory.markDirty();
				markDirty();
				currentSlot++;
			}
			return new Object[]{getStackInSlot(0).isEmpty() || getStackInSlot(0).getCount() != oldSize};
			
		} else if (method == 2) {
			EnumFacing dir;
			if (arguments.length < 1)
				throw new LuaException("Too few arguments");
			if (!(arguments[0] instanceof String) && !(arguments[0] instanceof Double))
				throw new LuaException("Bad argument #1 (expected string or number)");
			if (arguments.length > 1 && !(arguments[1] instanceof Double))
				throw new LuaException("Bad argument #2 (expected number)");
			if (arguments.length > 2 && !(arguments[2] instanceof Double))
				throw new LuaException("Bad argument #3 (expected number)");
			if (arguments[0] instanceof String)
				dir = EnumFacing.valueOf(((String) arguments[0]).toUpperCase());
			else
				dir = EnumFacing.getFront(((int) (double) (Double) arguments[0]));
			IInventory inventory = TileEntityMEBridge.getInventoryForSide(world, getPos(), dir);
			if (inventory == null)
				throw new LuaException("Block is not a valid inventory");
			int slots[] = inventory instanceof ISidedInventory ? 
					((ISidedInventory) inventory).getSlotsForFace(dir.getOpposite()) : getDefaultSlots(inventory);
			int slot = -1;
			if (arguments.length > 2) {
				slot = getNearestSlot((int)(double)(Double)arguments[2], slots);
				if (inventory.getStackInSlot(slot).isEmpty())
					return new Object[]{false};
			} else {
				for (int slot1 : slots)
					if (getStackInSlot(0).isEmpty()) {
						if (!inventory.getStackInSlot(slot1).isEmpty()) {
							slot = slot1;
							break;
						}
					} else {
						if (!inventory.getStackInSlot(slot1).isEmpty()) {
							if (inventory.getStackInSlot(slot1).isItemEqual(getStackInSlot(0))) {
								slot = slot1;
								break;
							}
						}
					}
			}
			if (slot == -1)
				return new Object[]{false};
			int amount = arguments.length > 1 ? MathHelper.clamp((int) (double) (Double) arguments[1], 0,
					inventory.getStackInSlot(slot).getCount()) : inventory.getStackInSlot(slot).getCount();
			int transferred;
			if (!getStackInSlot(0).isEmpty()) {
				transferred = MathHelper.clamp(getStackInSlot(0).getCount()+amount,
						getStackInSlot(0).getCount(), getStackInSlot(0).getMaxStackSize());
				getStackInSlot(0).setCount(getStackInSlot(0).getCount() + transferred);
				inventory.getStackInSlot(slot).setCount(inventory.getStackInSlot(slot).getCount() - transferred);
			} else {
				transferred = amount;
				setInventorySlotContents(0, inventory.getStackInSlot(slot).splitStack(transferred));
			}
			if (!inventory.getStackInSlot(slot).isEmpty() && inventory.getStackInSlot(slot).getCount() < 1)
				inventory.setInventorySlotContents(slot, ItemStack.EMPTY);
			inventory.markDirty();
			markDirty();
			return new Object[]{true};
			
		} else if (method == 3) {
			EnumFacing dir;
			if (arguments.length < 1)
				throw new LuaException("Too few arguments");
			if (!(arguments[0] instanceof String) && !(arguments[0] instanceof Double))
				throw new LuaException("Bad argument #1 (expected string or number)");
			if (arguments[0] instanceof String)
				dir = EnumFacing.valueOf(((String) arguments[0]).toUpperCase());
			else
				dir = EnumFacing.getFront((int) (double) (Double) arguments[0]);
			return new Object[]{TileEntityMEBridge.getInventoryForSide(world, getPos(), dir) != null};
		}
		return new Object[0];
	}
	
	private int getNearestSlot(int requested, int[] slots) {
		int difference = Integer.MAX_VALUE;
		int currentSlot = slots[0];
		for (int slot : slots) {
			if (slot == requested)
				return slot;
			if (Math.abs(requested-slot) < difference) {
				difference = Math.abs(requested-slot);
				currentSlot = slot;
			}
		}
		return currentSlot;
	}
	
	private int[] getDefaultSlots(IInventory inventory) {
		int[] array = new int[inventory.getSizeInventory()];
		for (int i = 0; i < inventory.getSizeInventory(); i++)
			array[i] = i;
		return array;
	}
	
	@Override
	public boolean equals(IPeripheral other) {
		return other == this;
	}
	
	@Override
	public void attach(IComputerAccess computer) {
		super.attach(computer);
		computers.add(computer);
	}
	
	@Override
	public void detach(IComputerAccess computer) {
		super.detach(computer);
		computers.remove(computer);
	}
	
	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		super.setInventorySlotContents(slot, stack);
		if (!stack.isEmpty() && stack.getCount() > 0 && slot == 0)
			for (IComputerAccess computer : computers)
				computer.queueEvent("itemReady", null);
	}
	
	private HashMap<String, Object> getItemInfo(ItemStack stack) {
		if (stack.isEmpty())
			return null;
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("amount", stack.getCount());
		ResourceLocation id;
		if (stack.getItem() instanceof ItemBlock) {
			Block block = Block.getBlockFromItem(stack.getItem());
			id = ForgeRegistries.BLOCKS.getKey(block);
		} else {
			id = ForgeRegistries.ITEMS.getKey(stack.getItem());
		}
		map.put("stringId", id);
		map.put("oreDictionaryEntries", Util.getOreDictEntries(stack));
		map.put("meta", stack.getItemDamage());
		map.put("name", stack.getDisplayName());
		if (stack.hasTagCompound()) 
			map.put("nbt", convertNBTToMap(stack.getTagCompound()));
		
		return map;
	}
	
	private HashMap<String, Object> convertNBTToMap(NBTTagCompound tag) {
		HashMap<String, Object> nbtMap = new HashMap<>();
		for (Object key : tag.getKeySet()) {
			NBTBase nbtBase = tag.getTag((String) key);
			nbtMap.put((String) key, getObjectForNBT(nbtBase.copy()));
		}
		return nbtMap;
	}
	
	private Object getObjectForNBT(NBTBase nbtBase) {
		if (nbtBase == null)
			return null;
		switch (nbtBase.getId()) {
			case 0: //NBTTagEnd
				return null;
			
			case 1:
				NBTTagByte tagByte = (NBTTagByte)nbtBase;
				return tagByte.getByte();
			
			case 2:
				NBTTagShort tagShort = (NBTTagShort)nbtBase;
				return tagShort.getShort();
				
			case 3:
				NBTTagInt tagInt = (NBTTagInt)nbtBase;
				return tagInt.getInt();
				
			case 4:
				NBTTagLong tagLong = (NBTTagLong)nbtBase;
				return tagLong.getLong();
				
			case 5:
				NBTTagFloat tagFloat = (NBTTagFloat)nbtBase;
				return tagFloat.getFloat();
				
			case 6:
				NBTTagDouble tagDouble = (NBTTagDouble)nbtBase;
				return tagDouble.getDouble();
				
			case 7:
				NBTTagByteArray tagByteArray = (NBTTagByteArray)nbtBase;
				return Util.arrayToMap(tagByteArray.getByteArray());
				
			case 8:
				NBTTagString tagString = (NBTTagString)nbtBase;
				return tagString.getString();
				
			case 9:
				NBTTagList tagList = (NBTTagList)nbtBase;
				Object[] tags = new Object[tagList.tagCount()];
				for (int i = 0; i < tagList.tagCount(); i++)
					tags[i] = getObjectForNBT(tagList.removeTag(i));
				return Util.arrayToMap(tags);
				
			case 10:
				NBTTagCompound tagCompound = (NBTTagCompound)nbtBase;
				return convertNBTToMap(tagCompound);
				
			case 11:
				NBTTagIntArray tagIntArray = (NBTTagIntArray)nbtBase;
				return Util.arrayToMap(tagIntArray.getIntArray());
				
			default:
				return null;
		}
	}
}
