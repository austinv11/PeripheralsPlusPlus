package com.austinv11.peripheralsplusplus;

import com.austinv11.collectiveframework.minecraft.config.ConfigException;
import com.austinv11.collectiveframework.minecraft.config.ConfigRegistry;
import com.austinv11.collectiveframework.minecraft.logging.Logger;
import com.austinv11.collectiveframework.minecraft.reference.ModIds;
import com.austinv11.peripheralsplusplus.client.gui.GuiHandler;
import com.austinv11.peripheralsplusplus.creativetab.CreativeTabPPP;
import com.austinv11.peripheralsplusplus.entities.EntityNanoBotSwarm;
import com.austinv11.peripheralsplusplus.entities.EntityRidableTurtle;
import com.austinv11.peripheralsplusplus.hooks.ComputerCraftHooks;
import com.austinv11.peripheralsplusplus.hooks.ComputerCraftNotFoundException;
import com.austinv11.peripheralsplusplus.hooks.ComputerCraftRegistry;
import com.austinv11.peripheralsplusplus.init.ModBlocks;
import com.austinv11.peripheralsplusplus.init.ModItems;
import com.austinv11.peripheralsplusplus.init.Recipes;
import com.austinv11.peripheralsplusplus.items.ItemNanoSwarm;
import com.austinv11.peripheralsplusplus.mount.DynamicMount;
import com.austinv11.peripheralsplusplus.network.*;
import com.austinv11.peripheralsplusplus.pocket.PocketMotionDetector;
import com.austinv11.peripheralsplusplus.pocket.PocketPeripheralContainer;
import com.austinv11.peripheralsplusplus.proxy.CommonProxy;
import com.austinv11.peripheralsplusplus.reference.Config;
import com.austinv11.peripheralsplusplus.reference.Reference;
import com.austinv11.peripheralsplusplus.turtles.*;
import com.austinv11.peripheralsplusplus.turtles.peripherals.PeripheralChunkLoader;
import com.austinv11.peripheralsplusplus.utils.IPlusPlusPeripheral;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.relauncher.Side;
import dan200.computercraft.api.ComputerCraftAPI;
import dan200.computercraft.api.turtle.ITurtleUpgrade;
import net.minecraft.block.BlockDispenser;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.MinecraftForge;

@Mod(modid= Reference.MOD_ID,name = Reference.MOD_NAME,version = Reference.VERSION/*, guiFactory = Reference.GUI_FACTORY_CLASS*/, dependencies = "after:CollectiveFramework")
public class PeripheralsPlusPlus {

	public static int VILLAGER_ID = 1337; //:P
	
	public static SimpleNetworkWrapper NETWORK;

	@Mod.Instance(Reference.MOD_ID)
	public static PeripheralsPlusPlus instance;

	@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
	public static CommonProxy proxy;
	
	public static Logger LOGGER = new Logger(Reference.MOD_NAME);

	public static String BASE_PPP_DIR = "./mods/PPP/";
	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		try {
			ConfigRegistry.registerConfig(new Config());
		} catch (ConfigException e) {
			LOGGER.fatal("Fatal problem with the Peripherals++ config has been caught, if this continues, please delete the config file");
			e.printStackTrace();
		}
		NETWORK = NetworkRegistry.INSTANCE.newSimpleChannel("ppp");
		NETWORK.registerMessage(AudioPacket.AudioPacketHandler.class, AudioPacket.class, 0, Side.CLIENT);
		NETWORK.registerMessage(AudioResponsePacket.AudioResponsePacketHandler.class, AudioResponsePacket.class, 1, Side.SERVER);
		NETWORK.registerMessage(ChatPacket.ChatPacketHandler.class, ChatPacket.class, 4, Side.CLIENT);
		NETWORK.registerMessage(ScaleRequestPacket.ScaleRequestPacketHandler.class, ScaleRequestPacket.class, 5, Side.CLIENT);
		NETWORK.registerMessage(ScaleRequestResponsePacket.ScaleRequestResponsePacketHandler.class, ScaleRequestResponsePacket.class, 6, Side.SERVER);
		NETWORK.registerMessage(CommandPacket.CommandPacketHandler.class, CommandPacket.class, 7, Side.CLIENT);
		NETWORK.registerMessage(ParticlePacket.ParticlePacketHandler.class, ParticlePacket.class, 8, Side.CLIENT);
		NETWORK.registerMessage(InputEventPacket.InputEventPacketHandler.class, InputEventPacket.class, 9, Side.SERVER);
		NETWORK.registerMessage(GuiPacket.GuiPacketHandler.class, GuiPacket.class, 10, Side.CLIENT);
		NETWORK.registerMessage(TextFieldInputEventPacket.TextFieldInputEventPacketHandler.class, TextFieldInputEventPacket.class, 11, Side.SERVER);
		NETWORK.registerMessage(RidableTurtlePacket.RidableTurtlePacketHandler.class, RidableTurtlePacket.class, 12, Side.SERVER);
		NETWORK.registerMessage(RobotEventPacket.RobotEventPacketHandler.class, RobotEventPacket.class, 13, Side.CLIENT);
        NETWORK.registerMessage(PermCardChangePacket.PermCardChangePacketHandler.class, PermCardChangePacket.class, 14, Side.SERVER);
		NETWORK.registerMessage(SynthPacket.SynthPacketHandler.class, SynthPacket.class, 15, Side.CLIENT);
		NETWORK.registerMessage(SynthResponsePacket.SynthResponsePacketHandler.class, SynthResponsePacket.class, 16, Side.SERVER);
		proxy.iconManagerInit();
		proxy.prepareGuis();
		proxy.registerEvents();
		ModItems.preInit();
		ModBlocks.init();
		LOGGER.info("Preparing the mount...");
		DynamicMount.prepareMount();
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());
		LOGGER.info("Registering peripherals...");
		proxy.registerTileEntities();
		ComputerCraftAPI.registerPeripheralProvider(new IPlusPlusPeripheral.Provider());
		LOGGER.info("Registering turtle upgrades...");
		registerUpgrade(new TurtleChatBox());
		registerUpgrade(new TurtlePlayerSensor());
		registerUpgrade(new TurtleCompass());
		registerUpgrade(new TurtleXP());
		if (Loader.isModLoaded(ModIds.Factorization) || Loader.isModLoaded(ModIds.JABBA)) {
			LOGGER.info("A mod that adds barrels is loaded! Registering the barrel turtle upgrade...");
			registerUpgrade(new TurtleBarrel());
		} else
			LOGGER.info("No barrel-adding mods found, skipping the barrel turtle upgrade");
		registerUpgrade(new TurtleOreDictionary());
		registerUpgrade(new TurtleEnvironmentScanner());
		registerUpgrade(new TurtleFeeder());
		registerUpgrade(new TurtleShear());
		registerUpgrade(new TurtleSignReader());
		registerUpgrade(new TurtleGarden());
		if (Loader.isModLoaded(ModIds.ProjectRed_Exploration) || Loader.isModLoaded(ModIds.BluePower)) {
			LOGGER.info("At least one RedPower-like mod is loaded! Registering RedPower-like turtle upgrades...");
			registerRedPowerLikeUpgrades();
		} else
			LOGGER.info("No RedPower-like mods found, skipping RedPower-like turtle upgrades");
		registerUpgrade(new TurtleSpeaker());
		registerUpgrade(new TurtleTank());
		registerUpgrade(new TurtleNoteBlock());
		registerUpgrade(new TurtleRidable());
		registerUpgrade(new TurtleDispenser());
		registerUpgrade(new TurtleChunkLoader());
		registerUpgrade(new TurtleResupply());
		LOGGER.info("Registering pocket computer upgrades...");
		try {
			ComputerCraftRegistry.registerPocketUpgrade(new PocketMotionDetector());
			ComputerCraftRegistry.registerPocketUpgrade(new PocketPeripheralContainer());
		} catch (Exception e) {
			e.printStackTrace();
		}
		LOGGER.info("All peripherals and turtle upgrades registered!");
		proxy.registerRenderers();
		if (Config.enableVillagers)
			proxy.setupVillagers();
		EntityRegistry.registerModEntity(EntityRidableTurtle.class, "Ridable Turtle", 1, instance, 64, 1, true);
		EntityRegistry.registerModEntity(EntityNanoBotSwarm.class, "NanoBotSwarm", 2, instance, 64, 20, true);
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) throws ComputerCraftNotFoundException {
		Recipes.init();
		BlockDispenser.dispenseBehaviorRegistry.putObject(ModItems.nanoSwarm, new ItemNanoSwarm.BehaviorNanoSwarm());
		ForgeChunkManager.setForcedChunkLoadingCallback(this, new PeripheralChunkLoader.TurtleChunkLoadingCallback());
	}
	
	@Mod.EventHandler
	public void onServerStop(FMLServerStoppedEvent event) {
		ComputerCraftHooks.cachedPeripherals.clear();
		ComputerCraftHooks.cachedExtraPeripherals.clear();
	}
	
	public static void registerUpgrade(ITurtleUpgrade u) {
		ComputerCraftAPI.registerTurtleUpgrade(u);
		CreativeTabPPP.upgrades.add(u);
		if (u instanceof TurtleDropCollector)
			MinecraftForge.EVENT_BUS.register(((TurtleDropCollector) u).newInstanceOfListener());
	}

	private void registerRedPowerLikeUpgrades() {
		
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
		if (Loader.isModLoaded(ModIds.ProjectRed_Exploration)) {
			int i = 0;
			for (final TurtleProjRed.ToolMaterial material : new TurtleProjRed.ToolMaterial[]{TurtleProjRed.ToolMaterial.PERIDOT, TurtleProjRed.ToolMaterial.RUBY, TurtleProjRed.ToolMaterial.SAPPHIRE}) {
				for (final TurtleProjRed.ToolType type : new TurtleProjRed.ToolType[]{TurtleProjRed.ToolType.AXE, TurtleProjRed.ToolType.HOE, TurtleProjRed.ToolType.PICKAXE, TurtleProjRed.ToolType.SHOVEL, TurtleProjRed.ToolType.SWORD}) {
					
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
		
		if (Loader.isModLoaded(ModIds.BluePower)) {
			int j = 0;
			for (final TurtleBluePower.ToolMaterial material : new TurtleBluePower.ToolMaterial[]{TurtleBluePower.ToolMaterial.AMETHYST, TurtleBluePower.ToolMaterial.RUBY, TurtleBluePower.ToolMaterial.SAPPHIRE}) {
				for (final TurtleBluePower.ToolType type : new TurtleBluePower.ToolType[]{TurtleBluePower.ToolType.AXE, TurtleBluePower.ToolType.HOE, TurtleBluePower.ToolType.PICKAXE, TurtleBluePower.ToolType.SHOVEL, TurtleBluePower.ToolType.SWORD}) {
					
					final int id = j++;
					registerUpgrade(new TurtleBluePower() {
						
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
}
