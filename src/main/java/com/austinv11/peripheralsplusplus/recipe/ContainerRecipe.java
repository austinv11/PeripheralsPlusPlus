package com.austinv11.peripheralsplusplus.recipe;

import com.austinv11.collectiveframework.minecraft.utils.Colors;
import com.austinv11.collectiveframework.minecraft.utils.NBTHelper;
import com.austinv11.peripheralsplusplus.blocks.BlockPeripheralContainer;
import com.austinv11.peripheralsplusplus.init.ModBlocks;
import com.austinv11.peripheralsplusplus.reference.Config;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ContainerRecipe implements IRecipe {
	private final ResourceLocation group;
	private ResourceLocation name;

    public ContainerRecipe(ResourceLocation group) {
        super();
		this.group = group;
    }

    @Override
	public boolean matches(InventoryCrafting craftingInventory, World world) {
		ItemStack container = getPeripheralContainer(craftingInventory);
		if (container.isEmpty())
			return false;
		List<ContainedPeripheral> peripherals = getPeripherals(craftingInventory);
		if (peripherals.size() == 0)
			return false;
		int items = 0;
		for (int itemStack = 0; itemStack < craftingInventory.getSizeInventory(); itemStack++)
			if (!craftingInventory.getStackInSlot(itemStack).isEmpty())
				items++;
		if (items != peripherals.size() + 1)
			return false;
		NBTTagList contained = getContainedPeripherals(container);
		return contained.tagCount() + peripherals.size() <= Config.maxNumberOfPeripherals;
	}

	private NBTTagList getContainedPeripherals(ItemStack container) {
		NBTTagList tagList = new NBTTagList();
		if (container.getTagCompound() != null && container.getTagCompound()
				.hasKey(ModBlocks.PERIPHERAL_CONTAINER.getRegistryName().toString())) {
			NBTBase idTag = container.getTagCompound()
					.getTag(ModBlocks.PERIPHERAL_CONTAINER.getRegistryName().toString());
			if (!(idTag instanceof NBTTagList))
				return tagList;
			return (NBTTagList)idTag;
		}
		return tagList;
	}

	private ItemStack getPeripheralContainer(InventoryCrafting inventory) {
		ItemStack returnStack = ItemStack.EMPTY;
		for (int slot = 0; slot < inventory.getSizeInventory(); slot++) {
			ItemStack itemStack = inventory.getStackInSlot(slot).copy();
			if (Block.getBlockFromItem(itemStack.getItem()) instanceof BlockPeripheralContainer) {
				if (itemStack.getCount() != 1 || !returnStack.isEmpty())
					return ItemStack.EMPTY;
				returnStack = itemStack.copy();
			}
		}
		return returnStack;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting craftingInventory) {
		ItemStack container = getPeripheralContainer(craftingInventory);
		List<ContainedPeripheral> peripherals = getPeripherals(craftingInventory);
		NBTTagList contained = getContainedPeripherals(container);
		List<String> text = new ArrayList<>();
		if (contained.tagCount() == 0)
			text.add(Colors.RESET.toString() + Colors.UNDERLINE + "Contained Peripherals:");
		for (ContainedPeripheral peripheral : peripherals) {
			contained.appendTag(peripheral.toNbt());
			text.add(Colors.RESET + peripheral.getBlockResourceLocation().toString());
		}
		NBTHelper.setTag(container, ModBlocks.PERIPHERAL_CONTAINER.getRegistryName().toString(), contained);
		NBTHelper.addInfo(container, text);
		return container;
	}

	private List<ContainedPeripheral> getPeripherals(InventoryCrafting inventory) {
		List<ContainedPeripheral> peripherals = new ArrayList<>();
		for (int slot = 0; slot < inventory.getSizeInventory(); slot++) {
			ItemStack itemStack = inventory.getStackInSlot(slot).copy();
			// FIXME handle multi-blocks
			// The "crafting" recipe may need to be changed to an in world method to make use of the peripheral provider
			// interfaces. A possibility may just be creating a fake world, placing the block, and requesting peripheral
			// providers to check that world in the check/create methods of the crafting recipe.
			if (itemStack.getMetadata() != 0)
				continue;
			if (itemStack.getCount() != 1)
				continue;
			Block block = Block.getBlockFromItem(itemStack.getItem());
			IPeripheral peripheral = ContainedPeripheral.getPeripheralForBlock(block);
			if (peripheral == null)
				continue;
			ResourceLocation name = ForgeRegistries.BLOCKS.getKey(block);
			peripherals.add(new ContainedPeripheral(name, peripheral));
		}
		return peripherals;
	}

	@Override
    public boolean canFit(int width, int height) {
        return true;
    }

	@Override
	public ItemStack getRecipeOutput() {
		return new ItemStack(ModBlocks.PERIPHERAL_CONTAINER);
	}

    @Override
    public IRecipe setRegistryName(ResourceLocation name) {
	    this.name = name;
        return this;
    }

    @Nullable
    @Override
    public ResourceLocation getRegistryName() {
        return name;
    }

    @Override
    public Class<IRecipe> getRegistryType() {
        return IRecipe.class;
    }

	@Override
	public String getGroup() {
		return group.toString();
	}
}
