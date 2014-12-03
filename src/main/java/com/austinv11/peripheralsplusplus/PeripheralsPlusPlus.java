package com.austinv11.peripheralsplusplus;

import com.austinv11.peripheralsplusplus.api.satellites.upgrades.ISatelliteUpgrade;
import com.austinv11.peripheralsplusplus.blocks.*;
import com.austinv11.peripheralsplusplus.client.gui.GuiHandler;
import com.austinv11.peripheralsplusplus.creativetab.CreativeTabPPP;
import com.austinv11.peripheralsplusplus.entities.EntityRocket;
import com.austinv11.peripheralsplusplus.init.ModBlocks;
import com.austinv11.peripheralsplusplus.init.ModItems;
import com.austinv11.peripheralsplusplus.init.Recipes;
import com.austinv11.peripheralsplusplus.items.SatelliteUpgradeBase;
import com.austinv11.peripheralsplusplus.proxy.CommonProxy;
import com.austinv11.peripheralsplusplus.reference.Config;
import com.austinv11.peripheralsplusplus.reference.Reference;
import com.austinv11.peripheralsplusplus.tiles.TileEntityChatBox;
import com.austinv11.peripheralsplusplus.turtles.*;
import com.austinv11.peripheralsplusplus.utils.ConfigurationHandler;
import com.austinv11.peripheralsplusplus.utils.Logger;
import com.austinv11.peripheralsplusplus.villagers.TradeHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.VillagerRegistry;
import dan200.computercraft.api.ComputerCraftAPI;
import dan200.computercraft.api.turtle.ITurtleUpgrade;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;
import java.util.List;

@Mod(modid= Reference.MOD_ID,name = Reference.MOD_NAME,version = Reference.VERSION/*, guiFactory = Reference.GUI_FACTORY_CLASS*/)
public class PeripheralsPlusPlus {

	public static int VILLAGER_ID = 1337; //:P

	public static final List<SatelliteUpgradeBase> SATELLITE_UPGRADE_REGISTRY = new ArrayList<SatelliteUpgradeBase>();
	public static final List<Integer> SATELLITE_UPGRADE_ID_REGISTRY = new ArrayList<Integer>();

	/**
	 * Object containing all registered upgrades, the iterator is the upgrade id
	 */
	public final ArrayList<ISatelliteUpgrade> UPGRADE_REGISTRY = new ArrayList<ISatelliteUpgrade>();

	@Mod.Instance(Reference.MOD_ID)
	public static PeripheralsPlusPlus instance;

	@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
	public static CommonProxy proxy;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		ConfigurationHandler.init(event.getSuggestedConfigurationFile());
		proxy.iconManagerInit();
		FMLCommonHandler.instance().bus().register(new ConfigurationHandler());
		MinecraftForge.EVENT_BUS.register(new TileEntityChatBox.ChatListener());
		ModItems.preInit();
		ModBlocks.init();
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());
		ModItems.init();//Inits satellite upgrades
		Recipes.init();
		Logger.info("Registering peripherals...");
		proxy.registerTileEntities();
		ComputerCraftAPI.registerPeripheralProvider(new BlockChatBox());
		ComputerCraftAPI.registerPeripheralProvider(new BlockPlayerSensor());
		ComputerCraftAPI.registerPeripheralProvider(new BlockOreDictionary());
		if (Loader.isModLoaded("Forestry")) {
			Logger.info("Forestry is loaded! Registering analyzer peripherals...");
			ComputerCraftAPI.registerPeripheralProvider(new BlockAnalyzerBee());
			ComputerCraftAPI.registerPeripheralProvider(new BlockAnalyzerTree());
			ComputerCraftAPI.registerPeripheralProvider(new BlockAnalyzerButterfly());
		} else {
			Logger.info("Forestry not found, skipping analyzer peripherals");
		}
		ComputerCraftAPI.registerPeripheralProvider(new BlockTeleporter());
		ComputerCraftAPI.registerPeripheralProvider(new BlockTeleporterT2());
		ComputerCraftAPI.registerPeripheralProvider(new BlockEnvironmentScanner());
		Logger.info("Registering turtle upgrades...");
		registerUpgrade(new TurtleChatBox());
		registerUpgrade(new TurtlePlayerSensor());
		registerUpgrade(new TurtleCompass());
		registerUpgrade(new TurtleXP());
		if (Loader.isModLoaded("factorization") || Loader.isModLoaded("JABBA")) {
			Logger.info("A mod that adds barrels is loaded! Registering the barrel turtle upgrade...");
			registerUpgrade(new TurtleBarrel());
		} else {
			Logger.info("No barrel-adding mods found, skipping the barrel turtle upgrade");
		}
		registerUpgrade(new TurtleOreDictionary());
		registerUpgrade(new TurtleEnvironmentScanner());
		registerUpgrade(new TurtleFeeder());
		registerUpgrade(new TurtleShear());
		if (Loader.isModLoaded("ProjRed|Exploration")) {
			Logger.info("Project Red Exploration is loaded! Registering Project Red tools turtle upgrades...");
			registerProjRedUpgrades();
		} else {
			Logger.info("Project Red Exploration not found, skipping Project Red tools turtle upgrades");
		}
		Logger.info("All peripherals and turtle upgrades registered!");
		proxy.registerRenderers();
		EntityRegistry.registerGlobalEntityID(EntityRocket.class, "Rocket", EntityRegistry.findGlobalUniqueEntityId());
		EntityRegistry.registerModEntity(EntityRocket.class, "Rocket", 0, instance, 64, 20, true);
		if (Config.enableVillagers)
			setupVillagers();
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {

	}
	
	public static void registerUpgrade(ITurtleUpgrade u) {
		ComputerCraftAPI.registerTurtleUpgrade(u);
		CreativeTabPPP.upgrades.add(u);
	}

	private void setupVillagers() {
		while (VillagerRegistry.getRegisteredVillagers().contains(VILLAGER_ID)) //Dynamic villager ids ftw?
			VILLAGER_ID++;
		VillagerRegistry.instance().registerVillagerId(VILLAGER_ID);
		VillagerRegistry.instance().registerVillagerSkin(VILLAGER_ID, new ResourceLocation(Reference.MOD_ID.toLowerCase()+":textures/models/CCVillager.png"));
		VillagerRegistry.instance().registerVillageTradeHandler(VILLAGER_ID, new TradeHandler());
	}

	private void registerProjRedUpgrades() {
		registerUpgrade(new TurtleProjRed(TurtleProjRed.ToolMaterial.PERIDOT, TurtleProjRed.ToolType.AXE, 0));
		registerUpgrade(new TurtleProjRed(TurtleProjRed.ToolMaterial.PERIDOT, TurtleProjRed.ToolType.HOE, 1));
		registerUpgrade(new TurtleProjRed(TurtleProjRed.ToolMaterial.PERIDOT, TurtleProjRed.ToolType.PICKAXE, 2));
		registerUpgrade(new TurtleProjRed(TurtleProjRed.ToolMaterial.PERIDOT, TurtleProjRed.ToolType.SHOVEL, 3));
		registerUpgrade(new TurtleProjRed(TurtleProjRed.ToolMaterial.PERIDOT, TurtleProjRed.ToolType.SWORD, 4));
		registerUpgrade(new TurtleProjRed(TurtleProjRed.ToolMaterial.RUBY, TurtleProjRed.ToolType.AXE, 5));
		registerUpgrade(new TurtleProjRed(TurtleProjRed.ToolMaterial.RUBY, TurtleProjRed.ToolType.HOE, 6));
		registerUpgrade(new TurtleProjRed(TurtleProjRed.ToolMaterial.RUBY, TurtleProjRed.ToolType.PICKAXE, 7));
		registerUpgrade(new TurtleProjRed(TurtleProjRed.ToolMaterial.RUBY, TurtleProjRed.ToolType.SHOVEL, 8));
		registerUpgrade(new TurtleProjRed(TurtleProjRed.ToolMaterial.RUBY, TurtleProjRed.ToolType.SWORD, 9));
		registerUpgrade(new TurtleProjRed(TurtleProjRed.ToolMaterial.SAPPHIRE, TurtleProjRed.ToolType.AXE, 10));
		registerUpgrade(new TurtleProjRed(TurtleProjRed.ToolMaterial.SAPPHIRE, TurtleProjRed.ToolType.HOE, 11));
		registerUpgrade(new TurtleProjRed(TurtleProjRed.ToolMaterial.SAPPHIRE, TurtleProjRed.ToolType.PICKAXE, 12));
		registerUpgrade(new TurtleProjRed(TurtleProjRed.ToolMaterial.SAPPHIRE, TurtleProjRed.ToolType.SHOVEL, 13));
		registerUpgrade(new TurtleProjRed(TurtleProjRed.ToolMaterial.SAPPHIRE, TurtleProjRed.ToolType.SWORD, 14));
	}
}
