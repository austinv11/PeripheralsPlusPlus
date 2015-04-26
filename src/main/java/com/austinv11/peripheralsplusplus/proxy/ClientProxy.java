package com.austinv11.peripheralsplusplus.proxy;

import com.austinv11.peripheralsplusplus.PeripheralsPlusPlus;
import com.austinv11.peripheralsplusplus.client.gui.GuiRocket;
import com.austinv11.peripheralsplusplus.client.gui.GuiSmartHelmetOverlay;
import com.austinv11.peripheralsplusplus.client.models.*;
import com.austinv11.peripheralsplusplus.entities.EntityNanoBotSwarm;
import com.austinv11.peripheralsplusplus.entities.EntityRidableTurtle;
import com.austinv11.peripheralsplusplus.entities.EntityRocket;
import com.austinv11.peripheralsplusplus.event.RobotHandler;
import com.austinv11.peripheralsplusplus.event.SmartHelmetHandler;
import com.austinv11.peripheralsplusplus.event.VersionCheckHandler;
import com.austinv11.peripheralsplusplus.init.ModBlocks;
import com.austinv11.peripheralsplusplus.reference.Reference;
import com.austinv11.peripheralsplusplus.tiles.TileEntityAntenna;
import com.austinv11.peripheralsplusplus.tiles.TileEntityTurtle;
import com.austinv11.peripheralsplusplus.turtles.TurtleCompass;
import com.austinv11.collectiveframework.minecraft.utils.IconManager;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.VillagerRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy {

	@SideOnly(Side.CLIENT)
	@Override
	public void setupVillagers() {
		super.setupVillagers();
		VillagerRegistry.instance().registerVillagerSkin(PeripheralsPlusPlus.VILLAGER_ID, new ResourceLocation(Reference.MOD_ID.toLowerCase()+":textures/models/CCVillager.png"));
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void iconManagerInit() {
		IconManager.register(new TurtleCompass());
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerRenderers() {
		RenderingRegistry.registerEntityRenderingHandler(EntityRocket.class, new RenderRocket());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAntenna.class, new RenderAntenna());
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlocks.antenna), new ItemRenderAntenna(new RenderAntenna(), new TileEntityAntenna()));
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTurtle.class, new RenderTurtle());
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlocks.turtle), new ItemRenderAntenna(new RenderTurtle(), new TileEntityTurtle()));
		RenderingRegistry.registerEntityRenderingHandler(EntityRidableTurtle.class, new RenderRidableTurtle());
		RenderingRegistry.registerEntityRenderingHandler(EntityNanoBotSwarm.class, new RenderNanoSwarm());
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void prepareGuis() {
		MinecraftForge.EVENT_BUS.register(new GuiRocket.EventHandler());
		MinecraftForge.EVENT_BUS.register(new GuiSmartHelmetOverlay());
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerEvents() {
		super.registerEvents();
		FMLCommonHandler.instance().bus().register(new SmartHelmetHandler());
		MinecraftForge.EVENT_BUS.register(new SmartHelmetHandler());
		FMLCommonHandler.instance().bus().register(new VersionCheckHandler());
		FMLCommonHandler.instance().bus().register(new RobotHandler());
	}
}
