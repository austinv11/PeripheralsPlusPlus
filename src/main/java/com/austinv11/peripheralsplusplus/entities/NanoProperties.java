package com.austinv11.peripheralsplusplus.entities;

import com.austinv11.peripheralsplusplus.PeripheralsPlusPlus;
import com.austinv11.peripheralsplusplus.tiles.TileEntityAntenna;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;
import java.util.UUID;

public class NanoProperties implements IExtendedEntityProperties {
	
	public static String IDENTIFIER = "NANOBOT_PROPERTIES";
	
	public int numOfBots = 0;
	public UUID antennaID;
	public Entity entity;
	
	@Override
	public void saveNBTData(NBTTagCompound compound) {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger("bots", numOfBots);
		if (this.antennaID != null) {
			tag.setString("antenna", antennaID.toString());
		}
		compound.setTag(IDENTIFIER, tag);
	}
	
	@Override
	public void loadNBTData(NBTTagCompound compound) {
		if (compound.hasKey(IDENTIFIER)) {
			NBTTagCompound tag = compound.getCompoundTag(IDENTIFIER);
			numOfBots = tag.getInteger("bots");

			try {
				antennaID = UUID.fromString(tag.getString("antenna"));
			}
			catch (IllegalArgumentException e) {
				// NO-OP
			}

			if (TileEntityAntenna.antenna_registry.containsKey(antennaID)) {
				PeripheralsPlusPlus.LOGGER.info("Added");
				TileEntityAntenna.antenna_registry.get(antennaID).associatedEntities.add(this.entity);
			}
			else {
				PeripheralsPlusPlus.LOGGER.info("Not added");
			}
		}
	}
	
	@Override
	public void init(Entity entity, World world) {
		this.entity = entity;
	}
}
