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
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import dan200.computercraft.api.ComputerCraftAPI;
import dan200.computercraft.api.turtle.ITurtleUpgrade;
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
		MinecraftForge.EVENT_BUS.register(new TurtleProjRed.Listener());
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
			proxy.setupVillagers();
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {

	}
	
	public static void registerUpgrade(ITurtleUpgrade u) {
		ComputerCraftAPI.registerTurtleUpgrade(u);
		CreativeTabPPP.upgrades.add(u);
	}

	private void registerProjRedUpgrades() { //I'm so sorry
		registerUpgrade(new TurtleProjRed() {
			@Override
			public int getID() {
				return 0;
			}

			@Override
			public ToolType getToolType() {
				return ToolType.AXE;
			}

			@Override
			public ToolMaterial getToolMaterial() {
				return ToolMaterial.PERIDOT;
			}
		});
		registerUpgrade(new TurtleProjRed() {
			@Override
			public int getID() {
				return 1;
			}

			@Override
			public ToolType getToolType() {
				return ToolType.HOE;
			}

			@Override
			public ToolMaterial getToolMaterial() {
				return ToolMaterial.PERIDOT;
			}
		});
		registerUpgrade(new TurtleProjRed() {
			@Override
			public int getID() {
				return 2;
			}

			@Override
			public ToolType getToolType() {
				return ToolType.PICKAXE;
			}

			@Override
			public ToolMaterial getToolMaterial() {
				return ToolMaterial.PERIDOT;
			}
		});
		registerUpgrade(new TurtleProjRed() {
			@Override
			public int getID() {
				return 3;
			}

			@Override
			public ToolType getToolType() {
				return ToolType.SHOVEL;
			}

			@Override
			public ToolMaterial getToolMaterial() {
				return ToolMaterial.PERIDOT;
			}
		});
		registerUpgrade(new TurtleProjRed() {
			@Override
			public int getID() {
				return 4;
			}

			@Override
			public ToolType getToolType() {
				return ToolType.SWORD;
			}

			@Override
			public ToolMaterial getToolMaterial() {
				return ToolMaterial.PERIDOT;
			}
		});
		registerUpgrade(new TurtleProjRed() {
			@Override
			public int getID() {
				return 5;
			}

			@Override
			public ToolType getToolType() {
				return ToolType.AXE;
			}

			@Override
			public ToolMaterial getToolMaterial() {
				return ToolMaterial.RUBY;
			}
		});
		registerUpgrade(new TurtleProjRed() {
			@Override
			public int getID() {
				return 6;
			}

			@Override
			public ToolType getToolType() {
				return ToolType.HOE;
			}

			@Override
			public ToolMaterial getToolMaterial() {
				return ToolMaterial.RUBY;
			}
		});
		registerUpgrade(new TurtleProjRed() {
			@Override
			public int getID() {
				return 7;
			}

			@Override
			public ToolType getToolType() {
				return ToolType.PICKAXE;
			}

			@Override
			public ToolMaterial getToolMaterial() {
				return ToolMaterial.RUBY;
			}
		});
		registerUpgrade(new TurtleProjRed() {
			@Override
			public int getID() {
				return 8;
			}

			@Override
			public ToolType getToolType() {
				return ToolType.SHOVEL;
			}

			@Override
			public ToolMaterial getToolMaterial() {
				return ToolMaterial.RUBY;
			}
		});
		registerUpgrade(new TurtleProjRed() {
			@Override
			public int getID() {
				return 9;
			}

			@Override
			public ToolType getToolType() {
				return ToolType.SWORD;
			}

			@Override
			public ToolMaterial getToolMaterial() {
				return ToolMaterial.RUBY;
			}
		});
		registerUpgrade(new TurtleProjRed() {
			@Override
			public int getID() {
				return 10;
			}

			@Override
			public ToolType getToolType() {
				return ToolType.AXE;
			}

			@Override
			public ToolMaterial getToolMaterial() {
				return ToolMaterial.SAPPHIRE;
			}
		});
		registerUpgrade(new TurtleProjRed() {
			@Override
			public int getID() {
				return 11;
			}

			@Override
			public ToolType getToolType() {
				return ToolType.HOE;
			}

			@Override
			public ToolMaterial getToolMaterial() {
				return ToolMaterial.SAPPHIRE;
			}
		});
		registerUpgrade(new TurtleProjRed() {
			@Override
			public int getID() {
				return 12;
			}

			@Override
			public ToolType getToolType() {
				return ToolType.PICKAXE;
			}

			@Override
			public ToolMaterial getToolMaterial() {
				return ToolMaterial.SAPPHIRE;
			}
		});
		registerUpgrade(new TurtleProjRed() {
			@Override
			public int getID() {
				return 13;
			}

			@Override
			public ToolType getToolType() {
				return ToolType.SHOVEL;
			}

			@Override
			public ToolMaterial getToolMaterial() {
				return ToolMaterial.SAPPHIRE;
			}
		});
		registerUpgrade(new TurtleProjRed() {
			@Override
			public int getID() {
				return 14;
			}

			@Override
			public ToolType getToolType() {
				return ToolType.SWORD;
			}

			@Override
			public ToolMaterial getToolMaterial() {
				return ToolMaterial.SAPPHIRE;
			}
		});
	}
}
