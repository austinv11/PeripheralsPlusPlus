package miscperipherals.module;

import miscperipherals.core.Module;
import miscperipherals.upgrade.UpgradeGrafter;
import miscperipherals.upgrade.UpgradeScoop;
import net.minecraft.network.packet.NetHandler;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.registry.GameRegistry;

public class ModuleThaumicBees extends Module {
	@Override
	public void onPreInit() {
		
	}

	@Override
	public void onInit() {
		
	}

	@Override
	public void onPostInit() {
		UpgradeGrafter.registerGrafter(GameRegistry.findItemStack("ThaumicBees", "thaumiumGrafter", 1));
		UpgradeScoop.registerScoop(GameRegistry.findItemStack("ThaumicBees", "thaumiumScoop", 1));
	}

	@Override
	public void handleNetworkMessage(NetHandler source, boolean isClient, ByteArrayDataInput data) {
		
	}
}
