package miscperipherals.module;

import miscperipherals.core.MiscPeripherals;
import miscperipherals.core.Module;
import miscperipherals.upgrade.UpgradeToolBuilder;
import net.minecraft.network.packet.NetHandler;

import com.google.common.io.ByteArrayDataInput;

public class ModuleTConstruct extends Module {
	public boolean enableToolBuilder = true;
	
	@Override
	public void onPreInit() {
		enableToolBuilder = MiscPeripherals.instance.settings.get("features", "enableToolBuilder", enableToolBuilder, "Enable the Tool Builder turtle upgrade").getBoolean(enableToolBuilder);
	}

	@Override
	public void onInit() {
		
	}

	@Override
	public void onPostInit() {
		if (enableToolBuilder) {
			MiscPeripherals.registerUpgrade(new UpgradeToolBuilder());
		}
	}

	@Override
	public void handleNetworkMessage(NetHandler source, boolean isClient, ByteArrayDataInput data) {
		
	}
}
