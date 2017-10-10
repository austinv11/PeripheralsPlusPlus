package com.austinv11.peripheralsplusplus;

import com.austinv11.collectiveframework.minecraft.config.ConfigException;
import com.austinv11.collectiveframework.minecraft.config.ConfigRegistry;
import com.austinv11.collectiveframework.multithreading.SimpleRunnable;
import com.austinv11.peripheralsplusplus.init.ModBlocks;
import com.austinv11.peripheralsplusplus.init.ModItems;
import com.austinv11.peripheralsplusplus.init.ModPeripherals;
import com.austinv11.peripheralsplusplus.init.Recipes;
import com.austinv11.peripheralsplusplus.items.ItemNanoSwarm;
import com.austinv11.peripheralsplusplus.mount.DynamicMount;
import com.austinv11.peripheralsplusplus.proxy.CommonProxy;
import com.austinv11.peripheralsplusplus.reference.Config;
import com.austinv11.peripheralsplusplus.reference.Reference;
import com.austinv11.peripheralsplusplus.turtles.peripherals.PeripheralChunkLoader;
import net.minecraft.block.BlockDispenser;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid= Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION,
		guiFactory = Reference.GUI_FACTORY_CLASS,
		dependencies = "required-after:collectiveframework;required-after:computercraft")
public class PeripheralsPlusPlus {
	
	public static SimpleNetworkWrapper NETWORK;

	@Mod.Instance(Reference.MOD_ID)
	public static PeripheralsPlusPlus instance;

	@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
	public static CommonProxy proxy;
	
	public static Logger LOGGER = LogManager.getLogger(Reference.MOD_ID);

	public static String BASE_PPP_DIR = "./mods/PPP/";
	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		LOGGER = event.getModLog();
		try {
			ConfigRegistry.registerConfig(Config.INSTANCE);
		} catch (ConfigException e) {
			LOGGER.fatal("Fatal problem with the Peripherals++ config has been caught, if this continues, please delete the config file");
			e.printStackTrace();
		}
		ModBlocks.register();
		ModItems.register();
		ModPeripherals.registerInternally();
		proxy.textureAndModelInit();
		proxy.registerNetwork();
		proxy.prepareGuis();
		proxy.registerEvents();
		proxy.registerEntities();
		proxy.registerTileEntities();
		proxy.registerRenderers();
		proxy.registerCapabilities();
		if (Config.enableVillagers)
			proxy.setupVillagers();
		LOGGER.info("Preparing the mount...");
		DynamicMount.prepareMount();
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		ModPeripherals.registerWithComputerCraft();
		LOGGER.info("All peripherals and TURTLE upgrades registered!");
		ForgeChunkManager.setForcedChunkLoadingCallback(PeripheralsPlusPlus.instance,
				new PeripheralChunkLoader.LoaderHandler());
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		Recipes.init();
		BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(ModItems.NANO_SWARM, new ItemNanoSwarm.BehaviorNanoSwarm());
		SimpleRunnable.RESTRICT_THREAD_USAGE = false;
	}
}
