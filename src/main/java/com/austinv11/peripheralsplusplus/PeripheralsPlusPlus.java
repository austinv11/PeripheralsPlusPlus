package com.austinv11.peripheralsplusplus;

import com.austinv11.peripheralsplusplus.api.satellites.upgrades.ISatelliteUpgrade;
import com.austinv11.peripheralsplusplus.blocks.*;
import com.austinv11.peripheralsplusplus.client.gui.GuiHandler;
import com.austinv11.peripheralsplusplus.creativetab.PPPCreativeTab;
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
		proxy.registerTileEntities();
		ComputerCraftAPI.registerPeripheralProvider(new ChatBox());
		ComputerCraftAPI.registerPeripheralProvider(new PlayerSensor());
		ComputerCraftAPI.registerPeripheralProvider(new OreDictionaryBlock());
		if (Loader.isModLoaded("Forestry")) {
			ComputerCraftAPI.registerPeripheralProvider(new AnalyzerBee());
			ComputerCraftAPI.registerPeripheralProvider(new AnalyzerTree());
			ComputerCraftAPI.registerPeripheralProvider(new AnalyzerButterfly());
		}
		ComputerCraftAPI.registerPeripheralProvider(new Teleporter());
		ComputerCraftAPI.registerPeripheralProvider(new TeleporterT2());
		ComputerCraftAPI.registerPeripheralProvider(new EnvironmnetScanner());
		registerUpgrade(new TurtleChatBox());
		registerUpgrade(new TurtlePlayerSensor());
		registerUpgrade(new TurtleCompass());
		registerUpgrade(new TurtleXP());
		if (Loader.isModLoaded("factorization") || Loader.isModLoaded("JABBA"))
			registerUpgrade(new TurtleBarrel());
		registerUpgrade(new TurtleOreDictionary());
		//if (TurtleWrench.isUsable())
		//	registerUpgrade(new TurtleWrench());
		registerUpgrade(new TurtleEnvironmentScanner());
		registerUpgrade(new TurtleFeeder());
		proxy.registerRenderers();
		EntityRegistry.registerGlobalEntityID(EntityRocket.class, "Rocket", EntityRegistry.findGlobalUniqueEntityId());
		EntityRegistry.registerModEntity(EntityRocket.class, "Rocket", 0, this.instance, 64, 20, true);
		if (Config.enableVillagers)
			setupVillagers();
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {

	}
	
	public static void registerUpgrade(ITurtleUpgrade u) {
		ComputerCraftAPI.registerTurtleUpgrade(u);
		PPPCreativeTab.upgrades.add(u);
	}

	private void setupVillagers() {
		while (VillagerRegistry.getRegisteredVillagers().contains(VILLAGER_ID)) { //Dynamic villager ids ftw?
			VILLAGER_ID++;
		}
		VillagerRegistry.instance().registerVillagerId(VILLAGER_ID);
		VillagerRegistry.instance().registerVillagerSkin(VILLAGER_ID, new ResourceLocation(Reference.MOD_ID.toLowerCase()+":textures/models/CCVillager.png"));
		VillagerRegistry.instance().registerVillageTradeHandler(VILLAGER_ID, new TradeHandler());
	}
}
