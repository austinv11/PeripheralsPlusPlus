package miscperipherals.module;

import miscperipherals.core.MiscPeripherals;
import miscperipherals.core.Module;
import miscperipherals.safe.ReflectionStore;
import miscperipherals.upgrade.UpgradeMFFS;
import net.minecraft.network.packet.NetHandler;

import com.google.common.io.ByteArrayDataInput;

public class ModuleMFFS extends Module {
	public static boolean enableMFFSTool = true;
	
	@Override
	public void onPreInit() {
		enableMFFSTool = MiscPeripherals.instance.settings.get("features", "enableMFFSTool", enableMFFSTool, "Enable the MFFS turtle upgrade").getBoolean(enableMFFSTool);
	}

	@Override
	public void onInit() {
		
	}

	@Override
	public void onPostInit() {
		ReflectionStore.initModularForceFieldSystem();
		
		if (enableMFFSTool) {
			MiscPeripherals.registerUpgrade(new UpgradeMFFS());
		}
	}

	@Override
	public void handleNetworkMessage(NetHandler source, boolean isClient, ByteArrayDataInput data) {
		
	}
}
