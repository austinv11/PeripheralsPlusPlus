package miscperipherals.module;

import miscperipherals.api.IXPSource;
import miscperipherals.core.MiscPeripherals;
import miscperipherals.core.Module;
import miscperipherals.safe.Reflector;
import miscperipherals.upgrade.UpgradeThaumScanner;
import miscperipherals.upgrade.UpgradeWand;
import net.minecraft.network.packet.NetHandler;
import net.minecraft.tileentity.TileEntity;

import com.google.common.io.ByteArrayDataInput;

import dan200.turtle.api.ITurtleAccess;

public class ModuleThaumcraft extends Module {
	public static boolean enableThaumScanner = true;
	public static boolean enableWand = true;
	
	@Override
	public void onPreInit() {
		enableThaumScanner = MiscPeripherals.instance.settings.get("features", "enableThaumScanner", enableThaumScanner, "Enable the Thaum Scanner turtle upgrade").getBoolean(enableThaumScanner);
		enableWand = MiscPeripherals.instance.settings.get("features", "enableWand", enableWand, "Enable the Wand turtle upgrade").getBoolean(enableWand);
	}

	@Override
	public void onInit() {
		
	}

	@Override
	public void onPostInit() {		
		if (enableThaumScanner) {
			MiscPeripherals.registerUpgrade(new UpgradeThaumScanner());
		}
		
		if (enableWand) {
			MiscPeripherals.registerUpgrade(new UpgradeWand());
		}
		
		IXPSource.handlers.add(new IXPSource() {
			@Override
			public int get(ITurtleAccess turtle, int x, int y, int z, int side) {
				TileEntity te = turtle.getWorld().getBlockTileEntity(x, y, z);
				if (!te.getClass().getName().equals("thaumcraft.common.blocks.jars.TileJarBrain")) return 0;
				
				Integer xp = Reflector.getField(te, "xp", Integer.class);
				if (xp != null) {
					Reflector.setField(te, "xp", 0);
					return xp;
				} else return 0;
			}
		});
	}

	@Override
	public void handleNetworkMessage(NetHandler source, boolean isClient, ByteArrayDataInput data) {
		
	}
}
