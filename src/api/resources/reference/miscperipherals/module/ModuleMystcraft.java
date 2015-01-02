package miscperipherals.module;

import miscperipherals.core.MiscPeripherals;
import miscperipherals.core.Module;
import miscperipherals.upgrade.UpgradeMystcraft;
import net.minecraft.network.packet.NetHandler;
import net.minecraft.world.World;

import com.google.common.io.ByteArrayDataInput;

public class ModuleMystcraft extends Module {
	public static boolean enableMystcraft = true;
	
	@Override
	public void onPreInit() {
		enableMystcraft = MiscPeripherals.instance.settings.get("features", "enableMystcraft", enableMystcraft, "Enable the Mystcraft turtle upgrade").getBoolean(enableMystcraft);
	}

	@Override
	public void onInit() {
		
	}

	@Override
	public void onPostInit() {
		if (enableMystcraft) {
			MiscPeripherals.registerUpgrade(new UpgradeMystcraft());
		}
	}

	@Override
	public void handleNetworkMessage(NetHandler source, boolean isClient, ByteArrayDataInput data) {
		if (isClient) return;
		
		int x = data.readInt();
		int y = data.readInt();
		int z = data.readInt();
		String sound = data.readUTF();
		
		World world = MiscPeripherals.proxy.getClientWorld();
		
		for (int i = 0; i < 50; i++) {
			MiscPeripherals.proxy.spawnSmoke(x + 0.5D + world.rand.nextFloat() - world.rand.nextFloat(), y + world.rand.nextFloat() - world.rand.nextFloat(), z + 0.5D + world.rand.nextFloat() - world.rand.nextFloat(), 0.0D, 0.0D, 0.0D);
		}
		
		world.playSoundEffect(x + 0.5D, y + 0.5D, z + 0.5D, sound, 0.8F, world.rand.nextFloat() * 0.2F + 0.9F);
	}
}
