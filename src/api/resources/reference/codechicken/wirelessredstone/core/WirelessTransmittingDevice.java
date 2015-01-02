package codechicken.wirelessredstone.core;

import net.minecraft.entity.EntityLiving;
import codechicken.core.Vector3;

public interface WirelessTransmittingDevice
{
	public Vector3 getPosition();
	public int getDimension();
	public int getFreq();
	EntityLiving getAttachedEntity();
}
