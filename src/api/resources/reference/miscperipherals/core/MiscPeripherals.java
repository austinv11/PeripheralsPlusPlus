package miscperipherals.core;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import miscperipherals.api.MiscPeripheralsAPI;
import miscperipherals.block.BlockLanCable;
import miscperipherals.block.BlockMultiTile;
import miscperipherals.block.BlockMultiTile.TileData;
import miscperipherals.block.BlockMultiTile.TileData.FacingMode;
import miscperipherals.block.ItemBlockMultiTile;
import miscperipherals.entity.EntityMinecartData;
import miscperipherals.item.ItemCart;
import miscperipherals.item.ItemMulti;
import miscperipherals.item.ItemSmartHelmet;
import miscperipherals.network.GuiHandler;
import miscperipherals.network.TinyPacketHandler;
import miscperipherals.render.Icons;
import miscperipherals.tile.TileAccelerator;
import miscperipherals.tile.TileChargeStation;
import miscperipherals.tile.TileChargeStationT2;
import miscperipherals.tile.TileChargeStationT3;
import miscperipherals.tile.TileChargeStationT4;
import miscperipherals.tile.TileChatBox;
import miscperipherals.tile.TileCrafter;
import miscperipherals.tile.TileEnergyMeter;
import miscperipherals.tile.TileFireworks;
import miscperipherals.tile.TileInteractiveSorter;
import miscperipherals.tile.TileNote;
import miscperipherals.tile.TilePlayerDetector;
import miscperipherals.tile.TileRailReader;
import miscperipherals.tile.TileRedstoneBox;
import miscperipherals.tile.TileResupplyStation;
import miscperipherals.tile.TileSmSender;
import miscperipherals.tile.TileSpeaker;
import miscperipherals.tile.TileTeleporter;
import miscperipherals.tile.TileTeleporterT2;
import miscperipherals.upgrade.UpgradeAccelerator;
import miscperipherals.upgrade.UpgradeAnvil;
import miscperipherals.upgrade.UpgradeChatBox;
import miscperipherals.upgrade.UpgradeChest;
import miscperipherals.upgrade.UpgradeChunkLoader;
import miscperipherals.upgrade.UpgradeCompass;
import miscperipherals.upgrade.UpgradeEnergyMeter;
import miscperipherals.upgrade.UpgradeFeeder;
import miscperipherals.upgrade.UpgradeFireworks;
import miscperipherals.upgrade.UpgradeInventory;
import miscperipherals.upgrade.UpgradeNote;
import miscperipherals.upgrade.UpgradeRTG;
import miscperipherals.upgrade.UpgradeResupply;
import miscperipherals.upgrade.UpgradeShears;
import miscperipherals.upgrade.UpgradeSignReader;
import miscperipherals.upgrade.UpgradeSpeaker;
import miscperipherals.upgrade.UpgradeTank;
import miscperipherals.upgrade.UpgradeXP;
import miscperipherals.util.CCItems;
import miscperipherals.util.ShapedRecipesExt;
import miscperipherals.util.SmallNetHelper;
import miscperipherals.util.Util;
import net.minecraft.block.Block;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.FingerprintWarning;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.Mod.ServerStarting;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLFingerprintViolationEvent;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.common.versioning.ArtifactVersion;
import cpw.mods.fml.common.versioning.VersionParser;
import cpw.mods.fml.relauncher.RelaunchClassLoader;
import cpw.mods.fml.relauncher.Side;
import dan200.turtle.api.ITurtleUpgrade;
import dan200.turtle.api.TurtleAPI;

@Mod(
		modid = "MiscPeripherals",
		name = "MiscPeripherals",
		version = "3.3",
		dependencies = MiscPeripherals.DEBUG ? "after:ComputerCraft;after:CCTurtle;after:GregTech_Addon" : "required-after:ComputerCraft;required-after:CCTurtle;after:GregTech_Addon" // >_<
		//certificateFingerprint = "ffd5cabbd067245b7d76d99178c8b2e06c4c0ca2"
	)
@NetworkMod(clientSideRequired = true, tinyPacketHandler = TinyPacketHandler.class)
public class MiscPeripherals {
	@Instance("MiscPeripherals")
	public static MiscPeripherals instance;
	@SidedProxy(clientSide = "miscperipherals.core.ProxyClient", serverSide = "miscperipherals.core.Proxy")
	public static Proxy proxy;
	@SidedProxy(clientSide = "miscperipherals.network.GuiHandlerClient", serverSide = "miscperipherals.network.GuiHandler")
	public static GuiHandler guiHandler;
	public static Logger log = Logger.getLogger("MiscPeripherals");
	public static String ccVersion = null;
	
	public Configuration settings;
	public CreativeTabs tabMiscPeripherals = new CreativeTabMiscPeripherals();
	public Map<String, Module> modules = new HashMap<String, Module>();
	public BlockMultiTile blockAlpha;
	public BlockMultiTile blockBeta;
	public BlockLanCable blockLanCable;
	public ItemMulti itemAlpha;
	public ItemSmartHelmet itemSmartHelmet;
	public ItemCart itemCart;
	
	public boolean enableChargeStation = true;
	public boolean enableChargeStationT2 = true;
	public boolean enableChargeStationT3 = true;
	public boolean enableChargeStationT4 = true;
	public boolean enableNote = true;
	public boolean enableChunkLoader = true;
	public boolean enableFeeder = true;
	public boolean enableResupply = true;
	public boolean enableShears = true;
	public boolean enableCompass = true;
	public boolean enableXP = true;
	public boolean enableInventory = true;
	public boolean enableInteractiveSorter = true;
	public boolean enableSignReader = true;
	public boolean enableRailReader = true;
	public boolean enableRTG = true;
	public boolean enableTank = true;
	public boolean enableTeleporter = true;
	public boolean enableTeleporterT2 = true;
	public boolean enablePlayerDetector = true;
	public boolean enableCrafter = true;
	public boolean enableAccelerator = true;
	public boolean enableFireworkLauncher = true;
	public boolean enableChatBox = true;
	public boolean enableSpeaker = true;
	public boolean enableChest = true;
	public boolean enableSmSender = true;
	public boolean enableEnergyMeter = true;
	public boolean enableRedstoneBox = true;
	public boolean enableDataCart = true;
	
	public int fuelMJ = 20;
	public int fuelEU = 50;
	public int itemIDSeed = 0;
	public boolean sideSensitive = false;
	public int chunkLoaderRadius = 0;
	public double teleporterPenalty = 2.0D;
	public boolean descriptive = false;
	public boolean chargeStationMultiCharge = true;
	public String speechProvider = "";
	
	public int chatSayRange = 64;
	public boolean chatSayUnlimitedVertical = true;
	public int chatSayRate = 1;
	public double chatReadRange = -1;
	public boolean chatColorCodes = false;
	public boolean chatLogCoords = true;
	
	public static final boolean DEBUG = false;
	
	public MiscPeripherals() {
		instance = this;
	}
	
	@PreInit
	public void onLoad(FMLPreInitializationEvent event) {
		log = event.getModLog();
		ModContainer cc = Loader.instance().getIndexedModList().get("ComputerCraft");
		if (cc != null) ccVersion = cc.getDisplayVersion();
		((RelaunchClassLoader) getClass().getClassLoader()).registerTransformer("miscperipherals.asm.ImplementIfLoadedTransformer");
		new EventHandler();
		
		MiscPeripheralsAPI.instance = new APICallHandler();
		
		LanguageRegistry lang = LanguageRegistry.instance();
		
		settings = new Configuration(new File(new File(proxy.getMinecraftFolder(), "config"), "MiscPeripherals.cfg"));
		
		int blockAlphaId = settings.getBlock("alpha", 2671, "ID for the first block").getInt();		
		int blockBetaId = settings.getBlock("beta", blockAlphaId + 1, "ID for the second block").getInt();
		//int blockLanCableId = settings.getBlock("lanCable", blockBetaId + 1, "ID for the LAN Cable block").getInt();
		int itemAlphaId = settings.getItem("alpha", 26454, "ID for the first item").getInt();
		int itemSmartHelmetId = settings.getItem("smartHelmet", itemAlphaId + 3, "ID for the Smart Helmet item (set to 0 to disable)").getInt();
		int itemCartId = settings.getItem("cart", itemAlphaId + 4, "ID for the minecart item").getInt();
		
		enableChargeStation = settings.get("features", "enableChargeStation", enableChargeStation, "Enable the Charge Station block").getBoolean(enableChargeStation);
		enableChargeStationT2 = settings.get("features", "enableChargeStationT2", enableChargeStationT2, "Enable the Advanced Charge Station").getBoolean(enableChargeStationT2);
		enableChargeStationT3 = settings.get("features", "enableChargeStationT3", enableChargeStationT3, "Enable the Lapotronic Charge Station").getBoolean(enableChargeStationT3);
		enableChargeStationT4 = settings.get("features", "enableChargeStationT4", enableChargeStationT4, "Enable the Ultimate Charge Station").getBoolean(enableChargeStationT4);
		enableNote = settings.get("features", "enableNote", enableNote, "Enable the Iron Note Block peripheral and turtle upgrade").getBoolean(enableNote);
		enableChunkLoader = settings.get("features", "enableChunkLoader", enableNote, "Enable the Chunk Loader turtle upgrade").getBoolean(enableChunkLoader);
		enableFeeder = settings.get("features", "enableFeeder", enableFeeder, "Enable the Feeder turtle upgrade").getBoolean(enableFeeder);
		enableResupply = settings.get("features", "enableResupply", enableResupply, "Enable the Resupply Station and Resupply turtle upgrade").getBoolean(enableResupply);
		enableShears = settings.get("features", "enableShears", enableShears, "Enable the Shears turtle upgrade").getBoolean(enableShears);
		enableCompass = settings.get("features", "enableCompass", enableCompass, "Enable the Compass turtle upgrade").getBoolean(enableCompass);
		enableXP = settings.get("features", "enableXP", enableXP, "Enable the XP and Anvil turtle upgrades").getBoolean(enableXP);
		enableInventory = settings.get("features", "enableInventory", enableInventory, "Enable the Inventory turtle upgrade").getBoolean(enableInventory);
		enableInteractiveSorter = settings.get("features", "enableInteractiveSorter", enableInteractiveSorter, "Enable the Interactive Sorter peripheral").getBoolean(enableInteractiveSorter);
		enableSignReader = settings.get("features", "enableSignReader", enableSignReader, "Enable the Sign Reader turtle upgrade").getBoolean(enableSignReader);
		enableRailReader = settings.get("features", "enableRailReader", enableRailReader, "Enable the Rail Reader peripheral").getBoolean(enableRailReader);
		enableTank = settings.get("features", "enableTank", enableTank, "Enable the Tank turtle upgrade").getBoolean(enableTank);
		enableRTG = settings.get("features", "enableRTG", enableRTG, "Enable the RTG turtle upgrade").getBoolean(enableRTG);
		enableTeleporter = settings.get("features", "enableTeleporter", enableTeleporter, "Enable the Turtle Teleporter").getBoolean(enableTeleporter);
		enableTeleporterT2 = settings.get("features", "enableTeleporterT2", enableTeleporterT2, "Enable the Advanced Turtle Teleporter").getBoolean(enableTeleporterT2);
		enablePlayerDetector = settings.get("features", "enablePlayerDetector", enablePlayerDetector, "Enable the Player Detector peripheral").getBoolean(enablePlayerDetector);
		enableCrafter = settings.get("features", "enableCrafter", enableCrafter, "Enable the Computer Controlled Crafter peripheral").getBoolean(enableCrafter);
		enableAccelerator = settings.get("features", "enableAccelerator", enableAccelerator, "Enable the Hardware Accelerator peripheral").getBoolean(enableAccelerator);
		enableFireworkLauncher = settings.get("features", "enableFireworkLauncher", enableFireworkLauncher, "Enable the Firework Launcher peripheral").getBoolean(enableFireworkLauncher);
		enableChatBox = settings.get("features", "enableChatBox", enableChatBox, "Enable the Chat Box peripheral and turtle upgrade").getBoolean(enableChatBox);
		enableSpeaker = settings.get("features", "enableSpeaker", enableSpeaker, "Enable the Speaker peripheral and turtle upgrade").getBoolean(enableSpeaker);
		enableChest = settings.get("features", "enableChest", enableChest, "Enable the Chest turtle upgrade").getBoolean(enableChest);
		enableSmSender = settings.get("features", "enableSmSender", enableSmSender, "Enable the Smallnet Sender peripheral").getBoolean(enableSmSender);
		enableEnergyMeter = settings.get("features", "enableEnergyMeter", enableEnergyMeter, "Enable the Energy Meter peripheral and turtle upgrade").getBoolean(enableEnergyMeter);
		enableRedstoneBox = settings.get("features", "enableRedstoneBox", enableRedstoneBox, "Enable the Redstone Box peripheral").getBoolean(enableRedstoneBox);
		enableDataCart = settings.get("features", "enableDataCart", enableDataCart, "Enable the Data Cart").getBoolean(enableDataCart);
		
		fuelEU = settings.get("general", "fuelEU", fuelEU, "Amount of EU per turtle fuel value. Coal burned in a generator gives 50 EU per fuel value").getInt();
		fuelMJ = settings.get("general", "fuelMJ", fuelMJ, "Amount of MJ per turtle fuel value. Coal burned in a stirling engine gives 20 MJ per fuel value").getInt();
		/*****/
		String seed = settings.get("general", "itemIDSeed", "", "Seed used to encode item IDs given by peripherals, works similarly to a regular map seed. Item IDs are not encoded if empty").getString().trim();
		if (seed.isEmpty()) itemIDSeed = 0;
		else {
			try {
				itemIDSeed = Integer.parseInt(seed);
			} catch (NumberFormatException e) {
				itemIDSeed = seed.hashCode();
			}
		}
		/****/
		sideSensitive = settings.get("general", "sideSensitive", sideSensitive, "EXPERIMENTAL: If set to true, some turtle upgrades get more picky about the side they are placed on, for example, Solar Turtles needing the block the solar panel is facing to see the sun, instead of the turtle itself").getBoolean(sideSensitive);
		chunkLoaderRadius = settings.get("general", "chunkLoaderRadius", chunkLoaderRadius, "Radius of chunks the Chunk Loader peripheral will keep loaded. 0 for 1x1, 1 for 3x3, 2 for 5x5, etc. The maximum allowed by the default chunkloading settings is 5x5, edit config/forgeChunkLoading.cfg to change those.").getInt();
		teleporterPenalty = settings.get("general", "teleporterPenalty", teleporterPenalty, "Fuel penalty for using a Turtle Teleporter. For example, 1.0 is a 0% penalty, or 2.0 (default) is a 100% penalty").getDouble(teleporterPenalty);
		descriptive = settings.get("general", "descriptive", descriptive, "Make stuff a little bit more descriptive, prevents confusion").getBoolean(descriptive);
		chargeStationMultiCharge = settings.get("general", "chargeStationMultiCharge", chargeStationMultiCharge, "Allow advanced charge stations to charge multiple turtles").getBoolean(chargeStationMultiCharge);
		speechProvider = settings.get("general", "speechProvider", speechProvider, "Preferred speech provider for the Speaker peripheral, leave blank for automatic. Supported: windows osx espeak festival pico2wave").getString().trim();
		
		chatSayRange = settings.get("chatBox", "sayRange", chatSayRange, "Range for the Chat Box peripheral's say function. Negative values indicate infinite, always infinite on singleplayer").getInt();
		chatReadRange = settings.get("chatBox", "readRange", chatReadRange, "Range for the Chat Box peripheral's reading. Negative values indicate infinite, always infinite on singleplayer").getInt();
		chatColorCodes = settings.get("chatBox", "colorCodes", chatColorCodes, "Enable color codes on the Chat Box peripheral's say function").getBoolean(chatColorCodes);
		chatSayUnlimitedVertical = settings.get("chatBox", "sayUnlimitedVertical", chatSayUnlimitedVertical, "Allow the Chat Box peripheral to send messages with unlimited vertical distance, but only if so the program chooses").getBoolean(chatColorCodes);
		chatSayRate = settings.get("chatBox", "sayRate", chatSayRate, "Maximum number of messages per second a Chat Box peripheral can say").getInt();
		chatLogCoords = settings.get("chatBox", "logCoords", chatLogCoords, "Log the Chat Box peripheral's coordinates when it says a message").getBoolean(chatLogCoords); 
		
		settings.save();
		
		blockAlpha = new BlockMultiTile(blockAlphaId);
		GameRegistry.registerBlock(blockAlpha, ItemBlockMultiTile.class, "miscperipherals.blockAlpha");
		
		blockBeta = new BlockMultiTile(blockBetaId);
		GameRegistry.registerBlock(blockBeta, ItemBlockMultiTile.class, "miscperipherals.blockBeta");
		
		/*blockLanCable = new BlockLanCable(blockLanCableId);
		GameRegistry.registerBlock(blockLanCable, ItemBlockLanCable.class, "miscperipherals.lanCable");*/
		
		itemAlpha = new ItemMulti(itemAlphaId);
		GameRegistry.registerItem(itemAlpha, "miscperipherals.itemAlpha");
		
		if (itemSmartHelmetId > 0) {
			itemSmartHelmet = new ItemSmartHelmet(itemSmartHelmetId);
			GameRegistry.registerItem(itemSmartHelmet, "miscperipherals.itemSmartHelmet");
		}
		
		itemCart = new ItemCart(itemCartId);
		GameRegistry.registerItem(itemCart, "miscperipherals.itemCart");
		
		loadModule("IC2@[1.112,)", "IC2");
		loadModule("IC2NuclearControl", "NuclearControl");
		loadModule("ModularForceFieldSystem@[2.0.0.0.0,3.0.0)", "MFFS");
		loadModule("GregTech_Addon", "GregTech");
		loadModule("AdvancedSolarPanel", "AdvancedSolarPanel");
		loadModule("CompactSolars", "CompactSolars");
		
		loadModule("Forestry@[2.2.4.0,)", "Forestry");
		
		loadModule("AdditionalPipes", "AdditionalPipes");
		loadModule("APUnofficial", "APUnofficial");
		
		loadModule("Railcraft", "Railcraft");
		
		loadModule("StevesCarts", "StevesCarts");
		
		loadModule("ChickenChunks", "ChickenChunks");
		
		loadModule("BuildCraft|Core", "BuildCraftCore");
		loadModule("BuildCraft|Factory", "BuildCraftFactory");
		loadModule("BuildCraft|Transport", "BuildCraftTransport");
		
		loadModule("EnderStorage", "EnderStorage");
		
		loadModule("EE3", "EE3");
		
		loadModule("WR-CBE|Core", "WRCBE");
		
		loadModule("factorization", "Factorization");
		
		loadModule("Thaumcraft", "Thaumcraft");
		
		loadModule("ThermalExpansion", "ThermalExpansion");
		
		//loadModule("Mystcraft", "Mystcraft");
		
		loadModule("PortalGun", "PortalGun");
		
		loadModule("AppliedEnergistics", "AppEng");
		
		loadModule("OmniTools", "OmniTools");
		
		loadModule("MultiPageChest", "MultiPageChest");
		
		loadModule("TConstruct", "TConstruct");
		
		for (Module module : modules.values()) module.onPreInit();
	}
	
	@Init
	public void onLoading(FMLInitializationEvent event) {
		LanguageRegistry lang = LanguageRegistry.instance();
		
		if (enableChargeStation) {
			blockAlpha.registerTile(0).setClass(TileChargeStation.class).setSprites("generic","generic","generic","charge","generic","generic").setName("chargeStation").setFacingMode(FacingMode.All).setInfoText(MiscPeripherals.instance.descriptive ? "This block is used by turtles!" : null);
			GameRegistry.registerTileEntity(TileChargeStation.class, "MiscPeripherals Charge Station");
			LanguageRegistry.instance().addStringLocalization("miscperipherals.chargeStation.name", "Charge Station");
			GameRegistry.registerCustomItemStack("chargeStation", new ItemStack(blockAlpha, 1, 0));
		}
		
		if (enableChargeStationT2) {
			TileData data = blockBeta.registerTile(1).setClass(TileChargeStationT2.class).setName("chargeStationT2").setFacingMode(FacingMode.All).setInfoText(MiscPeripherals.instance.descriptive ? "This block is used by turtles!" : null);
			if (chargeStationMultiCharge) data.setSprites("charge_t2_side","charge_t2_side","charge_t2","charge_t2","charge_t2_side","charge_t2_side");
			else data.setSprites("charge_t2_side","charge_t2_side","charge_t2_side","charge_t2","charge_t2_side","charge_t2_side");
			GameRegistry.registerTileEntity(TileChargeStationT2.class, "MiscPeripherals Charge Station T2");
			LanguageRegistry.instance().addStringLocalization("miscperipherals.chargeStationT2.name", "Advanced Charge Station");
			GameRegistry.registerCustomItemStack("chargeStationT2", new ItemStack(blockBeta, 1, 1));
		}
		
		if (enableChargeStationT3) {
			TileData data = blockBeta.registerTile(2).setClass(TileChargeStationT3.class).setName("chargeStationT3").setFacingMode(FacingMode.All).setInfoText(MiscPeripherals.instance.descriptive ? "This block is used by turtles!" : null);
			if (chargeStationMultiCharge) data.setSprites("charge_t3_side","charge_t3_side","charge_t3","charge_t3","charge_t3","charge_t3");
			else data.setSprites("charge_t3_side","charge_t3_side","charge_t3_side","charge_t3","charge_t3_side","charge_t3_side");
			GameRegistry.registerTileEntity(TileChargeStationT3.class, "MiscPeripherals Charge Station T3");
			LanguageRegistry.instance().addStringLocalization("miscperipherals.chargeStationT3.name", "Lapotronic Charge Station");
			GameRegistry.registerCustomItemStack("chargeStationT3", new ItemStack(blockBeta, 1, 2));
		}
		
		if (enableChargeStationT4) {
			TileData data = blockBeta.registerTile(3).setClass(TileChargeStationT4.class).setName("chargeStationT4").setFacingMode(FacingMode.All).setInfoText(MiscPeripherals.instance.descriptive ? "This block is used by turtles!" : null);
			if (chargeStationMultiCharge) data.setSprites("charge_t4");
			else data.setSprites("charge_t4_side","charge_t4_side","charge_t4_side","charge_t4","charge_t4_side","charge_t4_side");
			GameRegistry.registerTileEntity(TileChargeStationT4.class, "MiscPeripherals Charge Station T4");
			LanguageRegistry.instance().addStringLocalization("miscperipherals.chargeStationT4.name", "Ultimate Charge Station");
			GameRegistry.registerCustomItemStack("chargeStationT4", new ItemStack(blockBeta, 1, 3));
		}
		
		if (enableNote) {
			blockAlpha.registerTile(1).setClass(TileNote.class).setSprites("note").setName("ironNoteBlock").setInfoText(descriptive ? "This block is computer powered!" : null);
			GameRegistry.registerTileEntity(TileNote.class, "MiscPeripherals Iron Note Block");
			lang.addStringLocalization("miscperipherals.ironNoteBlock.name", "Iron Note Block");
			GameRegistry.addRecipe(new ItemStack(blockAlpha, 1, 1), "IGI", "RNR", "IGI", 'I', Item.ingotIron, 'G', Item.ingotGold, 'R', Item.redstone, 'N', Block.music);
			GameRegistry.registerCustomItemStack("ironNoteBlock", new ItemStack(blockAlpha, 1, 1));
		}
		
		if (enableChunkLoader) {
			itemAlpha.registerItem(4, "chunkLoader", "chunkLoaderModule");
			lang.addStringLocalization("miscperipherals.chunkLoaderModule.name", "Chunk Loader Module");
		}
		
		if (enableFeeder) {
			itemAlpha.registerItem(0, "feeder", "feedingModule");
			lang.addStringLocalization("miscperipherals.feedingModule.name", "Feeding Module");
			GameRegistry.addRecipe(new ItemStack(itemAlpha, 1, 0), "IWI", "WEW", "IWI", 'I', Item.ingotIron, 'W', Item.wheat, 'E', Item.eyeOfEnder);
			GameRegistry.registerCustomItemStack("feedingModule", new ItemStack(itemAlpha, 1, 0));
		}
		
		if (enableResupply) {
			itemAlpha.registerItem(1, "resupply", "resupplyModule");
			lang.addStringLocalization("miscperipherals.resupplyModule.name", "Resupply Module");
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemAlpha, 1, 1), "GRG", "RCR", "GRG", 'G', Item.ingotGold, 'R', Item.redstone, 'C', "MiscPeripherals$enderChest"));
			GameRegistry.registerCustomItemStack("resupplyModule", new ItemStack(itemAlpha, 1, 1));
			
			blockAlpha.registerTile(2).setClass(TileResupplyStation.class).setSprites("resupply").setName("resupplyStation").setInfoText(descriptive ? "This block is used by turtles!" : null);
			GameRegistry.registerTileEntity(TileResupplyStation.class, "MiscPeripherals Resupply Station");
			lang.addStringLocalization("miscperipherals.resupplyStation.name", "Resupply Station");
			GameRegistry.addRecipe(new ItemStack(blockAlpha, 1, 2), "IRI", "CMC", "IRI", 'I', Item.ingotIron, 'R', Item.redstone, 'C', Block.chest, 'M', new ItemStack(itemAlpha, 1, 1));
			GameRegistry.registerCustomItemStack("resupplyStation", new ItemStack(blockAlpha, 1, 2));
		}
		
		if (enableInteractiveSorter) {
			blockAlpha.registerTile(5).setClass(TileInteractiveSorter.class).setSprites("isorter","isorter_active").setName("interactiveSorter").setInfoText(descriptive ? "This block is computer powered!" : null);
			GameRegistry.registerTileEntity(TileInteractiveSorter.class, "MiscPeripherals Interactive Sorter");
			lang.addStringLocalization("miscperipherals.interactiveSorter.name", "Interactive Sorter");
			GameRegistry.addRecipe(new ItemStack(blockAlpha, 1, 5), "DPD", "PEP", "DPD", 'D', Item.diamond, 'P', Block.pistonBase, 'E', Item.eyeOfEnder);
			GameRegistry.registerCustomItemStack("interactiveSorter", new ItemStack(blockAlpha, 1, 5));
		}
		
		if (enableRailReader) {
			blockAlpha.registerTile(7).setClass(TileRailReader.class).setSprites("railReader").setName("railReader").setInfoText(descriptive ? "This block is computer powered!" : null);
			GameRegistry.registerTileEntity(TileRailReader.class, "MiscPeripherals Rail Reader");
			lang.addStringLocalization("miscperipherals.railReader.name", "Rail Reader");
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockAlpha, 1, 7), "GRG", "RER", "GRG", 'G', Item.ingotGold, 'R', "MiscPeripherals$cartDetectorRail", 'E', "MiscPeripherals$cartDetector"));
			GameRegistry.registerCustomItemStack("railReader", new ItemStack(blockAlpha, 1, 7));
		}
		
		if (enableInventory) {
			itemAlpha.registerItem(3, "inventory", "inventoryModule");
			lang.addStringLocalization("miscperipherals.inventoryModule.name", "Inventory Module");
			GameRegistry.addRecipe(new ItemStack(itemAlpha, 1, 3), "GEG", "E@E", "GEG", 'G', Item.ingotGold, 'E', Item.eyeOfEnder, '@', enableInteractiveSorter ? new ItemStack(blockAlpha, 1, 5) : Item.netherStar);
			GameRegistry.registerCustomItemStack("inventoryModule", new ItemStack(itemAlpha, 1, 3));
		}
		
		if (enableTeleporter) {
			blockAlpha.registerTile(10).setClass(TileTeleporter.class).setSprites("teleporter_side","teleporter_side","teleporter_side","teleporter","teleporter_side","teleporter_side").setName("teleporter").setFacingMode(FacingMode.All);
			GameRegistry.registerTileEntity(TileTeleporter.class, "MiscPeripherals Teleporter");
			lang.addStringLocalization("miscperipherals.teleporter.name", "Turtle Teleporter");
			GameRegistry.addRecipe(new ItemStack(blockAlpha, 1, 10), "PEP", "POP", "PEP", 'P', Item.enderPearl, 'E', Item.eyeOfEnder, 'O', Block.obsidian);
			GameRegistry.registerCustomItemStack("teleporter", new ItemStack(blockAlpha, 1, 10));
		}
		
		if (enableTeleporterT2) {
			blockBeta.registerTile(0).setClass(TileTeleporterT2.class).setSprites("teleporter_side","teleporter_side","teleporter_side","teleporter_t2","teleporter_side","teleporter_side").setName("teleporterT2").setFacingMode(FacingMode.All);
			GameRegistry.registerTileEntity(TileTeleporterT2.class, "MiscPeripherals Teleporter T2");
			lang.addStringLocalization("miscperipherals.teleporterT2.name", "Advanced Turtle Teleporter");
			GameRegistry.addRecipe(new ItemStack(blockBeta, 1, 0), "ERE", "M@M", "ERE", 'R', Item.redstone, 'E', Item.eyeOfEnder, 'M', Item.emerald, '@', enableTeleporter ? new ItemStack(blockAlpha, 1, 10) : Block.blockGold);
			GameRegistry.registerCustomItemStack("teleporterT2", new ItemStack(blockBeta, 1, 0));
		}
		
		if (enablePlayerDetector) {
			blockAlpha.registerTile(12).setClass(TilePlayerDetector.class).setSprites("playerDetector").setName("playerDetector").setInfoText(descriptive ? "This block is computer powered!" : null);
			GameRegistry.registerTileEntity(TilePlayerDetector.class, "MiscPeripherals Player Detector");
			lang.addStringLocalization("miscperipherals.playerDetector.name", "Player Detector");
			GameRegistry.addRecipe(new ItemStack(blockAlpha, 1, 12), "GRG", "EDE", "GRG", 'G', Item.ingotGold, 'E', Item.eyeOfEnder, 'D', Item.diamond, 'R', Item.redstone);
			GameRegistry.registerCustomItemStack("playerDetector", new ItemStack(blockAlpha, 1, 12));
		}
		
		if (enableCrafter) {
			blockAlpha.registerTile(13).setClass(TileCrafter.class).setSprites("crafter").setName("crafter");
			GameRegistry.registerTileEntity(TileCrafter.class, "MiscPeripherals Crafter");
			lang.addStringLocalization("miscperipherals.crafter.name", "Computer Controlled Crafter");
			GameRegistry.addRecipe(new ItemStack(blockAlpha, 1, 13), "RWR", "W@W", "RWR", 'W', Block.workbench, 'R', Item.redstone, '@', enableInteractiveSorter ? new ItemStack(blockAlpha, 1, 5) : Block.blockDiamond);
			GameRegistry.registerCustomItemStack("crafter", new ItemStack(blockAlpha, 1, 13));
		}
		
		if (enableAccelerator) {
			blockAlpha.registerTile(15).setClass(TileAccelerator.class).setSprites("accelerator").setName("accelerator").setInfoText(descriptive ? "This block is computer powered!" : null);
			GameRegistry.registerTileEntity(TileAccelerator.class, "MiscPeripherals Accelerator");
			lang.addStringLocalization("miscperipherals.accelerator.name", "Hardware Accelerator");
			GameRegistry.registerCustomItemStack("accelerator", new ItemStack(blockAlpha, 1, 15));
		}
		
		if (enableFireworkLauncher) {
			blockBeta.registerTile(4).setClass(TileFireworks.class).setSprites("fireworks").setName("fireworks").setInfoText(descriptive ? "This block is computer powered!" : null);
			GameRegistry.registerTileEntity(TileFireworks.class, "MiscPeripherals Firework Launcher");
			lang.addStringLocalization("miscperipherals.fireworks.name", "Firework Launcher");
			GameRegistry.addRecipe(new ItemStack(blockBeta, 1, 4), "IGI", "#@#", "IGI", 'I', Item.ingotIron, 'G', Item.gunpowder, '#', Block.chest, '@', Block.dispenser);
			GameRegistry.registerCustomItemStack("fireworks", new ItemStack(blockBeta, 1, 4));
		}
		
		if (enableChatBox) {
			blockBeta.registerTile(5).setClass(TileChatBox.class).setSprites("chat").setName("chatBox").setInfoText(descriptive ? "This block is computer powered!" : null);
			GameRegistry.registerTileEntity(TileChatBox.class, "MiscPeripherals Chat Box");
			lang.addStringLocalization("miscperipherals.chatBox.name", "Chat Box");
			GameRegistry.addRecipe(new ItemStack(blockBeta, 1, 5), "G@G", "@*@", "G@G", 'G', Item.ingotGold, '*', Item.diamond, '@', Block.music);
			GameRegistry.registerCustomItemStack("chatBox", new ItemStack(blockBeta, 1, 5));
		}
		
		if (enableSpeaker) {
			blockBeta.registerTile(7).setClass(TileSpeaker.class).setSprites("chat","chat","speaker","speaker","speaker","speaker").setName("speaker").setInfoText(descriptive ? "This block is computer powered!" : null);
			GameRegistry.registerTileEntity(TileSpeaker.class, "MiscPeripherals Speaker");
			lang.addStringLocalization("miscperipherals.speaker.name", "Speaker");
			GameRegistry.addRecipe(new ItemStack(blockBeta, 1, 7), "G@G", "@*@", "G@G", 'G', Item.ingotGold, '*', Block.blockRedstone, '@', Block.music);
			GameRegistry.registerCustomItemStack("speaker", new ItemStack(blockBeta, 1, 7));
		}
		
		if (enableSmSender) {
			blockBeta.registerTile(8).setClass(TileSmSender.class).setSprites("smSender").setName("smSender");
			GameRegistry.registerTileEntity(TileSmSender.class, "MiscPeripherals Smallnet Sender");
			lang.addStringLocalization("miscperipherals.smSender.name", "Smallnet Sender");
			GameRegistry.addRecipe(new ItemStack(blockBeta, 1, 8), "@I@", "IGI", "@I@", 'I', Item.ingotIron, '@', Block.torchRedstoneActive, 'G', Item.ingotGold);
			GameRegistry.registerCustomItemStack("smSender", new ItemStack(blockBeta, 1, 8));
		}
		
		if (enableEnergyMeter) {
			blockBeta.registerTile(10).setClass(TileEnergyMeter.class).setSprites("energyMeter","energyMeter","energyMeter","energyMeter_active","energyMeter","energyMeter").setName("energyMeter").setFacingMode(FacingMode.All).setInfoText(descriptive ? "This block is computer powered!" : null);
			GameRegistry.registerTileEntity(TileEnergyMeter.class, "MiscPeripherals Energy Meter");
			lang.addStringLocalization("miscperipherals.energyMeter.name", "Energy Meter");
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockBeta, 1, 10), "IRI", "I@I", "IRI", 'I', Item.ingotIron, 'R', Block.blockRedstone, '@', "MiscPeripherals$energyMeter"));
			GameRegistry.registerCustomItemStack("energyMeter", new ItemStack(blockBeta, 1, 10));
		}
		
		if (enableRedstoneBox) {
			blockBeta.registerTile(11).setClass(TileRedstoneBox.class).setSprites("redstoneBox_top","redstoneBox_top","redstoneBox","redstoneBox_front","redstoneBox","redstoneBox").setName("redstoneBox").setFacingMode(FacingMode.Horizontal).setInfoText(descriptive ? "This block is computer powered!" : null);
			GameRegistry.registerTileEntity(TileRedstoneBox.class, "MiscPeripherals Redstone Box");
			lang.addStringLocalization("miscperipherals.redstoneBox.name", "Redstone Box");
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockBeta, 1, 11), "ITI", "R@R", "ITI", 'I', Item.ingotIron, 'T', Block.torchRedstoneActive, 'R', Item.redstone, '@', Block.stone));
			GameRegistry.registerCustomItemStack("redstoneBox", new ItemStack(blockBeta, 1, 11));
		}
		
		if (enableDataCart) {
			itemCart.registerCart(0).setClass(EntityMinecartData.class).setSprite("dataCart").setName("dataCart");
			lang.addStringLocalization("miscperipherals.dataCart.name", Loader.isModLoaded("Railcraft") ? "Data Cart" : "Minecart with Data Storage"); // teehee
			EntityRegistry.registerModEntity(EntityMinecartData.class, "Data Cart", 0, this, 80, 3, true);
		}
		
		for (Module module : modules.values()) module.onInit();
		
		TickRegistry.registerTickHandler(new TickHandler(), Side.SERVER);
		NetworkRegistry.instance().registerGuiHandler(this, guiHandler);
		
		if (DEBUG) log.info("Debug mode is on!");
	}
	
	@PostInit
	public void onLoaded(FMLPostInitializationEvent event) {
		LanguageRegistry lang = LanguageRegistry.instance();
		
		lang.addStringLocalization("itemGroup.miscperipherals", "MiscPeripherals");
		
		if (itemSmartHelmet != null) {
			GameRegistry.addShapelessRecipe(new ItemStack(itemSmartHelmet), Item.helmetIron, CCItems.getModem(), CCItems.getMonitor());
		}
		
		if (enableNote) {
			registerUpgrade(new UpgradeNote());
		}
		
		if (enableChunkLoader) {
			registerUpgrade(new UpgradeChunkLoader());
			
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(itemAlpha, 1, 4), CCItems.getModem(), "MiscPeripherals$chunkLoader"));
		
			ForgeChunkManager.setForcedChunkLoadingCallback(this, new ChunkLoadingCallback());
		}
		
		if (enableFeeder) {
			registerUpgrade(new UpgradeFeeder());
		}
		
		if (enableResupply) {
			registerUpgrade(new UpgradeResupply());
		}
		
		if (enableShears) {
			registerUpgrade(new UpgradeShears());
		}
		
		if (enableCompass) {
			registerUpgrade(new UpgradeCompass());
		}
		
		if (enableInventory) {
			registerUpgrade(new UpgradeInventory());
		}
		
		if (enableSignReader) {
			registerUpgrade(new UpgradeSignReader());
		}
		
		if (enableXP) {
			registerUpgrade(new UpgradeXP());
			registerUpgrade(new UpgradeAnvil());
		}
		
		if (enableInventory) {
			registerUpgrade(new UpgradeInventory());
		}
		
		if (enableTank) {
			registerUpgrade(new UpgradeTank());
		}
		
		if (!OreDictionary.getOres("dropUranium").isEmpty() && enableRTG) {
			itemAlpha.registerItem(5, "rtg", "rtgModule");
			lang.addStringLocalization("miscperipherals.rtgModule.name", "RTG Module");
			
			Object copper = OreDictionary.getOres("ingotCopper").isEmpty() ? Item.ingotGold : "ingotCopper";
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemAlpha, 1, 5), true, "CIC", "@G#", "CIC", 'C', copper, 'I', Item.ingotIron, '@', "MiscPeripherals$rtgChamber", 'G', "MiscPeripherals$rtgGenerator", '#', "MiscPeripherals$rtgCircuit"));
			
			registerUpgrade(new UpgradeRTG());
		}
		
		if (enableAccelerator) {
			GameRegistry.addRecipe(new ItemStack(blockAlpha, 1, 15), "GRG", "R@R", "GRG", 'G', Item.ingotGold, 'R', Item.redstone, '@', CCItems.getComputer());
			
			registerUpgrade(new UpgradeAccelerator());
		}
		
		if (enableFireworkLauncher) {
			registerUpgrade(new UpgradeFireworks());
		}
		
		if (enableChatBox) {
			registerUpgrade(new UpgradeChatBox());
		}
		
		if (enableSpeaker) {
			registerUpgrade(new UpgradeSpeaker());
		}
		
		if (enableChest) {
			registerUpgrade(new UpgradeChest());
		}
		
		if (enableEnergyMeter) {
			registerUpgrade(new UpgradeEnergyMeter());
		}
		
		for (Module module : modules.values()) module.onPostInit();
		
		settings.save();
		
		OreDictionary.registerOre("MiscPeripherals$enderChest", new ItemStack(Block.enderChest, 1, OreDictionary.WILDCARD_VALUE));
		
		for (ItemStack stack : CCItems.getDisks()) {
			if (stack != null) OreDictionary.registerOre("MiscPeripherals$disk", stack);
		}
		
		addOreDictFallback("MiscPeripherals$cartDetector", new ItemStack(Item.eyeOfEnder));
		addOreDictFallback("MiscPeripherals$cartDetectorRail", new ItemStack(Block.railDetector));
		addOreDictFallback("MiscPeripherals$rtgChamber", new ItemStack(Item.netherStar));
		addOreDictFallback("MiscPeripherals$rtgGenerator", new ItemStack(Block.furnaceIdle));
		addOreDictFallback("MiscPeripherals$rtgCircuit", new ItemStack(Item.redstoneRepeater));
		addOreDictFallback("MiscPeripherals$energyMeter", new ItemStack(Item.comparator));
		addOreDictFallback("MiscPeripherals$disk", new ItemStack(Item.redstone));
		
		if (blockLanCable != null) {
			BlockLanCable.registerType(0, "Wood", Block.planks, 8.0D, true, false).addRecipe("plankWood");
			BlockLanCable.registerType(1, "Tin", Block.planks, 16.0D, true, false).addRecipe("ingotTin").setOreDictTexture("blockTin", new ItemStack(Block.cloth, 1, 0));
			BlockLanCable.registerType(2, "Copper", Block.planks, 32.0D, true, false).addRecipe("ingotCopper").setOreDictTexture("blockCopper", new ItemStack(Block.cloth, 1, 1));
			BlockLanCable.registerType(3, "Bronze", Block.planks, 64.0D, true, false).addRecipe("ingotBronze").setOreDictTexture("blockBronze", new ItemStack(Block.cloth, 1, 12));
			BlockLanCable.registerType(4, "Iron", Block.blockIron, 128.0D, true, false).addRecipe("ingotIron");
			BlockLanCable.registerType(5, "Gold", Block.blockGold, 256.0D, true, false).addRecipe("ingotGold");
			BlockLanCable.registerType(6, "Silver", Block.planks, 512.0D, true, false).addRecipe("ingotSilver").setOreDictTexture("blockSilver", new ItemStack(Block.cloth, 1, 7));
			BlockLanCable.registerType(7, "Nickel", Block.planks, 1024.0D, true, false).addRecipe("ingotNickel").setOreDictTexture("blockNickel", new ItemStack(Block.cloth, 1, 4));
			BlockLanCable.registerType(8, "Platinum", Block.planks, 2048.0D, true, false).addRecipe("ingotPlatinum").setOreDictTexture("blockPlatinum", new ItemStack(Block.cloth, 1, 9));
			BlockLanCable.registerType(9, "Tungsten", Block.planks, 4096.0D, true, false).addRecipe("ingotTungsten").setOreDictTexture("blockTungsten", new ItemStack(Block.cloth, 1, 15));
			BlockLanCable.registerType(10, "Iridium", Block.planks, 8192.0D, true, false).addRecipe("ingotIridium").setOreDictTexture("blockIridium", new ItemStack(Block.cloth, 1, 8));
		}
		
		LuaManager.init();
		SmallNetHelper.init();
		
		proxy.onPostInit();
		
		// snark
		GameRegistry.addShapelessRecipe(name(new ItemStack(Item.netherStar), "The Nether Star of Justice"), behead("SOTMead"), behead("Nebris"), new ItemStack(Item.netherStar));
	}
	
	@ServerStarting
	public void onServerStarting(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandBase() {
			@Override
			public void processCommand(ICommandSender icommandsender, String[] astring) {
				EntityPlayer player = getCommandSenderAsPlayer(icommandsender);
				
				MovingObjectPosition mop = Util.rayTrace(player, 4.5D);
				if (mop.typeOfHit == EnumMovingObjectType.TILE) {
					TileEntity te = player.worldObj.getBlockTileEntity(mop.blockX, mop.blockY, mop.blockZ);
					if (te instanceof TileChargeStation) {
						if (astring.length < 1) icommandsender.sendChatToPlayer("Turtle fuel limit: "+((TileChargeStation)te).limit);
						else {
							((TileChargeStation)te).limit = parseIntWithMin(icommandsender, astring[0], 0);
							icommandsender.sendChatToPlayer("Turtle fuel limit set to "+astring[0]);
						}
					}
				}
				
				throw new WrongUsageException(getCommandUsage(icommandsender));
			}
			
			@Override
			public String getCommandName() {
				return "chargestationlimit";
			}
			
			@Override
			public String getCommandUsage(ICommandSender par1ICommandSender) {
				return "/"+getCommandName()+" <limit> - while looking at a charge station";
			}
			
			@Override
			public boolean canCommandSenderUseCommand(ICommandSender par1ICommandSender) {
				return par1ICommandSender instanceof EntityPlayer && ((EntityPlayer)par1ICommandSender).capabilities.allowEdit;
			}
		});
	}
	
	@FingerprintWarning
	public void onFingerprintWarning(FMLFingerprintViolationEvent event) {
		//System.out.println("MiscPeripherals has been tampered with!");
	}
	
	public static void debug(String message) {
		if (DEBUG) log.info("Debug: "+message);
	}
	
	public static boolean addOreDictFallback(String id, ItemStack ore) {
		List<ItemStack> ores = OreDictionary.getOres(id);
		if (ores.isEmpty()) {
			ores.add(ore);
			return true;
		} else return false;
	}
	
	public static void registerUpgrade(ITurtleUpgrade upgrade) {
		if (upgrade.getUpgradeID() > 255 && ccVersion != null && (ccVersion.contains("1.52") || ccVersion.contains("1.51"))) {
			log.warning("Not registering upgrade [" + upgrade.getAdjective() + "] due to high ID and old CC (" + ccVersion + ")");
			return;
		}
		
		TurtleAPI.registerUpgrade(upgrade);
		Icons.registerUpgrade(upgrade);
		CreativeTabMiscPeripherals.registerUpgrade(upgrade);
	}
	
	public boolean loadModule(String mod, String name) {
		if (hasMod(mod)) {
			try {
				Object module = Class.forName("miscperipherals.module.Module"+name).newInstance();
				if (!(module instanceof Module)) throw new IllegalArgumentException("Not a valid module");
				
				modules.put(name, (Module)module);
			} catch (Throwable e) {
				log.warning("Could not load module "+name+" for mod "+mod);
				e.printStackTrace();
				return false;
			}
			
			log.info("Loaded module: "+name);
			return true;
		}
		return false;
	}
	
	public static boolean hasMod(String mod) {
		ArtifactVersion ver = VersionParser.parseVersionReference(mod);
		return Loader.isModLoaded(ver.getLabel()) && ver.containsVersion(Loader.instance().getIndexedModList().get(ver.getLabel()).getProcessedVersion());
	}
	
	private ItemStack behead(String a) {
		ItemStack b = new ItemStack(Item.skull, 1, 3);
		b.stackTagCompound = new NBTTagCompound();
		b.stackTagCompound.setString("SkullOwner", a);
		return b;
	}
	
	private ItemStack name(ItemStack a, String b) {
		a.setItemName(b);
		return a;
	}
}
