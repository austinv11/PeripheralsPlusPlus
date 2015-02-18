package com.austinv11.peripheralsplusplus;

import com.austinv11.peripheralsplusplus.api.satellites.upgrades.ISatelliteUpgrade;
import com.austinv11.peripheralsplusplus.blocks.*;
import com.austinv11.peripheralsplusplus.client.gui.GuiHandler;
import com.austinv11.peripheralsplusplus.creativetab.CreativeTabPPP;
import com.austinv11.peripheralsplusplus.entities.EntityRocket;
import com.austinv11.peripheralsplusplus.event.PeripheralContainerHandler;
import com.austinv11.peripheralsplusplus.init.ModBlocks;
import com.austinv11.peripheralsplusplus.init.ModItems;
import com.austinv11.peripheralsplusplus.init.Recipes;
import com.austinv11.peripheralsplusplus.items.SatelliteUpgradeBase;
import com.austinv11.peripheralsplusplus.mount.DynamicMount;
import com.austinv11.peripheralsplusplus.network.*;
import com.austinv11.peripheralsplusplus.proxy.CommonProxy;
import com.austinv11.peripheralsplusplus.reference.Config;
import com.austinv11.peripheralsplusplus.reference.Reference;
import com.austinv11.peripheralsplusplus.satellites.SatelliteEventHandler;
import com.austinv11.peripheralsplusplus.satellites.SatelliteTickHandler;
import com.austinv11.peripheralsplusplus.tiles.TileEntityAntenna;
import com.austinv11.peripheralsplusplus.tiles.TileEntityChatBox;
import com.austinv11.peripheralsplusplus.turtles.*;
import com.austinv11.peripheralsplusplus.turtles.TurtleProjRed.ToolMaterial;
import com.austinv11.peripheralsplusplus.turtles.TurtleProjRed.ToolType;
import com.austinv11.peripheralsplusplus.utils.ConfigurationHandler;
import com.austinv11.peripheralsplusplus.utils.Logger;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.relauncher.Side;
import dan200.computercraft.api.ComputerCraftAPI;
import dan200.computercraft.api.turtle.ITurtleUpgrade;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Mod(modid= Reference.MOD_ID,name = Reference.MOD_NAME,version = Reference.VERSION/*, guiFactory = Reference.GUI_FACTORY_CLASS*/)
public class PeripheralsPlusPlus {

	public static int VILLAGER_ID = 1337; //:P

	public static final List<SatelliteUpgradeBase> SATELLITE_UPGRADE_REGISTRY = new ArrayList<SatelliteUpgradeBase>();
	public static final List<Integer> SATELLITE_UPGRADE_ID_REGISTRY = new ArrayList<Integer>();

	/**
	 * Object containing all registered upgrades, the key is the upgrade id
	 */
	public static final HashMap<Integer, ISatelliteUpgrade> UPGRADE_REGISTRY = new HashMap<Integer, ISatelliteUpgrade>();

	public static SimpleNetworkWrapper NETWORK;

	@Mod.Instance(Reference.MOD_ID)
	public static PeripheralsPlusPlus instance;

	@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
	public static CommonProxy proxy;

	public static String BASE_PPP_DIR = "./mods/PPP/";

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		ConfigurationHandler.init(event.getSuggestedConfigurationFile());
		NETWORK = NetworkRegistry.INSTANCE.newSimpleChannel("ppp");
		NETWORK.registerMessage(AudioPacket.AudioPacketHandler.class, AudioPacket.class, 0, Side.CLIENT);
		NETWORK.registerMessage(AudioResponsePacket.AudioResponsePacketHandler.class, AudioResponsePacket.class, 1, Side.SERVER);
		NETWORK.registerMessage(RocketCountdownPacket.RocketCountdownPacketHandler.class, RocketCountdownPacket.class, 2, Side.CLIENT);
		NETWORK.registerMessage(RocketLaunchPacket.RocketLaunchPacketHandler.class, RocketLaunchPacket.class, 3, Side.SERVER);
		NETWORK.registerMessage(ChatPacket.ChatPacketHandler.class, ChatPacket.class, 4, Side.CLIENT);
		NETWORK.registerMessage(ScaleRequestPacket.ScaleRequestPacketHandler.class, ScaleRequestPacket.class, 5, Side.CLIENT);
		NETWORK.registerMessage(ScaleRequestResponsePacket.ScaleRequestResponsePacketHandler.class, ScaleRequestResponsePacket.class, 6, Side.SERVER);
		NETWORK.registerMessage(CommandPacket.CommandPacketHandler.class, CommandPacket.class, 7, Side.CLIENT);
        NETWORK.registerMessage(ParticlePacket.ParticlePacketHandler.class, ParticlePacket.class, 8, Side.CLIENT);
		proxy.iconManagerInit();
		proxy.prepareGuis();
		FMLCommonHandler.instance().bus().register(new ConfigurationHandler());
		FMLCommonHandler.instance().bus().register(new SatelliteTickHandler());
		MinecraftForge.EVENT_BUS.register(new TileEntityChatBox.ChatListener());
		MinecraftForge.EVENT_BUS.register(new SatelliteEventHandler());
		MinecraftForge.EVENT_BUS.register(new PeripheralContainerHandler());
		MinecraftForge.EVENT_BUS.register(new TileEntityAntenna());
		ModItems.preInit();
		ModBlocks.init();
		Logger.info("Preparing the mount...");
		try {
			DynamicMount.prepareMount();
			Logger.info("Mount has been successfully prepared!");
		}catch (Exception e) {
			Logger.error("An exception was thrown attempting to prepare mount programs; if your internet connection is fine, please report the following to the mod author:");
			e.printStackTrace();
		}
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());
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
		} else
			Logger.info("Forestry not found, skipping analyzer peripherals");
		ComputerCraftAPI.registerPeripheralProvider(new BlockTeleporter());
		ComputerCraftAPI.registerPeripheralProvider(new BlockTeleporterT2());
		ComputerCraftAPI.registerPeripheralProvider(new BlockEnvironmentScanner());
		ComputerCraftAPI.registerPeripheralProvider(new BlockSpeaker());
		ComputerCraftAPI.registerPeripheralProvider(new BlockAntenna());
		ComputerCraftAPI.registerPeripheralProvider(new BlockPeripheralContainer());
		if (Loader.isModLoaded("appliedenergistics2")) {
			Logger.info("Applied Energistics is loaded! Registering the ME Bridge...");
			ComputerCraftAPI.registerPeripheralProvider(new BlockMEBridge());
		}else
			Logger.info("Applied Energistics not found, skipping the ME Bridge");
		Logger.info("Registering turtle upgrades...");
		registerUpgrade(new TurtleChatBox());
		registerUpgrade(new TurtlePlayerSensor());
		registerUpgrade(new TurtleCompass());
		registerUpgrade(new TurtleXP());
		if (Loader.isModLoaded("factorization") || Loader.isModLoaded("JABBA")) {
			Logger.info("A mod that adds barrels is loaded! Registering the barrel turtle upgrade...");
			registerUpgrade(new TurtleBarrel());
		} else
			Logger.info("No barrel-adding mods found, skipping the barrel turtle upgrade");
		registerUpgrade(new TurtleOreDictionary());
		registerUpgrade(new TurtleEnvironmentScanner());
		registerUpgrade(new TurtleFeeder());
		registerUpgrade(new TurtleShear());
		registerUpgrade(new TurtleSignReader());
		registerUpgrade(new TurtleGarden());
		if (Loader.isModLoaded("ProjRed|Exploration")) {
			Logger.info("Project Red Exploration is loaded! Registering Project Red tools turtle upgrades...");
			registerProjRedUpgrades();
		} else
			Logger.info("Project Red Exploration not found, skipping Project Red tools turtle upgrades");
		registerUpgrade(new TurtleSpeaker());
		registerUpgrade(new TurtleTank());
        registerUpgrade(new TurtleNoteBlock());
		Logger.info("All peripherals and turtle upgrades registered!");
		Logger.info("Registering satellite upgrades...");
		Logger.info("All satellite upgrades registered!");
		proxy.registerRenderers();
		EntityRegistry.registerGlobalEntityID(EntityRocket.class, "Rocket", EntityRegistry.findGlobalUniqueEntityId());
		EntityRegistry.registerModEntity(EntityRocket.class, "Rocket", 0, instance, 64, 20, true);
		if (Config.enableVillagers)
			proxy.setupVillagers();
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		ModItems.init();//Inits satellite upgrades
		Recipes.init();
	}
	
	public static void registerUpgrade(ITurtleUpgrade u) {
		ComputerCraftAPI.registerTurtleUpgrade(u);
		CreativeTabPPP.upgrades.add(u);
		if (u instanceof TurtleDropCollector)
			MinecraftForge.EVENT_BUS.register(((TurtleDropCollector) u).newInstanceOfListener());
	}

	private void registerProjRedUpgrades() {
		
//		Better solution but don't keeps old ids
		
//		int i = 0;
//		for (final ToolType type : ToolType.values()) {
//			if (type == ToolType.UNKNOWN) continue;
//			
//			for (final ToolMaterial material : ToolMaterial.values()) {
//				if (material == ToolMaterial.UNKNOWN) continue;
//				
//				final int id = i++;
//				registerUpgrade(new TurtleProjRed() {
//					
//					@Override
//					public ToolType getToolType() {
//						return type;
//					}
//					
//					@Override
//					public ToolMaterial getToolMaterial() {
//						return material;
//					}
//					
//					@Override
//					public int getID() {
//						return id;
//					}
//				});
//			}
//		}
		
//		Not as good as the first but better than yours and it keeps the same ids as before
		
		int i = 0;
		for (final ToolMaterial material : new ToolMaterial[] { ToolMaterial.PERIDOT, ToolMaterial.RUBY, ToolMaterial.SAPPHIRE }) {
			for (final ToolType type : new ToolType[] { ToolType.AXE, ToolType.HOE, ToolType.PICKAXE, ToolType.SHOVEL, ToolType.SWORD }) {
				
				final int id = i++;
				registerUpgrade(new TurtleProjRed() {
					
					@Override
					public ToolType getToolType() {
						return type;
					}
					
					@Override
					public ToolMaterial getToolMaterial() {
						return material;
					}
					
					@Override
					public int getID() {
						return id;
					}
				});
			}
		}
	}
}
