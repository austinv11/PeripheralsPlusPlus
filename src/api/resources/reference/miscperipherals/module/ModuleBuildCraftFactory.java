package miscperipherals.module;

import miscperipherals.core.Module;
import miscperipherals.upgrade.UpgradeTank;
import net.minecraft.network.packet.NetHandler;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.registry.GameRegistry;

public class ModuleBuildCraftFactory extends Module {
	@Override
	public void onPreInit() {
		
	}

	@Override
	public void onInit() {
		
	}

	@Override
	public void onPostInit() {
		UpgradeTank.CRAFTING_ITEM[1] = ModuleBuildCraftCore.getBCItem("Factory", "tankBlock");
	}

	@Override
	public void handleNetworkMessage(NetHandler source, boolean isClient, ByteArrayDataInput data) {
		
	}
}
