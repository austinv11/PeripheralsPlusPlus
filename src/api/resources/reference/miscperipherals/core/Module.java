package miscperipherals.core;

import net.minecraft.network.packet.NetHandler;

import com.google.common.io.ByteArrayDataInput;

public abstract class Module {	
	public abstract void onPreInit();
	
	public abstract void onInit();
	
	public abstract void onPostInit();
	
	public abstract void handleNetworkMessage(NetHandler source, boolean isClient, ByteArrayDataInput data);
}
