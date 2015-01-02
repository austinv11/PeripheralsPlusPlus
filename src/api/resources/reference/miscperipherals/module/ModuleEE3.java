package miscperipherals.module;

import miscperipherals.core.MiscPeripherals;
import miscperipherals.core.Module;
import miscperipherals.safe.ReflectionStore;
import miscperipherals.upgrade.UpgradeAlchemist;
import net.minecraft.network.packet.NetHandler;

import com.google.common.io.ByteArrayDataInput;

public class ModuleEE3 extends Module {
	public static boolean enableAlchemist = true;
	
	@Override
	public void onPreInit() {
		enableAlchemist = MiscPeripherals.instance.settings.get("features", "enableAlchemist", enableAlchemist, "Enable the Alchemist turtle upgrade").getBoolean(enableAlchemist);
	}

	@Override
	public void onInit() {
		
	}

	@Override
	public void onPostInit() {
		if (enableAlchemist) {
			MiscPeripherals.registerUpgrade(new UpgradeAlchemist());
		}
		
		ReflectionStore.initEE3();
	}

	@Override
	public void handleNetworkMessage(NetHandler source, boolean isClient, ByteArrayDataInput data) {
		
	}
}
