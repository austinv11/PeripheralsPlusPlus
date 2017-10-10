package com.austinv11.peripheralsplusplus.pocket;

import com.austinv11.collectiveframework.minecraft.utils.Colors;
import com.austinv11.collectiveframework.minecraft.utils.NBTHelper;
import com.austinv11.collectiveframework.utils.LogicUtils;
import com.austinv11.peripheralsplusplus.hooks.ComputerCraftRegistry;
import com.austinv11.peripheralsplusplus.init.ModBlocks;
import com.austinv11.peripheralsplusplus.pocket.peripherals.PeripheralPeripheralContainer;
import com.austinv11.peripheralsplusplus.reference.Reference;
import com.austinv11.peripheralsplusplus.utils.TurtleUtil;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.pocket.IPocketAccess;
import dan200.computercraft.api.pocket.IPocketUpgrade;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PocketPeripheralContainer implements IPocketUpgrade {
	@Override
	public ResourceLocation getUpgradeID() {
		return new ResourceLocation(Reference.POCKET_PERIPHERAL_CONTAINER);
	}
	
	@Override
	public String getUnlocalisedAdjective() {
		return "peripheralsplusplus.pocket_upgrade.peripheral_container";
	}
	
	@Override
	public ItemStack getCraftingItem() {
		return new ItemStack(ModBlocks.PERIPHERAL_CONTAINER);
	}

	@Nullable
	@Override
	public IPeripheral createPeripheral(@Nonnull IPocketAccess access) {
		Map<IPocketUpgrade, IPeripheral> pocketUpgrades = new HashMap<>();
		ItemStack stack = TurtleUtil.getPocketServerItemStack(access);
		NBTTagCompound tag = stack.getTagCompound();
		if (tag != null) {
			NBTBase listBase = tag.getTag(Reference.POCKET_PERIPHERAL_CONTAINER);
			if ((listBase instanceof NBTTagList)) {
				NBTTagList upgrades = (NBTTagList) listBase;
				for (int upgradeIndex = 0; upgradeIndex < upgrades.tagCount(); upgradeIndex++) {
					ResourceLocation upgradeId = new ResourceLocation(upgrades.getStringTagAt(upgradeIndex));
					for (IPocketUpgrade pocketUpgrade : ComputerCraftRegistry.getPocketUpgrades().values())
						if (upgradeId.equals(pocketUpgrade.getUpgradeID())) {
							pocketUpgrades.put(pocketUpgrade, pocketUpgrade.createPeripheral(access));
						}
				}
			}
		}
		return new PeripheralPeripheralContainer(pocketUpgrades);
	}

	@Override
	public void update(@Nonnull IPocketAccess access, @Nullable IPeripheral peripheral) {
		if (!(peripheral instanceof PeripheralPeripheralContainer))
			return;
		Map<IPocketUpgrade, IPeripheral> peripherals = ((PeripheralPeripheralContainer) peripheral).getUpgrades();
		for (Map.Entry<IPocketUpgrade, IPeripheral> upgrade : peripherals.entrySet())
			upgrade.getKey().update(access, upgrade.getValue());
		// Remove unequipped peripherals
		Map<ResourceLocation, ItemStack> unequippedItems = ((PeripheralPeripheralContainer) peripheral)
				.getUnequippedItems();
		if (unequippedItems.size() > 0) {
			for (Map.Entry<ResourceLocation, ItemStack> unequipped : unequippedItems.entrySet())
				if (access.getEntity() instanceof EntityPlayer) {
					ItemStack pocketItem = TurtleUtil.getPocketServerItemStack(access);
					NBTTagCompound tag = pocketItem.getTagCompound();
					if (tag != null) {
						NBTBase tagList = tag.getTag(Reference.POCKET_PERIPHERAL_CONTAINER);
						NBTTagList newTagList = new NBTTagList();
						List<String> lore = new ArrayList<>();
						if (tagList instanceof NBTTagList) {
							for (int tagIndex = 0; tagIndex < ((NBTTagList) tagList).tagCount(); tagIndex++)
								if (!((NBTTagList) tagList).getStringTagAt(tagIndex)
										.equals(unequipped.getKey().toString())) {
									newTagList.appendTag(((NBTTagList) tagList).get(tagIndex));
									lore.add(Colors.RESET + unequipped.getKey().toString());
								}
							tag.setTag(Reference.POCKET_PERIPHERAL_CONTAINER, newTagList);
						}
						if (newTagList.tagCount() > 0)
							lore.add(0, Colors.RESET.toString() + Colors.UNDERLINE + "Contained Peripherals:");
						pocketItem.setTagCompound(tag);
						NBTHelper.setInfo(pocketItem, lore);
						((EntityPlayer) access.getEntity()).inventory.addItemStackToInventory(pocketItem);
						((EntityPlayer) access.getEntity()).inventory.addItemStackToInventory(unequipped.getValue());
					}
				}
			unequippedItems.clear();
		}
	}

	@Override
	public boolean onRightClick(@Nonnull World world, @Nonnull IPocketAccess access, @Nullable IPeripheral peripheral) {
		if (!(peripheral instanceof PeripheralPeripheralContainer))
			return false;
		Map<IPocketUpgrade, IPeripheral> peripherals = ((PeripheralPeripheralContainer) peripheral).getUpgrades();
		boolean[] results = new boolean[peripherals.size()];
		int resultIndex = 0;
		for (Map.Entry<IPocketUpgrade, IPeripheral> upgrade : peripherals.entrySet())
			results[resultIndex++] = upgrade.getKey().onRightClick(world, access, upgrade.getValue());
		return LogicUtils.or(results);
	}
}
