package com.austinv11.peripheralsplusplus.capabilities.nano;

import com.austinv11.peripheralsplusplus.tiles.TileEntityAntenna;
import com.austinv11.peripheralsplusplus.utils.Util;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;
import java.util.UUID;

public class NanoBotHolderStorage implements Capability.IStorage<NanoBotHolder> {
	@Nullable
	@Override
	public NBTBase writeNBT(Capability<NanoBotHolder> capability, NanoBotHolder instance,
							EnumFacing side) {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger("bots", instance.getBots());
		if (instance.getEntity() != null)
			tag.setString("entity", instance.getEntity().getPersistentID().toString());
        if (instance.getAntenna() != null)
            tag.setString("antenna", instance.getAntenna().toString());
		return tag;
	}

	@Override
	public void readNBT(Capability<NanoBotHolder> capability, NanoBotHolder instance, EnumFacing side,
						NBTBase nbt) {
	    if (!(nbt instanceof NBTTagCompound))
	        return;
        NBTTagCompound tag = (NBTTagCompound) nbt;
		instance.setBots(tag.getInteger("bots"));
		if (!tag.getString("entity").isEmpty())
			instance.setEntity(Util.getEntityFromId(UUID.fromString(tag.getString("entity"))));
		if (!tag.getString("antenna").isEmpty())
			instance.setAntenna(UUID.fromString(tag.getString("antenna")));
		if (instance.getEntity() != null && instance.getAntenna() != null &&
				TileEntityAntenna.ANTENNA_REGISTRY.containsKey(instance.getAntenna())) {
			TileEntityAntenna tileEntityAntenna = TileEntityAntenna.ANTENNA_REGISTRY.get(instance.getAntenna());
			tileEntityAntenna.registerEntity(instance.getEntity());
		}
	}
}
