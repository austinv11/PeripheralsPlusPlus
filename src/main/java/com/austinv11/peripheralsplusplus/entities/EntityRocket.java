package com.austinv11.peripheralsplusplus.entities;

import com.austinv11.peripheralsplusplus.PeripheralsPlusPlus;
import com.austinv11.peripheralsplusplus.client.sounds.RocketSound;
import com.austinv11.peripheralsplusplus.init.ModItems;
import com.austinv11.peripheralsplusplus.reference.Reference;
import com.austinv11.peripheralsplusplus.utils.ChatUtil;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
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
	public static final int FUEL_ID = 2;
	public static final int OXIDIZER_ID = 3;
	public static final int IS_USABLE_ID = 4;
	public boolean isActive = false;
	private int countDownTicker = 0;
	public int countDown = 10;
	private double lastMotion = 0;
	private int flameTicker = 0;
	private RocketSound sound;
	public boolean isFlipped = false;
	public static final double ACCELERATION_MODIFIER = .3;
	public static final double BASE_FUEL_USAGE = 1;
	public static final double MAX_HEIGHT = 450;
	public static final double INITIAL_ACCELERATION_CONSTANT = .002;
	public static final double ACCELERATION_CONSTANT = .03;

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

	public EntityRocket(World p_i1582_1_, double x, double y, double z) {
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
			p_130002_1_.openGui(PeripheralsPlusPlus.instance, Reference.GUIs.ROCKET.ordinal(), worldObj, this.getEntityId()/*Im a 1337 uber haxor*/, (int)this.posY, (int)this.posZ);
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
		return AxisAlignedBB.getBoundingBox(0,0,0,3,3,3);
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
		if (posY > MAX_HEIGHT) {
			initSatellite();
			return;
		}
		if (!isActive)
			getDataWatcher().updateObject(IS_USABLE_ID, getOxidizer() != 0 && getFuel() != 0 && isSkyClear() && items[0] != null && isItemValidForSlot(0, items[0]) ? 1 : 0);
		if (items[1] != null && isItemValidForSlot(1, items[1])) {
			getDataWatcher().updateObject(FUEL_ID, getFuel() + ((TileEntityFurnace.getItemBurnTime(items[1])*items[1].stackSize)/200));
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
					if (worldObj.isRemote) {
						sound = new RocketSound(this);
						Minecraft.getMinecraft().getSoundHandler().playSound(sound);
					}
				} else if (countDownTicker == 20 && countDown > 0) {
					sendMessageToAllNearby(countDown+"");
					countDownTicker = 0;
					countDown--;
				} else if (countDown == 0) {
					sendMessageToAllNearby(StatCollector.translateToLocal("peripheralsplusplus.chat.launch"));
					countDown--;
				}
				countDownTicker++;
			} else
				calcMotion();
			if (worldObj.isRemote) {
				for (int i = 0; i < 3; i++) {
					double divisionFactor = countDown < 7 ? 5.0D : 14.0D;
					worldObj.spawnParticle("explode", posX, posY, posZ, rand.nextGaussian()/divisionFactor, motionY == 0 ? .01 : Math.signum(motionY)*motionY-.4, rand.nextGaussian()/divisionFactor);
				}
				if (countDown < 7) {
					if (flameTicker >= 5) {
						worldObj.spawnParticle("flame", posX, posY, posZ, rand.nextGaussian()/12, motionY == 0 ? .005 : Math.signum(motionY)*motionY-.04, rand.nextGaussian()/12);
						flameTicker = 0;
					} else
						flameTicker++;
				}
				if (sound != null)
					sound.update();
			}
		}
		if (motionY <= 0 && isFloorClear() && !isActive)
			motionY -= 2*ACCELERATION_CONSTANT;
		if (motionY != 0)
			this.moveEntity(0, motionY, 0);
		if (isActive && motionY < -1 && countDown < 0)
			isActive = false;
		isFlipped = motionY < -.5;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void setPositionAndRotation2(double par1, double par3, double par5, float par7, float par8, int par9) {
		this.setRotation(par7, par8);
	}

	public boolean isSkyClear() {
		boolean skyClear = true;
		for (double i = this.posY-1; i < 255; i+=1)
			skyClear = this.worldObj.isAirBlock((int)this.posX, (int)i, (int)this.posZ);
		return skyClear;
	}

	public void setActive() {
		isActive = true;
	}

	public void sendMessageToAllNearby(String message) {
		ChatUtil.sendMessage(this, new ChatComponentText("["  + Reference.MOD_NAME + "] " + message), 30, true);
	}

	private void initSatellite() {
		setDead();
		//TODO
	}

	private void calcMotion() {
		double newMotion = lastMotion;
		if (getOxidizer() > 0)
			newMotion += lastMotion >= ACCELERATION_CONSTANT ? ACCELERATION_CONSTANT : INITIAL_ACCELERATION_CONSTANT;
		else
			newMotion -= ACCELERATION_CONSTANT;
		int fuelRemainder = (int) Math.floor(getFuel()-(BASE_FUEL_USAGE+(newMotion*ACCELERATION_MODIFIER)));
		if (fuelRemainder <= getFuel()) {
			getDataWatcher().updateObject(OXIDIZER_ID, getOxidizer() <= 0 ? 0 : getOxidizer()-1);
			getDataWatcher().updateObject(FUEL_ID, fuelRemainder < 0 ? 0 : fuelRemainder);
			lastMotion = motionY = newMotion;
		}
	}

	private boolean isFloorClear() {
		return worldObj.isAirBlock((int)posX, (int)posY-1, (int)posZ);
	}
}
