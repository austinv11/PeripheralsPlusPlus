package com.austinv11.peripheralsplusplus.proxy;

import com.austinv11.collectiveframework.minecraft.utils.ModelManager;
import com.austinv11.collectiveframework.minecraft.utils.TextureManager;
import com.austinv11.peripheralsplusplus.client.gui.GuiSmartHelmetOverlay;
import com.austinv11.peripheralsplusplus.client.models.RenderAntenna;
import com.austinv11.peripheralsplusplus.client.models.RenderNanoSwarm;
import com.austinv11.peripheralsplusplus.client.models.RenderRidableTurtle;
import com.austinv11.peripheralsplusplus.client.models.RenderTurtle;
import com.austinv11.peripheralsplusplus.entities.EntityNanoBotSwarm;
import com.austinv11.peripheralsplusplus.entities.EntityRidableTurtle;
import com.austinv11.peripheralsplusplus.event.handler.RobotHandler;
import com.austinv11.peripheralsplusplus.event.handler.SmartHelmetHandler;
import com.austinv11.peripheralsplusplus.init.ModItems;
import com.austinv11.peripheralsplusplus.init.ModPeripherals;
import com.austinv11.peripheralsplusplus.tiles.TileEntityAntenna;
import com.austinv11.peripheralsplusplus.tiles.TileEntityTurtle;
import dan200.computercraft.api.turtle.ITurtleUpgrade;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ClientProxy extends CommonProxy {

	@SideOnly(Side.CLIENT)
	@Override
	public void setupVillagers() {
		super.setupVillagers();
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void textureAndModelInit() {
		for (ITurtleUpgrade upgrade : ModPeripherals.TURTLE_UPGRADES) {
			if (upgrade instanceof TextureManager.TextureRegistrar)
				TextureManager.register((TextureManager.TextureRegistrar) upgrade);
			if (upgrade instanceof ModelManager.ModelRegistrar)
				ModelManager.register((ModelManager.ModelRegistrar) upgrade);
		}
		ModItems.registerModels();
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerRenderers() {
//		RenderingRegistry.registerEntityRenderingHandler(EntityRocket.class, new RenderRocket());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAntenna.class, new RenderAntenna());
		TileEntityItemStackRenderer.instance = new RenderAntenna.RenderAntennaItem(TileEntityItemStackRenderer.instance);
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTurtle.class, new RenderTurtle());
		TileEntityItemStackRenderer.instance = new RenderTurtle.RenderTurtleItem(TileEntityItemStackRenderer.instance);
		RenderingRegistry.registerEntityRenderingHandler(EntityRidableTurtle.class, RenderRidableTurtle::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityNanoBotSwarm.class, RenderNanoSwarm::new);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void prepareGuis() {
		super.prepareGuis();
		MinecraftForge.EVENT_BUS.register(new GuiSmartHelmetOverlay());
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerEvents() {
		super.registerEvents();
		MinecraftForge.EVENT_BUS.register(new SmartHelmetHandler());
		MinecraftForge.EVENT_BUS.register(new RobotHandler());
	}
}
