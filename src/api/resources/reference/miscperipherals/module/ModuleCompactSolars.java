package miscperipherals.module;

import miscperipherals.core.MiscPeripherals;
import miscperipherals.core.Module;
import miscperipherals.upgrade.UpgradeCompactSolar;
import net.minecraft.network.packet.NetHandler;

import com.google.common.io.ByteArrayDataInput;

public class ModuleCompactSolars extends Module {
	public static boolean enableLVSolar = true;
	public static boolean enableMVSolar = true;
	public static boolean enableHVSolar = true;

	@Override
	public void onPreInit() {
		enableLVSolar = MiscPeripherals.instance.settings.get("features", "enableLVSolar", enableLVSolar, "Enable the LV Solar turtle upgrade").getBoolean(enableLVSolar);
		enableMVSolar = MiscPeripherals.instance.settings.get("features", "enableMVSolar", enableMVSolar, "Enable the MV Solar turtle upgrade").getBoolean(enableMVSolar);
		enableHVSolar = MiscPeripherals.instance.settings.get("features", "enableHVSolar", enableHVSolar, "Enable the HV Solar turtle upgrade").getBoolean(enableHVSolar);
	}

	@Override
	public void onInit() {
		
	}

	@Override
	public void onPostInit() {
		if (enableLVSolar) {
			MiscPeripherals.registerUpgrade(new UpgradeCompactSolar(0));
		}
		
		if (enableMVSolar) {
			MiscPeripherals.registerUpgrade(new UpgradeCompactSolar(1));
		}
		
		if (enableHVSolar) {
			MiscPeripherals.registerUpgrade(new UpgradeCompactSolar(2));
		}
	}

	@Override
	public void handleNetworkMessage(NetHandler source, boolean isClient, ByteArrayDataInput data) {
		
	}
}
