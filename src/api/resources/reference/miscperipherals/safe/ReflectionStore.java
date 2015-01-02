package miscperipherals.safe;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ReflectionStore {	
	// EE3
	public static Item miniumStone;
	public static Item philStone;
	public static Object actionResultSuccess;
	
	// Additional Pipes
	public static Block blockChunkLoader;
	public static Block blockChunkLoaderB;
	
	// ChickenChunks
	public static Block blockChunkLoaderC;
	
	// EnderStorage
	public static Block blockEnderChest;
	
	// Steve's Carts
	public static Item modules;
	public static Class moduleDataHull;
	public static Block blockDetector;
	public static Block blockAdvDetector;
	
	// Nuclear Control
	public static Integer damageInfoPanel;
	public static Integer damageInfoPanelExtender;
	public static Integer damageRange;
	public static Block blockNuclearControlMain;
	public static Item itemUpgrade;
	
	// Advanced Solar Panel
	public static Block blockAdvSolarPanel;
	public static Integer advGenDay;
	public static Integer hGenDay;
	public static Integer uhGenDay;
	public static Integer advGenNight;
	public static Integer hGenNight;
	public static Integer uhGenNight;
	
	// WRCBE
	public static Item recieverDish;
	public static Item obsidianStick;
	public static Item wirelessTransceiver;
	
	// MFFS
	public static Integer forcefieldtransportcost;
	public static Object eb;
	public static Object ffb;
	public static Object oss;
	public static Item MFFSitemcardempty;
	public static Item MFFSItemIDCard;
	public static Item MFFSitemWrench;
	
	// Factorization
	public static ItemStack barrel_item;
	
	// Multi Page Chest
	public static Block chestBlock;
	
	public static void initEE3() {
		miniumStone = Reflector.getField("com.pahimar.ee3.item.ModItems", "miniumStone", Item.class);
		philStone = Reflector.getField("com.pahimar.ee3.item.ModItems", "philStone", Item.class);
		actionResultSuccess = Reflector.getField("com.pahimar.ee3.event.ActionEvent$ActionResult", "SUCCESS", Object.class);
	}
	
	public static void initAPUnofficial() {
		Object instance = Reflector.getField("buildcraft.additionalpipes.AdditionalPipes", "instance", Object.class);
		if (instance != null) {
			blockChunkLoader = Reflector.getField(instance, "blockChunkLoader", Block.class);
		}
	}
	
	public static void initAdditionalPipes() {
		blockChunkLoaderB = Reflector.getField("net.kyprus.additionalpipes.AdditionalPipes", "blockChunkLoader", Block.class);
	}
	
	public static void initChickenChunks() {
		blockChunkLoaderC = Reflector.getField("codechicken.chunkloader.ChickenChunks", "blockChunkLoader", Block.class);
	}
	
	public static void initEnderStorage() {
		blockEnderChest = Reflector.getField("codechicken.enderstorage.EnderStorage", "blockEnderChest", Block.class);
	}

	public static void initStevesCarts() {
		modules = Reflector.getField("vswe.stevescarts.StevesCarts", "modules", Item.class);
		moduleDataHull = Reflector.getClass("vswe.stevescarts.ModuleData.ModuleDataHull");
		
		Object instance = Reflector.getField("vswe.stevescarts.StevesCarts", "instance", Object.class);
		if (instance != null) {
			blockDetector = Reflector.getField(instance, "blockDetector", Block.class);
			blockAdvDetector = Reflector.getField(instance, "blockAdvDetector", Block.class);
		}
	}
	
	public static void initIC2NuclearControl() {
		damageInfoPanel = Reflector.getField("shedar.mods.ic2.nuclearcontrol.BlockNuclearControlMain", "DAMAGE_INFO_PANEL", Integer.class);
		damageInfoPanelExtender = Reflector.getField("shedar.mods.ic2.nuclearcontrol.BlockNuclearControlMain", "DAMAGE_INFO_PANEL_EXTENDER", Integer.class);
		damageRange = Reflector.getField("shedar.mods.ic2.nuclearcontrol.items.ItemUpgrade", "DAMAGE_RANGE", Integer.class);
		
		Object instance = Reflector.getField("shedar.mods.ic2.nuclearcontrol.IC2NuclearControl", "instance", Object.class);
		if (instance != null) {
			blockNuclearControlMain = Reflector.getField(instance, "blockNuclearControlMain", Block.class);
			itemUpgrade = Reflector.getField(instance, "itemUpgrade", Item.class);
		}
	}

	public static void initAdvancedSolarPanel() {
		blockAdvSolarPanel = Reflector.getField("advsolar.AdvancedSolarPanel", "blockAdvSolarPanel", Block.class);
		advGenDay = Reflector.getField("advsolar.AdvancedSolarPanel", "advGenDay", Integer.class);
		hGenDay = Reflector.getField("advsolar.AdvancedSolarPanel", "hGenDay", Integer.class);
		uhGenDay = Reflector.getField("advsolar.AdvancedSolarPanel", "uhGenDay", Integer.class);
		advGenNight = Reflector.getField("advsolar.AdvancedSolarPanel", "advGenNight", Integer.class);
		hGenNight = Reflector.getField("advsolar.AdvancedSolarPanel", "hGenNight", Integer.class);
		uhGenNight = Reflector.getField("advsolar.AdvancedSolarPanel", "uhGenNight", Integer.class);
	}

	public static void initWRCBECore() {
		recieverDish = Reflector.getField("codechicken.wirelessredstone.core.WirelessRedstoneCore", "recieverDish", Item.class);
		wirelessTransceiver = Reflector.getField("codechicken.wirelessredstone.core.WirelessRedstoneCore", "wirelessTransceiver", Item.class);
		obsidianStick = Reflector.getField("codechicken.wirelessredstone.core.WirelessRedstoneCore", "obsidianStick", Item.class);
	}

	public static void initModularForceFieldSystem() {
		forcefieldtransportcost = Reflector.getField("mods.mffs.common.ModularForceFieldSystem", "forceFieldTransportCost", Integer.class);
		eb = Reflector.getField("mods.mffs.common.SecurityRight", "EB", Object.class);
		ffb = Reflector.getField("mods.mffs.common.SecurityRight", "FFB", Object.class);
		oss = Reflector.getField("mods.mffs.common.SecurityRight", "OSS", Object.class);
		MFFSitemcardempty = Reflector.getField("mods.mffs.common.ModularForceFieldSystem", "MFFSitemcardempty", Item.class);
		MFFSItemIDCard = Reflector.getField("mods.mffs.common.ModularForceFieldSystem", "MFFSItemIDCard", Item.class);
		MFFSitemWrench = Reflector.getField("mods.mffs.common.ModularForceFieldSystem", "MFFSitemWrench", Item.class);
	}
	
	public static void initFactorization() {
		Object registry = Reflector.getField("factorization.common.Core", "registry", Object.class);
		if (registry != null) {
			barrel_item = Reflector.getField(registry, "barrel_item", ItemStack.class);
		}
	}
	
	public static void initMultiPageChest() {
		chestBlock = Reflector.getField("cubex2.mods.multipagechest.MultiPageChest", "chestBlock", Block.class);
	}
}
