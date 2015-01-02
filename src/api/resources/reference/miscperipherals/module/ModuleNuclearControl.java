package miscperipherals.module;

import miscperipherals.core.MiscPeripherals;
import miscperipherals.core.Module;
import miscperipherals.safe.ReflectionStore;
import miscperipherals.tile.TileNuclearReader;
import miscperipherals.tile.TileNuclearReaderT2;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.NetHandler;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class ModuleNuclearControl extends Module {
	public static boolean enableNuclearReader = true;
	public static boolean enableNuclearReaderT2 = true;
	
	@Override
	public void onPreInit() {
		enableNuclearReader = MiscPeripherals.instance.settings.get("features", "enableNuclearReader", enableNuclearReader, "Enable the Nuclear Information Reader peripheral").getBoolean(enableNuclearReader);
		enableNuclearReaderT2 = MiscPeripherals.instance.settings.get("features", "enableNuclearReaderT2", enableNuclearReaderT2, "Enable the Advanced Nuclear Information Reader peripheral").getBoolean(enableNuclearReaderT2);
	}

	@Override
	public void onInit() {
		if (enableNuclearReader) {
			MiscPeripherals.instance.blockAlpha.registerTile(8).setClass(TileNuclearReader.class).setSprites("nuclearReader").setName("nuclearReader").setInfoText(MiscPeripherals.instance.descriptive ? "This block is computer powered!" : null);
			GameRegistry.registerTileEntity(TileNuclearReader.class, "MiscPeripherals Nuclear Reader");
			LanguageRegistry.instance().addStringLocalization("miscperipherals.nuclearReader.name", "Nuclear Information Reader");
			GameRegistry.addRecipe(new ItemStack(MiscPeripherals.instance.blockAlpha, 1, 8), "GEG", "#@#", "GRG", 'G', Item.ingotGold, 'E', Item.eyeOfEnder, 'R', Item.redstone, '@', (ReflectionStore.blockNuclearControlMain == null || ReflectionStore.damageInfoPanel == null) ? Block.blockIron : new ItemStack(ReflectionStore.blockNuclearControlMain, 1, ReflectionStore.damageInfoPanel), '#', (ReflectionStore.itemUpgrade == null || ReflectionStore.damageRange == null) ? Item.ingotGold : new ItemStack(ReflectionStore.itemUpgrade, 1, ReflectionStore.damageRange));
			GameRegistry.registerCustomItemStack("nuclearReader", new ItemStack(MiscPeripherals.instance.blockAlpha, 1, 8));
		}
		
		if (enableNuclearReaderT2) {
			MiscPeripherals.instance.blockAlpha.registerTile(9).setClass(TileNuclearReaderT2.class).setSprites("nuclearReader_t2").setName("nuclearReaderT2").setInfoText(MiscPeripherals.instance.descriptive ? "This block is computer powered!" : null);
			GameRegistry.registerTileEntity(TileNuclearReaderT2.class, "MiscPeripherals Nuclear Reader T2");
			LanguageRegistry.instance().addStringLocalization("miscperipherals.nuclearReaderT2.name", "Advanced Nuclear Information Reader");
			ItemStack stack = new ItemStack(Item.netherStar);
			stack.setItemName("Bane of Mead");
			stack.addEnchantment(Enchantment.sharpness, 7);
			stack.addEnchantment(Enchantment.thorns, 5);
			stack.addEnchantment(Enchantment.fireAspect, 10);
			for (Object o : new Object[] {Block.chest, stack})
				GameRegistry.addRecipe(new ItemStack(MiscPeripherals.instance.blockAlpha, 1, 9), "D#D", "@*@", "D@D", 'D', Item.diamond, '#', enableNuclearReader ? new ItemStack(MiscPeripherals.instance.blockAlpha, 1, 8) : Block.blockDiamond, '*', o, '@', (ReflectionStore.blockNuclearControlMain == null || ReflectionStore.damageInfoPanelExtender == null) ? Item.ingotGold : new ItemStack(ReflectionStore.blockNuclearControlMain, 1, ReflectionStore.damageInfoPanelExtender));
			GameRegistry.registerCustomItemStack("nuclearReaderT2", new ItemStack(MiscPeripherals.instance.blockAlpha, 1, 9));
		}
	}

	@Override
	public void onPostInit() {
		ReflectionStore.initIC2NuclearControl();
	}

	@Override
	public void handleNetworkMessage(NetHandler source, boolean isClient, ByteArrayDataInput data) {
		
	}
}
