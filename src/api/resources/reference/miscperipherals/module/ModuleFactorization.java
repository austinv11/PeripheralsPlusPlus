package miscperipherals.module;

import miscperipherals.core.MiscPeripherals;
import miscperipherals.core.Module;
import miscperipherals.upgrade.UpgradeBarrel;
import net.minecraft.network.packet.NetHandler;

import com.google.common.io.ByteArrayDataInput;

public class ModuleFactorization extends Module {
	public static boolean enableBarrel = true;
	
	@Override
	public void onPreInit() {
		enableBarrel = MiscPeripherals.instance.settings.get("features", "enableBarrel", enableBarrel, "Enable the Barrel turtle upgrade").getBoolean(enableBarrel);
	}

	@Override
	public void onInit() {
		
	}

	@Override
	public void onPostInit() {
		if (enableBarrel) {
			MiscPeripherals.registerUpgrade(new UpgradeBarrel());
		}
	}

	@Override
	public void handleNetworkMessage(NetHandler source, boolean isClient, ByteArrayDataInput data) {
		
	}
}
