package com.austinv11.peripheralsplusplus.items;

import com.austinv11.peripheralsplusplus.PeripheralsPlusPlus;
import com.austinv11.peripheralsplusplus.api.PeripheralsPlusPlusAPI;
import com.austinv11.peripheralsplusplus.api.satellites.upgrades.ISatelliteUpgrade;
import com.austinv11.peripheralsplusplus.api.satellites.upgrades.SatelliteUpgradeType;
import com.austinv11.peripheralsplusplus.reference.Reference;
import com.austinv11.peripheralsplusplus.utils.NBTHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ItemSatellite extends ItemPPP {

	public ItemSatellite() {
		super();
		this.setMaxStackSize(1);
		this.setUnlocalizedName("satellite");
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		if (!world.isRemote)
			player.openGui(PeripheralsPlusPlus.instance, Reference.GUIs.SATELLITE.ordinal(), world, (int)player.posX, (int)player.posY, (int)player.posZ);
		return super.onItemRightClick(stack, world, player);
	}

	public SatelliteInventory getInventoryFromStack(EntityPlayer player) {
		SatelliteInventory inv = new SatelliteInventory();
		inv.player = player;
		if (player.getCurrentEquippedItem().hasTagCompound()) {
			int slots = NBTHelper.getInt(player.getCurrentEquippedItem(), "slots");
			for (int i = 0; i < slots; i++) {
				ItemStack upStack = ItemStack.loadItemStackFromNBT(player.getCurrentEquippedItem().getTagCompound().getCompoundTag("slot:"+String.valueOf(i)));
				if (upStack != null)
					inv.items.put(i, upStack);
			}
		}
		return inv;
	}

	@Override
	public void onUpdate(ItemStack itemStack, World world, Entity entity, int slot, boolean p_77663_5_) {
		if (entity instanceof EntityPlayer)
			if (((EntityPlayer) entity).getCurrentEquippedItem().equals(itemStack))
				if (!world.isRemote) {
					SatelliteInventory inv = getInventoryFromStack((EntityPlayer) entity);
					if (inv != null)
						if (inv.items.containsKey(1) && PeripheralsPlusPlusAPI.getUpgradeFromItem(inv.items.get(1).getItem()) != null) {
							ISatelliteUpgrade up = PeripheralsPlusPlusAPI.getUpgradeFromItem(inv.items.get(1).getItem());
							if (calcWeight(itemStack)+up.getAddonWeight() <= 1.0F) {
								inv.setInventorySlotContents(inv.getSizeInventory()+1, inv.getStackInSlot(1));
								inv.setInventorySlotContents(1, null);
							}
						}
				}
	}

	private float calcWeight(ItemStack stack) {
		float weight = 0.0F;
		List<ISatelliteUpgrade> upgrades = PeripheralsPlusPlusAPI.getUpgradesFromItemStack(stack);
		for (ISatelliteUpgrade upgrade : upgrades)
			if (upgrade.getType() == SatelliteUpgradeType.MODIFIER)
				weight += upgrade.getAddonWeight();
		return weight;
	}

	public class SatelliteInventory implements IInventory {//FIXME: This has the framework for a dynamically sloted gui, just need to figure out the gui part

		public EntityPlayer player;
		public HashMap<Integer, ItemStack> items = new HashMap<Integer, ItemStack>();

		@Override
		public int getSizeInventory() {
			return items.size() <= 1 ? 2 : items.size()+1;
		}

		@Override
		public ItemStack getStackInSlot(int slotNum) {
			return items.get(slotNum);
		}

		@Override
		public ItemStack decrStackSize(int slotNum, int amount) {
			if ((items.size() <= 1 ? 2 : items.size()) > slotNum) {
				if (items.containsKey(slotNum)) {
					if (items.get(slotNum).stackSize <= amount) {
						ItemStack item = items.get(slotNum);
						items.remove(slotNum);
						markDirty();
						return item;
					}
					ItemStack item = items.get(slotNum).splitStack(amount);
					if (items.get(slotNum).stackSize == 0) {
						items.remove(slotNum);
					}
					markDirty();
					return item;
				}
			}
			return null;
		}

		@Override
		public ItemStack getStackInSlotOnClosing(int slotNum) {
			if ((items.size() <= 1 ? 2 : items.size()) > slotNum) {
				if (items.containsKey(slotNum)) {
					ItemStack item = items.get(slotNum);
					items.remove(slotNum);
					return item;
				}
			}
			return null;
		}

		@Override
		public void setInventorySlotContents(int slotNum, ItemStack itemStack) {
			if ((items.size() <= 1 ? 2 : items.size()) > slotNum) {
				items.put(slotNum, itemStack);
				if (itemStack != null && itemStack.stackSize > getInventoryStackLimit()) {
					itemStack.stackSize = getInventoryStackLimit();
				}
				markDirty();
			}
		}

		@Override
		public String getInventoryName() {
			return "Satellite";
		}

		@Override
		public boolean hasCustomInventoryName() {
			return false;
		}

		@Override
		public int getInventoryStackLimit() {
			return 1;
		}

		@Override
		public void markDirty() {
			player.getCurrentEquippedItem().setTagCompound(saveToTag());
			List<String> text = new ArrayList<String>();
			List<ISatelliteUpgrade> upgrades = PeripheralsPlusPlusAPI.getUpgradesFromItemStack(player.getCurrentEquippedItem());
			text.add(Reference.Colors.RESET+Reference.Colors.GRAY+Reference.Colors.UNDERLINE+"Main Upgrade:");
			text.add(Reference.Colors.RESET+Reference.Colors.GRAY+(items.containsKey(0) && upgrades != null && upgrades.get(0) != null ? StatCollector.translateToLocal(upgrades.get(0).getUnlocalisedName()) : "No Main Upgrade!"));
			text.add(Reference.Colors.RESET+Reference.Colors.GRAY+Reference.Colors.UNDERLINE+"Addon Upgrades:");
			boolean hasMod = false;
			if (items.containsKey(1) && upgrades != null) {
				for (int i = 0; i < items.size(); i++) {
					if (upgrades.size() > i) {
						ISatelliteUpgrade upgrade = upgrades.get(i);
						if (upgrade != null && upgrade.getType() == SatelliteUpgradeType.MODIFIER) {
							text.add(Reference.Colors.RESET+Reference.Colors.GRAY+StatCollector.translateToLocal(upgrade.getUnlocalisedName())+" (uses "+String.format("%.2f", upgrade.getAddonWeight()*100)+"% of the satellite's capacity)");
							hasMod = true;
						}
					}
				}
			}
			if (!hasMod)
				text.add(Reference.Colors.RESET+Reference.Colors.GRAY+"No Addon Upgrades!");
			NBTHelper.setInfo(player.getCurrentEquippedItem(), text);
		}

		@Override
		public boolean isUseableByPlayer(EntityPlayer player) {
			return player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() instanceof ItemSatellite;
		}

		@Override
		public void openInventory() {}

		@Override
		public void closeInventory() {}

		@Override
		public boolean isItemValidForSlot(int slotNum, ItemStack itemStack) {
			return (slotNum == 0 && PeripheralsPlusPlusAPI.getUpgradeFromItem(itemStack.getItem()).getType() == SatelliteUpgradeType.MAIN) || (slotNum != 0 && PeripheralsPlusPlusAPI.getUpgradeFromItem(itemStack.getItem()).getType() == SatelliteUpgradeType.MODIFIER);
		}

		public NBTTagCompound saveToTag() {
			NBTTagCompound tagCompound = new NBTTagCompound();
			tagCompound.setInteger("slots", items.size() <= 0 ? 2 : items.size());
			for (int i = 0; i < items.size(); i++)
				if (items.get(i) != null)
					tagCompound.setTag("slot:"+String.valueOf(i), items.get(i).writeToNBT(new NBTTagCompound()));
			return tagCompound;
		}
	}
}
