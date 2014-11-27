package com.austinv11.peripheralsplusplus.entities;

import com.austinv11.peripheralsplusplus.PeripheralsPlusPlus;
import com.austinv11.peripheralsplusplus.init.ModItems;
import com.austinv11.peripheralsplusplus.reference.Reference;
import com.austinv11.peripheralsplusplus.utils.Util;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityRocket extends EntityInventory {

	protected int damage = 0;

	public EntityRocket(World p_i1582_1_) {
		super(p_i1582_1_);
		this.preventEntitySpawning = true;
		this.ignoreFrustumCheck = true;
		this.renderDistanceWeight = 5.0D;
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
	public boolean interactFirst(EntityPlayer p_130002_1_) {
		if (!worldObj.isRemote) {
			p_130002_1_.openGui(PeripheralsPlusPlus.instance, Reference.GUIs.ROCKET.ordinal(), worldObj, (int) Math.ceil(this.posX), (int) Math.ceil(this.posY), (int) Math.ceil(this.posZ));
			return true;
		}
		return false;
	}

	@Override
	protected boolean canTriggerWalking() {
		return false;
	}

	@Override
	public AxisAlignedBB getCollisionBox(Entity par1Entity) {
		return null;
	}

	@Override
	public AxisAlignedBB getBoundingBox() {
		return null;
	}

	@Override
	public boolean canBePushed() {
		return false;
	}

	@Override
	public boolean attackEntityFrom(DamageSource par1DamageSource, float par2) {
		if (!this.worldObj.isRemote && !this.isDead) {
			if (this.isEntityInvulnerable())
				return false;
			if (par1DamageSource.getEntity() instanceof EntityPlayer && ((EntityPlayer) par1DamageSource.getEntity()).capabilities.isCreativeMode)
				this.damage = 100;
			if (this.damage > 90) {
				this.setDead();
				this.dropItem(ModItems.rocket, 0);
				for (int i = 0; i < this.getSizeInventory(); i++)
					if (this.getStackInSlot(i) != null)
						this.entityDropItem(this.getStackInSlot(i), 0.5F);
				return true;
			}
			return true;
		}
		return true;
	}

	@Override
	public boolean canBeCollidedWith() {
		return !this.isDead;
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		//TODO
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void setPositionAndRotation2(double par1, double par3, double par5, float par7, float par8, int par9) {
		this.setRotation(par7, par8);
	}
}
