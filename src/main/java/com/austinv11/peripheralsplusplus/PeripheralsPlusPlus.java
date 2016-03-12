package com.austinv11.peripheralsplusplus;

import com.austinv11.peripheralsplusplus.init.ModBlocks;
import com.austinv11.peripheralsplusplus.init.ModItems;
import com.austinv11.peripheralsplusplus.proxy.CommonProxy;
import com.austinv11.peripheralsplusplus.reference.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION)
public class PeripheralsPlusPlus {

	@Mod.Instance(Reference.MOD_ID)
	public static PeripheralsPlusPlus instance;

	@SidedProxy(clientSide = Reference.CLIENT_PROXY, serverSide = Reference.COMMON_PROXY)
	public static CommonProxy proxy;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		ModBlocks.init();
		ModItems.init();
		proxy.registerTileEntities();
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.setupItemRenderer();
	}
}
