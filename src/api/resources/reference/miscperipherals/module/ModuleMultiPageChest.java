package miscperipherals.module;

import miscperipherals.core.MiscPeripherals;
import miscperipherals.core.Module;
import miscperipherals.safe.ReflectionStore;
import miscperipherals.upgrade.UpgradeMultiPageChest;
import net.minecraft.network.packet.NetHandler;

import com.google.common.io.ByteArrayDataInput;

public class ModuleMultiPageChest extends Module {
	public boolean enableMultiPageChest = true;
	
	@Override
	public void onPreInit() {
		enableMultiPageChest = MiscPeripherals.instance.settings.get("features", "enableMultiPageChest", enableMultiPageChest, "Enable the Multi Page Chest turtle upgrade").getBoolean(enableMultiPageChest);
	}

	@Override
	public void onInit() {
		
	}

	@Override
	public void onPostInit() {
		ReflectionStore.initMultiPageChest();
		
		if (enableMultiPageChest) {
			MiscPeripherals.registerUpgrade(new UpgradeMultiPageChest());
		}
	}

	@Override
	public void handleNetworkMessage(NetHandler source, boolean isClient, ByteArrayDataInput data) {
		
	}
}
