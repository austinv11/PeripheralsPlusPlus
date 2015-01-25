package com.austinv11.peripheralsplusplus.entities;

import com.austinv11.peripheralsplusplus.PeripheralsPlusPlus;
import com.austinv11.peripheralsplusplus.client.sounds.RocketSound;
import com.austinv11.peripheralsplusplus.init.ModItems;
import com.austinv11.peripheralsplusplus.network.RocketCountdownPacket;
import com.austinv11.peripheralsplusplus.reference.Reference;
import com.austinv11.peripheralsplusplus.satellites.Satellite;
import com.austinv11.peripheralsplusplus.satellites.SatelliteData;
import cpw.mods.fml.common.network.NetworkRegistry;
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
	public static final int IS_ACTIVE_ID = 5;
	public static final int MOTION_ID = 6;
	private int countDownTicker = 0;
	public int countDown = 10;
	private double lastMotion = 0;
	public boolean isFlipped = false;
	private int flameTicker = 0;
	private RocketSound sound;
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
		data.addObject(IS_ACTIVE_ID, 0);
		data.addObject(MOTION_ID, 0.0F);
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

	public boolean getIsActive() {
		return getDataWatcher().getWatchableObjectInt(IS_ACTIVE_ID) == 1;
	}

	public double getMotion() {
		return (double)getDataWatcher().getWatchableObjectFloat(MOTION_ID);
	}

	public void setFuel(int fuel) {
		getDataWatcher().updateObject(FUEL_ID, fuel);
	}

	public void setOxidizer(int oxidizer) {
		getDataWatcher().updateObject(OXIDIZER_ID, oxidizer);
	}

	public void setIsUsable(boolean isUsable) {
		getDataWatcher().updateObject(IS_USABLE_ID, isUsable ? 1 : 0);
	}

	public void setIsActive(boolean isActive) {
		getDataWatcher().updateObject(IS_ACTIVE_ID, isActive ? 1 : 0);
	}

	public void setMotion(float motion) {
		getDataWatcher().updateObject(MOTION_ID, motion);
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
		if (!worldObj.isRemote && !getIsActive()) {
			p_130002_1_.openGui(PeripheralsPlusPlus.instance, Reference.GUIs.ROCKET.ordinal(), worldObj, this.getEntityId()/*Im a 1337 uber haxor*/, (int)this.posY, (int)this.posZ);
			return true;
		}
		return false;
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound p_70037_1_) {
		super.readEntityFromNBT(p_70037_1_);
		setFuel(p_70037_1_.getInteger("fuel"));
		setOxidizer(p_70037_1_.getInteger("oxidizer"));
		setIsActive(p_70037_1_.getBoolean("isActive"));
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound p_70014_1_) {
		super.writeEntityToNBT(p_70014_1_);
		p_70014_1_.setInteger("fuel", getFuel());
		p_70014_1_.setInteger("oxidizer", getOxidizer());
		p_70014_1_.setBoolean("isActive", getIsActive());
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
		return AxisAlignedBB.getBoundingBox(posX-.25,posY-.25,posZ-.25,posX+.25,posY+3.5,posZ+.25);
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
				this.entityDropItem(new ItemStack(ModItems.rocket), 0.5F);
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
		if (!getIsActive())
			setIsUsable(getOxidizer() != 0 && getFuel() != 0 && isSkyClear() && items[0] != null && isItemValidForSlot(0, items[0]));
		if (items[1] != null && isItemValidForSlot(1, items[1])) {
			setFuel(getFuel() + ((TileEntityFurnace.getItemBurnTime(items[1])*items[1].stackSize)/200));
			items[1] = null;
		}
		if (items[2] != null && isItemValidForSlot(2, items[2])) {
			setOxidizer(getOxidizer()+items[2].stackSize);
			items[2] = null;
		}
		if (getIsActive()) {
			if (!worldObj.isRemote) {
				if (countDown >= 0) {
					if (countDown == 10) {
						PeripheralsPlusPlus.NETWORK.sendToAllAround(new RocketCountdownPacket(this, countDown), new NetworkRegistry.TargetPoint(this.worldObj.provider.dimensionId, posX, posY, posZ, 30));
						countDown--;
					} else if (countDownTicker == 20 && countDown > 0) {
						PeripheralsPlusPlus.NETWORK.sendToAllAround(new RocketCountdownPacket(this, countDown), new NetworkRegistry.TargetPoint(this.worldObj.provider.dimensionId, posX, posY, posZ, 30));
						countDownTicker = 0;
						countDown--;
					} else if (countDown == 0) {
						PeripheralsPlusPlus.NETWORK.sendToAllAround(new RocketCountdownPacket(this, countDown), new NetworkRegistry.TargetPoint(this.worldObj.provider.dimensionId, posX, posY, posZ, 30));
						countDown--;
					}
					countDownTicker++;
				} else
					calcMotion();
			} else {
				updateMotion();
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
		if (motionY <= 0 && isFloorClear() && !getIsActive())
			motionY -= 2*ACCELERATION_CONSTANT;
		if (motionY != 0)
			this.moveEntity(0, motionY, 0);
		if (isFlipped || motionY < -.5)
			isFlipped = true;
		if (isFlipped && !isFloorClear()) {
			if (!worldObj.isRemote)
				worldObj.createExplosion(this, posX, posY, posZ, 7.0F, true);
			setDead();
		}
		if (getIsActive() && isFlipped)
			setIsActive(false);
		if (!worldObj.isRemote)
			setMotion((float)motionY);
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

	private void initSatellite() {
		setDead();
		if (!worldObj.isRemote)
			if (SatelliteData.isWorldWhitelisted(worldObj)) {
				SatelliteData data = SatelliteData.forWorld(worldObj);
				if (data.getSatelliteForCoords((int)posX, (int)posZ) == null) {
					Satellite sat = new Satellite((int) posX, calcAdditionalY(), (int) posZ, worldObj);
					data.addSatellite(sat);
					data.markDirty();
				}
			}
	}

	private void calcMotion() {
		double newMotion = lastMotion;
		if (getOxidizer() > 0)
			newMotion += lastMotion >= ACCELERATION_CONSTANT ? ACCELERATION_CONSTANT : INITIAL_ACCELERATION_CONSTANT;
		else
			newMotion -= ACCELERATION_CONSTANT;
		int fuelRemainder = (int) Math.floor(getFuel()-(BASE_FUEL_USAGE+(newMotion*ACCELERATION_MODIFIER)));
		if (fuelRemainder <= getFuel()) {
			setOxidizer(getOxidizer() <= 0 ? 0 : getOxidizer()-1);
			setFuel(fuelRemainder < 0 ? 0 : fuelRemainder);
			lastMotion = motionY = newMotion;
		}
	}

	private void updateMotion() {
		lastMotion = motionY;
		motionY = getMotion();
	}

	private boolean isFloorClear() {
		return worldObj.isAirBlock((int)posX, (int)posY-1, (int)posZ);
	}

	private int calcAdditionalY() {
		double newMotion, motion;
		do {
			motion = motionY;
			calcMotion();
			newMotion = motionY;
		} while (motion != newMotion);
		return (int)(posY + motionY);
	}

	public void onCount(int countdownNum) {
		switch (countdownNum) {
			case 10:
				Minecraft.getMinecraft().thePlayer.addChatComponentMessage(new ChatComponentText(StatCollector.translateToLocal("peripheralsplusplus.chat.launchStart")));
				Minecraft.getMinecraft().thePlayer.addChatComponentMessage(new ChatComponentText(countdownNum+""));
				sound = new RocketSound(this);
				Minecraft.getMinecraft().getSoundHandler().playSound(sound);
				break;
			case 0:
				Minecraft.getMinecraft().thePlayer.addChatComponentMessage(new ChatComponentText(StatCollector.translateToLocal("peripheralsplusplus.chat.launch")));
				break;
			default:
				Minecraft.getMinecraft().thePlayer.addChatComponentMessage(new ChatComponentText(countdownNum+""));
		}
		countDown = countdownNum;
	}
}
