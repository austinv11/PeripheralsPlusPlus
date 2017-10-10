package com.austinv11.peripheralsplusplus.lua;

import com.austinv11.peripheralsplusplus.reference.Config;
import com.austinv11.peripheralsplusplus.tiles.TileEntityPlayerInterface;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.ILuaObject;
import dan200.computercraft.api.lua.LuaException;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.ArrayList;
import java.util.HashMap;

public class LuaObjectPlayerInv implements ILuaObject {
    private InventoryPlayer inv;
    private TileEntityPlayerInterface playerInterface;
    private ItemStack permCard;

    public LuaObjectPlayerInv(EntityPlayer player, TileEntityPlayerInterface playerInterface, ItemStack permCard) {
        this.inv = player.inventory;
        this.playerInterface = playerInterface;
        this.permCard = permCard;
    }

    @Override
    public Object[] callMethod(ILuaContext context, int method, Object[] arguments) throws LuaException, InterruptedException {
        ItemStack origStack;
        ItemStack newStack;
        switch (method) {
            case 0:
                if (arguments.length != 1) {
                    throw new LuaException("Wrong number of arguments. 1 expected.");
                }
                if (!(arguments[0] instanceof Double)) {
                    throw new LuaException("Bad argument #1 (expected number)");
                }

                if (hasGetStacksPermission()) {
                    return new Object[] {getObjectFromStack(inv.getStackInSlot(((Double) arguments[0]).intValue()))};
                }
                break;
            case 1:
                if (arguments.length != 2) {
                    throw new LuaException("Wrong number of arguments. 2 expected.");
                }
                if (!(arguments[0] instanceof Double)) {
                    throw new LuaException("Bad argument #1 (expected number)");
                }
                if (!(arguments[1] instanceof Double)) {
                    throw new LuaException("Bad argument #2 (expected number)");
                }

                if (getOutputInventory() == null) {
                    throw new LuaException("No output side set.");
                }

                if (hasWithdrawPermission()) {
                    origStack = inv.getStackInSlot(((Double) arguments[0]).intValue());
                    if (origStack.isEmpty()) {
                        return new Object[0];
                    }


                    newStack = inv.decrStackSize(((Double) arguments[0]).intValue(), ((Double) arguments[1]).intValue());
                    if (!addStackToInv(getOutputInventory(), newStack, -1)) {
                        inv.addItemStackToInventory(newStack);
                        return new Object[]{false};
                    }
                    return new Object[]{true};
                }
                break;
            case 2:
                if (arguments.length != 3) {
                    throw new LuaException("Wrong number of arguments. 3 expected.");
                }
                if (!(arguments[0] instanceof Double)) {
                    throw new LuaException("Bad argument #1 (expected number)");
                }
                if (!(arguments[1] instanceof Double)) {
                    throw new LuaException("Bad argument #2 (expected number)");
                }
                if (!(arguments[2] instanceof Double)) {
                    throw new LuaException("Bad argument #3 (expected number)");
                }

                if (hasDepositPermission()) {
                    origStack = getInputInventory().getStackInSlot(((Double) arguments[1]).intValue());
                    newStack = getInputInventory().decrStackSize(((Double) arguments[1]).intValue(), ((Double) arguments[2]).intValue());
                    if (newStack.isEmpty()) {
                        return new Object[0];
                    }
                    if (addStackToInv(inv, newStack, 0)) {
                        return new Object[]{true};
                    } else {
                        getInputInventory().setInventorySlotContents(((Double) arguments[1]).intValue(), origStack);
                        return new Object[]{false};
                    }
                }
                break;
            case 3:
                if (arguments.length != 2) {
                    throw new LuaException("Wrong number of arguments. 2 expected.");
                }
                if (!(arguments[0] instanceof Double)) {
                    throw new LuaException("Bad argument #1 (expected number)");
                }
                if (!(arguments[1] instanceof Double)) {
                    throw new LuaException("Bad argument #2 (expected number)");
                }

                if (hasDepositPermission()) {
                    origStack = getInputInventory().getStackInSlot(((Double) arguments[0]).intValue());
                    newStack = getInputInventory().decrStackSize(((Double) arguments[0]).intValue(), ((Double) arguments[1]).intValue());
                    if (newStack.isEmpty()) {
                        return new Object[0];
                    }
                    if (addStackToInv(inv, newStack, -1)) {
                        return new Object[]{true};
                    } else {
                        getInputInventory().setInventorySlotContents(((Double) arguments[0]).intValue(), origStack);
                        return new Object[]{false};
                    }
                } else
                    throw new LuaException("No deposit permissions for player inventory");
            case 4:
                if (hasGetStacksPermission())
                {
                    return new Object[]{inv.getSizeInventory()};
                }
                break;
        }
        inv.markDirty();
        return new Object[0];
    }

    @Override
    public String[] getMethodNames() {
        return new String[]{"getStackInSlot", "retrieveFromSlot", "pushToSlot", "push", "getSize"};
    }

    private Object getObjectFromStack(ItemStack stack) {
        HashMap<String, Object> map = new HashMap<String, Object>();

        if (stack == null)
        {
            return null;
        }

        ResourceLocation itemName = ForgeRegistries.ITEMS.getKey(stack.getItem());
        int meta = stack.getItemDamage();
        long amount = stack.getCount();
        String displayName = stack.getDisplayName();
        map.put("name", itemName == null ? "NOT REGISTERED" : itemName.toString());
        map.put("meta", meta);
        map.put("amount", amount);
        map.put("displayName", displayName);
        return map;
    }

    private IInventory getOutputInventory() throws LuaException {
        EnumFacing outDir = playerInterface.outputSide;
        if (outDir == null) {
            throw new LuaException("Output Side has not yet been set.");
        }
        BlockPos pos = playerInterface.getPos().offset(outDir);
        Block block = playerInterface.getWorld().getBlockState(pos).getBlock();
        if (block instanceof BlockContainer) {
            return (IInventory) playerInterface.getWorld().getTileEntity(pos);
        } else {
            throw new LuaException("Invalid Output Inventory.");
        }
    }

    private IInventory getInputInventory() throws LuaException {
        EnumFacing inDir = playerInterface.inputSide;
        if (inDir == null) {
            throw new LuaException("Input Side has not yet been set.");
        }
        BlockPos pos = playerInterface.getPos().offset(inDir);
        Block block = playerInterface.getWorld().getBlockState(pos).getBlock();
        if (block instanceof BlockContainer) {
            return (IInventory) playerInterface.getWorld().getTileEntity(pos);
        } else {
            throw new LuaException("Invalid Input Inventory.");
        }
    }

    /**
     * Don't look at me, I'm hideous! D:
     * Attempts to add the passed ItemStack to the passed IInventory.
     * Specify a slot ID if the stack must be placed in that slot. Pass -1 if they are all valid.
     *
     * @return Success at depositing any items in the stack.
     */
    private boolean addStackToInv(IInventory inv, ItemStack addStack, int slot) {
        for (Integer slotNum : getValidSlotsForStack(inv, addStack, slot)) {
            ItemStack currentStack = inv.getStackInSlot(slotNum);
            if (inv.getStackInSlot(slotNum).isEmpty()) {
                inv.setInventorySlotContents(slotNum, addStack);
                return true;
            } else {
                int add = currentStack.getMaxStackSize() - currentStack.getCount();
                if (addStack.getCount() <= add) {
                    currentStack.setCount(currentStack.getCount() + addStack.getCount());
                    return true;
                } else {
                    // Was unable to add all of the stack to one slot and must add what it can to this slot and move on to the next.
                    currentStack.setCount(currentStack.getCount() + add);
                    currentStack.setCount(addStack.getCount() - add);

                    // We should only move onto the next slot if the user specified that this is ok (slot == -1)
                    if (slot != -1) {
                        return false;
                    }
                }

                if (addStack.getCount() == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns an ArrayList of the valid slot IDs for the passed ItemStack in the passed IInventory.
     * Valid meaning the slot is either empty or contains the correct item and is not full.
     * The passed slot takes priority over other found slots and will be placed first.
     * Passing -1 in its place indicates that no slot should take priority.
     */
    private ArrayList<Integer> getValidSlotsForStack(IInventory inv, ItemStack stack, int slot) {
        ArrayList<Integer> slots = new ArrayList<Integer>();
        if (slot != -1) {
            slots.add(slot);
        }
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            if ((inv.getStackInSlot(i).isEmpty() || (inv.getStackInSlot(i).getItem().equals(stack.getItem()) &&
                    inv.getStackInSlot(i).getCount() != inv.getStackInSlot(i).getMaxStackSize() &&
                    inv.getStackInSlot(i).getItemDamage() == stack.getItemDamage())) && i != slot) {
                slots.add(i);
            }
        }
        return slots;
    }

    private boolean hasDepositPermission() {
        return !Config.enableInterfacePermissions || (permCard.getTagCompound() != null &&
                permCard.getTagCompound().getBoolean("deposit"));
    }

    private boolean hasWithdrawPermission() {
        return !Config.enableInterfacePermissions || (permCard.getTagCompound() != null &&
                permCard.getTagCompound().getBoolean("withdraw"));
    }

    private boolean hasGetStacksPermission() {
        return !Config.enableInterfacePermissions || (permCard.getTagCompound() != null &&
                permCard.getTagCompound().getBoolean("getStacks"));
    }
}
