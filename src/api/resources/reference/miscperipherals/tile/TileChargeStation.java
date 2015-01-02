package miscperipherals.tile;

import ic2.api.Direction;
import ic2.api.energy.tile.IEnergySink;

import java.util.ArrayList;
import java.util.List;

import miscperipherals.asm.ImplementIfLoaded;
import miscperipherals.asm.OwnerInterface;
import miscperipherals.core.MiscPeripherals;
import miscperipherals.network.GuiHandler;
import miscperipherals.util.Util;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Facing;
import net.minecraftforge.common.ForgeDirection;
import buildcraft.api.power.IPowerProvider;
import buildcraft.api.power.IPowerReceptor;
import cpw.mods.fml.common.FMLCommonHandler;
import dan200.turtle.api.ITurtleAccess;

@ImplementIfLoaded({"IC2", "ic2.", "BuildCraft|Core", "buildcraft."})
public class TileChargeStation extends TileInventory implements IEnergySink, IPowerReceptor {
	public static final List<ChargeStationPlugin> PLUGINS = new ArrayList<ChargeStationPlugin>(3);
	
	public double energy = 0;
	public int limit = 0;
	public int[] sides = getSides();
	public final int tier;
	
	public boolean addedToEnergyNet = false;
	public boolean addedToUE = false;
	public Object bcProvider;
	
	public TileChargeStation() {
		this(1);
	}
	
	public TileChargeStation(int tier) {
		super(1);
		this.tier = tier;
		
		for (ChargeStationPlugin plugin : PLUGINS) {
			plugin.initialize(this);
		}
	}
	
	@Override
	public boolean canUpdate() {
		return !FMLCommonHandler.instance().getEffectiveSide().isClient();
	}
	
	@Override
	public void updateEntity() {
		super.updateEntity();
		
		for (ChargeStationPlugin plugin : PLUGINS) {
			plugin.update(this);
		}
		
		if (!isDisabled() && energy > 1) {
			List<ITurtleAccess> turtles = new ArrayList<ITurtleAccess>(sildes.length);
			for (int i = 0; i < sides.length; i++) {
				int x = xCoord + Facing.offsetsXForSide[sides[i]];
				int y = yCoord + Facing.offsetsYForSide[sides[i]];
				int z = zCoord + Facing.offsetsZForSide[sides[i]];
				if (!worldObj.blockExists(x, y, z)) continue;
				
				TileEntity te = worldObj.getBlockTileEntity(x, y, z);
				if (te instanceof ITurtleAccess) {
					turtles.add((ITurtleAccess)te);
				}
			}
			
			int rate = (int)Math.floor((float)sides.length / (float)turtles.size());
			for (ITurtleAccess turtle : turtles) {
				energy -= Util.addFuel(turtle, rate);
			}
		}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		
		try {
			energy = tag.getDouble("energy");
		} catch (Throwable e) {
			energy = tag.getInteger("energy");
		}
		limit = tag.getInteger("limit");
		
		for (ChargeStationPlugin plugin : PLUGINS) {
			plugin.readFromNBT(this, tag);
		}
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		
		tag.setDouble("energy", energy);
		tag.setInteger("limit", limit);

		for (ChargeStationPlugin plugin : PLUGINS) {
			plugin.writeToNBT(this, tag);
		}
	}
	
	@Override
	public String getInventoryName() {
		return "Charge Station";
	}
	
	@Override
	public int getGuiId() {
		return GuiHandler.CHARGE_STATION;
	}
	
	@Override
	public void setFacing(int facing) {
		super.setFacing(facing);
		sides = getSides();
	}
	
	public int getMaxCharge() {
		return 50 * (int)Math.pow(10, tier);
	}
	
	public int[] getSides() {
		return new int[] {getFacing()};
	}

	@Override
	public boolean isStackValidForSlot(int slot, ItemStack item) {
		return true;
	}
	
	@Override
	public void onUnloaded() {
		for (ChargeStationPlugin plugin : PLUGINS) {
			plugin.unload(this);
		}
		
		super.onUnloaded();
	}
	
	@Override
	public boolean onBlockActivated(EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		for (ChargeStationPlugin plugin : PLUGINS) {
			if (plugin.onBlockActivated(this, player, side, hitX, hitY, hitZ)) return true;
		}
		
		return super.onBlockActivated(player, side, hitX, hitY, hitZ);
	}
	
	@Override
	public int getComparator(int side) {
		return (int)Math.ceil((energy / (double)getMaxCharge()) * 15.0D);
	}
	
	@OwnerInterface("ic2.api.energy.tile.IEnergySink")
	@Override
	public boolean acceptsEnergyFrom(TileEntity emitter, Direction direction) {
		return true;
	}

	@OwnerInterface("ic2.api.energy.tile.IEnergySink")
	@Override
	public boolean isAddedToEnergyNet() {
		return addedToEnergyNet;
	}

	@OwnerInterface("ic2.api.energy.tile.IEnergySink")
	@Override
	public int demandsEnergy() {
		return (int)Math.floor((getMaxCharge() - energy) * MiscPeripherals.instance.fuelEU);
	}

	@OwnerInterface("ic2.api.energy.tile.IEnergySink")
	@Override
	public int injectEnergy(Direction directionFrom, int amount) {
		if (amount > getMaxSafeInput()) {
			for (ChargeStationPlugin plugin : PLUGINS) {
				plugin.unload(this);
			}
			
			worldObj.setBlock(xCoord, yCoord, zCoord, 0, 0, 2);
			worldObj.createExplosion(null, xCoord + 0.5F, yCoord + 0.5F, zCoord + 0.5F, 2.0F, false);
			return amount;
		}
		
		double toAdd = Math.min((double)amount / (double)MiscPeripherals.instance.fuelEU, getMaxCharge() - energy);
		energy += toAdd;
		return (int)Math.floor(amount - toAdd * MiscPeripherals.instance.fuelEU);
	}

	@OwnerInterface("ic2.api.energy.tile.IEnergySink")
	@Override
	public int getMaxSafeInput() {
		return tier == 1 ? 32 : (tier == 2 ? 128 : (tier == 3 ? 512 : (tier == 4 ? 2048 : Integer.MAX_VALUE)));
	}
	
	@OwnerInterface("buildcraft.api.power.IPowerReceptor")
	@Override
	public void setPowerProvider(IPowerProvider provider) {
		this.bcProvider = provider;
	}
	
	@OwnerInterface("buildcraft.api.power.IPowerReceptor")
	@Override
	public IPowerProvider getPowerProvider() {
		return (IPowerProvider)bcProvider;
	}
	
	@OwnerInterface("buildcraft.api.power.IPowerReceptor")
	@Override
	public void doWork() {
		float sink = ((IPowerProvider)bcProvider).useEnergy(0.0F, (float)((getMaxCharge() - energy) * MiscPeripherals.instance.fuelMJ), true);
		energy += sink / (double)MiscPeripherals.instance.fuelMJ;
	}
	
	@OwnerInterface("buildcraft.api.power.IPowerReceptor")
	@Override
	public int powerRequest(ForgeDirection direction) {
		return MiscPeripherals.instance.fuelMJ * getSides().length;
	}
	
	public boolean isDisabled() {
		//if (getRedstone() > 0) return true;
		
		for (ChargeStationPlugin plugin : PLUGINS) {
			if (plugin.isDisabled(this)) return true;
		}
		
		return false;
	}
	
	public static interface ChargeStationPlugin {
		public boolean isBattery(TileChargeStation station, ItemStack item);
		public void update(TileChargeStation station);
		public void unload(TileChargeStation station);
		public boolean onBlockActivated(TileChargeStation station, EntityPlayer player, int side, float hitX, float hitY, float hitZ);
		public void readFromNBT(TileChargeStation station, NBTTagCompound compound);
		public void writeToNBT(TileChargeStation station, NBTTagCompound compound);
		public void initialize(TileChargeStation station);
		public boolean isDisabled(TileChargeStation station);
	}
}
