package miscperipherals.module;

import java.util.List;

import miscperipherals.core.MiscPeripherals;
import miscperipherals.core.Module;
import miscperipherals.safe.Reflector;
import miscperipherals.upgrade.UpgradeWrench;
import net.minecraft.network.packet.NetHandler;

import com.google.common.io.ByteArrayDataInput;

public class ModuleOmniTools extends Module {
	public static boolean enableWrench = true;

	@Override
	public void onPreInit() {
		enableWrench = MiscPeripherals.instance.settings.get("features", "enableWrench", enableWrench, "Enable the Wrench turtle upgrade").getBoolean(enableWrench);
	}

	@Override
	public void onInit() {
		
	}

	@Override
	public void onPostInit() {
		if (enableWrench) {
			MiscPeripherals.registerUpgrade(new UpgradeWrench());
		}
	}

	@Override
	public void handleNetworkMessage(NetHandler source, boolean isClient, ByteArrayDataInput data) {
		
	}
}
