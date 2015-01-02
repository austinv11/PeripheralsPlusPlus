package miscperipherals.module;

import miscperipherals.core.MiscPeripherals;
import miscperipherals.core.Module;
import miscperipherals.safe.ReflectionStore;
import miscperipherals.upgrade.UpgradeAdvancedSolar;
import net.minecraft.network.packet.NetHandler;

import com.google.common.io.ByteArrayDataInput;

public class ModuleAdvancedSolarPanel extends Module {
	public static boolean enableAdvancedSolar = true;
	public static boolean enableHybridSolar = true;
	public static boolean enableUltimateHybridSolar = true;

	@Override
	public void onPreInit() {
		enableAdvancedSolar = MiscPeripherals.instance.settings.get("features", "enableAdvancedSolar", enableAdvancedSolar, "Enable the Advanced Solar turtle upgrade").getBoolean(enableAdvancedSolar);
		enableHybridSolar = MiscPeripherals.instance.settings.get("features", "enableHybridSolar", enableHybridSolar, "Enable the Hybrid Solar turtle upgrade").getBoolean(enableHybridSolar);
		enableUltimateHybridSolar = MiscPeripherals.instance.settings.get("features", "enableUltimateHybridSolar", enableUltimateHybridSolar, "Enable the Ultimate Hybrid Solar turtle upgrade").getBoolean(enableUltimateHybridSolar);
	}

	@Override
	public void onInit() {
		
	}

	@Override
	public void onPostInit() {
		ReflectionStore.initAdvancedSolarPanel();
		
		if (enableAdvancedSolar) {
			MiscPeripherals.registerUpgrade(new UpgradeAdvancedSolar(0));
		}
		
		if (enableHybridSolar) {
			MiscPeripherals.registerUpgrade(new UpgradeAdvancedSolar(1));
		}
		
		if (enableUltimateHybridSolar) {
			MiscPeripherals.registerUpgrade(new UpgradeAdvancedSolar(2));
		}
	}

	@Override
	public void handleNetworkMessage(NetHandler source, boolean isClient, ByteArrayDataInput data) {
		
	}
}
