package miscperipherals.module;

import miscperipherals.core.MiscPeripherals;
import miscperipherals.core.Module;
import miscperipherals.safe.ReflectionStore;
import miscperipherals.upgrade.UpgradeWirelessRedstone;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.NetHandler;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class ModuleWRCBE extends Module {
	public static boolean enableREther = true;

	@Override
	public void onPreInit() {
		enableREther = MiscPeripherals.instance.settings.get("features", "enableREther", enableREther, "Enable the REther turtle upgrade").getBoolean(enableREther);
	}

	@Override
	public void onInit() {
		if (enableREther) {
			MiscPeripherals.instance.itemAlpha.registerItem(2, "rether", "retherModule");
			LanguageRegistry.instance().addStringLocalization("miscperipherals.retherModule.name", "REther Module");
			GameRegistry.registerCustomItemStack("retherModule", new ItemStack(MiscPeripherals.instance.itemAlpha, 1, 2));
		}
	}

	@Override
	public void onPostInit() {
		ReflectionStore.initWRCBECore();
		
		if (enableREther) {
			GameRegistry.addShapelessRecipe(new ItemStack(MiscPeripherals.instance.itemAlpha, 1, 2), ReflectionStore.recieverDish == null ? Item.redstoneRepeater : ReflectionStore.recieverDish, ReflectionStore.obsidianStick == null ? Item.stick : ReflectionStore.obsidianStick, ReflectionStore.wirelessTransceiver == null ? Block.torchRedstoneActive : ReflectionStore.wirelessTransceiver);
			
			MiscPeripherals.registerUpgrade(new UpgradeWirelessRedstone());
		}
	}

	@Override
	public void handleNetworkMessage(NetHandler source, boolean isClient, ByteArrayDataInput data) {
		
	}
}
