package com.austinv11.peripheralsplusplus.tiles;

import appeng.api.AEApi;
import appeng.api.config.Actionable;
import appeng.api.networking.*;
import appeng.api.networking.crafting.ICraftingCallback;
import appeng.api.networking.crafting.ICraftingGrid;
import appeng.api.networking.crafting.ICraftingJob;
import appeng.api.networking.security.IActionHost;
import appeng.api.networking.security.MachineSource;
import appeng.api.networking.storage.IStorageGrid;
import appeng.api.storage.IMEMonitor;
import appeng.api.storage.data.IAEItemStack;
import appeng.api.util.AECableType;
import appeng.api.util.AEColor;
import appeng.api.util.DimensionalCoord;
import appeng.core.WorldSettings;
import com.austinv11.peripheralsplusplus.init.ModBlocks;
import com.austinv11.peripheralsplusplus.reference.Config;
import com.austinv11.peripheralsplusplus.utils.Logger;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.registry.GameRegistry;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.block.BlockContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;

@Optional.InterfaceList(value = {@Optional.Interface(modid="appliedenergistics2",iface="appeng.api.networking.security.IActionHost", striprefs=true), @Optional.Interface(modid="appliedenergistics2",iface="appeng.api.networking.IGridBlock", striprefs=true)})
public class TileEntityMEBridge extends MountedTileEntity implements IActionHost, IGridBlock {

	public static String publicName = "meBridge";
	private  String name = "tileEntityMEBridge";
	private HashMap<IComputerAccess, Boolean> computers = new HashMap<IComputerAccess,Boolean>();
	private IGridNode node;
	private boolean initialized = false;
	public EntityPlayer placed;

	public TileEntityMEBridge() {
		super();
	}

	public String getName() {
		return name;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		if (node != null)
			node.destroy();
		node = AEApi.instance().createGridNode(this);
		node.loadFromNBT("node", nbttagcompound);
		initialized = false;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
		if (node != null)
			node.saveToNBT("node", nbttagcompound);
	}

	@Override
	public void updateEntity() {
		if (!worldObj.isRemote)
			if (!initialized) {
				if (placed != null)
					getNode().setPlayerID(WorldSettings.getInstance().getPlayerID(placed.getGameProfile()));
				getNode().updateState();
				initialized = true;
			}
	}

	@Override
	public String getType() {
		return publicName;
	}

	@Override
	public String[] getMethodNames() {
		return new String[]{"listAll", "listItems", "listCraft", "retrieve", "craft"};
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws LuaException, InterruptedException {
		if (!Config.enableMEBridge)
			throw new LuaException("ME Bridges have been disabled");
		IMEMonitor<IAEItemStack> monitor = ((IStorageGrid) node.getGrid().getCache(IStorageGrid.class)).getItemInventory();
//		try {
		switch (method) {
			case 0:
				return new Object[]{iteratorToMap(monitor.getStorageList().iterator(), 0)};
			case 1:
				return new Object[]{iteratorToMap(monitor.getStorageList().iterator(), 1)};
			case 2:
				return new Object[]{iteratorToMap(monitor.getStorageList().iterator(), 2)};
			case 3:
				ForgeDirection dir;
				if (arguments.length < 3)
					throw new LuaException("Too few arguments");
				if (!(arguments[0] instanceof String))
					throw new LuaException("Bad argument #1 (expected string)");
				if (!(arguments[1] instanceof Double))
					throw new LuaException("Bad argument #2 (expected number");
				if (!(arguments[2] instanceof String) && !(arguments[2] instanceof Double))
					throw new LuaException("Bad argument #3 (expected string or number)");
				Item item = GameRegistry.findItem(((String) arguments[0]).split(":")[0], ((String) arguments[0]).split(":")[1].split(" ")[0]);
				long amount = (long) (int) (double) (Double) arguments[1];
				if (arguments[2] instanceof String)
					dir = ForgeDirection.valueOf(((String) arguments[2]).toUpperCase());
				else
					dir = ForgeDirection.getOrientation((int) (double) (Double) arguments[2]);
				if (worldObj.isAirBlock(xCoord+dir.offsetX, yCoord+dir.offsetY, zCoord+dir.offsetZ) || !(worldObj.getBlock(xCoord+dir.offsetX, yCoord+dir.offsetY, zCoord+dir.offsetZ) instanceof BlockContainer))
					throw new LuaException("Block is not a valid inventory");
				IInventory inventory = (IInventory) worldObj.getTileEntity(xCoord+dir.offsetX, yCoord+dir.offsetY, zCoord+dir.offsetZ);
				long extracted = 0;
				int meta = ((String) arguments[0]).contains(" ") ? Integer.valueOf(((String)arguments[0]).split(" ")[1]) : 0;
				IAEItemStack stack = findAEStackFromItemStack(monitor, new ItemStack(item, 1, meta));
				if (stack != null) {
					if (amount > stack.getStackSize())
						amount = stack.getStackSize();
					if (amount > getRemainingSlots(item, inventory))
						amount = getRemainingSlots(item, inventory);
					IAEItemStack stackToGet = stack.copy();
					stackToGet.setStackSize(amount);
					IAEItemStack resultant = monitor.extractItems(stackToGet, Actionable.MODULATE, new MachineSource(this));
					if (resultant != null) {
						extracted = resultant.getStackSize();
						int currentSlot = 0;
						while (!(resultant.getStackSize() < 1)) {
							Logger.info(currentSlot);
							if (inventory.isItemValidForSlot(currentSlot, new ItemStack(resultant.getItem()))) {
								if (inventory.getStackInSlot(currentSlot) == null) {
									ItemStack toAdd = resultant.getItemStack();
									int stackSize = (int) (resultant.getStackSize() <= inventory.getInventoryStackLimit() ? resultant.getStackSize() : inventory.getInventoryStackLimit());
									toAdd.stackSize = stackSize;
									inventory.setInventorySlotContents(currentSlot, toAdd);
									resultant.setStackSize(resultant.getStackSize()-stackSize);
								} else {
									ItemStack current = inventory.getStackInSlot(currentSlot);
									ItemStack toAdd = resultant.getItemStack();
									if (current.isItemEqual(toAdd)) {
										int stackSize = (int) (resultant.getStackSize()+current.stackSize <= inventory.getInventoryStackLimit() ? resultant.getStackSize()+current.stackSize : inventory.getInventoryStackLimit());
										int change = stackSize - current.stackSize;
										current.stackSize = stackSize;
										inventory.setInventorySlotContents(currentSlot, current);
										resultant.setStackSize(resultant.getStackSize()-change);
									}
								}
								inventory.markDirty();
							}
							currentSlot++;
						}
					}
				}
				return new Object[]{extracted};
			case 4:
				if (arguments.length < 3)
					throw new LuaException("Too few arguments");
				if (!(arguments[0] instanceof String))
					throw new LuaException("Bad argument #1 (expected string)");
				if (!(arguments[1] instanceof Double))
					throw new LuaException("Bad argument #2 (expected number");
				final ICraftingGrid craftingGrid = node.getGrid().getCache(ICraftingGrid.class);
				Item toCraft = GameRegistry.findItem(((String) arguments[0]).split(":")[0], ((String) arguments[0]).split(":")[1].split(" ")[0]);
				int meta_ = ((String) arguments[0]).contains(" ") ? Integer.valueOf(((String)arguments[0]).split(" ")[1]) : 0;
				IAEItemStack aeToCraft_ = findAEStackFromItemStack(monitor, new ItemStack(toCraft, 1, meta_));
				if (aeToCraft_ != null && aeToCraft_.isCraftable()) {
					IAEItemStack aeToCraft = aeToCraft_.copy();
					aeToCraft.setStackSize((long) (int) (double) (Double) arguments[1]);
					synchronized (this) {
						craftingGrid.beginCraftingJob(worldObj, node.getGrid(), new MachineSource(this), aeToCraft, new ICraftingCallback() {
							@Override
							public void calculationComplete(ICraftingJob job) {
								craftingGrid.submitJob(job, null, null, false, new MachineSource((IActionHost) getMachine()));
								for (IComputerAccess comp : computers.keySet())
									comp.queueEvent("craftingComplete", new Object[]{Item.itemRegistry.getNameForObject(job.getOutput().getItem()), job.getOutput().getStackSize(), job.getByteTotal()});
							}
						});
					}
				}
				return new Object[]{};
		}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		return new Object[0];
	}

	private int getRemainingSlots(Item item, IInventory inventory) {
		int slots = 0;
		for (int i = 0; i < inventory.getSizeInventory(); i++) {
			if (inventory.isItemValidForSlot(i, new ItemStack(item)) && inventory.getStackInSlot(i) == null)
				slots += inventory.getInventoryStackLimit();
			else if (inventory.isItemValidForSlot(i, new ItemStack(item)) && inventory.getStackInSlot(i).getItem() == item && (inventory.getInventoryStackLimit() >= inventory.getStackInSlot(i).stackSize))
				slots += inventory.getInventoryStackLimit() - inventory.getStackInSlot(i).stackSize;
		}
		return slots;
	}

	@Override
	public void attach(IComputerAccess computer) {
		computers.put(computer, true);
		super.attach(computer);
	}

	@Override
	public void detach(IComputerAccess computer) {
		computers.remove(computer);
		super.detach(computer);
	}

	@Override
	public boolean equals(IPeripheral other) {
		return (this == other);
	}

	@Override
	public void invalidate() {
		super.invalidate();
		if (node != null) {
			node.destroy();
			node = null;
			initialized = false;
		}
	}

	private IAEItemStack findAEStackFromItemStack(IMEMonitor<IAEItemStack> monitor, ItemStack item) {
		IAEItemStack stack = null;
		for (IAEItemStack temp : monitor.getStorageList()) {
			if (temp.isSameType(item)) {
				stack = temp;
				break;
			}
		}
		return stack;
	}

	private HashMap<Integer, Object> iteratorToMap(Iterator<IAEItemStack> iterator, int flag) {
		HashMap<Integer,Object> map = new HashMap<Integer,Object>();
		int i = 1;
		while (iterator.hasNext()) {
			Object o = getObjectFromStack(iterator.next(), flag);
			if (o != null) {
				map.put(i, o);
				i++;
			}
		}
		return map;
	}

	private Object getObjectFromStack(IAEItemStack stack, int flag) {
		if (flag == 0) {
			return Item.itemRegistry.getNameForObject(stack.getItem())+" "+stack.getItemDamage();
		} else if (flag == 1) {
			if (stack.getCountRequestable() > 0)
				return Item.itemRegistry.getNameForObject(stack.getItem())+" "+stack.getItemDamage();
		} else if (flag == 2) {
			if (stack.isCraftable())
				return Item.itemRegistry.getNameForObject(stack.getItem())+" "+stack.getItemDamage();
		}
		return null;
	}

	private IGridNode getNode() {
		if (worldObj == null || worldObj.isRemote)
			return null;
		return node = AEApi.instance().createGridNode(this);
	}

	@Override
	public double getIdlePowerUsage() {
		return 1;
	}

	@Override
	public EnumSet<GridFlags> getFlags() {
		return EnumSet.of(GridFlags.REQUIRE_CHANNEL);
	}
	
	
	@Override
	public boolean isWorldAccessible() {
		return true;
	}

	@Override
	public DimensionalCoord getLocation() {
		return new DimensionalCoord(this);
	}

	@Override
	public AEColor getGridColor() {
		return AEColor.Transparent;
	}

	@Override
	public void onGridNotification(GridNotification notification) {
		for (IComputerAccess computer : computers.keySet())
			computer.queueEvent("gridNotification", new Object[]{notification.toString()});
	}

	@Override
	public void setNetworkStatus(IGrid grid, int channelsInUse) {
	}

	@Override
	public EnumSet<ForgeDirection> getConnectableSides() {
		return EnumSet.allOf(ForgeDirection.class);
	}

	@Override
	public IGridHost getMachine() {
		return this;
	}

	@Override
	public void gridChanged() {
		for (IComputerAccess computer : computers.keySet())
			computer.queueEvent("gridChanged", new Object[0]);
	}

	@Override
	public ItemStack getMachineRepresentation() {
		return new ItemStack(ModBlocks.meBridge);
	}

	@Override
	public IGridNode getGridNode(ForgeDirection dir) {
		return node;
	}

	@Override
	public AECableType getCableConnectionType(ForgeDirection dir) {
		return AECableType.COVERED;
	}

	@Override
	public void securityBreak() {
		for (IComputerAccess computer : computers.keySet())
			computer.queueEvent("securityBreak", new Object[0]);
		worldObj.setBlockToAir(xCoord, yCoord, zCoord);
	}

	@Override
	public IGridNode getActionableNode() {
		return node;
	}
}
