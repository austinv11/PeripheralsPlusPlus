package com.austinv11.peripheralsplusplus.entities;

import com.austinv11.peripheralsplusplus.PeripheralsPlusPlus;
import com.austinv11.peripheralsplusplus.init.ModItems;
import com.austinv11.peripheralsplusplus.reference.Reference;
import com.austinv11.peripheralsplusplus.utils.ChatUtil;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class EntityRocket extends EntityInventory{

	protected int damage = 0;
	public static int FUEL_ID = 2;
	public static int OXIDIZER_ID = 3;
	public static int IS_USABLE_ID = 4;
	private boolean isActive = false;
	private int countDownTicker = 0;
	private int countDown = 10;

	public EntityRocket(World p_i1582_1_) {
		super(p_i1582_1_);
		this.preventEntitySpawning = true;
		this.ignoreFrustumCheck = true;
		this.renderDistanceWeight = 5.0D;
		DataWatcher data = this.getDataWatcher();
		data.addObject(FUEL_ID, 0);
		data.addObject(OXIDIZER_ID, 0);
		data.addObject(IS_USABLE_ID, 0);
	}

	public EntityRocket(World p_i1582_1_, int x, int y, int z) {
		this(p_i1582_1_);
		this.posX = x;
		this.posY = y;
		this.posZ = z;
	}

	public int getFuel() {
		return getDataWatcher().getWatchableObjectInt(FUEL_ID);
	}

	public int getOxidizer() {
		return getDataWatcher().getWatchableObjectInt(OXIDIZER_ID);
	}

	public boolean getIsUsable() {
		return getDataWatcher().getWatchableObjectInt(IS_USABLE_ID) == 1;
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
				return p_94041_2_.isItemEqual(new ItemStack(ModItems.satellite)) && items[p_94041_1_] != null && p_94041_2_.stackSize == 1;
			case 1:
				return TileEntityFurnace.isItemFuel(p_94041_2_);
			case 2:
				return p_94041_2_.isItemEqual(new ItemStack(Items.gunpowder));
			default:
				return false;
		}
	}

	@Override
	public boolean interactFirst(EntityPlayer p_130002_1_) {
		if (!worldObj.isRemote && !isActive) {
			p_130002_1_.openGui(PeripheralsPlusPlus.instance, Reference.GUIs.ROCKET.ordinal(), worldObj, this.getEntityId()/*Im a 1337 uber haxor*/, (int) Math.floor(this.posY), (int) Math.floor(this.posZ));
			return true;
		}
		return false;
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound p_70037_1_) {
		super.readEntityFromNBT(p_70037_1_);
		DataWatcher data = getDataWatcher();
		data.updateObject(FUEL_ID, p_70037_1_.getInteger("fuel"));
		data.updateObject(OXIDIZER_ID, p_70037_1_.getInteger("oxidizer"));
		isActive = p_70037_1_.getBoolean("isActive");
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound p_70014_1_) {
		super.writeEntityToNBT(p_70014_1_);
		p_70014_1_.setInteger("fuel", getFuel());
		p_70014_1_.setInteger("oxidizer", getOxidizer());
		p_70014_1_.setBoolean("isActive", isActive);
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
			if (par1DamageSource.getEntity() instanceof EntityPlayer)
				this.damage = 100;
			if (this.damage > 90) {
				this.dropItem(ModItems.rocket, 0);
				for (int i = 0; i < this.getSizeInventory(); i++)
					if (this.getStackInSlot(i) != null)
						this.entityDropItem(this.getStackInSlot(i), 0.5F);
				this.setDead();
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
		if (!isActive)
			getDataWatcher().updateObject(IS_USABLE_ID, getOxidizer() != 0 && getFuel() != 0 && isSkyClear() && items[0] != null && isItemValidForSlot(0, items[0]) ? 1 : 0);
		if (items[1] != null && isItemValidForSlot(1, items[1])) {
			getDataWatcher().updateObject(FUEL_ID, getFuel() + (TileEntityFurnace.getItemBurnTime(items[1])/200));
			items[1] = null;
		}
		if (items[2] != null && isItemValidForSlot(2, items[2])) {
			getDataWatcher().updateObject(OXIDIZER_ID, getOxidizer()+items[2].stackSize);
			items[2] = null;
		}
		if (isActive) {
			if (countDown >= 0) {
				if (countDown == 10) {
					sendMessageToAllNearby(StatCollector.translateToLocal("peripheralsplusplus.chat.launchStart"));
					sendMessageToAllNearby(countDown+"");
					countDown--;
				} else if (countDownTicker == 20 && countDown > 0) {
					sendMessageToAllNearby(countDown+"");
					countDownTicker = 0;
					countDown--;
				} else if (countDown == 0) {
					sendMessageToAllNearby(StatCollector.translateToLocal("peripheralsplusplus.chat.launch"));
					countDown--;
				}
				countDownTicker++;
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void setPositionAndRotation2(double par1, double par3, double par5, float par7, float par8, int par9) {
		this.setRotation(par7, par8);
	}

	public boolean isSkyClear() {
		boolean skyClear = true;
		for (int i = (int)Math.floor(this.posY)-1; i < 255; i++)
			skyClear = this.worldObj.isAirBlock((int)Math.floor(this.posX), i, (int)Math.floor(this.posZ));
		return skyClear;
	}

	public void setActive() {
		isActive = true;
	}

	public void sendMessageToAllNearby(String message) {
		ChatUtil.sendMessage(this, new ChatComponentText(ChatUtil.getCoordsPrefix(this) + message), 30, true);
	}
}
