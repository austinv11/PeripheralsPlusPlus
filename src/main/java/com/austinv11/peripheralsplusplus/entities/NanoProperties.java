package com.austinv11.peripheralsplusplus.entities;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public class NanoProperties implements IExtendedEntityProperties {
	
	public static String IDENTIFIER = "NANOBOT_PROPERTIES";
	
	public int numOfBots = 0;
	
	@Override
	public void saveNBTData(NBTTagCompound compound) {
		compound.setInteger("bots", numOfBots);
	}
	
	@Override
	public void loadNBTData(NBTTagCompound compound) {
		numOfBots = compound.getInteger("bots");
	}
	
	@Override
	public void init(Entity entity, World world) {
		
	}
}
