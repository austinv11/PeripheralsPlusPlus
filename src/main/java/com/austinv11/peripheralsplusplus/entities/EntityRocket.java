package com.austinv11.peripheralsplusplus.entities;

import com.austinv11.peripheralsplusplus.PeripheralsPlusPlus;
import com.austinv11.peripheralsplusplus.init.ModItems;
import com.austinv11.peripheralsplusplus.reference.Reference;
import com.austinv11.peripheralsplusplus.utils.Util;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EntityRocket extends EntityInventory {
	
	public EntityRocket(World p_i1582_1_) {
		super(p_i1582_1_);
	}

	public EntityRocket(World p_i1582_1_, int x, int y, int z) {
		this(p_i1582_1_);
		this.posX = x;
		this.posY = y;
		this.posZ = z;
	}

	@Override
	public int getSizeInventory() {
		return 3;
	}

	@Override
	public String getInventoryName() {
		return "Rocket";
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;//This might not work
	}

	@Override
	public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
		switch (p_94041_1_) {
			case 0:
				return p_94041_2_.isItemEqual(new ItemStack(ModItems.satellite)) && items[p_94041_1_] == null && p_94041_2_.stackSize == 1;
			case 1:
				return GameRegistry.getFuelValue(p_94041_2_) > 0;
			case 2:
				return Util.compare(p_94041_2_, new ItemStack(Items.gunpowder));
			default:
				return false;
		}
	}

	@Override
	public boolean interact(EntityPlayer p_130002_1_) {
		if (!worldObj.isRemote) {
			p_130002_1_.openGui(PeripheralsPlusPlus.instance, Reference.GUIs.ROCKET.ordinal(), worldObj, (int) Math.ceil(this.posX), (int) Math.ceil(this.posY), (int) Math.ceil(this.posZ));
			return true;
		}
		return false;
	}
}
