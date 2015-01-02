package miscperipherals.module;

import miscperipherals.block.BlockMultiTile.TileData.FacingMode;
import miscperipherals.core.MiscPeripherals;
import miscperipherals.core.Module;
import miscperipherals.tile.TileMEBridge;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.NetHandler;
import appeng.api.Blocks;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class ModuleAppEng extends Module {
	public boolean enableMEBridge = true;
	
	@Override
	public void onPreInit() {
		enableMEBridge = MiscPeripherals.instance.settings.get("features", "enableMEBridge", enableMEBridge, "Enable the ME Bridge peripheral").getBoolean(enableMEBridge);
	}

	@Override
	public void onInit() {
		if (enableMEBridge) {
			MiscPeripherals.instance.blockBeta.registerTile(6).setClass(TileMEBridge.class).setSprites("meBridge_top","meBridge_top","meBridge","meBridge","meBridge","meBridge","meBridge_top_active","meBridge_top_active","meBridge_active","meBridge_active","meBridge_active","meBridge_active").setName("meBridge").setFacingMode(FacingMode.All).setInfoText(MiscPeripherals.instance.descriptive ? "This block is computer powered!" : null);
			GameRegistry.registerTileEntity(TileMEBridge.class, "MiscPeripherals ME Bridge");
			LanguageRegistry.instance().addStringLocalization("miscperipherals.meBridge.name", "ME Bridge");
			GameRegistry.addShapelessRecipe(new ItemStack(MiscPeripherals.instance.blockBeta, 1, 6), MiscPeripherals.instance.enableInteractiveSorter ? new ItemStack(MiscPeripherals.instance.blockAlpha, 1, 5) : Block.blockRedstone, Blocks.blkTerminal);
			GameRegistry.registerCustomItemStack("meBridge", new ItemStack(MiscPeripherals.instance.blockBeta, 1, 6));
		}
	}

	@Override
	public void onPostInit() {
		
	}

	@Override
	public void handleNetworkMessage(NetHandler source, boolean isClient, ByteArrayDataInput data) {
		
	}
}
