package com.austinv11.peripheralsplusplus.satellites;

import com.austinv11.peripheralsplusplus.PeripheralsPlusPlus;
import com.austinv11.peripheralsplusplus.api.satellites.ISatellite;
import com.austinv11.peripheralsplusplus.api.satellites.upgrades.ISatelliteUpgrade;
import com.austinv11.peripheralsplusplus.api.satellites.upgrades.SatelliteUpgradeType;
import com.austinv11.peripheralsplusplus.event.SateliiteCrashEvent;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Satellite implements ISatellite{

	/**
	 * K = Satellite object, V = dimensionId
	 */
	public final static HashMap<Satellite, Integer> SATELLITE_REGISTRY = new HashMap<Satellite, Integer>();

	private ChunkCoordinates coords;
	private int id = -1;
	private World world;
	private ISatelliteUpgrade mainUpgrade;
	private List<ISatelliteUpgrade> addons = new ArrayList<ISatelliteUpgrade>();
	private boolean isInOrbit = true;
	private NBTTagCompound mainTag = new NBTTagCompound();
	private HashMap<Integer, NBTTagCompound> addonTags =  new HashMap<Integer,NBTTagCompound>();

	public Satellite(int x, int y, int z, World w) {
		coords = new ChunkCoordinates(x,y,z);
		world = w;
		Satellite.SATELLITE_REGISTRY.put(this, world.provider.dimensionId);
	}

	public void setID(int id) {
		this.id = id;
	}

	public NBTTagCompound toNBT() {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setInteger("x", coords.posX);
		nbt.setInteger("y", coords.posY);
		nbt.setInteger("z", coords.posZ);
		nbt.setInteger("id", id);
		nbt.setInteger("dim", world.provider.dimensionId);
		nbt.setInteger("mainId", mainUpgrade.getUpgradeID());
		int[] ids = new int[addons.size()];
		for (int i = 0; i < addons.size(); i++)
			ids[i] = addons.get(i).getUpgradeID();
		nbt.setIntArray("addonIds", ids);
		nbt.setTag("main", mainTag);
		for (int j : addonTags.keySet())
			nbt.setTag(String.valueOf(j), addonTags.get(j));
		return nbt;
	}

	public static Satellite fromNBT(NBTTagCompound nbt) {
		Satellite sat = new Satellite(nbt.getInteger("x"), nbt.getInteger("y"), nbt.getInteger("z"),
				MinecraftServer.getServer().worldServerForDimension(nbt.getInteger("dim")));
		sat.setID(nbt.getInteger("id"));
		try {
			sat.setMainUpgrade(PeripheralsPlusPlus.UPGRADE_REGISTRY.get(nbt.getInteger("mainId")));
			sat.mainTag = nbt.getCompoundTag("main");
		} catch (Exception e) {
			PeripheralsPlusPlus.LOGGER.error("There was a problem loading the upgrade for satellite "+sat.getID());
		}
		try {
			List<ISatelliteUpgrade> upgrades = new ArrayList<ISatelliteUpgrade>();
			for (int i : nbt.getIntArray("addonIds")) {
				ISatelliteUpgrade upgrade = PeripheralsPlusPlus.UPGRADE_REGISTRY.get(i);
				upgrades.add(upgrade);
				sat.addonTags.put(upgrade.getUpgradeID(), nbt.getCompoundTag(String.valueOf(i)));
			}
			sat.setAddons(upgrades);
		} catch (Exception e) {
			PeripheralsPlusPlus.LOGGER.error("There was a problem loading the addons for satellite "+sat.getID());
		}
		return sat;
	}

	@Override
	public ChunkCoordinates getPosition() {
		return coords;
	}

	@Override
	public ISatelliteUpgrade getMainUpgrade() {
		return mainUpgrade;
	}

	@Override
	public List<ISatelliteUpgrade> getAddons() {
		return addons;
	}

	@Override
	public World getWorld() {
		return world;
	}

	@Override
	public ChunkCoordinates recall(boolean doExplode, List<ItemStack> drops) {
		int x = coords.posX;
		int y = coords.posY;
		int z = coords.posZ;
		while (world.isAirBlock(x, y, z))
			y--;
		y++;
		float radius = (float)5*((coords.posY-y)/4);
		return this.recall(doExplode, drops, radius);
	}

	@Override
	public ChunkCoordinates recall(boolean doExplode, List<ItemStack> drops, float explosionRadius) {
		int x = coords.posX;
		int y = coords.posY;
		int z = coords.posZ;
		while (world.isAirBlock(x, y, z))
			y--;
		y++;
		ChunkCoordinates dropCoords = new ChunkCoordinates(x, y, z);
		if (doExplode)
			if (!world.isRemote) {
				world.createExplosion(null, x, y, z, explosionRadius > 0 ? explosionRadius : 0, true);
			}
		for (ItemStack i : drops)
			world.spawnEntityInWorld(new EntityItem(world, x, y+10, z, i));
		SatelliteData data = SatelliteData.forWorld(world);
		data.removeSatellite(this.id);
		data.markDirty();
		isInOrbit = false;
		MinecraftForge.EVENT_BUS.post(new SateliiteCrashEvent(this, dropCoords));
		return dropCoords;
	}

	@Override
	public int getID() {
		return id;
	}

	@Override
	public void setMainUpgrade(ISatelliteUpgrade upgrade) throws Exception {
		if (upgrade.getType() == SatelliteUpgradeType.MAIN) {
			mainUpgrade = upgrade;
			mainTag = new NBTTagCompound();
			return;
		}
		throw new Exception("Incompatible Satellite Upgrade Type");
	}

	@Override
	public void setAddons(List<ISatelliteUpgrade> addons) throws Exception {
		for (ISatelliteUpgrade up : addons) {
			if (up.getType() != SatelliteUpgradeType.MODIFIER)
				throw new Exception("Incompatible Satellite Addon Type");
		}
		this.addons = addons;
		addonTags =  new HashMap<Integer,NBTTagCompound>();
	}

	@Override
	public boolean isInOrbit() {
		return isInOrbit;
	}

	@Override
	public NBTTagCompound getTag(ISatelliteUpgrade upgrade) {
		if (upgrade.getType() == SatelliteUpgradeType.MAIN) {
			if (mainUpgrade.getUpgradeID() == upgrade.getUpgradeID())
				return mainTag;
		} else {
			if (addonTags.containsKey(upgrade.getUpgradeID()))
				return addonTags.get(upgrade.getUpgradeID());
			else if (contains(upgrade))
				return new NBTTagCompound();
		}
		return null;
	}

	@Override
	public void updateTag(ISatelliteUpgrade upgrade, NBTTagCompound tag) {
		if (upgrade.getType() == SatelliteUpgradeType.MAIN) {
			if (mainUpgrade.getUpgradeID() == upgrade.getUpgradeID())
				mainTag = tag;
		} else {
			if (addonTags.containsKey(upgrade.getUpgradeID()))
				addonTags.put(upgrade.getUpgradeID(), tag);
		}
	}

	private boolean contains(ISatelliteUpgrade upgrade) {
		for (ISatelliteUpgrade up : addons)
			if (up.getUpgradeID() == upgrade.getUpgradeID())
				return true;
		return false;
	}
}
